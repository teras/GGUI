package com.panayotis.util;

import java.util.*;
import com.panayotis.util.PropertiesEntry;

public class PropertiesSection extends Vector
{
	String name;

	/**
	* Create a new Section
	*/
	public PropertiesSection ( String nam )
	{
		name = nam;
	}

	/**
	* Add a new entry to this section
	*/
	public void addEntry ( String keyword, String value )
	{
		addEntry ( new PropertiesEntry (keyword, value));
	}

	/**
	* Add a new entry to this section
	*/
	public void addEntry ( PropertiesEntry entry)
	{
		int where;

		where = findEntry (entry.getKeyword());
		if ( where != -1 ) {
			retrieveEntry (where).setValue ( entry.getValue() );
		}
		else {
			appendEntry ( entry );
		}
	}

	/**
	* Merge current section with a given one
	*/
	public void mergeSection ( PropertiesSection sect )
	{
		for ( int i = 0; i < sect.size (); i ++) {
			addEntry ( sect.retrieveEntry ( i ) );
		}
	}

	/**
	* Delete a specified entry in this section
	*/
	public void deleteEntry ( String keyword)
	{
		int where = findEntry ( keyword);
		if ( where != -1) {
			removeEntry (where);
		}
	}

	/**
	* Get the specified entry in this section
	*/
	public PropertiesEntry getEntry ( String keyword )
	{
		int where;

		where = findEntry ( keyword);
		return ( where == -1) ? null : retrieveEntry (where);
	}

	/**
	* Get the value of a specified entry in this section
	*/
	public String getValue ( String keyword )
	{
		PropertiesEntry entry;

		entry = getEntry ( keyword);
		return ( entry == null) ? null : entry.getValue();
	}

	/**
	* Get the name of this section
	*/
	public String getName ()
	{
		return name;
	}

	/**
	* Set the name of this section
	*/
	public void setName ( String nam)
	{
		name = nam;
	}

	/**
	* Check if the name of this section equals another string
	*/
	public boolean nameEquals ( String nam )
	{
		return (nam.equals(name) );
	}

	/**
	* Make a String out of this object
	*/
	public String produceString ()
	{
		StringBuffer buf;
		PropertiesEntry pEntry;

		buf = new StringBuffer( getName() + '\n');
		for ( int j = 0; j < size(); j++) {
			pEntry = retrieveEntry( j );
			buf.append( pEntry.getKeyword() + '=' + pEntry.getValue() + '\n');
		}
		return buf.toString();
	}

	/**
	* Low level add entry in this section
	*/
	private void appendEntry ( PropertiesEntry entry )
	{
		addElement ( entry );
	}

	/**
	* Low level remove entry in this section
	*/
	private void removeEntry ( int where )
	{
		removeElementAt ( where );
	}

	/**
	* Low level get entry in this section
	*/
	private PropertiesEntry retrieveEntry ( int pos )
	{
		return (PropertiesEntry) elementAt (pos);
	}

	/**
	* Low level find entry in this section
	*/
	private int findEntry (String keyword)
	{
		PropertiesEntry entry;

		for ( int i = 0; i < size(); i++) {
			entry = retrieveEntry (i);
			if ( entry.keywordEquals ( keyword ) ) {
				return i;
			}
		}
		return -1;
	}


}

