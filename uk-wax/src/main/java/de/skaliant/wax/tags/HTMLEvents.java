package de.skaliant.wax.tags;


/**
 * 
 *
 * @author Udo Kastilan
 */
public interface HTMLEvents
{
	final static String ON_FOCUS = "onfocus";
	final static String ON_BLUR = "onblur";
	final static String ON_CLICK = "onclick";
	final static String ON_DBL_CLICK = "ondblclick";
	final static String ON_MOUSE_DOWN = "onmousedown";
	final static String ON_MOUSE_UP = "onmouseup";
	final static String ON_MOUSE_OVER = "onmouseover";
	final static String ON_MOUSE_OUT = "onmouseout";
	final static String ON_KEY_PRESS = "onkeypress";
	final static String ON_KEY_DOWN = "onkeydown";
	final static String ON_KEY_UP = "onkeyup";
	
	
	void setOnfocus(String onfocus);
	
	String getOnfocus();

	void setOnblur(String onblur);
	
	String getOnblur();

	void setOnclick(String onclick);
	
	String getOnclick();
	
	void setOndblclick(String ondblclick);
	
	String getOndblclick();
	
	void setOnmousedown(String onmousedown);
	
	String getOnmousedown();
	
	void setOnmouseup(String onmouseup);
	
	String getOnmouseup();
	
	void setOnmouseover(String onmouseover);
	
	String getOnmouseover();
	
	void setOnmouseout(String onmouseout);
	
	String getOnmouseout();
	
	void setOnkeypress(String onkeypress);

	String getOnkeypress();
	
	void setOnkeydown(String onkeydown);

	String getOnkeydown();
	
	void setOnkeyup(String onkeyup);

	String getOnkeyup();
}
