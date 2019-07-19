package de.skaliant.wax.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.upload.MultipartParser;
import de.skaliant.wax.upload.Part;
import de.skaliant.wax.upload.UploadFormatException;


/**
 * Map-based ParameterProvider used to combine query string and form field
 * parameters for file uploads.
 *
 * @author Udo Kastilan
 */
public class MapBasedParameterProvider implements ParameterProvider {
	private Map<String, String[]> params = new HashMap<>();
	private List<String> names = new ArrayList<>();


	/**
	 * Create an instance based on the default ParameterProvider (typically the
	 * Call) and a MultipartParser.
	 * 
	 * @param pap
	 *          Default parameters from query string
	 * @param mup
	 *          MultipartParser for upload
	 * @param encoding
	 *          Encoding to be used for evaluating uploaded form fields
	 * @return Instance
	 * @throws IOException
	 * @throws UploadFormatException
	 */
	public static MapBasedParameterProvider create(ParameterProvider pap, MultipartParser mup, String encoding)
		throws IOException, UploadFormatException {
		Map<String, String[]> params = new HashMap<>();
		List<String> names = new ArrayList<>();
		/*
		 * 1.) Copy query string parameters into map and list; they should come
		 * first
		 */
		params.putAll(pap.getParameters());
		names.addAll(pap.getParameterNames());

		for (Part p : mup.getAllParts()) {
			if (!p.isFile()) {
				String n = p.getName();
				String v = p.getValue(encoding);

				if (!names.contains(n)) {
					names.add(n);
					params.put(n, new String[] { v });
				} else {
					String[] vs = params.get(n);
					String[] nvs = new String[vs.length + 1];
					/*
					 * TODO: not very efficient in case there are many form fields sharing
					 * the same name
					 */
					System.arraycopy(vs, 0, nvs, 0, vs.length);
					nvs[nvs.length - 1] = v;
					params.put(n, nvs);
				}
			}
		}
		return new MapBasedParameterProvider(names, params);
	}


	/**
	 * Create an instance based on a list and a map. Though the map already
	 * contains the names and there is a risk that list and map may differ due to
	 * an error, we should preserve the order the parameter came in.
	 * 
	 * @param names
	 *          Names only, for the order
	 * @param params
	 *          Names and values
	 */
	public MapBasedParameterProvider(List<String> names, Map<String, String[]> params) {
		this.names.addAll(names);
		this.params.putAll(params);
	}


	@Override
	public boolean isParameterPresent(String name) {
		return params.containsKey(name);
	}


	@Override
	public List<String> getParameterNames() {
		return names;
	}


	@Override
	public String getParameter(String name) {
		String[] arr = params.get(name);

		return ((arr == null) || (arr.length == 0)) ? null : arr[0];
	}


	@Override
	public String[] getParameterValues(String name) {
		return params.get(name);
	}


	@Override
	public Map<String, String[]> getParameters() {
		return params;
	}
}
