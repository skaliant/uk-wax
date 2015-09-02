package de.skaliant.wax.core.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.skaliant.wax.core.ControllerManager;
import de.skaliant.wax.core.Environment;
import de.skaliant.wax.core.Router;
import de.skaliant.wax.core.ViewEngine;
import de.skaliant.wax.util.Config;
import de.skaliant.wax.util.logging.Log;


/**
 * Information about one dispatcher. In most cases, web applications will stick
 * to one. There may be reasons to create more than one with different
 * controllers in order to separate different areas of concern, such as an
 * internal backoffice or administrative stage.
 *
 * @author Udo Kastilan
 */
public class DispatcherInfo
{
	private final static long CONF_CHECK_SPAN = 60000;
	private final Log LOG = Log.get(DispatcherInfo.class);

	private Map<String, ConfigEntry> confPerHint = new HashMap<String, ConfigEntry>(3);
	private ControllerManager resolver = null;
	private ViewEngine viewEngine = null;
	private Router router = null;
	private String pattern = null;

	
	public ViewEngine getViewEngine()
	{
		return viewEngine;
	}


	public void setViewEngine(ViewEngine viewEngine)
	{
		this.viewEngine = viewEngine;
	}


	public ControllerManager getControllerManager()
	{
		return resolver;
	}


	public void setControllerManager(ControllerManager resolver)
	{
		this.resolver = resolver;
	}


	public Router getRouter()
	{
		return router;
	}


	public void setRouter(Router router)
	{
		this.router = router;
	}


	public String getPattern()
	{
		return pattern;
	}


	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}
	
	
	public boolean isSuffixPattern()
	{
		return (pattern != null) && pattern.matches("^[*][.].+$");
	}


	public Config findConfig(Call ctx)
	{
		String hint = Environment.getInstance().getHint();
		ConfigEntry ce = null;

		if (hint == null)
		{
			hint = "dev";
		}
		ce = confPerHint.get(hint);
		if ((ce == null)
				|| ((System.currentTimeMillis() - ce.checked) > CONF_CHECK_SPAN))
		{
			File f = null;

			f = new File(ctx.getRealPath("/WEB-INF/config-" + hint + ".xml"));
			if (!f.isFile())
			{
				f = new File(ctx.getRealPath("/WEB-INF/config.xml"));
			}
			if (f.isFile() && ((ce == null) || (ce.timestamp != f.lastModified())))
			{
				try
				{
					ce = new ConfigEntry();
					LOG.info("Loading config file \"" + f.getAbsolutePath() + '"');
					ce.conf = Config.load(f);
					ce.timestamp = f.lastModified();
					confPerHint.put(hint, ce);
				}
				catch (Exception ex)
				{
					LOG.error("Cannot load config file \"" + f.getAbsolutePath() + '"', ex);
					return new Config();
				}
			}
			else if (!f.isFile())
			{
				LOG.info("No config file found");
				ce = new ConfigEntry();
				ce.conf = new Config();
			}
			ce.checked = System.currentTimeMillis();
		}
		return ce.conf;
	}

	private static class ConfigEntry
	{
		private Config conf = null;
		private long timestamp = 0;
		private long checked = 0;
	}
}
