package de.skaliant.wax.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class InjectionTarget {
	private int intField = 0;
	private char charField = 0;
	private boolean boolField = false;
	private byte byteField = 0;
	private short shortField = 0;
	private long longField = 0;
	private float floatField = 0;
	private double doubleField = 0;
	private Object objectField = null;
	private String stringField = null;
	private BigDecimal bigDecimalField = null;
	private BigInteger bigIntegerField = null;
	private int[] intArrayField = new int[10];
	private String[] stringArrayField = new String[10];
	private InjectionEnum enumField = null;
	private List<Integer> integerListField = new ArrayList<Integer>();
	private List<String> stringListField = new ArrayList<String>();
	private Set<Integer> integerSetField = new HashSet<Integer>();
	private Date dateField = null;


	public Date getDateField() {
		return dateField;
	}


	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}


	public BigInteger getBigIntegerField() {
		return bigIntegerField;
	}


	public void setBigIntegerField(BigInteger bigIntegerField) {
		this.bigIntegerField = bigIntegerField;
	}


	public InjectionEnum getEnumField() {
		return enumField;
	}


	public void setEnumField(InjectionEnum enumField) {
		this.enumField = enumField;
	}


	public List<Integer> getIntegerListField() {
		return integerListField;
	}


	public void setIntegerListField(List<Integer> integerListField) {
		this.integerListField = integerListField;
	}


	public List<String> getStringListField() {
		return stringListField;
	}


	public void setStringListField(List<String> stringListField) {
		this.stringListField = stringListField;
	}


	public Set<Integer> getIntegerSetField() {
		return integerSetField;
	}


	public void setIntegerSetField(Set<Integer> integerSetField) {
		this.integerSetField = integerSetField;
	}


	public int getIntField() {
		return intField;
	}


	public String getStringField() {
		return stringField;
	}


	public void setStringField(String stringField) {
		this.stringField = stringField;
	}


	public BigDecimal getBigDecimalField() {
		return bigDecimalField;
	}


	public void setBigDecimalField(BigDecimal bigDecimalField) {
		this.bigDecimalField = bigDecimalField;
	}


	public Object getObjectField() {
		return objectField;
	}


	public void setObjectField(Object objectField) {
		this.objectField = objectField;
	}


	public int[] getIntArrayField() {
		return intArrayField;
	}


	public void setIntArrayField(int[] intArrayField) {
		this.intArrayField = intArrayField;
	}


	public String[] getStringArrayField() {
		return stringArrayField;
	}


	public void setStringArrayField(String[] stringArrayField) {
		this.stringArrayField = stringArrayField;
	}


	public void setIntField(int intField) {
		this.intField = intField;
	}


	public char getCharField() {
		return charField;
	}


	public void setCharField(char charField) {
		this.charField = charField;
	}


	public boolean isBoolField() {
		return boolField;
	}


	public void setBoolField(boolean boolField) {
		this.boolField = boolField;
	}


	public byte getByteField() {
		return byteField;
	}


	public void setByteField(byte byteField) {
		this.byteField = byteField;
	}


	public short getShortField() {
		return shortField;
	}


	public void setShortField(short shortField) {
		this.shortField = shortField;
	}


	public long getLongField() {
		return longField;
	}


	public void setLongField(long longField) {
		this.longField = longField;
	}


	public float getFloatField() {
		return floatField;
	}


	public void setFloatField(float floatField) {
		this.floatField = floatField;
	}


	public double getDoubleField() {
		return doubleField;
	}


	public void setDoubleField(double doubleField) {
		this.doubleField = doubleField;
	}
}
