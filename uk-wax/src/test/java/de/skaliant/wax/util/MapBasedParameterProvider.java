package de.skaliant.wax.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.core.model.ParameterProvider;


/**
 * 
 *
 * @author Udo Kastilan
 */
class MapBasedParameterProvider implements ParameterProvider {
	private Map<String, String[]> map = new HashMap<>();


	void addParam(String name, String value) {
		map.put(name, new String[] { value });
	}


	void addParam(String name, String[] values) {
		map.put(name, values);
	}


	void setMap(Map<String, String[]> map) {
		this.map = map;
	}


	@Override
	public boolean isParameterPresent(String name) {
		return map.containsKey(name);
	}


	@Override
	public List<String> getParameterNames() {
		return new ArrayList<>(map.keySet());
	}


	@Override
	public String getParameter(String name) {
		String[] strs = map.get(name);

		if (strs != null) {
			return strs[0];
		}
		return null;
	}


	@Override
	public String[] getParameterValues(String name) {
		return map.get(name);
	}


	@Override
	public Map<String, String[]> getParameters() {
		return map;
	}
}
