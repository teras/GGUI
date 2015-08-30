package com.panayotis.ggui;

import com.panayotis.util.Arguments;
import com.panayotis.util.Zipper;
import com.panayotis.util.Properties;
import com.panayotis.util.transf;

import com.panayotis.io.util.Files;
import com.panayotis.io.gguiFilenameFilter;

import java.io.*;
import java.util.*;

public class gguiArguments extends Arguments
{
	Properties prefs;
	String ggui;

	public gguiArguments (String arg[])
	{
		super (arg);
		ggui = null;
	}

	public String getShell ()
	{
		return prefs.getValue ("files","shell", "sh -s");
	}

	public String getCurrentGGUIFile ()
	{
		if ( ggui == null ) ggui = getCurrentGGUI ();
		return ( ggui != null) ? Files.fixSlash(prefs.getValue ("files","data") ) + ggui + ".ggui" : null;
	}

	public String getUserGGUIFile ()
	{
		if ( ggui == null ) ggui = getCurrentGGUI ();
		return ( ggui != null) ? Files.fixSlash(System.getProperties().getProperty ("user.home"))  + ".ggui/" + ggui + ".ggui" : null;
	}

	public String getCreatorGGUI () {
		if ( ggui == null ) ggui = getOption ( "edit", gguiArguments.WITHMINUS );
		return ggui;
	}
	
	public String getCreatorGGUIFile () {
		return ( ggui != null ) ? getDataDir() + ggui + ".ggui" : null;
	}

	public void setCurrentGGUI(String newggui) {
		ggui = newggui;
	}
	
	public String getCurrentGGUI ()
	{
		if ( ggui != null ) return ggui;
		
		String file;
		Enumeration e;

		e = enumNoOptions ( WITHMINUS+WITHDOUBLEMINUS);
		while ( e.hasMoreElements () ) {
			ggui = (String)e.nextElement ();
			file = Files.fixSlash(prefs.getValue ("files","data") ) + ggui + ".ggui";
			if ( Files.existsFile (file) ) {
				return ggui;
			}
		}
		return null;
	}	

	public String getImageDir ()
	{
		String dir;

		dir = Files.fixSlash(prefs.getValue ("files","images") );
		return  ( Files.existsDir (dir) ) ? dir : null;
	}

	public String getDataDir ()
	{
		String dir;

		dir = Files.fixSlash(prefs.getValue ("files","data") ) ;
		return ( Files.existsDir (dir) ) ? dir : null;
	}

	/** 
	 * Create a initialization file if none exists
	 */
	public String createPrefs() {
		String home = Files.fixSlash(System.getProperties().getProperty ("user.home"));
		prefs = new Properties();
		prefs.setLoadIgnoreCase(true);
		prefs.setValue("Files", "Data", home+".ggui/data/");
		prefs.setValue("Files", "Images", home+".ggui/img/");
		prefs.setValue("Files", "Shell", "sh -s");

		if ( new File (home+".ggui/").mkdir() != true ) { return "Can not create ~/.ggui direcotry"; }
		if ( new File (home+".ggui/data/").mkdir() != true ) { return "Can not create ~/.ggui/data directory"; }
		if ( new File (home+".ggui/img/").mkdir() != true ) { return "Can not create ~/.ggui/img directory"; }

		if ( !prefs.saveFile(home+".ggui/gguirc")) {
			return "Could not save default preferences";
		}
		new Zipper (home+".ggui/");
		return null;
	}
	
	/**
	* Read the special initialization file for this wizard
	*/ 
	public boolean readPrefs ()
	{
		String home;

		home = Files.fixSlash(System.getProperties().getProperty ("user.home"));
		prefs = new Properties ();
		prefs.setLoadIgnoreCase (true);
		if ( ! prefs.readFile ("/etc/gguirc") ) {
			if ( ! prefs.readFile ("/usr/etc/gguirc")) {
				if ( ! prefs.readFile ("/usr/local/etc/gguirc")) {
					if ( ! prefs.readFile (home+".ggui/gguirc")) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public String getGGUIFiles (String seperator)
	{
		String res;
		String resL [];

		res = "";
		resL = getGGUIFilesList();
		if  ( resL == null || resL.length == 0 ) {
			return "Error: Not any datafiles found!";
		}
		for ( int i = 0; i < resL.length; i++) {
			res += (resL [i])+seperator;
		}
		res = res.substring (0, res.length()-1);
		return res;
	}

	public String[] getGGUIFilesList ()
	{
		String dir;
		File f_dir;
		String data [];

		dir = getDataDir();
		if ( dir == null ) {
			return null;
		}
		f_dir = new File (dir);
		data = f_dir.list (new gguiFilenameFilter ());
		if ( data == null ) {
			return null;
		}
		for ( int i = 0; i< data.length; i++ ) {
			data [i] = data[i].substring (0, data[i].length() - 5);
		}
		return data;
	}
}
