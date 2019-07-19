package de.skaliant.wax.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class MiscUtilsTest {
	@Test
	public void test_isEmpty() {
		assertTrue(MiscUtils.isEmpty(null));
		assertTrue(MiscUtils.isEmpty(""));
		assertFalse(MiscUtils.isEmpty(" "));
		assertFalse(MiscUtils.isEmpty("a"));
	}


	@Test
	public void test_isNotEmpty() {
		assertFalse(MiscUtils.isNotEmpty(null));
		assertFalse(MiscUtils.isNotEmpty(""));
		assertTrue(MiscUtils.isNotEmpty(" "));
		assertTrue(MiscUtils.isNotEmpty("a"));
	}


	@Test
	public void test_isBlank() {
		assertTrue(MiscUtils.isBlank(null));
		assertTrue(MiscUtils.isBlank(""));
		assertTrue(MiscUtils.isBlank(" "));
		assertFalse(MiscUtils.isBlank("a"));
	}


	@Test
	public void test_isNotBlank() {
		assertFalse(MiscUtils.isNotBlank(null));
		assertFalse(MiscUtils.isNotBlank(""));
		assertFalse(MiscUtils.isNotBlank(" "));
		assertTrue(MiscUtils.isNotBlank("a"));
	}


	@Test
	public void test_isDigitsOnly() {
		assertFalse(MiscUtils.isDigitsOnly(null));
		assertFalse(MiscUtils.isDigitsOnly(""));
		assertFalse(MiscUtils.isDigitsOnly("a"));
		assertFalse(MiscUtils.isDigitsOnly("a1"));
		assertFalse(MiscUtils.isDigitsOnly("1a"));
		assertFalse(MiscUtils.isDigitsOnly("1a1"));
		assertFalse(MiscUtils.isDigitsOnly("1."));
		assertFalse(MiscUtils.isDigitsOnly("-1"));
		assertTrue(MiscUtils.isDigitsOnly("1"));
		assertTrue(MiscUtils.isDigitsOnly("123"));
	}


	@Test
	public void test_firstLetterUpperCase() {
		assertNull(MiscUtils.firstLetterUpperCase(null));
		assertEquals("", MiscUtils.firstLetterUpperCase(""));
		assertEquals("Abc", MiscUtils.firstLetterUpperCase("abc"));
		assertEquals(" bc", MiscUtils.firstLetterUpperCase(" bc"));
	}


	@Test
	public void test_countChar() {
		assertEquals(0, MiscUtils.countChar(null, 'x'));
		assertEquals(0, MiscUtils.countChar("", 'x'));
		assertEquals(0, MiscUtils.countChar(" ", 'x'));
		assertEquals(0, MiscUtils.countChar("abcdef", 'x'));
		assertEquals(1, MiscUtils.countChar("abcxde", 'x'));
		assertEquals(1, MiscUtils.countChar("xabcde", 'x'));
		assertEquals(1, MiscUtils.countChar("abcdex", 'x'));
		assertEquals(2, MiscUtils.countChar("abcxdexfg", 'x'));
	}


	@Test
	public void test_countChars() {
		assertEquals(0, MiscUtils.countChars(null, "x"));
		assertEquals(0, MiscUtils.countChars("", "x"));
		assertEquals(0, MiscUtils.countChars(" ", "x"));
		assertEquals(0, MiscUtils.countChars("abcdef", "x"));
		assertEquals(1, MiscUtils.countChars("abcxde", "x"));
		assertEquals(1, MiscUtils.countChars("xabcde", "x"));
		assertEquals(1, MiscUtils.countChars("abcdex", "x"));
		assertEquals(2, MiscUtils.countChars("abcxdexfg", "x"));

		assertEquals(0, MiscUtils.countChars(null, "xf"));
		assertEquals(0, MiscUtils.countChars("", "xf"));
		assertEquals(0, MiscUtils.countChars(" ", "xf"));
		assertEquals(1, MiscUtils.countChars("abcdef", "xf"));
		assertEquals(1, MiscUtils.countChars("abcxde", "xf"));
		assertEquals(1, MiscUtils.countChars("xabcde", "xf"));
		assertEquals(1, MiscUtils.countChars("abcdex", "xf"));
		assertEquals(2, MiscUtils.countChars("abcdefx", "xf"));
		assertEquals(3, MiscUtils.countChars("abcxdexfg", "xf"));
	}


	@Test
	public void test_splitAtChar() {
		assertEquals(Arrays.asList(), MiscUtils.splitAtChar(null, ','));
		assertEquals(Arrays.asList(""), MiscUtils.splitAtChar("", ','));
		assertEquals(Arrays.asList("x"), MiscUtils.splitAtChar("x", ','));
		assertEquals(Arrays.asList("", "", ""), MiscUtils.splitAtChar(",,", ','));
		assertEquals(Arrays.asList("", "x", ""), MiscUtils.splitAtChar(",x,", ','));
		assertEquals(Arrays.asList("a", "", "c"),
				MiscUtils.splitAtChar("a,,c", ','));
		assertEquals(Arrays.asList("a", "b", "c"),
				MiscUtils.splitAtChar("a,b,c", ','));
		assertEquals(Arrays.asList("a", "b", ""),
				MiscUtils.splitAtChar("a,b,", ','));
	}


	@Test
	public void test_splitAtAnyCharOf() {
		assertEquals(Arrays.asList(), MiscUtils.splitAtAnyCharOf(null, ",;"));
		assertEquals(Arrays.asList(""), MiscUtils.splitAtAnyCharOf("", ",;"));
		assertEquals(Arrays.asList("x"), MiscUtils.splitAtAnyCharOf("x", ",;"));
		assertEquals(Arrays.asList("", "", ""),
				MiscUtils.splitAtAnyCharOf(",;", ",;"));
		assertEquals(Arrays.asList("a", "", "c"),
				MiscUtils.splitAtAnyCharOf("a,;c", ",;"));
		assertEquals(Arrays.asList("a", "b", "c"),
				MiscUtils.splitAtAnyCharOf("a,b;c", ",;"));
	}


	@Test
	public void test_date() {
		Calendar cal = Calendar.getInstance();

		cal.setTime(MiscUtils.date(2018, 12, 30));

		assertEquals(2018, cal.get(Calendar.YEAR));
		assertEquals(Calendar.DECEMBER, cal.get(Calendar.MONTH));
		assertEquals(30, cal.get(Calendar.DATE));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.SECOND));
		assertEquals(0, cal.get(Calendar.MILLISECOND));

		cal.setTime(MiscUtils.date(2018, 12, 30, 13, 37, 23));

		assertEquals(2018, cal.get(Calendar.YEAR));
		assertEquals(Calendar.DECEMBER, cal.get(Calendar.MONTH));
		assertEquals(30, cal.get(Calendar.DATE));
		assertEquals(13, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(37, cal.get(Calendar.MINUTE));
		assertEquals(23, cal.get(Calendar.SECOND));
		assertEquals(0, cal.get(Calendar.MILLISECOND));
	}


	@Test
	public void test_getFirstElementOf() {
		assertNull(MiscUtils.getFirstElementOf(null));
		assertNull(MiscUtils.getFirstElementOf(Collections.emptyList()));
		assertEquals("a",
				MiscUtils.getFirstElementOf(Arrays.asList("a", "b", "c")));
	}


	@Test
	public void test_getLastElementOf() {
		assertNull(MiscUtils.getLastElementOf(null));
		assertNull(MiscUtils.getLastElementOf(Collections.emptyList()));
		assertEquals("c", MiscUtils.getLastElementOf(Arrays.asList("a", "b", "c")));
	}


	@Test
	public void test_close()
		throws Exception {
		Closeable c = mock(Closeable.class);

		assertNull(MiscUtils.close(c));

		doThrow(new IOException()).when(c).close();
		assertSame(c, MiscUtils.close(c));
	}


	@Test
	public void test_nvl() {
		assertEquals("a", MiscUtils.nvl("a", "b"));
		assertEquals("", MiscUtils.nvl("", "b"));
		assertEquals("b", MiscUtils.nvl(null, "b"));
	}

}
