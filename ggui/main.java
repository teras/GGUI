package com.panayotis.ggui;


// Import code here
import java.io.*;

import com.panayotis.wizard.WizardListener;

import com.panayotis.ggui.gguiArguments;
import com.panayotis.ggui.gguiFileData; 
import com.panayotis.ggui.gguiWizard;
import com.panayotis.ggui.gguiResults;
import com.panayotis.ggui.creator.creatorFrame;
import com.panayotis.ggui.objects.gguiRoot;


import com.panayotis.wt.wFrame;

/**
*
* GGUI : a programmable user interface, in order to be able to
* run specific command line programs in a self
* explained graphical user interface, making the call
* to programs with complicated arguments much easier
* and safer.<P>
* This program is completely written in Java V 1.1
*
*/
public class main implements WizardListener
{

	private gguiWizard wz;

	private gguiArguments arg;


	private wFrame welcome;

	// for Wizard interface
	public void exitWizard(boolean how)
	{
		Runtime rt;
		Process proc;
		String command;
		BufferedReader exec_err;
		BufferedReader exec_out;
		PrintWriter exec_in;
		boolean windowed;

		windowed = wz.getRoot().getWindowed();
		proc = null;
		if (how) {
			gguiFileData.exportDefaults ( wz.getRoot(), arg.getUserGGUIFile() );
			command = wz.getAllParams ();
			rt = Runtime.getRuntime();
			try {
				proc = rt.exec ( arg.getShell());
				exec_err = new BufferedReader(new InputStreamReader(proc.getErrorStream ()));
				exec_out = new BufferedReader(new InputStreamReader(proc.getInputStream ()));
				exec_in = new PrintWriter (proc.getOutputStream (), true);
				exec_in.println (command);
				exec_in.close();
				proc.waitFor();
				new gguiResults ( exec_out, exec_err, proc.exitValue(), command, windowed);
				wz.hideWizard();
			}
			catch (Exception e) {
				new gguiResults ( "", e.toString(), 10, command, windowed);
			}
		}
		else {
			new gguiResults ( "", "Cancel selected.\n", 11, "", windowed);
		}
	}



	/**
	* Support for Interactive wizards
	* Needed in WizardInterface
	*/
	public int nextPage(int pg)
	{
		int newpage, countpages;
		newpage = wz.getRoot().getInteractivePage(pg-1);
		if ( newpage <1) {
			newpage = 0;
		}
		countpages = wz.getRoot().countPages();
		return ( newpage > countpages ) ? countpages : newpage;
	}
 


	/**
	* Constructor of this class
	*/ 
	public main( String args [] )
	{
		arg = new gguiArguments (args);
	}

	/**
	* Entry point of this application
	*/
	public static void main ( String args[] )
	{
		main me = new main( args );
		me.start();
	}

	private void start()
	{
		if ( ! arg.readPrefs ()) {
			System.err.println ("Warning: Can not find GGUI initialization file. Creating data structure.");
			String res = arg.createPrefs();
			if ( res!=null ) {
				System.err.println ("Error: "+res+" Exiting.");
			}
		}
		if ( arg.getDataDir () == null ) {
			System.err.println ( "Error: Can not find datafile directory. Exiting.");
			System.exit (2);
		}
		if ( arg.hasOption("help", gguiArguments.WITHMINUS) || arg.hasOption("help", gguiArguments.WITHDOUBLEMINUS) ) {
			printUsage();
			System.exit(0);
		}

		if ( arg.hasOption ("edit", gguiArguments.WITHMINUS )) {
			startCreator ();
		}
		else if ( arg.getCurrentGGUIFile () != null ) {
			startWizard();
		}
		else {
			System.err.println("Warning: error in providing GGUI datafile. Starting with editor.");
			startCreator();
		}
	}

	
	private void startCreator ()
	{
		creatorFrame creator;
		gguiRoot root;

		creator = new creatorFrame ( arg.getImageDir() );
		if ( arg.getCreatorGGUI() == null ) {
			System.err.println("Warning: Filename not provided, creating a new file with name \"unnamed\".");
			arg.setCurrentGGUI("unnamed");
		}
		String fname = arg.getCreatorGGUIFile();
		root = gguiFileData.importFile( fname, null, arg.getImageDir() );
		if ( root == null ) { root = new gguiRoot (); }
		root.setWizardName( arg.getCurrentGGUI());
		creator.setRoot ( root );
		creator.setDataDir ( arg.getDataDir() );
		creator.setVisible (true);
	}

	private void startWizard ()
	{
		gguiRoot root;

		wz = new gguiWizard ( this );
		wz.setSize ( 620,380);
		root = gguiFileData.importFile( arg.getCurrentGGUIFile(), arg.getUserGGUIFile(), arg.getImageDir() );
		if ( root == null ) {
			System.err.println("Can not create wizard. Exiting.");
			System.exit(1);
		}
		wz.setRoot ( root );
		root.setWizardName( arg.getCurrentGGUI());
		wz.displayWizard();
	}


	private void printUsage ()
	{
		System.err.println ( " GGUI: General Graphical User Interface");
		System.err.println ( "Usage:");
 		System.err.println ( "     ggui [-edit] <datafile>" );
		System.err.println ("Already defined datafiles:");
 		System.err.println ("     " + arg.getGGUIFiles (" ") );
	}
}
