package com.panayotis.util;

import java.io.*;
import java.util.*;

import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.util.PropertiesEntry;

/**
* With this file we can manipulate a Properties file.
* This special type of file has the same structure as
* an *.ini file
*/
public class  Properties extends Vector
{

	/**
	* Set if entry names are case sensitive
	*/
	private boolean ignoreCase = false;

	/**
	* Creates a new Properties object. Not any file commands are executed.
	*/
	public Properties ()
	{
	}

	/**
	* Load a file.  Discard old data first.
	*
	* @return true if operation is successfull. false if not. 
	*/
	public boolean readFile ( String filename)
	{
		removeAllElements ();
		return (appendFile (filename));
	}

	/**
	* Collect information from the selected filename  and store the readen data.
	*
	* @return true if operation is successfull. false if not.
	*/
	public boolean appendFile ( String filename )
	{
		String buf; 		/* temporary store area */
		PropertiesSection pSection;	/* temporary properties section */
		BufferedReader in ;

		pSection = null;
		try {	/* Open & manipulate the input file */
			in = new BufferedReader ( new FileReader( filename ));
			while ( (buf = transf.normalizeString( in.readLine(), ignoreCase)) != null )	/* Read buf - buf is not null (not in EOF ) */
			{
				if ( buf.startsWith("[") ) {	/* Start of a section */
					pSection = getSection ( buf ) ;
					if ( pSection == null ) {
						pSection =  new PropertiesSection (buf);
						addSection ( pSection );
					}
				}
				else if ( ! buf.equals("") && pSection != null )   {  		/* buf is not an empty string & pSection exists */
					pSection.addEntry (transf.getHead(buf), transf.getTail(buf));
				}
			}
			in.close();
		}
		catch (FileNotFoundException e) {
			return false;
		}
		catch ( IOException e ) {
			System.err.println ("Error on file " + filename);
			System.err.println (e.toString());
			return false;
		}
		return true;	/* everything o.k. */
	} 


	/**
	* Save this properties object to a file
	*/
	public boolean saveFile (String filename)
	{
		Enumeration enEntries, enParams;
		Object curValue;
		PrintWriter out;
		PropertiesSection pSection;
		PropertiesEntry pEntry;

		try {
			out = new PrintWriter (new FileOutputStream (filename));		//Open file for output
			out.println ( produceString() );
			out.close ();
		}
		catch ( IOException e ) {
			System.err.println ("Can not write to file " + filename);
	 		return false; 
 		}
		return true;
	}

	/**
	* Delete the specified section
	*/
	public void deleteSection ( String name )
	{
		int where;

		where = findSection ( name );
		if ( where != -1 ) {
			removeSection ( where );
		}
	}

	/**
	* Get the specified section
	*/
	public PropertiesSection getSection ( String name )
	{
		int where;

		where = findSection ( name );
		return ( where == -1) ? null : retrieveSection ( where );
	}

	/**
	* Add the specified section
	*/
	public void addSection ( String sect )
	{
		addSection ( new PropertiesSection (sect) );
	}

	/**
	* Add the specified section, removes old one
	*/
	public void addSection ( PropertiesSection sect )
	{
		int where;

		where = findSection (sect.getName());
		if ( where != -1 ) {
			retrieveSection (where).mergeSection ( sect );
		}
		else {
			appendSection ( sect );
		}
	}

	/**
	* Get the specified value of a given section & keyword
	* Returns a default value if this keyword doesn't exists
	*/
	public String getValue (String section, String keyword, String def) 
	{ 
		String res; 
		 
		res = getValue(section, keyword);
		return ( res == null ) ? def : res ;
	} 

	/**
	* Get the specified value of a given section & keyword
	*/
	public String getValue ( String section, String keyword)
	{
		PropertiesSection pSection;

		if (ignoreCase) {
			section = section.toLowerCase();
			keyword = keyword.toLowerCase();
		}
		section = transf.normalizeSecName(section, ignoreCase);
		pSection= getSection (section);
		return ( pSection == null ) ? null :  pSection.getValue (keyword);
	}	

	/**
	* Set the specified value of a given section & keyword
	*/
	public void setValue( String section, String keyword, String value)
	{
		PropertiesSection pSection;
		
		if (ignoreCase) {
			section = section.toLowerCase();
			keyword = keyword.toLowerCase();
		}
		section = transf.normalizeSecName(section, ignoreCase);
		pSection= getSection (section);
		if (pSection == null) {
			pSection = new PropertiesSection( section );
			addSection ( pSection ); 
		}
		pSection.addEntry (keyword, value);	
	}
	

	/**
	* Return a string with all the output data
	*/
	public String produceString()
	{
		PropertiesSection pSection;
		PropertiesEntry pEntry;
		StringBuffer res;

		res = new StringBuffer();
		for ( int i = 0; i < size(); i ++) {
			pSection = retrieveSection (i) ;
			res.append( pSection.produceString () );
			res.append ('\n');
		}
		return res.toString();
	}

	/**
	* Set whether durong load-time case is importand
	*/
	public void setLoadIgnoreCase (boolean b) 
	{
		ignoreCase = b;
	}

	/**
	* Check whether durong load-time case is importand
	*/
	public boolean getLoadIgnoreCase ()
	{
		return ignoreCase;
	}

	/**
	* Low level delete section
	*/
	private void removeSection ( int where )
	{
		removeElementAt ( where );
	}

	/**
	* Low level get Section
	*/
	private PropertiesSection retrieveSection ( int where)
	{
		return (PropertiesSection) elementAt (where);
	}

	/**
	* Low level add Section
	*/
	private void appendSection ( PropertiesSection section )
	{
		addElement ( section);
	}

	/**
	* Low level find section
	*/
	public int findSection ( String name )
	{
		PropertiesSection pSection;

		for ( int i = 0; i < size(); i ++) {
			pSection = (PropertiesSection) elementAt (i);
			if ( pSection.nameEquals ( name ) ) {
				return i;
			}
		}
		return -1;
	} 
	 
}
