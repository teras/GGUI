package com.panayotis.io;

import java.io.*;

public class gguiFilenameFilter implements FilenameFilter
{
	public boolean accept ( File dir, String name)
	{
		if ( name.endsWith (".ggui")) {
			return true;
		}
		return false;
	}
}
