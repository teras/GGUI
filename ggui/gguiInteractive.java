package com.panayotis.ggui.objects;

import java.util.Vector;
import java.awt.event.*;

import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.ggui.creator.pageEntry;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.awt.ChoiceList;
import com.panayotis.wizard.WizardLayout;
import com.panayotis.ggui.creator.pageEntry;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wButton;

public class gguiInteractive extends gguiObject
{
	/**
	* Variables to store the state of this object
	*/
	private Vector links;
	private int linkObject;

	/**
	* Visual components to define the attributes of this object to be created
	*/
	private LabeledChoice cObject;
	private ChoiceList clJumpto;

	public gguiInteractive ()
	{
		links = new Vector();
		linkObject = -1;
		parent = null;
	}

	public void importData ( PropertiesSection prop )
	{
		int counter;

		linkObject = transf.createInt (prop.getValue ("object"), "Object not defined in Page-Interactive.");
		if ( linkObject <1) {
			linkObject = 1;
		}
		counter = 1;
		while ( prop.getValue ( "on"+counter+"jumpto") != null ) {
			links.addElement (transf.setString (prop.getValue( "on"+counter+"jumpto" ), "on"+counter+"jumpto not set in Page-Interactive."));
			counter++;
		}
	}

	public wPanel getCreatorPanel ()
	{
		wPanel p;
		wButton update;

		p = new wPanel();
		p.setLayout ( new WizardLayout () );
		cObject = new LabeledChoice ( "Object to use for interactive input");
		clJumpto = new ChoiceList ( "Select the page to jump to, when the #th option is selected...", false);
		update = new wButton ( " Update Interactive data " );

		cObject.add ("None - don't use interactivity");
		clJumpto.insertPage (" Next Page ", 0);
		setValuesToObjects();

		cObject.select ( (linkObject == -1 ) ? 0 : linkObject);
		for ( int i = 0; i < links.size(); i ++) { 
			clJumpto.addEntry ( i, new pageEntry ((String) links.elementAt(i) ));
		}
		clJumpto.deleteEntry (links.size());		// by default, DoubleList has an entry already (now the last one)
		clJumpto.displayEntry (0);

		p.add ( cObject );
		p.add ( clJumpto );
		p.add ( update);
		update.addActionListener ( new  ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				recalculateDisplay ( );
			}
		} ) ;
		return p;
	}

	private void setValuesToObjects()
	{
		gguiRoot c_root;
		gguiPage c_page;

		c_page = (gguiPage) getParent();
		while ( cObject.getItemCount()>1) {
			cObject.remove (1);
		}
		for ( int i = 0; i < c_page.countObjects(); i ++ ) {
			cObject.insert ( c_page .getObject (i).getName() , i+1);
		}

		while ( clJumpto.getPageCount()>1) {
			clJumpto.removePage ( 1);
		}
		c_root = (gguiRoot) c_page.getParent();
		for ( int i = 0; i < c_root.countPages(); i ++) { 
			clJumpto.insertPage ( c_root.getPage(i).getName(), i+1 );
		}
	}

	private void recalculateDisplay()
	{
		String object_data;
		String [] page_data;
		String data;
		int datasize;

		object_data = cObject.getSelectedItem();

		datasize = clJumpto.getDataSize();
		page_data = new String [datasize];
		for ( int i = 0; i < datasize ; i ++ ) {
			data = ((pageEntry)clJumpto.retrieveEntry (i)).getValue();
			page_data [i] = clJumpto.getPageString(transf.createInt (data, null));
		}

		setValuesToObjects();

		cObject.select(0);
		cObject.select (object_data);
		for ( int i = 0 ; i < datasize; i ++ ) {
			clJumpto.selectPage(0);
			clJumpto.selectPage(page_data[i]) ;
			((pageEntry)clJumpto.retrieveEntry(i)).setValue ( String.valueOf(clJumpto.getSelectedPageIndex()));
		}
		clJumpto.displayEntry ( clJumpto.getSelected());
	}


	public int getInteractivePage ( gguiPage page )
	{
		gguiObject gObj;
		int opt;

		if ( linkObject <1 || linkObject > page.countObjects() ) {
			return -1;
		}
		gObj = page.getObject(linkObject-1);
		opt = gObj.getInteractive() - 1;
		if ( opt <0 || opt > links.size()) {
			System.err.println ("Error while trying to find interactive position: " + opt);
			return -1;
		}
		return transf.createInt ( (String) links.elementAt (opt), "Error while trying to find interactive position: can't create integer.");
 	}

	public Vector getInteractiveLinks ()
	{
		return links;
	}

	public PropertiesSection getPropertiesSection ( boolean onlyDefault )
	{
		PropertiesSection sect;

		refreshValues();
		sect = new PropertiesSection ( "Page" );
		if ( ! onlyDefault ) {
			if ( linkObject >0) {
				sect.addEntry ( "Object", String.valueOf ( linkObject));
				for ( int i = 0 ; i < links.size(); i ++ ) {
					sect.addEntry ( "on" + (i+1) + "jumpTo" , (String) links.elementAt (i));
				}
			}
		}
		return sect;
	}

	private void refreshValues ()
	{
		if ( clJumpto != null ) {
			recalculateDisplay();
			linkObject = cObject.getSelectedIndex();
			if ( linkObject <= 0) {
				linkObject = -1;
				links = null;
			}
			else {
				links = clJumpto.getPageList ();
			}
		}
	}

}
