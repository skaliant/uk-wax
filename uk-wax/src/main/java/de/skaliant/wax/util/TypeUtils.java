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

	
	public static Class<?> getWrapperFor(Type type)
	{
		Class<?> wrapper = null;
		
		if (isPrimitive(type))
		{
			Class<?> prim = rawType(type);
			
			if (prim == Boolean.TYPE)
			{
				wrapper = Boolean.class;
			}
			else if (prim == Byte.TYPE)
			{
				wrapper = Byte.class;
			}
			else if (prim == Short.TYPE)
			{
				wrapper = Short.class;
			}
			else if (prim == Character.TYPE)
			{
				wrapper = Character.class;
			}
			else if (prim == Integer.TYPE)
			{
				wrapper = Integer.class;
			}
			else if (prim == Long.TYPE)
			{
				wrapper = Long.class;
			}
			else if (prim == Float.TYPE)
			{
				wrapper = Float.class;
			}
			else if (prim == Double.TYPE)
			{
				wrapper = Double.class;
			}
		}
		return wrapper;
	}
	

	public static boolean isPrimitive(Type type)
	{
		return (type instanceof Class) && ((Class<?>) type).isPrimitive();
	}

	
	public static boolean isWrapper(Class<?> t)
	{
		return (t == Boolean.class) || (t == Byte.class) || (t == Short.class) || (t == Character.class) || (t == Integer.class) || (t == Long.class) || (t == Float.class) || (t == Double.class);
	}
	

	/**
	 * If the type <code>type</code> is a generic type, try to find the type of the first type argument.
	 * E.g. for a generic List declaration, this will deliver the list type.
	 * 
	 * @param type Possibly parameterized type
	 * @return First generic type argument or null, if there is none
	 */
	public static Type firstGenericArgument(Type type)
	{
		for (Type t : TypeUtils.typeArguments(type))
		{
			return t;
		}
		return null;
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

