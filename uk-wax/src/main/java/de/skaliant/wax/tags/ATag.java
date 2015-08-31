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
public class ATag
	extends TagSupport
	implements HTMLEvents, HTMLStylable
{
	private final static String HREF = "href";
	private final static String TARGET = "target";
	private final static String NAME = "name";
	private final static String REL = "rel";
	private final static String ACCESSKEY = "accesskey";
	private final static String SHAPE = "shape";
	private final static String COORDS = "coords";
	private final static String TITLE = "title";
	private final static String TABINDEX = "tabindex";
	
	private TagBuilder tag = new TagBuilder("a");


	@Override
	public int doStartTag()
		throws JspException
	{
		String href = tag.getString(HREF);
		
		tag.set("id", getId());
		if ((href != null) && href.startsWith("/"))
		{
			href = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + href;
			tag.set(HREF, href);
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
		return EVAL_BODY_INCLUDE;
	}
	
	
	@Override
	public int doEndTag()
		throws JspException
	{
		try
		{
			pageContext.getOut().print("</a>");
		}
		catch (IOException ex)
		{
			throw new JspException(ex);
		}
		return EVAL_PAGE;
	}

	
	public String getHref()
	{
		return tag.getString(HREF);
	}


	public void setHref(String href)
	{
		tag.set(HREF, href);
	}


	public String getTarget()
	{
		return tag.getString(TARGET);
	}


	public void setTarget(String target)
	{
		tag.set(TARGET, target);
	}


	public String getName()
	{
		return tag.getString(NAME);
	}


	public void setName(String name)
	{
		tag.set(NAME, name);
	}


	public String getRel()
	{
		return tag.getString(REL);
	}


	public void setRel(String rel)
	{
		tag.set(REL, rel);
	}


	public String getAccesskey()
	{
		return tag.getString(ACCESSKEY);
	}


	public void setAccesskey(String accesskey)
	{
		tag.set(ACCESSKEY);
	}


	public String getShape()
	{
		return tag.getString(SHAPE);
	}


	public void setShape(String shape)
	{
		tag.set(SHAPE, shape);
	}


	public String getCoords()
	{
		return tag.getString(COORDS);
	}


	public void setCoords(String coords)
	{
		tag.set(COORDS, coords);
	}


	public String getTitle()
	{
		return tag.getString(TITLE);
	}


	public void setTitle(String title)
	{
		tag.set(TITLE, title);
	}


	public int getTabindex()
	{
		return tag.getIntegerNotNull(TABINDEX);
	}


	public void setTabindex(int tabindex)
	{
		tag.set(TABINDEX, tabindex);
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
