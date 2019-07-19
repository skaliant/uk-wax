package de.skaliant.wax.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class JsonTest {
	@Test
	public void test_boolean() {
		assertEquals("true", Json.convert(true));
		assertEquals("false", Json.convert(false));
	}


	@Test
	public void test_numeric() {
		assertEquals("1234", Json.convert(1234));
		assertEquals("1234", Json.convert(1234L));
		assertEquals("5.2", Json.convert(5.2F));
		assertEquals("5.2", Json.convert(5.2D));
	}


	@Test
	public void test_string() {
		assertEquals("\"This is just a simple string\"",
				Json.convert("This is just a simple string"));
		assertEquals("\"This is a\\tstring with several\\ncontrol characters\"",
				Json.convert("This is a\tstring with several\ncontrol characters"));
		assertEquals("\"Das ist Käse für 2,50 \\u20ac je 100g\"",
				Json.convert("Das ist Käse für 2,50 € je 100g"));
	}


	@Test
	public void test_array() {
		assertEquals("[\"start\",123,true,\"end\"]",
				Json.convert(new Object[] { "start", 123, true, "end" }));
	}


	@Test
	public void test_collection() {
		List<Object> data = Arrays.asList("start", 123, true, "end");

		assertEquals("[\"start\",123,true,\"end\"]", Json.convert(data));
	}


	@Test
	public void test_map() {
		Map<String, Object> data = new LinkedHashMap<>();

		data.put("str", "string value");
		data.put("num", 123);
		data.put("bool", true);

		assertEquals("{\"str\":\"string value\",\"num\":123,\"bool\":true}",
				Json.convert(data));
	}


	@Test
	public void test_bean() {
		TestBean bean = new TestBean();
		String result = Json.convert(bean);

		assertTrue(result.contains("\"str\":\"string value\""));
		assertTrue(result.contains("\"bool\":true"));
		assertTrue(result.contains("\"num\":123"));
		assertFalse(result.contains("\"readOnly\":"));
		assertFalse(result.contains("\"writeOnly\":"));
	}

	public static class TestBean {
		private String str = "string value";
		private String readOnly = "readOnly";
		@SuppressWarnings("unused")
		private String writeOnly = "writeOnly";
		private boolean bool = true;
		private int num = 123;


		public String getReadOnly() {
			return readOnly;
		}


		public void setWriteOnly(String writeOnly) {
			this.writeOnly = writeOnly;
		}


		public String getStr() {
			return str;
		}


		public void setStr(String str) {
			this.str = str;
		}


		public boolean isBool() {
			return bool;
		}


		public void setBool(boolean bool) {
			this.bool = bool;
		}


		public int getNum() {
			return num;
		}


		public void setNum(int num) {
			this.num = num;
		}
	}
}
