package com.panayotis.ggui;

import java.io.*;

import com.panayotis.util.Properties;
import com.panayotis.util.PropertiesSection;
import com.panayotis.util.transf;
import com.panayotis.io.util.Files;
import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.ggui.objects.gguiRoot;
import com.panayotis.ggui.objects.gguiPage;
import com.panayotis.ggui.objects.gguiInteractive;
import com.panayotis.ggui.creator.creatorPagelist;

/**
* This class intends to be the interface which you can load/save data, depending the
* input type
*/
public class gguiFileData
{

    /**
    * Save to a file from the given creatorPagelist
    */
public static int exportFile ( gguiRoot in, String filename)
    {
        PrintWriter fileout;
        try {
	Files.copyFile (filename, filename+"~");
            fileout =  new PrintWriter ( new FileOutputStream ( filename ) );
            fileout.print ( exportString ( in) );
            fileout.close();
        }
        catch ( IOException e) {
            System.err.println ( "Error on writing to filename " + filename );
            System.err.println ( e.toString() );
            return 20;
        }
        return 0;
    }

private static String exportString ( gguiRoot root )
    {
        StringBuffer out;
        PropertiesSection sect;

        out = new StringBuffer();
        sect =  root.getPropertiesSection (false);
        sect.addEntry ( "Pages", String.valueOf ( root.countPages()));
        sect.setName ("[General]");
        out.append ( sect.produceString() + '\n');
        for ( int i = 0; i < root.countPages (); i++) {
	sect = root.getPage(i).getPropertiesSection(false);
	sect.addEntry ("Objects", String.valueOf ( root.countObjects(i) ) );
	sect.setName ( "[Page" + (i+1) + "]" );
	exportPropertiesSection (out , sect);

	sect = root.getPage(i).getInteractiveData().getPropertiesSection(false);
	sect.setName ( "[P"+ (i+1) + "Interactive]");
	exportPropertiesSection (out , sect);

	for ( int j = 0; j < root.countObjects (i); j++) {
		sect =root.getObject ( i,j).getPropertiesSection (false);
		sect.setName ( "[P" + (i+1) + "Object" + (j+1) + "]" );
		exportPropertiesSection ( out, sect);
	}
        }
        return out.toString() ;
    }


	private static void exportPropertiesSection ( StringBuffer out, PropertiesSection what)
	{
		if ( ! what.isEmpty() ) {
			out.append ( what.produceString () + '\n');
		}
	}

public static int exportDefaults (gguiRoot root, String filename)
    {
        PrintWriter out;
        int i,j;
        PropertiesSection sect ;
        gguiObject obj;

        try {
            Files.makeDirs(Files.stripFile(filename));
            out = new PrintWriter (new FileOutputStream (filename));	//Open file for output
            for (i=0; i < root.countPages(); i++) {
                for (j=0; j< root.countObjects(i); j++) {
                    obj = root.getObject (i,j);
                    sect = obj.getPropertiesSection ( true );
                    if ( ! sect.isEmpty () ) {
                        sect.setName ("[P" + (i+1) + "Object" + (j+1) + "]" );
                        out.println (sect.produceString() );
                    }
                }
            }
            out.close ();
        }
        catch ( IOException e ) {
            System.err.println ("Can not write to file " + filename);
            return 30;
        }
        return 0;
    }


	/**
	* Import a ggui file
	*/
	public static gguiRoot importFile ( String filename, String defaults, String imageDir)
	{

		Properties data;	// Filetype of the saved data
		PropertiesSection sObject;	// Temporary data storage of current object
		String type, loadClass;	// name of the type of current object
		int pages;		// how many pages do we have
		int objects;		// how many objects do we have in this page
		gguiRoot root;	// Here we store the readen data
		gguiPage page;	// the current page
		gguiObject obj;		// the current object


		if ( filename == null) {
			return null;
		}

		data = new Properties();
		data.setLoadIgnoreCase (true);
		data.readFile ( filename );
		if ( defaults != null && ( ! defaults.equals ("") ) ) {
			data.appendFile ( defaults );
		}

		sObject = data.getSection ("[general]");
		if ( sObject == null ) {	//Check if this file doesn't have "General" section
			return null;
		}
		root = new gguiRoot();
		root.importData (sObject);
		root.setName ( "general");

		// Count pages
		pages = transf.createInt (data.getValue("general","pages"), "Cannot count Wizard pages :  [General] Pages not properly set.");
		for ( int i=1; i <= pages; i++ ) {
			page = new gguiPage();
			page.setParent ( root );
			sObject = data.getSection ( "[page"+i+"]" );
			sObject.addEntry ( "image", imageDir + sObject.getValue ("image"));
			page.importData ( sObject );
			page.setName ( "page" + i );
			if ( transf.setBoolean (sObject.getValue ("interactive") , false, "Error on Page " + i + ". Can not find entry Interactive.") ) {
				page.setInteractiveData ( data.getSection ( "[p" + i + "interactive]" ) );
			}
			root.addPage (page);

			//Count objects for this page
			objects = transf.createInt ( data.getValue("page"  + i,"objects"), "Cannot count Wizard objects :  [Page" + i + "] Objects not properly set.");
			for ( int j=1; j<= objects; j++ ) {

				// Find current object information
				sObject = data.getSection ( "[p"+ i +"object"+ j + "]");
				type = sObject.getValue ("type");
				if ( type == null ) {
					type = "Object";
				}
				type = transf.simplifyString ( type );
				type = type.substring(0,1).toUpperCase() + type.substring (1 , type.length());
				loadClass = "com.panayotis.ggui.objects.ggui" + type;

				// Create object
				try {
					obj = (gguiObject) Class.forName(loadClass).newInstance();
					obj.setParent ( page ) ;
					obj.importData ( sObject );
					obj.setName (type.toLowerCase() + i + "-" + j );
					page.addObject ( obj );
				}
				catch (Exception e) {
					System.err.println ("Error on creating object " + type + ".");
					System.err.println ( e.toString());
				}
			}
		}
		return root;
	}

}

