package de.skaliant.wax.upload;

import de.skaliant.wax.util.MiscUtils;

/**
 * Parses HTTP header fields either partially (the value part after the colon only) or as a full line.
 *
 * @author Udo Kastilan
 */
public class HeaderFieldParser
{
	/**
	 * Parses a full header line containing name, value, and parameters.
	 * 
	 * @param line
	 * @return Instance or null, if parsing was not successful (syntax error)
	 */
	public static HeaderField parseFullLine(String line)
	{
		return parse(null, line, false);
	}


	/**
	 * Parses only the part of a header line containing value and parameters
	 * (anything right to the colon sign ":"). This way, you can use this class to
	 * parse servlet header field values.
	 * 
	 * @param name
	 *          Header field name
	 * @param line
	 *          Rest of the line
	 * @return Instance or null, if parsing was not successful (syntax error)
	 */
	public static HeaderField parseValue(String name, String line)
	{
		return parse(name, line, true);
	}


	private static HeaderField parse(String name, String line, boolean valueOnly)
	{
		if (MiscUtils.empty(line))
		{
			return null;
		}
		HeaderField hf = new HeaderField();
		StringBuilder sb = new StringBuilder();
		String paramName = null;
		final int SEEK_VALUE = 0, SEEK_NAME = 1, NAME = 2, VALUE = 3, SEEK_SEMICOLON = 4, SEEK_PARAM_START = 5, PARAM_NAME = 6, PARAM_VALUE_START = 7, PARAM_VALUE = 8, PARAM_VALUE_QUOTED = 9, ESCAPE = 10, SYNTAX_ERROR = 11;
		int state = valueOnly ? SEEK_VALUE : SEEK_NAME;
		char c = 0;

		hf.setName(name);
		for (int i = 0, len = line.length(); (i < len) && (state != SYNTAX_ERROR); i++)
		{
			c = line.charAt(i);
			switch (state)
			{
			// Search for start of name (ignore whitespace)
				case SEEK_NAME:
					if (!isWhitespace(c))
					{
						sb.append(c);
						state = NAME;
					}
					break;
				// Search for start of value (ignore whitespace)
				case SEEK_VALUE:
					if (!isWhitespace(c))
					{
						sb.append(c);
						state = VALUE;
					}
					break;
				// Collect name until colon
				case NAME:
					if (c == ':')
					{
						hf.setName(drain(sb).trim());
						state = SEEK_VALUE;
					}
					else
					{
						sb.append(c);
					}
					break;
				// Collect value until semicolon or end of line
				case VALUE:
					if (c == ';')
					{
						hf.setValue(drain(sb).trim());
						state = SEEK_PARAM_START;
					}
					else
					{
						sb.append(c);
					}
					break;
				// Search for next semicolon separating parameters or end of line
				case SEEK_SEMICOLON:
					if (c == ';')
					{
						state = SEEK_PARAM_START;
					}
					else if (!isWhitespace(c))
					{
						state = SYNTAX_ERROR;
					}
					break;
				// Search for start of parameter name ignoring whitespace
				case SEEK_PARAM_START:
					if (isTokenChar(c))
					{
						sb.append(c);
						state = PARAM_NAME;
					}
					else if (!isWhitespace(c))
					{
						state = SYNTAX_ERROR;
					}
					break;
				// Collect parameter name until equal sign
				case PARAM_NAME:
					if (c == '=')
					{
						paramName = drain(sb);
						state = PARAM_VALUE_START;
					}
					else if (isTokenChar(c))
					{
						sb.append(c);
					}
					else
					{
						state = SYNTAX_ERROR;
					}
					break;
				// Searches for start of parameter value, possibly quoted
				case PARAM_VALUE_START:
					if (c == '"')
					{
						state = PARAM_VALUE_QUOTED;
					}
					else if (isTokenChar(c))
					{
						sb.append(c);
						state = PARAM_VALUE;
					}
					else
					{
						state = SYNTAX_ERROR;
					}
					break;
				// Collects unquoted parameter value until semicolon
				case PARAM_VALUE:
					if (c == ';')
					{
						hf.addParam(paramName, drain(sb));
						state = SEEK_PARAM_START;
					}
					else if (isWhitespace(c))
					{
						hf.addParam(paramName, drain(sb));
						state = SEEK_SEMICOLON;
					}
					else if (isTokenChar(c))
					{
						sb.append(c);
					}
					else
					{
						state = SYNTAX_ERROR;
					}
					break;
				// Collects parameter value until end of quote
				case PARAM_VALUE_QUOTED:
					if (c == '"')
					{
						hf.addParam(paramName, drain(sb));
						state = SEEK_SEMICOLON;
					}
					else if (c == '\\')
					{
						state = ESCAPE;
					}
					else
					{
						sb.append(c);
					}
					break;
				// Handles an escape sequence
				case ESCAPE:
					sb.append(c);
					state = PARAM_VALUE_QUOTED;
					break;
			}
		}
		/*
		 * Take care of any open issues
		 */
		switch (state)
		{
			case VALUE:
				if (sb.length() != 0)
				{
					hf.setValue(drain(sb).trim());
				}
				break;
			case PARAM_VALUE:
				hf.addParam(paramName, drain(sb));
				break;
			case NAME:
			case PARAM_NAME:
			case PARAM_VALUE_START:
			case PARAM_VALUE_QUOTED:
			case ESCAPE:
				state = SYNTAX_ERROR;
				break;
		}
		if (state == SYNTAX_ERROR)
		{
			hf = null;
		}
		return hf;
	}


	/**
	 * Delivers content of the StringBuilder instance, clearing the StringBuilder
	 * for further use.
	 * 
	 * @param sb
	 *          StringBuilder
	 * @return Content
	 */
	private static String drain(StringBuilder sb)
	{
		String s = sb.toString();

		sb.delete(0, sb.length());
		return s;
	}


	/**
	 * Checks for whitespace (only space and tab).
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isWhitespace(char c)
	{
		return (c == ' ') || (c == '\t');
	}


	/**
	 * Checks char for being a valid token symbol.
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isTokenChar(char c)
	{
		return (c > 32) && (c != 127) && (c != '(') && (c != ')') && (c != '<')
				&& (c != '>') && (c != '@') && (c != ',') && (c != ';') && (c != ':')
				&& (c != '\\') && (c != '"') && (c != '/') && (c != '[') && (c != ']')
				&& (c != '?') && (c != '=') && (c != '{') && (c != '}');
	}

}
