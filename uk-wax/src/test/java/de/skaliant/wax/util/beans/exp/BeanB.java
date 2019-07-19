package de.skaliant.wax.util.beans.exp;

import java.util.List;


public class BeanB {
	private List<String> list;
	private BeanC child;
	private String text;


	public List<String> getList() {
		return list;
	}


	public void setList(List<String> list) {
		this.list = list;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public BeanC getChild() {
		return child;
	}


	public void setChild(BeanC child) {
		this.child = child;
	}
}
