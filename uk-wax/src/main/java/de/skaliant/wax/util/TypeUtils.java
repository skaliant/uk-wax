package de.skaliant.wax.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class TypeUtils
{
	public static Class<?> rawType(Type type)
	{
		Class<?> rawType = null;

		if (type instanceof Class)
		{
			rawType = (Class<?>) type;
		}
		else if (type instanceof ParameterizedType)
		{
			rawType = rawType(((ParameterizedType) type).getRawType());
		}
		return rawType;
	}


	public static Type[] typeArguments(Type type)
	{
		Type[] types = {};

		if (type instanceof ParameterizedType)
		{
			ParameterizedType pt = (ParameterizedType) type;

			types = pt.getActualTypeArguments();
		}
		return types;
	}


	public static boolean isPrimitive(Type type)
	{
		return (type instanceof Class) && ((Class<?>) type).isPrimitive();
	}

	
	public static boolean isWrapper(Class<?> t)
	{
		return (t == Boolean.class) || (t == Byte.class) || (t == Short.class) || (t == Character.class) || (t == Integer.class) || (t == Long.class) || (t == Float.class) || (t == Double.class);
	}


	public static Object defaultValue(Type type)
	{
		Object v = null;

		if (isPrimitive(type))
		{
			if (type == Boolean.TYPE)
			{
				v = Boolean.FALSE;
			}
			else if (type == Byte.TYPE)
			{
				v = Byte.valueOf((byte) 0);
			}
			else if (type == Short.TYPE)
			{
				v = Short.valueOf((short) 0);
			}
			else if (type == Character.TYPE)
			{
				v = Character.valueOf((char) 0);
			}
			else if (type == Integer.TYPE)
			{
				v = Integer.valueOf(0);
			}
			else if (type == Long.TYPE)
			{
				v = Long.valueOf(0);
			}
			else if (type == Float.TYPE)
			{
				v = Float.valueOf(0);
			}
			else if (type == Double.TYPE)
			{
				v = Double.valueOf(0);
			}
		}
		return v;
	}
}

