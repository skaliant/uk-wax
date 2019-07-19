package de.skaliant.wax.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class QueryStringBuilderTest {
	@Test
	public void test_empty() {
		QueryStringBuilder proband = new QueryStringBuilder();

		assertEquals("", proband.toString());
	}


	@Test
	public void test_appendPreset() {
		QueryStringBuilder proband = new QueryStringBuilder("?first=this");

		proband.append("second", "that");

		assertEquals("?first=this&second=that", proband.toString());
	}


	@Test
	public void test_urlEncoding() {
		QueryStringBuilder proband = new QueryStringBuilder();
		String result;

		proband.append("name", "value with spaces");
		result = proband.toString();

		assertTrue("?name=value%20with%20spaces".equals(result) || "?name=value+with+spaces".equals(result));
	}


	@Test
	public void test_charsetEncodingIso8859() {
		QueryStringBuilder proband = new QueryStringBuilder(Charset.forName("iso-8859-1"));

		proband.append("name", "käse");

		assertEquals("?name=k%e4se", proband.toString().toLowerCase());
	}


	@Test
	public void test_charsetEncodingUtf8() {
		QueryStringBuilder proband = new QueryStringBuilder(Charset.forName("utf-8"));

		proband.append("name", "käse");

		assertEquals("?name=k%c3%a4se", proband.toString().toLowerCase());
	}


	@Test
	public void test_append_singleParameterSingleValue() {
		QueryStringBuilder proband = new QueryStringBuilder();

		proband.append("name", "value");

		assertEquals("?name=value", proband.toString());
	}


	@Test
	public void test_append_singleParameterMultipleValues() {
		QueryStringBuilder proband = new QueryStringBuilder();

		proband.append("name", "value1", "value2", "value3");

		assertEquals("?name=value1&name=value2&name=value3", proband.toString());
	}


	@Test
	public void test_append_multipleParameters() {
		QueryStringBuilder proband = new QueryStringBuilder();

		proband.append("name1", "value1", "value2", "value3");
		proband.append("name2");
		proband.append("name3", 23, 42, 4711);

		assertEquals("?name1=value1&name1=value2&name1=value3&name2&name3=23&name3=42&name3=4711", proband.toString());
	}
}
