package de.skaliant.wax.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.skaliant.wax.util.MiscUtils;


/**
 * Simple XML-based configuration with named values. Each configuration entry
 * may have one or more values. The rules for the XML document structure are as
 * follows:
 * 
 * <ul>
 * <li>The name of the root element is irrelevant, you may name it as you like
 * (though I recommend "conf" or "config")</li>
 * <li>For each configuration entry, give a "entry" element defining both name
 * and value(s)</li>
 * <li>The name of the entry is to be given via "id" attribute</li>
 * <li>If the the entry has 1 value, give it as text content of the "entry"
 * element</li>
 * <li>If the entry has more than 1 value, insert a "value" element with a text
 * node for each value</li>
 * </ul>
 *
 * Example:
 * 
 * <pre>
 * &lt;?xml version="1.0"?&gt;
 * &lt;config&gt;
 *   &lt;entry id="type"&gt;car&lt;/entry&gt;
 *   &lt;entry id="colors"&gt;
 *     &lt;value&gt;red&lt;/value&gt;
 *     &lt;value&gt;green&lt;/value&gt;
 *     &lt;value&gt;blue&lt;/value&gt;
 *   &lt;/entry&gt;
 * &lt;/config&gt;
 * </pre>
 *
 * @author Udo Kastilan
 */
public class Config {
	private Map<String, List<String>> entries = new HashMap<>();


	/**
	 * Load configuration from a given file.
	 * 
	 * @param file
	 *          File
	 * @return Configuration
	 * @throws Exception
	 */
	public static Config load(File file)
		throws Exception {
		Config conf = null;

		if (file.isFile()) {
			InputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));
				conf = load(in);
			}
			finally {
				MiscUtils.close(in);
			}
		} else {
			conf = new Config();
		}
		return conf;
	}


	/**
	 * Load configuration from a stream. The stream will not be closed by this
	 * method.
	 * 
	 * @param source
	 *          Source stream
	 * @return Configuration
	 * @throws Exception
	 */
	public static Config load(InputStream source)
		throws Exception {
		Config conf = new Config();
		Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(source);

		for (Element e : elements(dom.getElementsByTagName("entry"))) {
			String id = e.getAttribute("id");

			if ((id != null) && (id.length() != 0)) {
				if (e.hasAttribute("value")) {
					conf.setValue(id, e.getAttribute("value"));
				} else if (e.hasChildNodes()) {
					List<String> ls = new ArrayList<>(3);

					for (Element v : elements(e.getElementsByTagName("value"))) {
						ls.add(v.getTextContent());
					}
					switch (ls.size()) {
						case 0:
							conf.setValue(id, e.getTextContent());
							break;
						case 1:
							conf.setValue(id, ls.get(0));
							break;
						default:
							conf.setValues(id, ls);
							break;
					}
				}
			}
		}
		return conf;
	}


	/**
	 * Get all entry ids (parameter names) available as a set.
	 * 
	 * @return Set of ids
	 */
	public Set<String> getIds() {
		return entries.keySet();
	}


	/**
	 * Get the value count for a given entry.
	 * 
	 * @param id
	 *          Entry id
	 * @return Number of values
	 */
	public int getValueCount(String id) {
		List<String> ls = entries.get(id);
		int count = 0;

		if (ls != null) {
			count = ls.size();
		}
		return count;
	}


	/**
	 * Check whether an entry is available.
	 * 
	 * @param id
	 *          Entry id
	 * @return Is it available
	 */
	public boolean hasEntry(String id) {
		return entries.containsKey(id);
	}


	/**
	 * Get all values for an entry.
	 * 
	 * @param id
	 *          Entry id
	 * @return Values, or null if the entry is not available
	 */
	public List<String> getValues(String id) {
		return entries.get(id);
	}


	/**
	 * Get the (first) value of an entry.
	 * 
	 * @param id
	 *          Entry id
	 * @return Value, or null if the value is not available
	 */
	public String getValue(String id) {
		return getValue(id, null);
	}


	/**
	 * Try to interpret the value as a boolean value using Boolean.valueOf().
	 * Returns the default value if the entry is not available.
	 * 
	 * @param id
	 *          Entry id
	 * @param def
	 *          Default value
	 * @return Value or default value
	 */
	public boolean getBooleanValue(String id, boolean def) {
		String s = getValue(id, null);

		if (s != null) {
			return Boolean.valueOf(s);
		}
		return def;
	}


	/**
	 * Try to interpret the value as a boolean value using Boolean.valueOf().
	 * Returns false if the entry is not available.
	 * 
	 * @param id
	 *          Entry id
	 * @return Boolean value
	 */
	public boolean getBooleanValue(String id) {
		String s = getValue(id, null);

		if (s != null) {
			return Boolean.valueOf(s);
		}
		return false;
	}


	/**
	 * Get the value of an entry as Integer object.
	 * 
	 * @param id
	 *          Entry id
	 * @param def
	 *          Default value to be returned if the entry is not available or
	 *          cannot be converted into a number
	 * @return Value or default value
	 */
	public int getIntValue(String id, int def) {
		String s = getValue(id, null);
		int v = def;

		if (s != null) {
			try {
				v = Integer.valueOf(s);
			}
			catch (NumberFormatException ex) {
				//
			}
		}
		return v;
	}


	/**
	 * Get the value of an entry as Integer object.
	 * 
	 * @param id
	 *          Entry id
	 * @return Converted value or null, if the entry is not available
	 * @throws NumberFormatException
	 *           If the value cannot be converted into a number
	 */
	public Integer getIntValue(String id)
		throws NumberFormatException {
		String s = getValue(id, null);

		if (s != null) {
			return Integer.valueOf(s);
		}
		return null;
	}


	/**
	 * Get a (the first) value.
	 * 
	 * @param id
	 *          Entry id
	 * @param def
	 *          Default value to be returned if the entry is not available
	 * @return Value of default value
	 */
	public String getValue(String id, String def) {
		List<String> ls = entries.get(id);
		String v = def;

		if ((ls != null) && !ls.isEmpty()) {
			v = ls.get(0);
		}
		return v;
	}


	/**
	 * Set values for an entry.
	 * 
	 * @param id
	 *          Entry id
	 * @param values
	 *          values
	 */
	private void setValues(String id, List<String> values) {
		entries.put(id, values);
	}


	/**
	 * Collect all element nodes in a node list into a list.
	 * 
	 * @param nl
	 *          Node list
	 * @return Element list
	 */
	private static List<Element> elements(NodeList nl) {
		List<Element> ls = Collections.emptyList();
		int len = nl.getLength();

		if (len > 0) {
			ls = new ArrayList<>(len);
			for (int i = 0; i < len; i++) {
				Node n = nl.item(i);

				if (n.getNodeType() == Node.ELEMENT_NODE) {
					ls.add((Element) n);
				}
			}
		}
		return ls;
	}


	/**
	 * Sets a single value.
	 * 
	 * @param id
	 *          Entry id
	 * @param value
	 *          Value
	 */
	private void setValue(String id, String value) {
		entries.put(id, Collections.singletonList(value));
	}
}
