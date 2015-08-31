package de.skaliant.wax.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * 
 *
 * @author Udo Kastilan
 */
@RunWith(JUnit4.class)
public class InjectorTest
{
	@Test
	public void testIntSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("intField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42, it.getIntField());
	}

	
	@Test
	public void testDateTimestamp()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("dateField", "4242424242");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(new Date(4242424242L), it.getDateField());
	}

	
	@Test
	public void testDateDigitsOnly()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("dateField", "20150501");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Converter.date(2015, 5, 1), it.getDateField());
	}

	
	@Test
	public void testDateSeparatedYymmdd()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("dateField", "2015-05-01");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Converter.date(2015, 5, 1), it.getDateField());
	}

	
	@Test
	public void testDateSeparatedYmd()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("dateField", "2015/5/1");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Converter.date(2015, 5, 1), it.getDateField());
	}

	
	@Test
	public void testDateSeparatedDmy()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("dateField", "1.5.2015");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Converter.date(2015, 5, 1), it.getDateField());
	}

	
	@Test
	public void testDateSeparatedDdmmyy()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("dateField", "01#05#2015");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Converter.date(2015, 5, 1), it.getDateField());
	}

	
	@Test
	public void testIntComplex()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		InjectionTarget itt = new InjectionTarget();
		InjectionTarget ittt = new InjectionTarget();
		
		it.setObjectField(itt);
		itt.setObjectField(ittt);
		
		dpp.addParam("objectField.objectField.intField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42, ittt.getIntField());
	}

	
	@Test
	public void testIntFailGracefully()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		InjectionTarget itt = new InjectionTarget();
		InjectionTarget ittt = new InjectionTarget();
		
		it.setObjectField(itt);
		itt.setObjectField(ittt);
		
		dpp.addParam("objectField.objectFieldX.intField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(0, ittt.getIntField());
	}

	
	@Test
	public void testIntFirstOnly()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("intField", new String[] { "42", "84" });
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42, it.getIntField());
	}

	
	@Test
	public void testIntTooBig()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("intField", "42424242424242424242424242");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(0, it.getIntField());
	}


	@Test
	public void testByteSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("byteField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42, it.getByteField());
	}


	@Test
	public void testCharSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("charField", "4");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals('4', it.getCharField());
	}


	@Test
	public void testCharTooLong()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("charField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(0, it.getCharField());
	}


	@Test
	public void testBooleanSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("boolField", "true");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(true, it.isBoolField());
	}


	@Test
	public void testLongSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("longField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42L, it.getLongField());
	}


	@Test
	public void testFloatSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("floatField", "4.2");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(4.2F, it.getFloatField());
	}


	@Test
	public void testDoubleSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("doubleField", "4.2");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(4.2D, it.getDoubleField());
	}


	@Test
	public void testShortSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("shortField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42, it.getShortField());
	}


	@Test
	public void testStringSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("stringField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals("42", it.getStringField());
	}


	@Test
	public void testBigDecimalSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("bigDecimalField", "4.2");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(new BigDecimal("4.2"), it.getBigDecimalField());
	}


	@Test
	public void testBigIntegerSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("bigIntegerField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(new BigInteger("42"), it.getBigIntegerField());
	}
	
	
	@Test
	public void testIntArray()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("intArrayField", new String[] { "42", "84", "126" });
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertTrue(Arrays.equals(new int[] { 42, 84, 126 }, it.getIntArrayField()));
	}
	
	
	@Test
	public void testIntArrayIndexed()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("intArrayField[2]", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(42, it.getIntArrayField()[2]);
	}
	
	
	@Test
	public void testIntegerList()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("integerListField", new String[] { "42", "84", "126" });
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Arrays.asList(new Integer(42), new Integer(84), new Integer(126)), it.getIntegerListField());
	}
	
	
	@Test
	public void testIntegerListIndexed()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("integerListField[2]", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(new Integer(42), it.getIntegerListField().get(2));
	}
	
	
	@Test
	public void testIntegerListGrow()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("integerListField[11]", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(12, it.getIntegerListField().size());
		Assert.assertNull(it.getIntegerListField().get(10));
		Assert.assertEquals(new Integer(42), it.getIntegerListField().get(11));
	}
	
	
	@Test
	public void testIntegerSet()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("integerSetField", new String[] { "42", "84", "126" });
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(new HashSet<Integer>(Arrays.asList(new Integer(42), new Integer(84), new Integer(126))), it.getIntegerSetField());
	}
	
	
	@Test
	public void testStringArray()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("stringArrayField", new String[] { "42", "84", "126" });
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertTrue(Arrays.equals(new String[] { "42", "84", "126" }, it.getStringArrayField()));
	}
	
	
	@Test
	public void testStringList()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("stringListField", new String[] { "42", "84", "126" });
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(Arrays.asList("42", "84", "126"), it.getStringListField());
	}
	
	
	@Test
	public void testStringAsObject()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("objectField", "42");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals("42", it.getObjectField());
	}


	@Test
	public void testEnumSimple()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("enumField", "TWO");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(InjectionEnum.TWO, it.getEnumField());
	}


	@Test
	public void testEnumCase()
	{
		DefaultParameterProvider dpp = new DefaultParameterProvider();
		InjectionTarget it = new InjectionTarget();
		
		dpp.addParam("enumField", "tWo");
		new Injector().injectBeanProperties(it, dpp, null);
		Assert.assertEquals(InjectionEnum.TWO, it.getEnumField());
	}
}
