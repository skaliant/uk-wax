package de.skaliant.wax.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import de.skaliant.wax.util.beans.Bean;


/**
 * Simple class to create a json string for a given object. The object may be
 * anything. Instances of Map, Collection, Number, String, Boolean are handled
 * in a special way as well as arrays and null references. Any other type is
 * treated as a bean with readable properties to be included in the result
 * (recursively).
 *
 * @author Udo Kastilan
 */
public class Json {
	/**
	 * Converts the Java object to a json string.
	 * 
	 * @param o
	 *          Object (may be null)
	 * @return Json
	 */
	public static String convert(Object o) {
		StringBuilder sb = new StringBuilder();

		convert(sb, o);
		return sb.toString();
	}


	/**
	 * Internal convert method.
	 * 
	 * @param sb
	 *          StringBuilder to append to
	 * @param o
	 *          Object
	 */
	private static void convert(StringBuilder sb, Object o) {
		if (o == null) {
			sb.append("null");
		} else if (o instanceof Boolean) {
			sb.append(o.toString());
		} else if (o instanceof Number) {
			sb.append(o.toString());
		} else if (o instanceof String) {
			convertString(sb, (String) o);
		} else if (o.getClass().isArray()) {
			convertArray(sb, o);
		} else if (o instanceof Collection) {
			convertCollection(sb, (Collection<?>) o);
		} else if (o instanceof Map) {
			convertMap(sb, (Map<?, ?>) o);
		} else {
			convertBean(sb, o);
		}
	}


	/**
	 * Converts a generic bean.
	 * 
	 * @param sb
	 * @param bean
	 */
	private static void convertBean(StringBuilder sb, Object bean) {
		Bean<?> b = Bean.wrap(bean);
		boolean first = true;

		sb.append('{');
		for (PropertyDescriptor pd : b.getProperties(Bean.Accessibility.READ_WRITE)) {
			if (pd.getReadMethod().getDeclaringClass() == Object.class) {
				continue;
			}
			if (!first) {
				sb.append(',');
			}
			convertString(sb, pd.getName());
			sb.append(':');
			convert(sb, b.get(pd.getName()));
			first = false;
		}
		sb.append('}');
	}


	/**
	 * Converts a string. Codes larger than 255 are converted to unicode hex
	 * sequences.
	 * 
	 * @param sb
	 * @param str
	 */
	private static void convertString(StringBuilder sb, String str) {
		sb.append('"');
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);

			switch (c) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '/':
					sb.append("\\/");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					if (c > 255) {
						String hx = Integer.toHexString(c);

						sb.append("\\u");
						switch (hx.length()) {
							case 1:
								sb.append('0');
							case 2:
								sb.append('0');
							case 3:
								sb.append('0');
								break;
						}
						sb.append(hx);
					} else {
						sb.append(c);
					}
					break;
			}
		}
		sb.append('"');
	}


	/**
	 * Converts a collection. Order depends on the iterator.
	 * 
	 * @param sb
	 * @param coll
	 */
	private static void convertCollection(StringBuilder sb, Collection<?> coll) {
		boolean first = true;

		sb.append('[');
		for (Object o : coll) {
			if (!first) {
				sb.append(',');
			}
			convert(sb, o);
			first = false;
		}
		sb.append(']');
	}


	/**
	 * Converts an array.
	 * 
	 * @param sb
	 * @param arr
	 */
	private static void convertArray(StringBuilder sb, Object arr) {
		int len = Array.getLength(arr);

		sb.append('[');
		for (int i = 0; i < len; i++) {
			if (i != 0) {
				sb.append(',');
			}
			convert(sb, Array.get(arr, i));
		}
		sb.append(']');
	}


	/**
	 * Converts a map.
	 * 
	 * @param sb
	 * @param map
	 */
	private static void convertMap(StringBuilder sb, Map<?, ?> map) {
		boolean first = true;

		sb.append('{');
		for (Map.Entry<?, ?> me : map.entrySet()) {
			if (!first) {
				sb.append(',');
			}
			convertString(sb, me.getKey().toString());
			sb.append(':');
			convert(sb, me.getValue());
			first = false;
		}
		sb.append('}');
	}
}
