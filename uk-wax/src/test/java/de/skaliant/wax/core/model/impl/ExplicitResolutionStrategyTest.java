package de.skaliant.wax.core.model.impl;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.skaliant.wax.app.Guardian;
import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.impl.controllers.AnnotatedController;
import de.skaliant.wax.core.model.impl.controllers.AnnotatedWithSimpleDefaultAction;
import de.skaliant.wax.core.model.impl.controllers.AnnotatedWithSuffixedDefaultAction;
import de.skaliant.wax.core.model.impl.controllers.GuardedActions;
import de.skaliant.wax.core.model.impl.controllers.GuardedController;
import de.skaliant.wax.core.model.impl.controllers.IndexController;
import de.skaliant.wax.core.model.impl.controllers.TestGuardian1;
import de.skaliant.wax.core.model.impl.controllers.TestGuardian2;
import de.skaliant.wax.core.model.impl.controllers.UnannotatedController;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class ExplicitResolutionStrategyTest {
	@Test
	public void test_allEmpty() {
		List<String> packages = emptyList();
		List<String> classes = emptyList();
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 0);
	}


	@Test
	public void test_controllersEmpty() {
		List<String> packages = asList(
				"de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = emptyList();
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 0);
	}


	@Test
	public void test_emptyControllersIgnored() {
		List<String> packages = asList(
				"de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = asList("EmptyController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 0);
	}


	@Test
	public void test_duplicateNameIgnored() {
		List<String> packages = asList(
				"de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = asList("NamedController1", "NamedController2");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 1);
	}


	@Test
	public void test_singleDefaultController() {
		List<String> packages = asList(
				"de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = asList("AnnotatedDefaultController1",
				"AnnotatedDefaultController2");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 2);
	}


	@Test
	public void test_packageAndControllersSimple() {
		List<String> packages = asList(
				"de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = asList("UnannotatedController",
				"AnnotatedController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 2);

		// @formatter:off
		wrap(result, "anno")
			.assertController(AnnotatedController.class, "anno", null, false)
			.assertInitMethods("initMethod1", "initMethod2")
			.assertExitMethods("exitMethod1", "exitMethod2")
			.assertAction("action1", "someMethod", null, false)
			.assertAction("action2", "anotherMethod", null, true)
			;

		wrap(result, "unannotated")
			.assertController(UnannotatedController.class, "unannotated", null, false)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("some", "someAction", null, false)
			.assertAction("another", "anotherAction", null, false)
			.assertAction("third", "third", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_packageAndControllersSimpleAndFull() {
		List<String> packages = Arrays
				.asList("de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = Arrays.asList("UnannotatedController",
				"de.skaliant.wax.core.model.impl.controllers.AnnotatedController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 2);

		// @formatter:off
		wrap(result, "anno")
			.assertController(AnnotatedController.class, "anno", null, false)
			.assertInitMethods("initMethod1", "initMethod2")
			.assertExitMethods("exitMethod1", "exitMethod2")
			.assertAction("action1", "someMethod", null, false)
			.assertAction("action2", "anotherMethod", null, true)
			;

		wrap(result, "unannotated")
			.assertController(UnannotatedController.class, "unannotated", null, false)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("some", "someAction", null, false)
			.assertAction("another", "anotherAction", null, false)
			.assertAction("third", "third", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_packageAndControllersMixed() {
		List<String> packages = Arrays.asList("de.skaliant.wax.core.model");
		List<String> classes = Arrays.asList(
				"impl.controllers.UnannotatedController",
				"impl.controllers.AnnotatedController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 2);

		// @formatter:off
		wrap(result, "anno")
			.assertController(AnnotatedController.class, "anno", null, false)
			.assertInitMethods("initMethod1", "initMethod2")
			.assertExitMethods("exitMethod1", "exitMethod2")
			.assertAction("action1", "someMethod", null, false)
			.assertAction("action2", "anotherMethod", null, true)
			;

		wrap(result, "unannotated")
			.assertController(UnannotatedController.class, "unannotated", null, false)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("some", "someAction", null, false)
			.assertAction("another", "anotherAction", null, false)
			.assertAction("third", "third", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_controllersFull() {
		List<String> packages = Collections.emptyList();
		List<String> classes = Arrays.asList(
				"de.skaliant.wax.core.model.impl.controllers.UnannotatedController",
				"de.skaliant.wax.core.model.impl.controllers.AnnotatedController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 2);

		// @formatter:off
		wrap(result, "anno")
			.assertController(AnnotatedController.class, "anno", null, false)
			.assertInitMethods("initMethod1", "initMethod2")
			.assertExitMethods("exitMethod1", "exitMethod2")
			.assertAction("action1", "someMethod", null, false)
			.assertAction("action2", "anotherMethod", null, true)
			;

		wrap(result, "unannotated")
			.assertController(UnannotatedController.class, "unannotated", null, false)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("some", "someAction", null, false)
			.assertAction("another", "anotherAction", null, false)
			.assertAction("third", "third", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_simpleDefaultAction() {
		List<String> packages = Collections.emptyList();
		List<String> classes = Arrays.asList(
				"de.skaliant.wax.core.model.impl.controllers.AnnotatedWithSimpleDefaultAction");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 1);

		// @formatter:off
		wrap(result, "simple")
			.assertController(AnnotatedWithSimpleDefaultAction.class, "simple", null, true)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("index", "index", null, true)
			.assertAction("normal", "normal", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_suffixedDefaultAction() {
		List<String> packages = Collections.emptyList();
		List<String> classes = Arrays.asList(
				"de.skaliant.wax.core.model.impl.controllers.AnnotatedWithSuffixedDefaultAction");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 1);

		// @formatter:off
		wrap(result, "suffixed")
			.assertController(AnnotatedWithSuffixedDefaultAction.class, "suffixed", null, true)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("index", "indexAction", null, true)
			.assertAction("normal", "normalAction", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_guardedController() {
		List<String> packages = Collections.emptyList();
		List<String> classes = Arrays.asList(
				"de.skaliant.wax.core.model.impl.controllers.GuardedController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 1);

		// @formatter:off
		wrap(result, "guarded")
			.assertController(GuardedController.class, "guarded", TestGuardian1.class, true)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("default", "mainMethod", null, true)
			.assertAction("normal", "otherMethod", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_guardedActions() {
		List<String> packages = Collections.emptyList();
		List<String> classes = Arrays
				.asList("de.skaliant.wax.core.model.impl.controllers.GuardedActions");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 1);

		// @formatter:off
		wrap(result, "guardedactions")
			.assertController(GuardedActions.class, "guardedactions", null, true)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("guarded1", "guarded1", TestGuardian1.class, false)
			.assertAction("guarded2", "guarded2", TestGuardian2.class, false)
			.assertAction("unguarded", "unguarded", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_indexUnannotated() {
		List<String> packages = Arrays
				.asList("de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = Arrays.asList("UnannotatedController",
				"IndexController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 2);

		// @formatter:off
		wrap(result, "unannotated")
			.assertController(UnannotatedController.class, "unannotated", null, false)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("some", "someAction", null, false)
			.assertAction("another", "anotherAction", null, false)
			.assertAction("third", "third", null, false)
			;

		wrap(result, "index")
			.assertController(IndexController.class, "index", null, true)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("index", "indexAction", null, true)
			.assertAction("normal", "normalAction", null, false)
			;
		// @formatter:on
	}


	@Test
	public void test_indexSingle() {
		List<String> packages = Arrays
				.asList("de.skaliant.wax.core.model.impl.controllers");
		List<String> classes = Arrays.asList("UnannotatedController");
		ExplicitResolutionStrategy proband = new ExplicitResolutionStrategy(
				packages, classes);
		Map<String, ControllerInfo> result;

		result = proband.getAll();
		assertResult(result, 1);

		// @formatter:off
		wrap(result, "unannotated")
			.assertController(UnannotatedController.class, "unannotated", null, true)
			.assertInitMethods()
			.assertExitMethods()
			.assertAction("some", "someAction", null, false)
			.assertAction("another", "anotherAction", null, false)
			.assertAction("third", "third", null, false)
			;
		// @formatter:on
	}


	private static void assertResult(Map<String, ControllerInfo> result,
			int controllerCount) {
		assertNotNull(result);
		assertEquals(controllerCount, result.size());
		assertMaxOneDefaultController(result.values());
	}


	private static void assertMaxOneDefaultController(
			Collection<ControllerInfo> controllers) {
		assertTrue(controllers.stream().filter(ControllerInfo::isDefaultController)
				.count() < 2);
	}


	private static AssertionHelper wrap(Map<String, ControllerInfo> result,
			String name) {
		return new AssertionHelper(result.get(name));
	}

	private static class AssertionHelper {
		private ControllerInfo ci;


		private AssertionHelper(ControllerInfo ci) {
			this.ci = ci;
		}


		private AssertionHelper assertController(Class<?> ctrlClass, String name,
				Class<? extends Guardian> guardian, boolean isDefault) {
			assertControllerData(ci, ctrlClass, name, guardian, isDefault);
			return this;
		}


		private AssertionHelper assertInitMethods(String... names) {
			assertMethods(ci.getInitMethods(), names);
			return this;
		}


		private AssertionHelper assertExitMethods(String... names) {
			assertMethods(ci.getExitMethods(), names);
			return this;
		}


		private AssertionHelper assertAction(String name, String methodName,
				Class<? extends Guardian> guardian, boolean isDefault) {
			assertActionData(ci.findAction(name), name, methodName, guardian,
					isDefault);
			return this;
		}
	}


	private static void assertControllerData(ControllerInfo ci,
			Class<?> ctrlClass, String name, Class<? extends Guardian> guardian,
			boolean isDefault) {
		ActionInfo defaultAction;

		assertNotNull(ci);
		assertEquals(name, ci.getName());
		assertEquals(ctrlClass, ci.getType());
		if (guardian == null) {
			assertNull(ci.getGuardian());
			assertFalse(ci.isGuarded());
		} else {
			assertEquals(guardian, ci.getGuardian());
			assertTrue(ci.isGuarded());
		}
		assertEquals(isDefault, ci.isDefaultController());
		assertTrue(ci.getAllActions().stream().filter(ActionInfo::isDefaultAction)
				.count() < 2);
		defaultAction = ci.getAllActions().stream()
				.filter(ActionInfo::isDefaultAction).findFirst().orElse(null);
		if (defaultAction == null) {
			assertNull(ci.getDefaultAction());
		} else {
			assertSame(defaultAction, ci.getDefaultAction());
		}
	}


	private static void assertMethods(List<Method> methods, String... names) {
		List<String> expected = new ArrayList<>(Arrays.asList(names));
		List<String> present = methods.stream().map(m -> m.getName()).sorted()
				.collect(toList());

		Collections.sort(expected);

		assertEquals(expected, present);
	}


	private static void assertActionData(ActionInfo ai, String name,
			String methodName, Class<? extends Guardian> guardian,
			boolean isDefault) {
		assertNotNull(ai);
		assertNotNull(ai.getMethod());
		assertEquals(name, ai.getName());
		assertEquals(methodName, ai.getMethod().getName());
		if (guardian == null) {
			assertNull(ai.getGuardian());
			assertFalse(ai.isGuarded());
		} else {
			assertEquals(guardian, ai.getGuardian());
			assertTrue(ai.isGuarded());
		}
		assertEquals(isDefault, ai.isDefaultAction());
	}
}
