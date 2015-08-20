package de.skaliant.wax.util;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class for converting strings into several other types. Currently, the following
 * types are supported:
 * 
 * <ul>
 * 	<li>All 8 primitive types and their wrappers</li>
 * 	<li>Object and String, as pass-through</li>
 * 	<li>Enums; by name, case-insensitive</li>
 * 	<li>BigInteger, BigDecimal</li>
 * 	<li>Arrays, Lists, and Sets of the aforemeantioned types</li>
 * 	<li>Date; possible formats are:</li>
 *  <ul>
 *    <li>All numeric, 8 digits; will be interpreted as yyyymmdd</li>
 *    <li>All numeric, more than 8 digits; will be interpreted as a millisecond timestamp</li>
 *    <li>
 *      Digits separated by any other chars, must form three groups of digits; first and last group
 *      will be checked for being 4 digits long, which will result in either y-m-d or d-m-y order
 *    </li>
 *  </ul>
 * </ul>
 * 
 * @author Udo Kastilan
 */
public class Converter
{
	public static Object convertValue(Type type, String input)
	{
		return convertValue(type, new String[] { input });
	}


	@SuppressWarnings("unchecked")
	public static Object convertValue(Type type, String[] input)
	{
		if ((input == null) || (input.length == 0))
		{
			return TypeUtils.defaultValue(type);
		}
		Class<?> raw = TypeUtils.rawType(type);
		Object v = null;

		if (raw == null)
		{
			return null;
		}
		try
		{
			if ((raw == String.class) || (raw == Object.class))
			{
				v = input[0];
			}
			if (raw.isPrimitive() || TypeUtils.isWrapper(raw))
			{
				/*
				 * Convert to a primitive value or its wrapper
				 */
				if ((raw == Boolean.TYPE) || (raw == Boolean.class))
				{
					v = Boolean.valueOf(input[0]);
				}
				else if ((raw == Byte.TYPE) || (raw == Byte.class))
				{
					v = Byte.valueOf(input[0]);
				}
				else if ((raw == Short.TYPE) || (raw == Short.class))
				{
					v = Short.valueOf(input[0]);
				}
				else if ((raw == Character.TYPE) || (raw == Character.class))
				{
					if (input[0].length() == 1)
					{
						v = Character.valueOf(input[0].charAt(0));
					}
				}
				else if ((raw == Integer.TYPE) || (raw == Integer.class))
				{
					v = Integer.valueOf(input[0]);
				}
				else if ((raw == Long.TYPE) || (raw == Long.class))
				{
					v = Byte.valueOf(input[0]);
				}
				else if ((raw == Float.TYPE) || (raw == Float.class))
				{
					v = Float.valueOf(input[0]);
				}
				else if ((raw == Double.TYPE) || (raw == Double.class))
				{
					v = Double.valueOf(input[0]);
				}
			}
			else if (raw == BigDecimal.class)
			{
				v = new BigDecimal(input[0]);
			}
			else if (raw == BigInteger.class)
			{
				v = new BigInteger(input[0]);
			}
			else if (Date.class.isAssignableFrom(raw))
			{
				if (input[0].matches("^\\d+$"))
				{
					if (input[0].length() == 8)
					{
						v = date(Integer.parseInt(input[0].substring(0, 4)),
									Integer.parseInt(input[0].substring(4, 6)),
									Integer.parseInt(input[0].substring(6)));
					}
					else if (input[0].length() > 8)
					{
						v = new Date(Long.parseLong(input[0]));
					}
				}
				else
				{
					String[] parts = input[0].split("[^0-9]");
					
					if (parts.length == 3)
					{
						if (parts[0].length() == 4)
						{
							v = date(Integer.parseInt(parts[0]),
									Integer.parseInt(parts[1]),
									Integer.parseInt(parts[2]));
						}
						else
						{
							v = date(Integer.parseInt(parts[2]),
									Integer.parseInt(parts[1]),
									Integer.parseInt(parts[0]));
						}
					}
				}
			}
			else if (raw.isEnum())
			{
				/*
				 * Convert enums
				 */
				Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) type;

				for (Enum<?> e : enumClass.getEnumConstants())
				{
					if (e.name().equalsIgnoreCase(input[0]))
					{
						v = e;
						break;
					}
				}
			}
			else if (raw.isArray())
			{
				/*
				 * Convert to an array
				 */
				List<Object> ls = new ArrayList<Object>(input.length);
				Class<?> ct = raw.getComponentType();
				String[] atom = new String[1];

				for (String s : input)
				{
					atom[0] = s;
					ls.add(convertValue(ct, atom));
				}
				v = Array.newInstance(ct, ls.size());
				for (int i = 0; i < ls.size(); i++)
				{
					Array.set(v, i, ls.get(i));
				}
			}
			else if (Collection.class.isAssignableFrom(raw)
					&& ((raw == Set.class) || (raw == List.class) || ((!raw.isInterface()) && !Modifier
							.isAbstract(raw.getModifiers()))))
			{
				/*
				 * Convert to a set, list or any other non-abstract collection
				 */
				Collection<Object> collie = null;

				if (raw == List.class)
				{
					collie = new ArrayList<Object>();
				}
				else if (raw == Set.class)
				{
					collie = new HashSet<Object>();
				}
				else
				{
					collie = (Collection<Object>) raw.newInstance();
				}
				if (collie != null)
				{
					Type elementType = Object.class;
					String[] atom = { null };

					for (Type t : TypeUtils.typeArguments(type))
					{
						elementType = t;
						break;
					}
					for (String s : input)
					{
						atom[0] = s;
						collie.add(convertValue(elementType, atom));
					}
				}
				v = collie;
			}
		}
		catch (RuntimeException ex)
		{
			//
		}
		catch (Exception ex)
		{
			//
		}
		if (raw.isPrimitive() && (v == null))
		{
			v = TypeUtils.defaultValue(raw);
		}
		return v;
	}
	
	
	static Date date(int y, int m, int d)
	{
		Calendar c = Calendar.getInstance();
		
		c.set(y, m, d, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
}
