package com.panayotis.util;

import java.util.*;
import com.panayotis.util.Arguments;

class EnumArguments implements Enumeration
{
	private Arguments arg;
	private int pointer;
	private int flags;

	EnumArguments (Arguments ar, int fl)
	{
		arg = ar;
		pointer = 0;
		flags = fl;
	}

	public Object nextElement ()
	{
		return (Object)arg.getEntry (pointer ++);
	}

	public boolean hasMoreElements ()
	{
		String current;

		while ( pointer < arg.getSize()) {
			current = arg.getEntry (pointer);
			if ( (flags & Arguments.WITHMINUS) != 0 && current.startsWith ("-")  && current.length()>1 && current.substring(1,1) != "-"  ) {
			}
			else if ( (flags & Arguments.WITHDOUBLEMINUS) != 0 && current.startsWith ("--")  ) {
			}
			else if ( (flags & Arguments.WITHSLASH) != 0 && current.startsWith ("/") ) {
			}
			else if ( (flags & Arguments.WITHREVERSESLASH) != 0 && current.startsWith ("\\") ) {
			}
			else {
				return true;
			}
			pointer ++;
		}
		return false;
	}
}
