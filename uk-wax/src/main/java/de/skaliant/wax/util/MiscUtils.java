package de.skaliant.wax.util;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class MiscUtils
{
	/**
	 * @param s
	 * @return
	 */
	public static boolean empty(String s)
	{
		return (s == null) || (s.length() == 0);
	}
	
	
	/**
	 * @param s
	 * @return
	 */
	public static boolean notEmpty(String s) 
	{
		 return (s != null) && (s.length() != 0);
	}
	

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String firstLetterCase(String s)
	{
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
	
	/**
	 * @param val
	 * @return
	 */
	public static <T> Set<T> createSet(T... val)
	{
		Set<T> set = new HashSet<T>(val.length);
		
		for (T t : val)
		{
			set.add(t);
		}
		return set;
	}
	
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static <C extends Closeable> C close(C c)
	{
		if (c != null)
		{
			try
			{
				c.close();
				c = null;
			}
			catch (Exception ex)
			{
				//
			}
		}
		return c;
	}
	
	
	/**
	 * @param test
	 * @param def
	 * @return
	 */
	public static <T> T nvl(T test, T def)
	{
		return test == null ? def : test;
	}
	
	
	/**
	 * @param e
	 * @param type
	 * @return
	 */
	public static <T> List<T> list(Enumeration<?> e, Class<T> type)
	{
		List<T> ls = new ArrayList<T>();
		
		while (e.hasMoreElements())
		{
			ls.add(type.cast(e.nextElement()));
		}
		return ls;
	}
	

	/**
	 * 
	 * @param parts
	 * @return
	 */
	public static String joinPath(List<String> parts)
	{
		if ((parts == null) || parts.isEmpty())
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		
		for (String s : parts)
		{
			sb.append('/').append(s);
		}
		return sb.toString();
	}
	
	
	/**
	 * 
	 * @param str
	 * @param sep
	 * @return
	 */
	public static List<String> split(String str, char sep)
	{
		return split(str, String.valueOf(sep));
	}
	
	
	/**
	 * 
	 * @param str
	 * @param sep
	 * @return
	 */
	public static List<String> split(String str, String sep)
	{
		if ((str == null) || (str.length() == 0))
		{
			return new ArrayList<String>(0);
		}
		List<String> ls = new ArrayList<String>();
		
		for (StringTokenizer st = new StringTokenizer(str, sep); st.hasMoreTokens(); )
		{
			ls.add(st.nextToken());
		}
		return ls;
	}

}
