package de.skaliant.wax.util.beans.exp;

public class BeanA {
	private String[] list;
	private BeanB child;
	private String text;
	private int number;


	public String[] getList() {
		return list;
	}


	public void setList(String[] list) {
		this.list = list;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public BeanB getChild() {
		return child;
	}


	public void setChild(BeanB child) {
		this.child = child;
	}
}
