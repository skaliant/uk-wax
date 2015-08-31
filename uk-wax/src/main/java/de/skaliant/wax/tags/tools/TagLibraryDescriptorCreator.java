package de.skaliant.wax.tags.tools;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.skaliant.wax.tags.ATag;
import de.skaliant.wax.tags.FormTag;
import de.skaliant.wax.tags.ImgTag;
import de.skaliant.wax.util.Bean;
import de.skaliant.wax.util.TypeUtils;


/**
 * Used to create the src/main/resources/META-INF/wax.tld
 *
 * @author Udo Kastilan
 */
public class TagLibraryDescriptorCreator
{
	public static void main(String[] args)
	{
		File dir = new File("src/main/resources/META-INF");
		File tldFile = new File(dir, "wax.tld");
		TagDesc[] tags =  {
			new TagDesc(ATag.class).required("href"),
			new TagDesc(FormTag.class).required("action"),
			new TagDesc(ImgTag.class).required("src", "alt").bodyType("empty")
		};
		Document tld = null;
		Element root = null;
		
		if (!dir.isDirectory())
		{
			dir.mkdirs();
		}
		try
		{
			tld = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			tld.appendChild(root = tld.createElement("taglib"));
			root.setAttribute("version", "2.0");
			root.setAttribute("xmlns", "http://java.sun.com/xml/j2ee");
			root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			root.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd");

			appendTextElement(root, "tlib-version", "1.0");
			appendTextElement(root, "jsp-version", "2.0");
			appendTextElement(root, "short-name", "uk-wax");
			appendTextElement(root, "uri", "http://www.skaliant.de/taglibs/wax");
			appendTextElement(root, "display-name", "uk-wax custom tag library");
			appendTextElement(root, "description", "uk-wax custom tag library");

			for (TagDesc td : tags)
			{
				Element te = tld.createElement("tag");
				String name = td.tagClass.getSimpleName().toLowerCase();
				
				name = name.substring(0, name.lastIndexOf("tag"));
				appendTextElement(te, "name", name);
				appendTextElement(te, "tag-class", td.tagClass.getName());
				appendTextElement(te, "body-content", td.bodyType);
				
				for (PropertyDescriptor pd : Bean.getProperties(td.tagClass, Bean.Accessibility.READ_WRITE))
				{
					if ("parent".equals(pd.getName()))
					{
						continue;
					}
					Element ae = tld.createElement("attribute");
					Class<?> type = pd.getPropertyType();
					
					if (type.isPrimitive())
					{
						type = TypeUtils.getWrapperFor(type);
					}

					appendTextElement(ae, "name", pd.getName());
					appendTextElement(ae, "required", td.required.contains(pd.getName()) ? "true" : "false");
					appendTextElement(ae, "rtexprvalue", "true");
					appendTextElement(ae, "type", type.getName());
					
					te.appendChild(ae);
				}
				root.appendChild(te);
			}
			
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(tld), new StreamResult(tldFile));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	private static void appendTextElement(Element parent, String name, String text)
	{
		Element e = parent.getOwnerDocument().createElement(name);
		
		e.appendChild(parent.getOwnerDocument().createTextNode(text));
		parent.appendChild(e);
	}
	
	
	private static class TagDesc
	{
		private Set<String> required = new HashSet<String>(3);
		private Class<?> tagClass = null;
		private String bodyType = "JSP";
		
		
		private TagDesc(Class<?> tagClass)
		{
			this.tagClass = tagClass;
		}
		
		
		private TagDesc bodyType(String bodyType)
		{
			this.bodyType = bodyType;
			return this;
		}

		
		private TagDesc required(String... reqs)
		{
			for (String r : reqs)
			{
				required.add(r);
			}
			return this;
		}
	}
}
