package com.panayotis.io.util;

import java.io.*;
import java.util.zip.*;
import java.util.*;

public class Files
{

	public static String fixSlash (String dir)
	{
		if (dir == null) {
			return null;
		}
		return (dir.endsWith("/")) ? dir : dir + "/" ;
	}

	public static boolean makeDirs (String dir)
	{
		File d;
		d = new File (dir);
		return d.mkdirs ();
	}

	public static boolean copyFile (String source, String dest)
	{
		FileInputStream in;
		FileOutputStream out;
		byte [] buffer;
		int size;

		buffer = new byte [1000];
		try {
			in = new FileInputStream ( source) ;
			out = new FileOutputStream (dest);
			while ( (size = in.read (buffer, 0, 1000) ) != -1 ) {
				out.write ( buffer, 0, size );
			}
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}


	/**
	* Unzips a file in a special location
	* @param dir the directory to unzip the files
	* @param file the zip file
	*/
	public static boolean unzipFile (String dir, String file )
	{
		ZipFile zipfile;
		ZipEntry entry;
		Enumeration zip_entries;
		InputStream in;
		FileOutputStream out;
		int size;
		byte [] buffer;

		buffer = new byte [1000];
		try {
			zipfile = new ZipFile (file);
			zip_entries = zipfile.entries();
			while ( zip_entries.hasMoreElements() ) {
				entry = (ZipEntry)zip_entries.nextElement();
				Files.createDir( stripFile( dir + entry.getName( )));
				if ( ! entry.isDirectory () ) {
					in = zipfile.getInputStream(entry);
					out = new FileOutputStream ( dir + entry.getName());
					while ( (size = in.read (buffer, 0, 1000) ) != -1 ) {
						out.write ( buffer, 0, size );
					}
					in.close();
					out.close();
				}
			}
		}	
		catch (IOException e) {
			return false;
		}
		return true;
	}


	public static boolean chmodFile (String file, String mode)
	{
		Process proc;

		try {
			proc = Runtime.getRuntime ().exec ( "chmod " + mode + " " + file );
			if ( proc.waitFor () != 0 ) {
				return false;
			}
		}
		catch (InterruptedException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}


	/**
	* Remove the filename from a path. It cuts out the remaining characters
	* from the end until a "/" is found
	*/
	public static String stripFile ( String file )
	{
		if ( file == null ) {
			return null;
		}
		int last = file.lastIndexOf ("/");
		return (last == -1) ? file : file.substring (0, last+1);
	}

	/**
	* Remove the directory name from a path. It cuts out the last character (/) and then 
	* the remaining characters until a new "/" is found
	*/
	public static String stripDir (String file )
	{
		return ( file == null) ? null : Files.stripFile ( file.substring(0, file.length()-1));
	}

	public static String leaveOnlyFilename ( String file)
	{
		if ( file == null ) {
			return null;
		}
		int last = file.lastIndexOf ("/");
		return ( last == -1) ? file : file.substring ( last+1 ) ;
	}

	/**
	* Tries to create a non existand directory
	*/
	private static boolean createDir (String direc)
	{
		File file = new File (direc);
		return (file.exists() )  ? true : file.mkdirs() ;
	}

	public static boolean existsDir ( String direc)
	{
		if ( direc == null ) { return false; }
		File dir = new File (direc);
		return ( dir.exists() ) ? ( dir.isDirectory () ) : false;
	}

	public static boolean existsFile ( String file)
	{
		if ( file == null ) return false;
		File c_file = new File (file);
		return ( c_file.exists() ) ? ( ! c_file.isDirectory () ) : false;
	}

	/**
	* Check if a specified filename is writeable and a directory
	* @param direc the name of the file&directory
	*/
	public static boolean writableDir (String direc)
	{
		File dir = new File (stripFile(direc));
		while ( !dir.exists()) {
			dir = new File ( Files.stripDir (dir.getPath()));
		}
		if ( ! dir.canWrite () ) {
			return false;
		}
		if ( ! dir.isDirectory() )  {
			return false;
		}
		return true;
	}
}
