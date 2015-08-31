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
public class ImgTag
	extends TagSupport
	implements HTMLEvents, HTMLStylable
{
	private final static String SRC = "src";
	private final static String ALT = "alt";
	private final static String HEIGHT = "height";
	private final static String WIDTH = "width";
	private final static String USEMAP = "usemap";
	private final static String ISMAP = "ismap";
	
	private TagBuilder tag = new TagBuilder("img");
	

	@Override
	public int doStartTag()
		throws JspException
	{
		String src = tag.getString(SRC);

		tag.set("id", getId());
		if ((src != null) && src.startsWith("/"))
		{
			src = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + src;
			tag.set(SRC, src);
		}
		try
		{
			pageContext.getOut().print(tag.build());
		}
		catch (IOException ex)
		{
			throw new JspException(ex);
		}
		finally
		{
			tag.clear();
		}
		return SKIP_BODY;
	}
	
	
	public String getSrc()
	{
		return tag.getString(SRC);
	}
	
	
	public void setSrc(String src)
	{
		tag.set(SRC, src);
	}
	
	
	public String getAlt()
	{
		return tag.getString(ALT);
	}


	public void setAlt(String alt)
	{
		tag.set(ALT, alt);
	}


	public int getHeight()
	{
		return tag.getIntegerNotNull(HEIGHT);
	}


	public void setHeight(int height)
	{
		tag.set(HEIGHT, height);
	}


	public int getWidth()
	{
		return tag.getIntegerNotNull(WIDTH);
	}


	public void setWidth(int width)
	{
		tag.set(WIDTH, width);
	}


	public String getUsemap()
	{
		return tag.getString(USEMAP);
	}


	public void setUsemap(String usemap)
	{
		tag.set(USEMAP, usemap);
	}


	public boolean isIsmap()
	{
		return tag.getBooleanNotNull(ISMAP);
	}


	public void setIsmap(boolean ismap)
	{
		tag.set(ISMAP, ismap);
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
