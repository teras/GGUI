package com.panayotis.util;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.IOException;

public class transf
{
	static String new_line;
	static final String def_new_line = "\n";

	static
	{
		java.util.Properties s_prop;

		s_prop = System.getProperties ();
		new_line = s_prop.getProperty ("line.separator");
		if ( new_line == null ){
			new_line = def_new_line;
		}
	}

	public static String setString (String s, String error)
	{
		if (s == null) {
			printError (error);
			return "";
		}
		else {
			s = convertSpecial ( s );
			return s;
		}
	}

	public static boolean setBoolean (String s, boolean def, String error)
	{
		if ( s == null ) {
			printError (error);
			return def;
		}
		else {
			s = simplifyString(s);
			if (s.equals("enabled")) {
				return true;
			}
			else if (s.equals("disabled")) {
				return false;
			}
			else if (s.equals("true")) {
				return true;
			}
			else if (s.equals("false")) {
				return false;
			}
			else if (s.equals("on")) {
				return true;
			}
			else if (s.equals("off")) {
				return false;
			}
			else if (s.equals("yes")) {
				return true;
			}
			else if (s.equals("no")) {
				return false;
			}
			else {
				printError (error);
				return def;
			}
		}
	}


	public static int createInt ( String value, String error )
	{
		int res = -1;

		try {
			value = value.trim();
			Integer Ires =new Integer (value);
			res = Ires.intValue();
		}
		catch (Exception e) {
			printError (error);
		}
		return res;
	}

	public static String simplifyString(String s)
	{
		return s.toLowerCase().trim();
	}

	/**
	* Use this method to convert string codes like [NEWLINE] to 
	* \n etc. characters
	*/
	public static String stringCodes (String s)
	{
		StringBuffer res;

		res = new StringBuffer (50);
		for ( int i = 0 ; i < s.length() ;  i++)  {
			switch ( s.charAt (i) ) {
			case '\n':
				res.append ("\\l");
				break;
			case '\r':
				break;
			default:
				res.append (s.charAt (i));
				break;
			}
		}
		return res.toString();
	}

	/**
	* This method converts special strings, e.g. \n \t, to their real meaning
	*/
	public static String convertSpecial (String s)
	{
		StringBuffer res;

		res = new StringBuffer (50);
		for ( int i = 0 ; i < s.length() ;  i++)  {
			if  ( s.charAt (i) == '\\' &&  i  < (s.length() -1) )  {
				switch ( s.charAt (i+1) ) {
				case 'n':
					res.append ('\n');
					break;
				case 'r':
					res.append ('\r');
					break;
				case 't':
					res.append ('\t');
					break;
				case 'l':
					res.append (new_line);
					break;
				case 'b':
					res.append ('\b');
					break;
				case 'f':
					res.append ('\f');
					break;
				case '\'':
					res.append ('\'');
					break;
				case '\"':
					res.append ('\"');
					break;
				case '\\':
					res.append ('\\');
					break;
				default:
					res.append ('\\');
					res.append (s.charAt (i+1));
					break;
				}
				i++;
			}
			else {
				res.append (s.charAt (i));
			}
		}
		return res.toString();
	}

	/** 
	* Retrieve the head properties name from a String 
	*/ 
	public static String getHead(String dat) 
	{ 

		String res;
		int eqPos; 
		 
		if (dat== null) return null; 
		eqPos = dat.indexOf ("=");	/* Find the equal sign */ 
		if ( eqPos > 0)	/* Equal sign exists (>-1) AND equal sign is NOT the first character (>0) */ 
		{ 
			res =  dat.substring (0, eqPos ).trim();    /* Find head section */
			if (res.equals(""))
				res = null;
			return res;
		} 
		return null;	/* not of proper type */ 
	} 
	/** 
	* Retrieve the properties value from a String 
	*/ 
	public static String getTail(String dat) 
	{ 
		int eqPos; 
		 
		if (dat==null) return null; 
		eqPos = dat.indexOf ("=");	/* Find the equal sign */ 
		if ( eqPos > -1)	/* Equal sign exists (>-1) */ 
		{ 
				return dat.substring (eqPos + 1);    /* Find tail section */ 
		} 
		return null;	/* not of proper type */ 
	} 
	 
	 
	public static String ignoreCase (String s, boolean ignCase)
	{
		if (ignCase) {
			return s.toLowerCase ();
		}
		else {
			return s;
		}
	}

 
	public static String normalizeSecName (String s, boolean ignCase) 
	{ 
		s = s.trim(); 
		if ( ! s.startsWith ("[") ) 
			s = "[" + s; 
		if ( ! s.endsWith ("]") ) 
			s = s + "]";
		return ignoreCase (s, ignCase); 
	} 
	 
 
	/** 
	* Normalize an input line data. 
	* 
	* @param in the String to be normalized 
	* @return the normalized String 
	*/ 
	public static String normalizeString (String s, boolean ignCase) 
	{ 
		String trimed,head,tail;    /* Temporary storage strings */ 
		int eqPos;		// position of the equal sign 
 
		if (s==null) return null; 
		 
		trimed = s.trim(); 
		if ( trimed.startsWith ("[") )    /* Is it a section? */ 
		{ 
			return normalizeSecName(trimed, ignCase); 
		} 
		else    /* Is it a property? */ 
		{ 
			head = getHead(s);    /* Find head section */ 
			if (head != null) 
			{ 
				tail = getTail(s);    /* Find tail section */ 
				head = ignoreCase (head, ignCase);    /* ignore case */ 
				return (head + "=" + tail);    /* Return new entry */ 
			} 
		} 
		return "";    /* Input string not of proper type - maybe ignored */ 
			      /* We must NOT return 'null' because the readFile will */
			      /* think that the input buffer is exausted. */
	} 
 
	public static String [] stringToArray ( String s )
	{
		StringTokenizer tokens;
		String res [];
		int howmany;

		tokens = new StringTokenizer (s, "\n\r\t", false);
		howmany = tokens.countTokens ();
		if (howmany <= 0) {
			return null;
		}
		res = new String [ howmany ];
		for ( int i = 0 ; i < howmany ; i++ ) {
			res [ i ] = (String) tokens.nextToken ();
		}
		return res;
	}

	public static String ReaderToString ( BufferedReader r) throws IOException
	{
		StringBuffer sb;
		String current;

		sb = new StringBuffer();
		while ( (current = r.readLine()) != null) {
			sb .append(current);
			sb.append (new_line);
		}
		return sb.toString();
	}


	private static void printError ( String error)
	{
		if ( error != null && ( ! error.equals(""))) {
			System.err.println ( error);
		}
	}

}
