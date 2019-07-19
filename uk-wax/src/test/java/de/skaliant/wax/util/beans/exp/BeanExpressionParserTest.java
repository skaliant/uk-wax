package de.skaliant.wax.util.beans.exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class BeanExpressionParserTest {
	@Test(expected = IllegalArgumentException.class)
	public void test_parse_expressionIsNull() {
		BeanExpressionParser.parse(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void test_parse_expressionIsEmpty() {
		BeanExpressionParser.parse("");
	}


	@Test(expected = RuntimeException.class)
	public void test_parse_indexedError() {
		BeanExpressionParser.parse("x[a]");
	}


	@Test
	public void test_parse() {
		ChainedExpression chain;
		BeanExpression result;

		result = BeanExpressionParser.parse("x");
		assertTrue(result instanceof SimpleExpression);
		assertEquals("x", ((SimpleExpression) result).getName());

		result = BeanExpressionParser.parse("x[123]");
		assertTrue(result instanceof IndexedExpression);
		assertEquals("x", ((IndexedExpression) result).getName());
		assertEquals(123, ((IndexedExpression) result).getIndex());

		result = BeanExpressionParser.parse("x.x[1]");
		assertTrue(result instanceof ChainedExpression);
		chain = (ChainedExpression) result;
		assertEquals(2, chain.getChain().size());
		assertTrue(chain.getChain().get(0) instanceof SimpleExpression);
		assertEquals("x", ((SimpleExpression) chain.getChain().get(0)).getName());
		assertTrue(chain.getChain().get(1) instanceof IndexedExpression);
		assertEquals("x", ((IndexedExpression) chain.getChain().get(1)).getName());
		assertEquals(1, ((IndexedExpression) chain.getChain().get(1)).getIndex());
	}
}
