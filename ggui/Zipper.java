// ******************************************************************************
// WizardExec.java:	Application
//  This application demostrates the use of the Wizard class
// ******************************************************************************

package com.panayotis.util;

import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Exception;

import java.util.Properties;

import com.panayotis.util.ZipFeedback;

//==============================================================================
// Main Class for applet WizardExec
//
//==============================================================================
public class Zipper
{
	ZipFile zip;
	String directory;
	ZipFeedback feed;
	int stripPathSize;
	
	public Zipper () {
	}
	
	
	public Zipper (String target) {
		this();
		if ( setZipFile (getSelf()) != null ) {
			System.err.println("An error occured while trying to open the ggui.jar file");
		}
		setTargetDir(target);
		setStripPathSize(12); // size of "install/"
		unzipDir("installdata/");
	}

	
	private String getSelf() {
		Properties sys = System.getProperties();
		String classpath = sys.getProperty("java.class.path");
		return classpath;
	}
	
	public String setZipFile ( String zp) {
		try {
			setZipFile( new ZipFile(zp));
		}
		catch ( IOException e ) {
			return e.toString();
		}
		return null;
	}

	public String setZipFile ( File zp ) {
		try {
			setZipFile(new ZipFile(zp));
		}
		catch ( IOException e ) { 
			return e.toString();
		}
		return null;
	}

	public void setZipFile ( ZipFile zp ) {
		zip = zp;
		directory = "./";
		feed=null;
		stripPathSize = 0;
	}
	
	public void setTargetDir ( String dir ) {
		directory = dir;
		if ( directory != "" ) directory = directory + '/';
	}

	public void setStripPathSize ( int spt ) {
		stripPathSize = spt;
	}

	public void setFeedback ( ZipFeedback fb) {
		feed=fb;
	}
	
	public int countElements (String fromdir) {
		int count = 0;
		Enumeration file_enum;
		file_enum = zip.entries();
		while ( file_enum.hasMoreElements() ) {
			if ( ((ZipEntry)file_enum.nextElement()).getName().startsWith(fromdir )) count++;
		}
		return count;
	}
		
	
	public String unzipFile ( String name) {
		ZipEntry entry;
		entry = zip.getEntry(name);
		if ( entry == null ) return "Entry not found";
		return constructFile ( entry, new File ( directory+entry.getName().substring(stripPathSize)) );
	}
	
	public String unzipDir ( String fromdir) {
		Enumeration file_enum;
		ZipEntry entry;
		int index = 0;
		String res;
		
		
		file_enum = zip.entries();
		while ( file_enum.hasMoreElements() ) {
			index++;
			entry = (ZipEntry)file_enum.nextElement();
			if ( entry.getName().startsWith(fromdir)){
				if ( feed != null ) feed.updatePosition();
				res = constructFile ( entry, new File ( directory+entry.getName().substring(stripPathSize) ) );
				if ( res != null ) return res;
			}
		}
		return null;
	}


	private String constructFile ( ZipEntry entry, File outfile) {
		if ( entry.isDirectory() ) {
			outfile.mkdirs();
		}
		else {
			int buffer;
			try {
				File pdir = new File ( outfile.getParent() );
				pdir.mkdirs();
				BufferedInputStream in = new BufferedInputStream ( zip.getInputStream ( entry ));
				BufferedOutputStream out = new BufferedOutputStream ( new FileOutputStream ( outfile ));
				while ( (buffer=in.read()) != -1 ) {
					out.write( (byte)buffer );
				}
				in.close();
				out.close();
			}
			catch ( Exception e ) {
				return e.toString();
			}
		}
		return null;
	}

}
