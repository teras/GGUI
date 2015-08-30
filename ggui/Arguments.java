package com.panayotis.util;

import java.util.*;

public class Arguments
{
	/**
	* Use this option if you want to check for a clean option, e.g. for an option which doesn't
	* begin with - or -- etc. You can combine this with the other variables.
	* together with the current one
	*/
	public static final int ALONE = 1;
	/**
	* Use this option if you want to check for options beginning with minus
	* together with the current one
	*/
	public static final int WITHMINUS = 2;
	/**
	* Use this option if you want to check for options beginning with double minus
	* together with the current one
	*/
	public static final int WITHDOUBLEMINUS = 4;
	/**
	* Use this option if you want to check for options beginning with slash
	* together with the current one
	*/
	public static final int WITHSLASH = 8;
	/**
	* Use this option if you want to check for options beginning with reverse slash
	* together with the current one
	*/
	public static final int WITHREVERSESLASH = 16;
	/**
	* Use this option if you don't want case sensitive searches
	*/
	public static final int NOCASE = 256;
	/**
	* Use this option if you want to check for the beginning of the entry only.
	* 
	* If you want to use it with getOption then this option is combined with the data
	* and not alone e.g. --file=~/my/file  and NOT -file ~/my/file
	*/
	public static final int STARTS = 512;

	/**
	* Here we store the argument data
	*/
	private String [] args;

	/**
	* Create a new Arguments object
	*/
	public Arguments (String [] ar)
	{
		args = ar;
	}

	/**
	* Returns the number of entries
	*/
	public int getSize ()
	{
		return args.length;
	}

	/**
	* Get the entry in the selected index
	* @param index index of the entry
	*/
	public String getEntry (int index)
	{
		return (getEntry (index, 0));
	}

	/**
	* Get the entry in the selected index
	* @param index index of the entry
	* @param flags on how to get this entry
	*/
	public String getEntry (int index, int flags)
	{
		if ( index >= getSize() ) {
			return null;
		}
		if ( (flags&NOCASE) != 0 ) {
			return (args [index].toLowerCase()) ;
		}
		return args [index];
	}

	/**
	* Gets the index of the selected entry
	* @param entry the entry to search for
	* @return the index of the selected entry, or -1 if this entry is not found
	*/
	public int getIndex (String entry)
	{
		return (getIndex (entry, 0));
	}

	/**
	* Gets the index of the selected entry
	* @param entry the entry to search for
	* @param flags various flags to define whay type of entry this is
	* @return the index of the selected entry, or -1 if this entry is not found
	*/
	public int getIndex (String entry, int flags)
	{
		boolean starts;

		starts = ( (flags&STARTS) != 0 );

		if ((flags&NOCASE)  != 0) {
			entry = entry.toLowerCase();
		}
		for ( int i = 0; i < getSize() ; i++ ) {
			if ( starts ) {
				if (getEntry (i, flags).startsWith (entry)) {
					return i;
				}
			}
			else {
				if (getEntry (i, flags).equals (entry)) {
					return i;
				}
			}
		}
		return -1;
	}
	

	/**
	* Check if this is one of the enties
	* @param entry the name of the entry
	*/
	public boolean hasEntry (String entry)
	{
		return hasEntry (entry, 0);
	}

	/**
	* Check if this is one of the enties
	* @param entry the name of the entry
	* @param ignoreCase ignore the case of the entry
	*/
	public boolean hasEntry (String entry, int flags)
	{
		return ( getIndex (entry, flags) != -1);
	}

	/**
	* Return a specified option parameter
	* @param opt option to search for
	*/
	public String getOption (String opt)
	{
		return (getOption (opt, 0));
	}

	/**
	* Return a specified option parameter
	* @param opt option to search for
	* @param flags these flags control the type of the option
	*/
	public String getOption (String opt, int flags)
	{
		int where, pos;

		if ((flags&NOCASE) != 0) {
			opt = opt.toLowerCase();
		}
		where = getIndexOption (opt, flags);
		if (where == -1) return null;
		if ((flags&STARTS) != 0 ) {
			pos = getEntry(where,flags).indexOf (opt) ;	// Should supply flags to find the option
			return (getEntry (where).substring (pos+opt.length()));	//Should NOT supply flags to getEntry to keep case sensitivity
		}
		return getEntry (where + 1);
	}

	/**
	* Gets the index number of the specified option
	*
	* @param opt the name of the entry
	*/
	public int getIndexOption (String opt)
	{
		return ( getIndexOption(opt, 0));
	}

	/**
	* Gets the index number of the specified option
	*
	* @param opt the name of the entry
	* @param flags these flags control if we should check other parameters also
	*/
	public int getIndexOption (String opt, int flags)
	{
		int res;

		if ( (flags&ALONE) != 0) {
			if ( (res = getIndex (opt, flags) )!= -1)  {
				return res;
			}
		}
		if ( (flags&WITHMINUS) != 0) {
			if ( (res = getIndex ( "-" + opt, flags )) != -1) {
				return res;
			}
		}
		if ( (flags&WITHDOUBLEMINUS) != 0) {
			if ( (res = getIndex ( "--" + opt, flags )) != -1) {
				return res;
			}
		}
		if ( (flags&WITHSLASH) != 0) {
			if ( (res = getIndex ( "/" + opt, flags )) != -1) {
				return res;
			}
		}
		if ( (flags&WITHREVERSESLASH) != 0) {
			if ( (res = getIndex ( "\\" + opt, flags ))  != -1) {
				return res;
			}
		}
		return -1;
	}

	/**
	* Check if this is one of the enties
	* @param opt the name of the entry
	*/
	public boolean hasOption (String opt)
	{
		return (hasOption (opt, 0)) ;
	}

	/**
	* Check if this is one of the enties
	*
	* @param opt the name of the entry
	* @param flags these flags control if we should check other parameters also
	*/
	public boolean hasOption (String opt, int flags)
	{
		return ( getIndexOption (opt, flags) != -1);
	}

	public Enumeration enumEntries ()
	{
		return new EnumArguments ( this, 0);
	}

	public Enumeration enumNoOptions (int flag)
	{
		return new EnumArguments ( this, flag);
	}


}
