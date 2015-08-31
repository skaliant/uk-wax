package de.skaliant.wax.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.skaliant.wax.tags.tools.TagBuilder;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class FormTag
	extends TagSupport
	implements HTMLEvents, HTMLStylable
{
	private final static String ACTION = "action";
	private final static String METHOD = "method";
	private final static String ENCTYPE = "enctype";
	private final static String NAME = "name";
	private final static String TARGET = "target";
	private final static String AUTOCOMPLETE = "autocomplete";
	private final static String NOVALIDATE = "novalidate";
	private final static String ON_SUBMIT = "onsubmit";
	
	private TagBuilder tag = new TagBuilder("form");
	

	@Override
	public int doStartTag()
		throws JspException
	{
		String action = tag.getString(ACTION);
		
		tag.set("id", getId());
		if ((action != null) && action.startsWith("/"))
		{
			action = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + action;
			tag.set(ACTION, action);
		}
		try
		{
			pageContext.getOut().print(tag.build());
		}
		catch (IOException ex)
		{
			throw new JspException(ex);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	
	@Override
	public int doEndTag()
		throws JspException
	{
		tag.clear();
		try
		{
			pageContext.getOut().print("</form>");
		}
		catch (IOException ex)
		{
			throw new JspException(ex);
		}
		return EVAL_PAGE;
	}

	
	public void setAction(String action)
	{
		tag.set(ACTION, action);
	}
	
	
	public String getAction()
	{
		return tag.getString(ACTION);
	}
	
	
	public void setMethod(String method)
	{
		tag.set(METHOD, method);
	}
	
	
	public String getMethod()
	{
		return tag.getString(METHOD);
	}

	
	public void setEnctype(String enctype)
	{
		tag.set(ENCTYPE, enctype);
	}
	
	
	public String getEnctype()
	{
		return tag.getString(ENCTYPE);
	}
	

	public String getName()
	{
		return tag.getString(NAME);
	}


	public void setName(String name)
	{
		tag.set(NAME, name);
	}


	public String getTarget()
	{
		return tag.getString(TARGET);
	}


	public void setTarget(String target)
	{
		tag.set(TARGET, target);
	}


	public String getAutocomplete()
	{
		return tag.getString(AUTOCOMPLETE);
	}


	public void setAutocomplete(String autocomplete)
	{
		tag.set(AUTOCOMPLETE, autocomplete);
	}


	public boolean getNovalidate()
	{
		return tag.getBooleanNotNull(NOVALIDATE);
	}


	public void setNovalidate(boolean novalidate)
	{
		if (novalidate)
		{
			tag.set(NOVALIDATE);
		}
	}


	public String getOnsubmit()
	{
		return tag.getString(ON_SUBMIT);
	}


	public void setOnsubmit(String onsubmit)
	{
		tag.set(ON_SUBMIT, onsubmit);
	}


	public void setStyle(String style)
	{
		tag.set(HTMLStylable.STYLE, style);
	}


	public String getStyle()
	{
		return tag.getString(HTMLStylable.STYLE);
	}


	public void setStyleClass(String styleClass)
	{
		tag.set(HTMLStylable.STYLE_CLASS, styleClass);
	}


	public String getStyleClass()
	{
		return tag.getString(HTMLStylable.STYLE_CLASS);
	}


	public void setOnfocus(String onfocus)
	{
		tag.set(HTMLEvents.ON_FOCUS, onfocus);
	}


	public String getOnfocus()
	{
		return tag.getString(HTMLEvents.ON_FOCUS);
	}


	public void setOnblur(String onblur)
	{
		tag.set(HTMLEvents.ON_BLUR, onblur);
	}


	public String getOnblur()
	{
		return tag.getString(HTMLEvents.ON_BLUR);
	}


	public void setOnclick(String onclick)
	{
		tag.set(HTMLEvents.ON_CLICK, onclick);
	}


	public String getOnclick()
	{
		return tag.getString(HTMLEvents.ON_CLICK);
	}


	public void setOndblclick(String ondblclick)
	{
		tag.set(HTMLEvents.ON_DBL_CLICK, ondblclick);
	}


	public String getOndblclick()
	{
		return tag.getString(HTMLEvents.ON_DBL_CLICK);
	}


	public void setOnmousedown(String onmousedown)
	{
		tag.set(HTMLEvents.ON_MOUSE_DOWN, onmousedown);
	}


	public String getOnmousedown()
	{
		return tag.getString(HTMLEvents.ON_MOUSE_DOWN);
	}


	public void setOnmouseup(String onmouseup)
	{
		tag.set(HTMLEvents.ON_MOUSE_UP, onmouseup);
	}


	public String getOnmouseup()
	{
		return tag.getString(HTMLEvents.ON_MOUSE_UP);
	}


	public void setOnmouseover(String onmouseover)
	{
		tag.set(HTMLEvents.ON_MOUSE_OVER, onmouseover);
	}


	public String getOnmouseover()
	{
		return tag.getString(HTMLEvents.ON_MOUSE_OVER);
	}


	public void setOnmouseout(String onmouseout)
	{
		tag.set(HTMLEvents.ON_MOUSE_OUT, onmouseout);
	}


	public String getOnmouseout()
	{
		return tag.getString(HTMLEvents.ON_MOUSE_OUT);
	}


	public void setOnkeypress(String onkeypress)
	{
		tag.set(HTMLEvents.ON_KEY_PRESS, onkeypress);
	}


	public String getOnkeypress()
	{
		return tag.getString(HTMLEvents.ON_KEY_PRESS);
	}


	public void setOnkeydown(String onkeydown)
	{
		tag.set(HTMLEvents.ON_KEY_DOWN, onkeydown);
	}


	public String getOnkeydown()
	{
		return tag.getString(HTMLEvents.ON_KEY_DOWN);
	}


	public void setOnkeyup(String onkeyup)
	{
		tag.set(HTMLEvents.ON_KEY_UP, onkeyup);
	}


	public String getOnkeyup()
	{
		return tag.getString(HTMLEvents.ON_KEY_UP);
	}
}
