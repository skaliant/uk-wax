package de.skaliant.wax.util.beans.exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class SimpleExpressionTest {
	@Test
	public void test_getTypeIn_ok() {
		SimpleExpression exp = new SimpleExpression("child");
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		assertEquals(BeanB.class, exp.getTypeIn(a));
		assertEquals(BeanC.class, exp.getTypeIn(b));
	}


	@Test(expected = RuntimeException.class)
	public void test_getTypeIn_notExists() {
		SimpleExpression exp = new SimpleExpression("child");

		exp.getTypeIn(new BeanC());
	}


	@Test
	public void test_readFrom_ok() {
		SimpleExpression exp = new SimpleExpression("child");
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		assertNull(exp.readFrom(a));
		assertNull(exp.readFrom(b));

		a.setChild(b);
		assertSame(b, exp.readFrom(a));
		assertNull(exp.readFrom(b));
	}


	@Test(expected = RuntimeException.class)
	public void test_readFrom_notExists() {
		SimpleExpression exp = new SimpleExpression("child");
		BeanC c = new BeanC();

		exp.readFrom(c);
	}


	@Test
	public void test_writeTo_ok() {
		SimpleExpression exp1 = new SimpleExpression("child");
		SimpleExpression exp2 = new SimpleExpression("text");
		BeanA a = new BeanA();
		BeanB b = new BeanB();

		assertNull(a.getChild());
		assertNull(b.getText());

		exp1.writeTo(a, b);
		assertSame(b, a.getChild());

		exp2.writeTo(b, "x");
		assertEquals("x", b.getText());

		exp1.writeTo(a, null);
		assertNull(a.getChild());
	}


	@Test(expected = RuntimeException.class)
	public void test_writeTo_notExists() {
		SimpleExpression exp = new SimpleExpression("child");
		BeanC c = new BeanC();

		exp.writeTo(c, "x");
	}


	@Test(expected = RuntimeException.class)
	public void test_writeTo_wrongType() {
		SimpleExpression exp = new SimpleExpression("child");
		BeanA a = new BeanA();
		BeanC c = new BeanC();

		exp.writeTo(a, c);
	}
}
