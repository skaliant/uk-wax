package de.skaliant.wax.core.boot;

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
class SimpleParameterProvider implements ParameterProvider {
	private Map<String, String> map;


	SimpleParameterProvider(Map<String, String> map) {
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
		return map.get(name);
	}


	@Override
	public String[] getParameterValues(String name) {
		String v = map.get(name);

		return v == null ? null : new String[] { v };
	}


	@Override
	public Map<String, String[]> getParameters() {
		Map<String, String[]> mapp = new HashMap<>(map.size());

		for (Map.Entry<String, String> me : map.entrySet()) {
			mapp.put(me.getKey(), new String[] { me.getValue() });
		}
		return mapp;
	}
}
