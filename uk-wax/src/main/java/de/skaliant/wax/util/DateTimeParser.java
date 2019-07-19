package de.skaliant.wax.util;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class DateTimeParser {

	public static LocalDateTime parseDateTime(String s) {
		return examineDateTime(tokenize(s));
	}


	public static LocalDate parseDate(String s) {
		return examineDateOnly(tokenize(s));
	}


	public static LocalTime parseTime(String s) {
		return examineTimeOnly(tokenize(s));
	}


	static LocalDateTime examineDateTime(List<String> tokens) {
		LocalDateTime result = null;
		LocalDate date = null;
		LocalTime time = null;

		switch (tokens.size()) {
			case 1:
				if (tokens.get(0).length() == 8) {
					date = examineDateOnly(tokens);
					time = LocalTime.ofSecondOfDay(0);
				} else {
					result = LocalDateTime.ofInstant(
							Instant.ofEpochMilli(parseLong(tokens.get(0))),
							TimeZone.getDefault().toZoneId());
				}
				break;
			case 3:
				date = examineDateOnly(tokens);
				time = LocalTime.ofSecondOfDay(0);
				break;
			case 5:
				date = examineDateOnly(tokens.subList(0, 3));
				time = examineTimeOnly(tokens.subList(3, 5));
				break;
			case 6:
				date = examineDateOnly(tokens.subList(0, 3));
				time = examineTimeOnly(tokens.subList(3, 6));
				break;
		}

		if ((date != null) && (time != null)) {
			result = LocalDateTime.of(date, time);
		}

		return result;
	}


	static LocalDate examineDateOnly(List<String> tokens) {
		LocalDate result = null;
		int y = 0;
		int m = 0;
		int d = 0;

		switch (tokens.size()) {
			case 1:
				String t = tokens.get(0);

				if (t.length() == 8) {
					y = parseInt(t.substring(0, 4));
					m = parseInt(t.substring(4, 6));
					d = parseInt(t.substring(6));
				}
				break;
			case 3:
				String t0 = tokens.get(0);
				String t1 = tokens.get(1);
				String t2 = tokens.get(2);

				if ((t0.length() == 4) && (t1.length() <= 2) && (t2.length() <= 2)) {
					y = parseInt(t0);
					m = parseInt(t1);
					d = parseInt(t2);
				} else if ((t0.length() <= 2) && (t1.length() <= 2)
						&& (t2.length() == 4)) {
					d = parseInt(t0);
					m = parseInt(t1);
					y = parseInt(t2);
				}
				break;
			default:
				return null;
		}
		if ((d > 0) && (d < 32) && (m > 0) && (m < 13)) {
			try {
				result = LocalDate.of(y, m, d);
			}
			catch (DateTimeException ex) {
				// happens if date/month do not match (e.g. not a leap year); ignore
			}
		}

		return result;
	}


	static LocalTime examineTimeOnly(List<String> tokens) {
		LocalTime result = null;
		int h = 0;
		int m = 0;
		int s = 0;

		switch (tokens.size()) {
			case 3:
				s = parseInt(tokens.get(2));
			case 2:
				h = parseInt(tokens.get(0));
				m = parseInt(tokens.get(1));
				if ((h < 24) && (m < 60) && (s < 60)) {
					result = LocalTime.of(h, m, s);
				}
				break;
		}

		return result;
	}


	static List<String> tokenize(String input) {
		List<String> numericParts = new ArrayList<>(6);
		StringBuilder currentToken = new StringBuilder();
		boolean isDigit = false;
		final short ST_NUMBER = 0;
		final short ST_SEPARATOR = 1;
		short state = ST_NUMBER;
		char c = 0;

		for (int i = 0, len = input.length(); i < len; i++) {
			c = input.charAt(i);
			isDigit = (c >= '0') && (c <= '9');

			switch (state) {
				case ST_NUMBER:
					if (isDigit) {
						currentToken.append(c);
					} else {
						flushToken(currentToken, numericParts);
						state = ST_SEPARATOR;
					}
					break;
				case ST_SEPARATOR:
					if (isDigit) {
						state = ST_NUMBER;
						currentToken.append(c);
					}
					break;
			}
		}
		if (currentToken.length() != 0) {
			flushToken(currentToken, numericParts);
		}

		return numericParts;
	}


	private static void flushToken(StringBuilder token, List<String> list) {
		if (token.length() != 0) {
			list.add(token.toString());
			token.delete(0, token.length());
		}
	}

}
