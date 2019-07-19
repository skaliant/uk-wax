package de.skaliant.wax.util;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class for converting strings into several other types. Currently, the
 * following types are supported:
 * 
 * <ul>
 * <li>All 8 primitive types and their wrappers</li>
 * <li>Object and String, as pass-through</li>
 * <li>Enums; by name, case-insensitive</li>
 * <li>BigInteger, BigDecimal</li>
 * <li>Arrays, Lists, and Sets of the aforemeantioned types</li>
 * <li>Date; possible formats are:</li>
 * <ul>
 * <li>All numeric, 8 digits; will be interpreted as yyyymmdd</li>
 * <li>All numeric, more than 8 digits; will be interpreted as a millisecond
 * timestamp</li>
 * <li>Digits separated by any other chars, must form three groups of digits;
 * first and last group will be checked for being 4 digits long, which will
 * result in either y-m-d or d-m-y order</li>
 * </ul>
 * </ul>
 * 
 * @author Udo Kastilan
 */
public class Converter {

	public static Object convertValue(Type type, String input) {
		return convertValue(type, new String[] { input });
	}


	public static Object convertValue(Type type, String[] input) {
		if ((input == null) || (input.length == 0)) {
			return TypeUtils.defaultValue(type);
		}
		Class<?> raw = TypeUtils.getRawType(type);
		Object v = null;

		if (raw == null) {
			return null;
		}
		try {
			if ((raw == String.class) || (raw == Object.class)) {
				v = input[0];
			}
			if (raw.isPrimitive() || TypeUtils.isWrapper(raw)) {
				v = convertPrimitive(raw, input[0]);
			} else if (raw == BigDecimal.class) {
				v = new BigDecimal(input[0]);
			} else if (raw == BigInteger.class) {
				v = new BigInteger(input[0]);
			} else if (Date.class.equals(raw)) {
				v = convertToDate(DateTimeParser.parseDateTime(input[0]));
			} else if (LocalDateTime.class.equals(raw)) {
				v = DateTimeParser.parseDateTime(input[0]);
			} else if (LocalTime.class.equals(raw)) {
				v = DateTimeParser.parseTime(input[0]);
			} else if (LocalDate.class.equals(raw)) {
				v = DateTimeParser.parseDate(input[0]);
			} else if (raw.isEnum()) {
				v = convertEnum(raw, input[0]);
			} else if (raw.isArray()) {
				v = convertArray(raw, input);
			} else if (Collection.class.isAssignableFrom(raw)) {
				v = convertCollection(type, raw, input);
			}
		}
		catch (Exception ex) {
			//
		}

		return v;
	}


	private static Date convertToDate(LocalDateTime dateTime) {
		Date result = null;

		if (dateTime != null) {
			/*
			 * TODO should probably use client timezone instead ...
			 */
			result = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		}

		return result;
	}


	@SuppressWarnings("unchecked")
	private static Object convertCollection(Type type, Class<?> raw, String[] arr)
		throws Exception {
		Collection<Object> collie = null;

		if (raw == List.class) {
			collie = new ArrayList<>();
		} else if (raw == Set.class) {
			collie = new HashSet<>();
		} else if (!(raw.isInterface()
				|| Modifier.isAbstract(raw.getModifiers()))) {
			collie = (Collection<Object>) raw.newInstance();
		}
		if (collie != null) {
			Type elementType = Object.class;
			String[] atom = { null };

			for (Type t : TypeUtils.typeArguments(type)) {
				elementType = t;
				break;
			}
			for (String s : arr) {
				atom[0] = s;
				collie.add(convertValue(elementType, atom));
			}
		}

		return collie;
	}


	private static Object convertArray(Class<?> raw, String[] arr) {
		List<Object> ls = new ArrayList<>(arr.length);
		Class<?> ct = raw.getComponentType();
		String[] atom = new String[1];
		Object v;

		for (String s : arr) {
			atom[0] = s;
			ls.add(convertValue(ct, atom));
		}
		v = Array.newInstance(ct, ls.size());
		for (int i = 0; i < ls.size(); i++) {
			Array.set(v, i, ls.get(i));
		}

		return v;
	}


	@SuppressWarnings("unchecked")
	private static Object convertEnum(Class<?> raw, String s) {
		Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) raw;
		Object v = null;

		if (MiscUtils.isDigitsOnly(s)) {
			Enum<?>[] consts = enumClass.getEnumConstants();
			int ix = Integer.parseInt(s);

			if (ix < consts.length) {
				v = consts[ix];
			}

		} else {
			for (Enum<?> e : enumClass.getEnumConstants()) {
				if (e.name().equalsIgnoreCase(s)) {
					v = e;
					break;
				}
			}
		}

		return v;
	}


	private static Object convertPrimitive(Class<?> raw, String s) {
		Object v = null;

		if (MiscUtils.isNotBlank(s)) {
			try {
				if ((raw == Boolean.TYPE) || (raw == Boolean.class)) {
					v = Boolean.valueOf(s);
				} else if ((raw == Byte.TYPE) || (raw == Byte.class)) {
					v = Byte.valueOf(s);
				} else if ((raw == Short.TYPE) || (raw == Short.class)) {
					v = Short.valueOf(s);
				} else if ((raw == Character.TYPE) || (raw == Character.class)) {
					if (s.length() == 1) {
						v = Character.valueOf(s.charAt(0));
					}
				} else if ((raw == Integer.TYPE) || (raw == Integer.class)) {
					v = Integer.valueOf(s);
				} else if ((raw == Long.TYPE) || (raw == Long.class)) {
					v = Long.valueOf(s);
				} else if ((raw == Float.TYPE) || (raw == Float.class)) {
					Float f = Float.valueOf(s);

					if (!(f.isInfinite() || f.isNaN())) {
						v = f;
					}
				} else if ((raw == Double.TYPE) || (raw == Double.class)) {
					Double d = Double.valueOf(s);

					if (!(d.isInfinite() || d.isNaN())) {
						v = d;
					}
				}
			}
			catch (Exception ex) {
				//
			}
		}
		if (raw.isPrimitive() && (v == null)) {
			v = TypeUtils.defaultValue(raw);
		}

		return v;
	}
}
