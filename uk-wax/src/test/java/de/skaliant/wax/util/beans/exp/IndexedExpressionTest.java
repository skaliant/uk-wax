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
public class IndexedExpressionTest {
	@Test
	public void test_getTypeIn_ok() {
		IndexedExpression exp = new IndexedExpression("list", 0);
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		assertEquals(new String[0].getClass(), exp.getTypeIn(a));
		assertListOf(String.class, exp.getTypeIn(b));
	}


	@Test(expected = RuntimeException.class)
	public void test_getTypeIn_notExists() {
		IndexedExpression exp = new IndexedExpression("last", 0);

		exp.getTypeIn(new BeanA());
	}


	@Test
	public void test_readFrom_ok() {
		IndexedExpression exp = new IndexedExpression("list", 1);
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		assertNull(exp.readFrom(a));
		assertNull(exp.readFrom(b));

		a.setList(new String[3]);
		b.setList(new ArrayList<>());
		assertNull(exp.readFrom(a));
		assertNull(exp.readFrom(b));

		a.setList(new String[] { "a", "b", "c" });
		b.setList(Arrays.asList("x", "y", "z"));
		assertEquals("b", exp.readFrom(a));
		assertEquals("y", exp.readFrom(b));
	}


	@Test
	public void test_readFrom_ignored() {
		IndexedExpression exp = new IndexedExpression("list", 5);
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		a.setList(new String[] { "a", "b", "c" });
		b.setList(Arrays.asList("a", "b", "c"));

		assertNull(exp.readFrom(a));
		assertNull(exp.readFrom(b));
	}


	@Test(expected = RuntimeException.class)
	public void test_readFrom_notExists() {
		IndexedExpression exp = new IndexedExpression("last", 0);

		exp.readFrom(new BeanA());
	}


	@Test
	public void test_writeTo_ok() {
		IndexedExpression exp = new IndexedExpression("list", 1);
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		a.setList(new String[3]);
		b.setList(new ArrayList<>());

		exp.writeTo(a, "x");
		exp.writeTo(b, "x");

		assertNull(a.getList()[0]);
		assertEquals("x", a.getList()[1]);
		assertNull(a.getList()[2]);
		assertNull(b.getList().get(0));
		assertEquals("x", b.getList().get(1));
	}


	@Test(expected = RuntimeException.class)
	public void test_writeTo_notExists() {
		IndexedExpression exp = new IndexedExpression("last", 0);

		exp.writeTo(new BeanA(), "x");
	}


	@Test(expected = RuntimeException.class)
	public void test_writeTo_wrongType_array() {
		IndexedExpression exp = new IndexedExpression("list", 0);
		BeanA a = new BeanA();

		a.setList(new String[3]);
		exp.writeTo(a, 4711);
	}


	@Test(expected = RuntimeException.class)
	public void test_writeTo_wrongType_list() {
		IndexedExpression exp = new IndexedExpression("list", 0);
		BeanB b = new BeanB();

		b.setList(new ArrayList<>());
		exp.writeTo(b, 4711);
	}


	@Test
	public void test_writeTo_ignored() {
		IndexedExpression exp = new IndexedExpression("list", 5);
		BeanA a = new BeanA();

		a.setList(new String[] { "a", "b", "c" });

		exp.writeTo(a, "x");
		assertEquals(3, a.getList().length);
		assertEquals("a", a.getList()[0]);
		assertEquals("b", a.getList()[1]);
		assertEquals("c", a.getList()[2]);
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
