package de.skaliant.wax.util;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class DateTimeParserTest {
	@Test
	public void test_tokenize() {
		List<String> result;

		result = DateTimeParser.tokenize("");
		assertNotNull(result);
		assertEquals(0, result.size());

		result = DateTimeParser.tokenize("123");
		assertEquals(asList("123"), result);

		result = DateTimeParser.tokenize("x123");
		assertEquals(asList("123"), result);

		result = DateTimeParser.tokenize("123x");
		assertEquals(asList("123"), result);

		result = DateTimeParser.tokenize("x123x");
		assertEquals(asList("123"), result);

		result = DateTimeParser.tokenize("1x23");
		assertEquals(asList("1", "23"), result);

		result = DateTimeParser.tokenize("1x2x3");
		assertEquals(asList("1", "2", "3"), result);

		result = DateTimeParser.tokenize("x1x2x3x");
		assertEquals(asList("1", "2", "3"), result);

		result = DateTimeParser.tokenize(".1.2.3.");
		assertEquals(asList("1", "2", "3"), result);
	}


	@Test
	public void test_examineTimeOnly() {
		LocalTime expected;
		LocalTime result;

		result = DateTimeParser.examineTimeOnly(asList("1"));
		assertNull(result);

		result = DateTimeParser.examineTimeOnly(asList("1", "2", "3", "4"));
		assertNull(result);

		result = DateTimeParser.examineTimeOnly(asList("1", "2", "3"));
		expected = LocalTime.of(1, 2, 3);
		assertNotNull(result);
		assertEquals(expected, result);

		result = DateTimeParser.examineTimeOnly(asList("1", "2"));
		expected = LocalTime.of(1, 2, 0);
		assertNotNull(result);
		assertEquals(expected, result);

		result = DateTimeParser.examineTimeOnly(asList("24", "2", "3"));
		assertNull(result);

		result = DateTimeParser.examineTimeOnly(asList("23", "60", "3"));
		assertNull(result);

		result = DateTimeParser.examineTimeOnly(asList("23", "59", "60"));
		assertNull(result);
	}


	@Test
	public void test_examineDateOnly() {
		LocalDate result;

		result = DateTimeParser.examineDateOnly(asList("1", "2"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("1", "2", "3", "4"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("1"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("123456789"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("20181304"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("20181232"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("20181204"));
		assertNotNull(result);
		assertEquals(LocalDate.of(2018, 12, 4), result);

		result = DateTimeParser.examineDateOnly(asList("2018", "12", "04"));
		assertNotNull(result);
		assertEquals(LocalDate.of(2018, 12, 4), result);

		result = DateTimeParser.examineDateOnly(asList("2018", "12", "4"));
		assertNotNull(result);
		assertEquals(LocalDate.of(2018, 12, 4), result);

		result = DateTimeParser.examineDateOnly(asList("04", "12", "2018"));
		assertNotNull(result);
		assertEquals(LocalDate.of(2018, 12, 4), result);

		result = DateTimeParser.examineDateOnly(asList("4", "12", "2018"));
		assertNotNull(result);
		assertEquals(LocalDate.of(2018, 12, 4), result);

		result = DateTimeParser.examineDateOnly(asList("004", "12", "2018"));
		assertNull(result);

		result = DateTimeParser.examineDateOnly(asList("29", "2", "2018"));
		assertNull(result);
	}


	@Test
	public void test_examineDateTime() {
		LocalDate dec4th = LocalDate.of(2018, 12, 4);
		LocalTime l33tTime = LocalTime.of(13, 37, 23);
		LocalTime zeroTime = LocalTime.of(0, 0, 0);
		ZoneOffset offset = ZoneId.systemDefault().getRules()
				.getOffset(LocalDateTime.of(dec4th, l33tTime));
		LocalDateTime result;

		result = DateTimeParser.examineDateTime(asList("20181204"));
		assertEquals(LocalDateTime.of(dec4th, zeroTime), result);

		result = DateTimeParser.examineDateTime(asList(String.valueOf(
				LocalDateTime.of(dec4th, l33tTime).toInstant(offset).toEpochMilli())));
		assertEquals(LocalDateTime.of(dec4th, l33tTime), result);

		result = DateTimeParser.examineDateTime(asList("2018", "12", "04"));
		assertEquals(LocalDateTime.of(dec4th, zeroTime), result);

		result = DateTimeParser.examineDateTime(asList("04", "12", "2018"));
		assertEquals(LocalDateTime.of(dec4th, zeroTime), result);

		result = DateTimeParser.examineDateTime(asList("04", "12", "2018", "13"));
		assertNull(result);

		result = DateTimeParser
				.examineDateTime(asList("04", "12", "2018", "13", "37"));
		assertEquals(LocalDateTime.of(dec4th, LocalTime.of(13, 37, 0)), result);

		result = DateTimeParser
				.examineDateTime(asList("04", "12", "2018", "13", "37", "23"));
		assertEquals(LocalDateTime.of(dec4th, l33tTime), result);

		result = DateTimeParser
				.examineDateTime(asList("04", "12", "2018", "13", "37", "23", "0"));
		assertNull(result);

		result = DateTimeParser
				.examineDateTime(asList("04", "12", "2018", "13", "37", "60"));
		assertNull(result);

		result = DateTimeParser
				.examineDateTime(asList("04", "13", "2018", "13", "37", "23"));
		assertNull(result);
	}
}
