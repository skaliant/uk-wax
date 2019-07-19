package de.skaliant.wax.util;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class TypeUtilsTest {
	@Test
	public void test_typeArguments() {
		Type[] args;
		Type type;

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getStringList");
		args = TypeUtils.typeArguments(type);
		assertEquals(asList(String.class), asList(args));

		type = getReturnTypeFromMethod(ForTestingOnly.class,
				"getMapOfIntegerString");
		args = TypeUtils.typeArguments(type);
		assertEquals(asList(Integer.class, String.class), asList(args));

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getRawList");
		args = TypeUtils.typeArguments(type);
		assertEquals(0, args.length);

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getWildcardList");
		args = TypeUtils.typeArguments(type);
		assertEquals(1, args.length);
		assertTrue(args[0] instanceof WildcardType);

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getSimpleType");
		args = TypeUtils.typeArguments(type);
		assertEquals(0, args.length);

		type = getReturnTypeFromMethod(ForTestingOnly.class,
				"getGenericTypeParameter");
		args = TypeUtils.typeArguments(type);
		assertEquals(0, args.length);
	}


	@Test
	public void test_getRawType() {
		Type type;

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getStringList");
		assertEquals(List.class, TypeUtils.getRawType(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class,
				"getMapOfIntegerString");
		assertEquals(Map.class, TypeUtils.getRawType(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getRawList");
		assertEquals(List.class, TypeUtils.getRawType(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getWildcardList");
		assertEquals(List.class, TypeUtils.getRawType(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getSimpleType");
		assertEquals(String.class, TypeUtils.getRawType(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class,
				"getGenericTypeParameter");
		assertNull(TypeUtils.getRawType(type));
	}


	@Test
	public void test_defaultValue() {
		assertEquals(Boolean.FALSE, TypeUtils.defaultValue(Boolean.TYPE));
		assertEquals(Byte.valueOf((byte) 0), TypeUtils.defaultValue(Byte.TYPE));
		assertEquals(Short.valueOf((short) 0), TypeUtils.defaultValue(Short.TYPE));
		assertEquals(Character.valueOf('\0'),
				TypeUtils.defaultValue(Character.TYPE));
		assertEquals(Integer.valueOf(0), TypeUtils.defaultValue(Integer.TYPE));
		assertEquals(Long.valueOf(0), TypeUtils.defaultValue(Long.TYPE));
		assertEquals(Float.valueOf(0), TypeUtils.defaultValue(Float.TYPE));
		assertEquals(Double.valueOf(0), TypeUtils.defaultValue(Double.TYPE));

		assertNull(TypeUtils.defaultValue(Boolean.class));
		assertNull(TypeUtils.defaultValue(String.class));
	}


	@Test
	public void test_getFirstGenericArgument() {
		Type type;

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getStringList");
		assertEquals(String.class, TypeUtils.getFirstGenericArgument(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class,
				"getMapOfIntegerString");
		assertEquals(Integer.class, TypeUtils.getFirstGenericArgument(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getRawList");
		assertNull(TypeUtils.getFirstGenericArgument(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getWildcardList");
		assertTrue(TypeUtils.getFirstGenericArgument(type) instanceof WildcardType);

		type = getReturnTypeFromMethod(ForTestingOnly.class, "getSimpleType");
		assertNull(TypeUtils.getFirstGenericArgument(type));

		type = getReturnTypeFromMethod(ForTestingOnly.class,
				"getGenericTypeParameter");
		assertNull(TypeUtils.getFirstGenericArgument(type));
	}


	@Test
	public void test_getWrapperFor() {
		assertEquals(Boolean.class, TypeUtils.getWrapperFor(Boolean.TYPE));
		assertEquals(Byte.class, TypeUtils.getWrapperFor(Byte.TYPE));
		assertEquals(Short.class, TypeUtils.getWrapperFor(Short.TYPE));
		assertEquals(Character.class, TypeUtils.getWrapperFor(Character.TYPE));
		assertEquals(Integer.class, TypeUtils.getWrapperFor(Integer.TYPE));
		assertEquals(Long.class, TypeUtils.getWrapperFor(Long.TYPE));
		assertEquals(Float.class, TypeUtils.getWrapperFor(Float.TYPE));
		assertEquals(Double.class, TypeUtils.getWrapperFor(Double.TYPE));

		assertNull(TypeUtils.getWrapperFor(Boolean.class));
		assertNull(TypeUtils.getWrapperFor(String.class));
	}


	@Test
	public void test_isPrimitive() {
		Type generic = getReturnTypeFromMethod(ForTestingOnly.class,
				"getGenericTypeParameter");

		assertTrue(TypeUtils.isPrimitive(Boolean.TYPE));
		assertTrue(TypeUtils.isPrimitive(Byte.TYPE));
		assertTrue(TypeUtils.isPrimitive(Short.TYPE));
		assertTrue(TypeUtils.isPrimitive(Character.TYPE));
		assertTrue(TypeUtils.isPrimitive(Integer.TYPE));
		assertTrue(TypeUtils.isPrimitive(Long.TYPE));
		assertTrue(TypeUtils.isPrimitive(Float.TYPE));
		assertTrue(TypeUtils.isPrimitive(Double.TYPE));

		assertFalse(TypeUtils.isPrimitive(Boolean.class));
		assertFalse(TypeUtils.isPrimitive(String.class));
		assertFalse(TypeUtils.isPrimitive(generic));
	}


	@Test
	public void test_collectPublicMethodsTopDown() {
		List<Method> result = TypeUtils
				.collectPublicMethodsTopDown(LowerClass.class);

		assertEquals(5, result.size());
		assertEquals("y", result.get(0).getName());
		assertEquals("a", result.get(1).getName());
		assertEquals(new HashSet<>(asList("b", "x", "z")),
				result.stream().skip(2).map(m -> m.getName()).collect(toSet()));
	}

	public static class UpperClass {
		public void x() {
			//
		}


		public void y() {
			//
		}


		public Object z() {
			return null;
		}
	}

	public static class MiddleClass extends UpperClass {
		public void a() {
			//
		}
	}

	public static class LowerClass extends MiddleClass {
		@Override
		public void x() {
			//
		}


		@Override
		public String z() {
			return null;
		}


		public void b() {
			//
		}
	}


	private static Type getReturnTypeFromMethod(Class<?> type,
			String methodName) {
		Type result = null;

		try {
			result = type.getMethod(methodName).getGenericReturnType();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return result;
	}

	public static class ForTestingOnly<T> {
		public List<String> getStringList() {
			return null;
		}


		public Map<Integer, String> getMapOfIntegerString() {
			return null;
		}


		@SuppressWarnings("rawtypes")
		public List getRawList() {
			return null;
		}


		public List<?> getWildcardList() {
			return null;
		}


		public String getSimpleType() {
			return null;
		}


		public T getGenericTypeParameter() {
			return null;
		}
	}
}
