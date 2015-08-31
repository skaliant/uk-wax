package de.skaliant.wax.core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.skaliant.wax.core.ControllerManager;
import de.skaliant.wax.core.Router;
import de.skaliant.wax.core.ViewEngine;
import de.skaliant.wax.core.model.impl.DefaultControllerManager;
import de.skaliant.wax.core.model.impl.DefaultRouter;
import de.skaliant.wax.core.model.impl.JspViewEngine;
import de.skaliant.wax.util.Injector;
import de.skaliant.wax.util.logging.Log;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class Bootstrapper
{
	private final static String ATTR_CORE = "__wax.core__";
	private final static String PREFIX_VIEWS = "views.";
	private final static String PREFIX_ROUTER = "router.";
	private final static String PREFIX_CONTROLLER = "controller.";
	
	
	public static DispatcherInfo configure(ServletContext app, ServletConfig conf)
	{
		return configure(app, new ServletConfigWrapper(conf));
	}
	
	
	public static DispatcherInfo configure(ServletContext app, FilterConfig conf)
	{
		return configure(app, new FilterConfigWrapper(conf));
	}
	
	
	private synchronized static DispatcherInfo configure(ServletContext app, ConfigWrapper conf)
	{
		DispatcherInfo disp = new DispatcherInfo();
		/*
		 * Configure and set implementations for core functions
		 */
		disp.setControllerManager(configure(loadAndCreate(ControllerManager.class, 
				conf.getParam("core.controllerManager"), DefaultControllerManager.class), conf, PREFIX_CONTROLLER));
		disp.setRouter(configure(loadAndCreate(Router.class, 
				conf.getParam("core.router"), DefaultRouter.class), conf, PREFIX_ROUTER));
		disp.setViewEngine(configure(loadAndCreate(ViewEngine.class, 
				conf.getParam("core.viewEngine"), JspViewEngine.class), conf, PREFIX_VIEWS));
		/*
		 * Try to find the servlet mapping's url pattern as identifier
		 */
		disp.setId(id(findUrlMapping(app, conf)));
		/*
		 * Final step: register dispatcher
		 */
		getCore(app).register(disp);
		return disp;
	}
	
	
	private static <C extends Configurable<?>> C configure(C c, ConfigWrapper conf, String prefix)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		for (String n : conf.getParamNames())
		{
			if (n.startsWith(prefix))
			{
				map.put(n.substring(prefix.length()), conf.getParam(n));
			}
		}
		new Injector().injectBeanProperties(c.getConfig(), new MappedParams(map));
		return c;
	}
	
	
	private static String id(String servletMapping)
	{
		/*
		 * TODO: more intelligent url pattern handling, create id independently of the pattern
		 */
		return servletMapping.replace("*", "");
	}
	
	
	private static String findUrlMapping(ServletContext app, ConfigWrapper conf)
	{
		File webXml = new File(app.getRealPath("/WEB-INF/web.xml"));
		XmlParserHandler handy = new XmlParserHandler(conf.getType(), conf.getHandlerName());
		
		try
		{
			SAXParserFactory fac = SAXParserFactory.newInstance();
			
			fac.setValidating(false);
			fac.newSAXParser().parse(webXml, handy);
		}
		catch (Exception ex)
		{
			Log.get(Bootstrapper.class).warn("Cannot parse web.xml for servlet mapping", ex);
		}
		return handy.mapping;
	}
	
	
	private static <C> C loadAndCreate(Class<? extends C> target, String name, Class<? extends C> fallback)
	{
		C c = null;
		
		if (name != null)
		{
			Log.get(Bootstrapper.class).info("Trying to load and create instance of \"" + name + "\" as replacement for " + target.getSimpleName());
			try
			{
				c = target.cast(Class.forName(name).newInstance());
			}
			catch (Exception ex)
			{
				Log.get(Bootstrapper.class).warn("Could not load \"" + name + '"', ex);
			}
		}
		if (c == null)
		{
			try
			{
				c = fallback.newInstance();
			}
			catch (Exception ex)
			{
				Log.get(Bootstrapper.class).fatal("Could not load default implementation " + fallback.getSimpleName(), ex);
			}
		}
		return c;
	}
	
	
	private static WaxCore getCore(ServletContext app)
	{
		WaxCore core = (WaxCore) app.getAttribute(ATTR_CORE);
		
		if (core == null)
		{
			Log.get(Bootstrapper.class).info("Initializing core information");
			app.setAttribute(ATTR_CORE, core = new WaxCore());
		}
		return core;
	}
	
	
	private static class XmlParserHandler
		extends DefaultHandler
	{
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
		
		
		private XmlParserHandler(String type, String name)
		{
			this.name = name;
			mappingElement = type + "-mapping";
			nameElement = type + "-name";
		}
		
		
		@Override
		public void characters(char[] ch, int start, int length)
			throws SAXException
		{
			switch (state)
			{
				case NAME:
				case URL_PATTERN:
					sb.append(ch, start, length);
					break;
			}
		}
		
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes)
			throws SAXException
		{
			switch (state)
			{
				case NONE:
					if (mappingElement.equals(qName))
					{
						state = MAPPING;
					}
					break;
				case MAPPING:
					if (nameElement.equals(qName))
					{
						sb.delete(0, sb.length());
						state = NAME;
					}
					else if ("url-pattern".equals(qName))
					{
						sb.delete(0, sb.length());
						state = URL_PATTERN;
					}
					break;
			}
		}
		
		
		@Override
		public void endElement(String uri, String localName, String qName)
			throws SAXException
		{
			switch (state)
			{
				case MAPPING:
					if (mappingElement.equals(qName))
					{
						if (name.equals(tempName))
						{
							mapping = tempMapping;
							state = FINISHED;
						}
						else
						{
							state = NONE;
						}
						tempMapping = tempName = null;
					}
					break;
				case NAME:
					if (nameElement.equals(qName))
					{
						tempName = sb.toString().trim();
						state = MAPPING;
					}
					break;
				case URL_PATTERN:
					if ("url-pattern".equals(qName))
					{
						tempMapping = sb.toString().trim();
						state = MAPPING;
					}
					break;
			}
		}
	}
	
	
	private static interface ConfigWrapper
	{
		List<String> getParamNames();
		
		String getParam(String name);
		
		String getHandlerName();
		
		String getType();
	}

	
	private static class FilterConfigWrapper
		implements ConfigWrapper
	{
		private List<String> names = new ArrayList<String>();
		private FilterConfig conf = null;
		
		
		private FilterConfigWrapper(FilterConfig conf)
		{
			this.conf = conf;
			for (Object o : Collections.list((Enumeration<?>) conf.getInitParameterNames()))
			{
				names.add(o.toString());
			}
		}
		

		public List<String> getParamNames()
		{
			return names;
		}

		public String getParam(String name)
		{
			return conf.getInitParameter(name);
		}

		public String getHandlerName()
		{
			return conf.getFilterName();
		}
		
		
		public String getType()
		{
			return "filter";
		}
	}
	
	
	private static class ServletConfigWrapper
		implements ConfigWrapper
	{
		private List<String> names = new ArrayList<String>();
		private ServletConfig conf = null;
		
		
		private ServletConfigWrapper(ServletConfig conf)
		{
			this.conf = conf;
			for (Object o : Collections.list((Enumeration<?>) conf.getInitParameterNames()))
			{
				names.add(o.toString());
			}
		}
		

		public List<String> getParamNames()
		{
			return names;
		}

		public String getParam(String name)
		{
			return conf.getInitParameter(name);
		}

		public String getHandlerName()
		{
			return conf.getServletName();
		}
		
		public String getType()
		{
			return "servlet";
		}
	}
	
	
	private static class MappedParams
		implements ParameterProvider
	{
		private Map<String, String> map = null;
		
		
		private MappedParams(Map<String, String> map)
		{
			this.map = map;
		}

		public boolean isParameterPresent(String name)
		{
			return map.containsKey(name);
		}

		public List<String> getParameterNames()
		{
			return new ArrayList<String>(map.keySet());
		}

		public String getParameter(String name)
		{
			return map.get(name);
		}

		public String[] getParameterValues(String name)
		{
			String v = map.get(name);
			
			return v == null ? null : new String[] { v };
		}

		public Map<String, String[]> getParameters()
		{
			Map<String, String[]> mapp = new HashMap<String, String[]>(map.size());
			
			for (Map.Entry<String, String> me : map.entrySet())
			{
				mapp.put(me.getKey(), new String[] { me.getValue() });
			}
			return mapp;
		}
	}
}
