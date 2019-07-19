package de.skaliant.wax.util;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Utility methods concerning java data types.
 *
 * @author Udo Kastilan
 */
public class TypeUtils {
	/**
	 * Return the raw type of <code>type</code>. This is either the type itself
	 * (if instance of class) or the raw type part of a parameterized type. E.g a
	 * type representing "List&lt;String&gt" will return <code>List.class</code>,
	 * whereas a type representing "String.class" will just cast this type to
	 * Class.
	 * 
	 * @param type
	 * @return
	 */
	public static Class<?> getRawType(Type type) {
		Class<?> rawType = null;

		if (type instanceof Class) {
			rawType = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			rawType = getRawType(((ParameterizedType) type).getRawType());
		}
		return rawType;
	}


	/**
	 * Does this class have a public constructor without parameters?
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean hasDefaultConstructor(Class<?> cls) {
		for (Constructor<?> ctor : cls.getConstructors()) {
			if (ctor.getParameterTypes().length == 0) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Returns the type arguments of a parameterized type. E.g. if called with a
	 * type representing "List&lt;String&gt", this method will return an array
	 * that contains one element (String.class). If called with a type that is not
	 * an instance of {@link ParameterizedType}, then the result will be an empty
	 * array.
	 * 
	 * @param type
	 * @return
	 */
	public static Type[] typeArguments(Type type) {
		Type[] types = {};

		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;

			types = pt.getActualTypeArguments();
		}
		return types;
	}


	/**
	 * If called with a type representing one of the 8 primitive types, this
	 * method will return the corresponding wrapper class. Otherwise it will
	 * return <code>null</code>.
	 * 
	 * @param type
	 * @return
	 */
	public static Class<?> getWrapperFor(Type type) {
		Class<?> wrapper = null;

		if (isPrimitive(type)) {
			Class<?> prim = getRawType(type);

			if (prim == Boolean.TYPE) {
				wrapper = Boolean.class;
			} else if (prim == Byte.TYPE) {
				wrapper = Byte.class;
			} else if (prim == Short.TYPE) {
				wrapper = Short.class;
			} else if (prim == Character.TYPE) {
				wrapper = Character.class;
			} else if (prim == Integer.TYPE) {
				wrapper = Integer.class;
			} else if (prim == Long.TYPE) {
				wrapper = Long.class;
			} else if (prim == Float.TYPE) {
				wrapper = Float.class;
			} else if (prim == Double.TYPE) {
				wrapper = Double.class;
			}
		}
		return wrapper;
	}


	/**
	 * Checks whether the type given is a primitive type.
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isPrimitive(Type type) {
		return (type instanceof Class) && ((Class<?>) type).isPrimitive();
	}


	/**
	 * Checks whether the type given is one of the wrapper classes representing
	 * containers for primitive values.
	 * 
	 * @param t
	 * @return
	 */
	public static boolean isWrapper(Class<?> t) {
		return (t == Boolean.class) || (t == Byte.class) || (t == Short.class)
				|| (t == Character.class) || (t == Integer.class) || (t == Long.class)
				|| (t == Float.class) || (t == Double.class);
	}


	/**
	 * If the type <code>type</code> is a generic type, try to find the type of
	 * the first type argument. E.g. for a generic List declaration, this will
	 * deliver the list type.
	 * 
	 * @param type
	 *          Possibly parameterized type
	 * @return First generic type argument or null, if there is none
	 */
	public static Type getFirstGenericArgument(Type type) {
		for (Type t : TypeUtils.typeArguments(type)) {
			return t;
		}
		return null;
	}


	/**
	 * Returns the default value for a given type. If the type is a primitive
	 * type, it will return the corresponding boxed default value (e.g. 0 for
	 * <code>int</code>, <code>false</code> for <code>boolean</code> and so on).
	 * Otherwise it will return <code>null</code>.
	 * 
	 * @param type
	 * @return
	 */
	public static Object defaultValue(Type type) {
		Object v = null;

		if (isPrimitive(type)) {
			if (type == Boolean.TYPE) {
				v = Boolean.FALSE;
			} else if (type == Byte.TYPE) {
				v = Byte.valueOf((byte) 0);
			} else if (type == Short.TYPE) {
				v = Short.valueOf((short) 0);
			} else if (type == Character.TYPE) {
				v = Character.valueOf((char) 0);
			} else if (type == Integer.TYPE) {
				v = Integer.valueOf(0);
			} else if (type == Long.TYPE) {
				v = Long.valueOf(0);
			} else if (type == Float.TYPE) {
				v = Float.valueOf(0);
			} else if (type == Double.TYPE) {
				v = Double.valueOf(0);
			}
		}
		return v;
	}


	/**
	 * 
	 * @param class1
	 * @return
	 */
	public static List<Method> collectPublicMethodsTopDown(Class<?> cls) {
		List<Method> result = new ArrayList<>();
		Class<?> current = cls;

		while ((current != null) && !Object.class.equals(current)) {
			List<Method> methods = collectPublicMethods(current);

			addNonDuplicateMethods(result, methods);
			current = current.getSuperclass();
		}
		Collections.reverse(result);

		return result;
	}


	private static void addNonDuplicateMethods(List<Method> target,
			List<Method> source) {
		for (Method m : source) {
			if (!containsCompatibleMethod(target, m)) {
				target.add(m);
			}
		}
	}


	private static List<Method> collectPublicMethods(Class<?> cls) {
		List<Method> result = new ArrayList<>();

		for (Method m : cls.getDeclaredMethods()) {
			int modifiers = m.getModifiers();

			if (isPublic(modifiers) && !isAbstract(modifiers)) {
				result.add(m);
			}
		}

		return result;
	}


	private static boolean containsCompatibleMethod(Collection<Method> methods,
			Method method) {
		Class<?>[] args = method.getParameterTypes();

		for (Method m : methods) {
			if (m.getName().equals(method.getName())
					&& areParametersCompatible(args, m.getParameterTypes())) {
				return true;
			}
		}

		return false;
	}


	private static boolean areParametersCompatible(Class<?>[] superArgs,
			Class<?>[] args) {
		if (superArgs.length != args.length) {
			return false;
		}
		for (int i = 0, len = superArgs.length; i < len; i++) {
			if (!superArgs[i].isAssignableFrom(args[i])) {
				return false;
			}
		}

		return true;
	}
}
