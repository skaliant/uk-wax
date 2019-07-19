package de.skaliant.wax.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class ConverterTest {
	@Test
	public void test_boolean() {
		assertEquals(Boolean.TRUE, Converter.convertValue(Boolean.class, "true"));
		assertEquals(Boolean.FALSE, Converter.convertValue(Boolean.class, "false"));
		assertEquals(Boolean.FALSE, Converter.convertValue(Boolean.class, "wat"));
		assertNull(Converter.convertValue(Boolean.class, ""));
		assertNull(Converter.convertValue(Boolean.class, (String) null));

		assertEquals(Boolean.TRUE, Converter.convertValue(Boolean.TYPE, "true"));
		assertEquals(Boolean.FALSE, Converter.convertValue(Boolean.TYPE, "false"));
		assertEquals(Boolean.FALSE, Converter.convertValue(Boolean.TYPE, "wat"));
		assertEquals(Boolean.FALSE, Converter.convertValue(Boolean.TYPE, ""));
		assertEquals(Boolean.FALSE, Converter.convertValue(Boolean.TYPE, (String) null));
	}


	@Test
	public void test_byte() {
		assertEquals(Byte.valueOf((byte) 123), Converter.convertValue(Byte.class, "123"));
		assertEquals(Byte.valueOf((byte) 0), Converter.convertValue(Byte.class, "0"));
		assertEquals(Byte.valueOf((byte) -123), Converter.convertValue(Byte.class, "-123"));
		assertNull(Converter.convertValue(Byte.class, "234234234123"));
		assertNull(Converter.convertValue(Byte.class, ""));
		assertNull(Converter.convertValue(Byte.class, "asdasds123"));
		assertNull(Converter.convertValue(Byte.class, (String) null));

		assertEquals(Byte.valueOf((byte) 123), Converter.convertValue(Byte.TYPE, "123"));
		assertEquals(Byte.valueOf((byte) 0), Converter.convertValue(Byte.TYPE, "0"));
		assertEquals(Byte.valueOf((byte) -123), Converter.convertValue(Byte.TYPE, "-123"));
		assertEquals(Byte.valueOf((byte) 0), Converter.convertValue(Byte.TYPE, "234234234123"));
		assertEquals(Byte.valueOf((byte) 0), Converter.convertValue(Byte.TYPE, ""));
		assertEquals(Byte.valueOf((byte) 0), Converter.convertValue(Byte.TYPE, "asdasds123"));
		assertEquals(Byte.valueOf((byte) 0), Converter.convertValue(Byte.TYPE, (String) null));
	}


	@Test
	public void test_short() {
		assertEquals(Short.valueOf((short) 123), Converter.convertValue(Short.class, "123"));
		assertEquals(Short.valueOf((short) 0), Converter.convertValue(Short.class, "0"));
		assertEquals(Short.valueOf((short) -123), Converter.convertValue(Short.class, "-123"));
		assertNull(Converter.convertValue(Short.class, "234234234123"));
		assertNull(Converter.convertValue(Short.class, ""));
		assertNull(Converter.convertValue(Short.class, "asdasds123"));
		assertNull(Converter.convertValue(Short.class, (String) null));

		assertEquals(Short.valueOf((short) 123), Converter.convertValue(Short.TYPE, "123"));
		assertEquals(Short.valueOf((short) 0), Converter.convertValue(Short.TYPE, "0"));
		assertEquals(Short.valueOf((short) -123), Converter.convertValue(Short.TYPE, "-123"));
		assertEquals(Short.valueOf((short) 0), Converter.convertValue(Short.TYPE, "234234234123"));
		assertEquals(Short.valueOf((short) 0), Converter.convertValue(Short.TYPE, ""));
		assertEquals(Short.valueOf((short) 0), Converter.convertValue(Short.TYPE, "asdasds123"));
	}


	@Test
	public void test_char() {
		assertEquals(Character.valueOf('1'), Converter.convertValue(Character.class, "1"));
		assertEquals(Character.valueOf('x'), Converter.convertValue(Character.class, "x"));
		assertEquals(Character.valueOf('&'), Converter.convertValue(Character.class, "&"));
		assertNull(Converter.convertValue(Character.class, "abc"));
		assertNull(Converter.convertValue(Character.class, ""));
		assertNull(Converter.convertValue(Character.class, (String) null));

		assertEquals(Character.valueOf('1'), Converter.convertValue(Character.TYPE, "1"));
		assertEquals(Character.valueOf('x'), Converter.convertValue(Character.TYPE, "x"));
		assertEquals(Character.valueOf('&'), Converter.convertValue(Character.TYPE, "&"));
		assertEquals(Character.valueOf('\0'), Converter.convertValue(Character.TYPE, "abc"));
		assertEquals(Character.valueOf('\0'), Converter.convertValue(Character.TYPE, ""));
		assertEquals(Character.valueOf('\0'), Converter.convertValue(Character.TYPE, (String) null));
	}


	@Test
	public void test_int() {
		assertEquals(123, Converter.convertValue(Integer.class, "123"));
		assertEquals(0, Converter.convertValue(Integer.class, "0"));
		assertEquals(-123, Converter.convertValue(Integer.class, "-123"));
		assertEquals(Integer.MAX_VALUE, Converter.convertValue(Integer.class, Integer.toString(Integer.MAX_VALUE)));
		assertNull(Converter.convertValue(Integer.class, "1" + Integer.toString(Integer.MAX_VALUE)));
		assertNull(Converter.convertValue(Integer.class, ""));
		assertNull(Converter.convertValue(Integer.class, "asdasds123"));
		assertNull(Converter.convertValue(Integer.class, (String) null));

		assertEquals(123, Converter.convertValue(Integer.TYPE, "123"));
		assertEquals(0, Converter.convertValue(Integer.TYPE, "0"));
		assertEquals(-123, Converter.convertValue(Integer.TYPE, "-123"));
		assertEquals(Integer.MAX_VALUE, Converter.convertValue(Integer.TYPE, Integer.toString(Integer.MAX_VALUE)));
		assertEquals(0, Converter.convertValue(Integer.TYPE, "1" + Integer.toString(Integer.MAX_VALUE)));
		assertEquals(0, Converter.convertValue(Integer.TYPE, ""));
		assertEquals(0, Converter.convertValue(Integer.TYPE, "asdasds123"));
		assertEquals(0, Converter.convertValue(Integer.TYPE, (String) null));
	}


	@Test
	public void test_long() {
		assertEquals(123L, Converter.convertValue(Long.class, "123"));
		assertEquals(0L, Converter.convertValue(Long.class, "0"));
		assertEquals(-123L, Converter.convertValue(Long.class, "-123"));
		assertEquals(Long.MAX_VALUE, Converter.convertValue(Long.class, Long.toString(Long.MAX_VALUE)));
		assertNull(Converter.convertValue(Long.class, "123.45"));
		assertNull(Converter.convertValue(Long.class, "1" + Long.toString(Long.MAX_VALUE)));
		assertNull(Converter.convertValue(Long.class, ""));
		assertNull(Converter.convertValue(Long.class, "asdasds123"));
		assertNull(Converter.convertValue(Long.class, (String) null));

		assertEquals(123L, Converter.convertValue(Long.TYPE, "123"));
		assertEquals(0L, Converter.convertValue(Long.TYPE, "0"));
		assertEquals(-123L, Converter.convertValue(Long.TYPE, "-123"));
		assertEquals(Long.MAX_VALUE, Converter.convertValue(Long.TYPE, Long.toString(Long.MAX_VALUE)));
		assertEquals(0L, Converter.convertValue(Long.TYPE, "123.45"));
		assertEquals(0L, Converter.convertValue(Long.TYPE, "1" + Long.toString(Long.MAX_VALUE)));
		assertEquals(0L, Converter.convertValue(Long.TYPE, ""));
		assertEquals(0L, Converter.convertValue(Long.TYPE, "asdasds123"));
		assertEquals(0L, Converter.convertValue(Long.TYPE, (String) null));
	}


	@Test
	public void test_float() {
		BigDecimal max = BigDecimal.valueOf(Float.MAX_VALUE);
		BigDecimal ulp = BigDecimal.valueOf(Float.MAX_VALUE).divide(new BigDecimal(2), RoundingMode.HALF_UP);

		max = max.add(ulp);

		assertEquals(123F, Converter.convertValue(Float.class, "123"));
		assertEquals(0.5F, Converter.convertValue(Float.class, ".5"));
		assertEquals(0F, Converter.convertValue(Float.class, "0"));
		assertEquals(-4.25F, Converter.convertValue(Float.class, "-4.25"));
		assertEquals(-123F, Converter.convertValue(Float.class, "-123"));
		assertNull(Converter.convertValue(Float.class, max.toPlainString()));
		assertNull(Converter.convertValue(Float.class, ""));
		assertNull(Converter.convertValue(Float.class, "asdasds123"));
		assertNull(Converter.convertValue(Float.class, (String) null));

		assertEquals(123F, Converter.convertValue(Float.TYPE, "123"));
		assertEquals(0.5F, Converter.convertValue(Float.TYPE, ".5"));
		assertEquals(0F, Converter.convertValue(Float.TYPE, "0"));
		assertEquals(-4.25F, Converter.convertValue(Float.TYPE, "-4.25"));
		assertEquals(-123F, Converter.convertValue(Float.TYPE, "-123"));
		assertEquals(0F, Converter.convertValue(Float.TYPE, max.toPlainString()));
		assertEquals(0F, Converter.convertValue(Float.TYPE, ""));
		assertEquals(0F, Converter.convertValue(Float.TYPE, "asdasds123"));
		assertEquals(0F, Converter.convertValue(Float.TYPE, (String) null));
	}


	@Test
	public void test_double() {
		BigDecimal max = BigDecimal.valueOf(Double.MAX_VALUE);
		BigDecimal ulp = BigDecimal.valueOf(Double.MAX_VALUE).divide(new BigDecimal(2), RoundingMode.HALF_UP);

		max = max.add(ulp);

		assertEquals(123D, Converter.convertValue(Double.class, "123"));
		assertEquals(0.5D, Converter.convertValue(Double.class, ".5"));
		assertEquals(0D, Converter.convertValue(Double.class, "0"));
		assertEquals(-4.25D, Converter.convertValue(Double.class, "-4.25"));
		assertEquals(-123D, Converter.convertValue(Double.class, "-123"));
		assertNull(Converter.convertValue(Double.class, max.toPlainString()));
		assertNull(Converter.convertValue(Double.class, ""));
		assertNull(Converter.convertValue(Double.class, "asdasds123"));
		assertNull(Converter.convertValue(Double.class, (String) null));

		assertEquals(123D, Converter.convertValue(Double.TYPE, "123"));
		assertEquals(0.5D, Converter.convertValue(Double.TYPE, ".5"));
		assertEquals(0D, Converter.convertValue(Double.TYPE, "0"));
		assertEquals(-4.25D, Converter.convertValue(Double.TYPE, "-4.25"));
		assertEquals(-123D, Converter.convertValue(Double.TYPE, "-123"));
		assertEquals(0D, Converter.convertValue(Double.TYPE, max.toPlainString()));
		assertEquals(0D, Converter.convertValue(Double.TYPE, ""));
		assertEquals(0D, Converter.convertValue(Double.TYPE, "asdasds123"));
		assertEquals(0D, Converter.convertValue(Double.TYPE, (String) null));
	}


	@Test
	public void test_BigDecimal() {
		assertEquals(new BigDecimal("123.45"), Converter.convertValue(BigDecimal.class, "123.45"));
		assertEquals(new BigDecimal("-123.45"), Converter.convertValue(BigDecimal.class, "-123.45"));
		assertNull(Converter.convertValue(BigDecimal.class, "asdasd1234"));
		assertNull(Converter.convertValue(BigDecimal.class, ""));
		assertNull(Converter.convertValue(BigDecimal.class, (String) null));
	}


	@Test
	public void test_BigInteger() {
		assertEquals(new BigInteger("12345"), Converter.convertValue(BigInteger.class, "12345"));
		assertEquals(new BigInteger("-12345"), Converter.convertValue(BigInteger.class, "-12345"));
		assertNull(Converter.convertValue(BigInteger.class, "123.45"));
		assertNull(Converter.convertValue(BigInteger.class, "asdasd1234"));
		assertNull(Converter.convertValue(BigInteger.class, ""));
		assertNull(Converter.convertValue(BigInteger.class, (String) null));
	}


	@Test
	public void test_enum() {
		assertEquals(TestEnum.TWO, Converter.convertValue(TestEnum.class, "TWO"));
		assertEquals(TestEnum.TWO, Converter.convertValue(TestEnum.class, "1"));
		assertNull(Converter.convertValue(TestEnum.class, "3"));
		assertNull(Converter.convertValue(TestEnum.class, "WAT"));
		assertNull(Converter.convertValue(TestEnum.class, ""));
		assertNull(Converter.convertValue(TestEnum.class, (String) null));
	}


	@Test
	public void test_date() {
		assertEquals(MiscUtils.date(2018, 9, 22), Converter.convertValue(Date.class, "20180922"));
		assertEquals(MiscUtils.date(2018, 9, 22), Converter.convertValue(Date.class, "2018,09,22"));
		assertEquals(MiscUtils.date(2018, 9, 22), Converter.convertValue(Date.class, "22.09.2018"));
		assertEquals(new Date(201809221L), Converter.convertValue(Date.class, "201809221"));
		assertNull(Converter.convertValue(Date.class, ""));
		assertNull(Converter.convertValue(Date.class, (String) null));
		assertNull(Converter.convertValue(Date.class, "2018,09,22,1"));
		assertNull(Converter.convertValue(Date.class, "2018,13,22"));
		assertNull(Converter.convertValue(Date.class, "2018,9,32"));
	}

	public static enum TestEnum {
		ONE, TWO, THREE;
	}
}
