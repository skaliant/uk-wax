package de.skaliant.wax.util.beans.exp;

import java.util.ArrayList;
import java.util.List;


public class BeanC {
	private List<Integer> list = new ArrayList<>();
	private String text;


	public List<Integer> getList() {
		return list;
	}


	public void setList(List<Integer> list) {
		this.list = list;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

}
