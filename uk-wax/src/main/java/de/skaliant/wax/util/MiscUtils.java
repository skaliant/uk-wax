package de.skaliant.wax.util;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class MiscUtils {
	/**
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return (s == null) || (s.length() == 0);
	}


	/**
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s) {
		return (s != null) && (s.length() != 0);
	}


	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isBlank(String s) {
		return isEmpty(s) || (s.trim().length() == 0);
	}


	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotBlank(String s) {
		return isNotEmpty(s) && (s.trim().length() != 0);
	}


	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDigitsOnly(String s) {
		if (isEmpty(s)) {
			return false;
		}
		char c = 0;

		for (int i = 0, len = s.length(); i < len; i++) {
			c = s.charAt(i);
			if ((c < '0') || (c > '9')) {
				return false;
			}
		}

		return true;
	}


	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String firstLetterUpperCase(String s) {
		if (isEmpty(s)) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}


	/**
	 * 
	 * @param yyyy
	 * @param mm
	 * @param dd
	 * @return
	 */
	public static Date date(int yyyy, int mm, int dd) {
		return date(yyyy, mm, dd, 0, 0, 0);
	}


	/**
	 * 
	 * @param yyyy
	 * @param mm
	 * @param dd
	 * @param hh
	 * @param mi
	 * @param ss
	 * @return
	 */
	public static Date date(int yyyy, int mm, int dd, int hh, int mi, int ss) {
		Calendar c = Calendar.getInstance();

		c.set(yyyy, mm - 1, dd, hh, mi, ss);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}


	/**
	 * @param val
	 * @return
	 */
	@SafeVarargs
	public static <T> Set<T> createSet(T... val) {
		Set<T> set = new HashSet<T>(val.length);

		for (T t : val) {
			set.add(t);
		}

		return set;
	}


	public static <T> T getFirstElementOf(Collection<T> coll) {
		if (coll != null) {
			for (T t : coll) {
				return t;
			}
		}

		return null;
	}


	public static <T> T getLastElementOf(Collection<T> coll) {
		T last = null;

		if (coll != null) {
			for (T t : coll) {
				last = t;
			}
		}

		return last;
	}


	/**
	 * 
	 * @param c
	 * @return
	 */
	public static <C extends Closeable> C close(C c) {
		if (c != null) {
			try {
				c.close();
				c = null;
			}
			catch (Exception ex) {
				//
			}
		}
		return c;
	}


	/**
	 * @param test
	 * @param def
	 * @return
	 */
	public static <T> T nvl(T test, T def) {
		return test == null ? def : test;
	}


	/**
	 * @param e
	 * @param type
	 * @return
	 */
	public static <T> List<T> list(Enumeration<?> e, Class<T> type) {
		List<T> ls = new ArrayList<>();

		while (e.hasMoreElements()) {
			ls.add(type.cast(e.nextElement()));
		}
		return ls;
	}


	public static int countChar(String str, char c) {
		int count = 0;

		if (str != null) {
			for (int i = 0, len = str.length(); i < len; i++) {
				if (str.charAt(i) == c) {
					count++;
				}
			}
		}

		return count;
	}


	public static int countChars(String str, String chars) {
		int count = 0;

		if (str != null) {
			for (int i = 0, len = str.length(); i < len; i++) {
				if (chars.indexOf(str.charAt(i)) != -1) {
					count++;
				}
			}
		}

		return count;
	}


	/**
	 * 
	 * @param str
	 * @param sep
	 * @return
	 */
	public static List<String> splitAtChar(String str, char sep) {
		return splitAtAnyCharOf(str, String.valueOf(sep));
	}


	/**
	 * 
	 * @param str
	 * @param sep
	 * @return
	 */
	public static List<String> splitAtAnyCharOf(String str, String sep) {
		List<String> result;

		if (str == null) {
			result = new ArrayList<>(0);
		} else {
			int sepCount = countChars(str, sep);

			if (sepCount == 0) {
				result = new ArrayList<>(1);
				result.add(str);
			} else {
				result = splitInternal(str, sep, sepCount);
			}
		}

		return result;
	}


	private static List<String> splitInternal(String str, String sep,
			int sepCount) {
		List<String> result = new ArrayList<>(sepCount + 1);
		boolean prevWasSeparator = true;

		for (StringTokenizer st = new StringTokenizer(str, sep, true); st
				.hasMoreTokens();) {
			String token = st.nextToken();

			if (sep.contains(token)) {
				if (prevWasSeparator) {
					result.add("");
				} else {
					prevWasSeparator = true;
				}
			} else {
				result.add(token);
				prevWasSeparator = false;
			}
		}
		if (prevWasSeparator) {
			result.add("");
		}

		return result;
	}
}
