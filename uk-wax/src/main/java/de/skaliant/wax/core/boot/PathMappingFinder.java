package de.skaliant.wax.core.boot;

import java.io.File;

import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.skaliant.wax.util.logging.Log;


/**
 * 
 *
 * @author Udo Kastilan
 */
class PathMappingFinder extends DefaultHandler {
	final static int NONE = 0;
	final static int MAPPING = 1;
	final static int NAME = 2;
	final static int URL_PATTERN = 3;
	final static int FINISHED = 4;

	private StringBuilder sb = new StringBuilder();
	private String name = null;
	private String mapping = null;
	private String tempName = null;
	private String tempMapping = null;
	private String mappingElement = null;
	private String nameElement = null;
	private int state = NONE;


	PathMappingFinder(String prefix, String name) {
		this.name = name;
		mappingElement = prefix + "-mapping";
		nameElement = prefix + "-name";
	}


	static String find(ServletContext app, DispatcherConfigWrapper conf) {
		File webXml = new File(app.getRealPath("/WEB-INF/web.xml"));
		PathMappingFinder handler = new PathMappingFinder(
				getConfigPrefix(conf.getType()), conf.getDispatcherName());
		/*
		 * TODO: this will create problems if the application war is not extracted
		 */
		if (!webXml.isFile()) {
			Log.get(Bootstrapper.class).warn("Could not find the web.xml file");
		} else {
			try {
				SAXParserFactory fac = SAXParserFactory.newInstance();

				fac.setValidating(false);
				fac.newSAXParser().parse(webXml, handler);
			}
			catch (Exception ex) {
				Log.get(Bootstrapper.class)
						.warn("Cannot parse web.xml for servlet mapping", ex);
			}
		}

		return handler.getMapping();
	}


	@Override
	public void characters(char[] ch, int start, int length)
		throws SAXException {
		switch (state) {
			case NAME:
			case URL_PATTERN:
				sb.append(ch, start, length);
				break;
		}
	}


	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes)
		throws SAXException {
		switch (state) {
			case NONE:
				if (mappingElement.equals(qName)) {
					state = MAPPING;
				}
				break;
			case MAPPING:
				if (nameElement.equals(qName)) {
					sb.delete(0, sb.length());
					state = NAME;
				} else if ("url-pattern".equals(qName)) {
					sb.delete(0, sb.length());
					state = URL_PATTERN;
				}
				break;
		}
	}


	@Override
	public void endElement(String uri, String localName, String qName)
		throws SAXException {
		switch (state) {
			case MAPPING:
				if (mappingElement.equals(qName)) {
					if (name.equals(tempName)) {
						mapping = tempMapping;
						state = FINISHED;
					} else {
						state = NONE;
					}
					tempMapping = tempName = null;
				}
				break;
			case NAME:
				if (nameElement.equals(qName)) {
					tempName = sb.toString().trim();
					state = MAPPING;
				}
				break;
			case URL_PATTERN:
				if ("url-pattern".equals(qName)) {
					tempMapping = sb.toString().trim();
					state = MAPPING;
				}
				break;
		}
	}


	String getMapping() {
		return mapping;
	}


	private static String getConfigPrefix(DispatcherType type) {
		switch (type) {
			case SERVLET:
				return "servlet";
			case FILTER:
				return "filter";
		}
		throw new RuntimeException("Unknow dispatcher type: " + type
				+ " - this was not expected to happen");
	}
}
