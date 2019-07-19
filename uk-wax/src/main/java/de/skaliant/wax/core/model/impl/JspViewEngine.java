package de.skaliant.wax.core.model.impl;

import java.io.IOException;

import javax.servlet.ServletException;

import de.skaliant.wax.core.ViewEngine;
import de.skaliant.wax.core.model.Call;
import de.skaliant.wax.core.model.ViewEngineConfig;


/**
 * Default view engine implementation mapping to JSP files. Default folder is
 * "/WEB-INF/views/", and default file suffix is ".jspx".
 *
 * @author Udo Kastilan
 */
public class JspViewEngine implements ViewEngine {
	private ViewEngineConfig info = new ViewEngineConfig();


	public JspViewEngine() {
		info.setLocation("/WEB-INF/views/");
		info.setSuffix(".jspx");
	}


	@Override
	public ViewEngineConfig getConfig() {
		return info;
	}


	@Override
	public void render(String view, Call call)
		throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		String path = null;
		String viewLocation = info.getLocation();
		String viewSuffix = info.getSuffix();

		if (!view.startsWith("/")) {
			sb.append(viewLocation);
		}
		sb.append(view);
		if (!(view.endsWith(".jspx") || view.endsWith(".jsp"))) {
			sb.append(viewSuffix);
		}
		path = sb.toString();
		call.dispatch(path);
	}
}
