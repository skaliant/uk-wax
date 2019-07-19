package de.skaliant.wax.util.beans.exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class ChainedExpressionTest {
	@Test
	public void test_getTypeIn() {
		List<BeanExpression> parts = new ArrayList<>();
		ChainedExpression exp;
		BeanA a = new BeanA();
		BeanB b = new BeanB();
		BeanC c = new BeanC();

		parts.add(new SimpleExpression("child"));
		parts.add(new SimpleExpression("list"));
		exp = new ChainedExpression(parts);

		assertNull(exp.getTypeIn(a));
		assertNull(exp.getTypeIn(b));

		a.setChild(b);
		assertListOf(String.class, exp.getTypeIn(a));
		assertNull(exp.getTypeIn(b));

		b.setChild(c);
		assertListOf(String.class, exp.getTypeIn(a));
		assertListOf(Integer.class, exp.getTypeIn(b));
	}


	@Test
	public void test_readFrom() {
		List<BeanExpression> parts = new ArrayList<>();
		ChainedExpression exp;
		BeanA a = new BeanA();
		BeanB b = new BeanB();
		BeanC c = new BeanC();

		a.setList(new String[] { "a", "b", "c" });
		b.setList(Arrays.asList("d", "e", "f"));
		c.setList(Arrays.asList(4, 5, 6));

		parts.add(new SimpleExpression("child"));
		parts.add(new IndexedExpression("list", 1));
		exp = new ChainedExpression(parts);

		assertNull(exp.readFrom(a));
		assertNull(exp.readFrom(b));

		a.setChild(b);
		b.setChild(c);

		assertEquals("e", exp.readFrom(a));
		assertEquals(Integer.valueOf(5), exp.readFrom(b));
	}


	@Test
	public void test_writeTo() {
		List<BeanExpression> parts = new ArrayList<>();
		ChainedExpression exp;
		BeanA a = new BeanA();
		BeanB b = new BeanB();
		BeanC c = new BeanC();

		a.setList(new String[] { "a", "b", "c" });
		b.setList(Arrays.asList("d", "e", "f"));
		c.setList(Arrays.asList(4, 5, 6));

		parts.add(new SimpleExpression("child"));
		parts.add(new IndexedExpression("list", 1));
		exp = new ChainedExpression(parts);

		exp.writeTo(a, "x");
		exp.writeTo(b, 8);

		assertEquals("e", b.getList().get(1));
		assertEquals(Integer.valueOf(5), c.getList().get(1));

		a.setChild(b);
		b.setChild(c);
		exp.writeTo(a, "x");
		exp.writeTo(b, 8);

		assertEquals("x", exp.readFrom(a));
		assertEquals(Integer.valueOf(8), exp.readFrom(b));
	}


	private static void assertListOf(Class<?> expectedListType, Type type) {
		assertNotNull(type);
		assertTrue(type instanceof ParameterizedType);
		assertEquals(List.class, ((ParameterizedType) type).getRawType());
		assertEquals(1, ((ParameterizedType) type).getActualTypeArguments().length);
		assertEquals(expectedListType,
				((ParameterizedType) type).getActualTypeArguments()[0]);
	}
}
