package de.skaliant.wax.tags;


/**
 * 
 *
 * @author Udo Kastilan
 */
public interface HTMLStylable
{
	final static String STYLE = "style";
	final static String STYLE_CLASS = "class";
	
	void setStyle(String style);
	
	String getStyle();
	
	void setStyleClass(String styleClass);
	
	String getStyleClass();
}
