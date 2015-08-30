package com.panayotis.awt;

import java.awt.event.*;
import java.util.Vector;

import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.ListNavigator;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.util.PropertiesEntry;
import com.panayotis.util.transf;
import com.panayotis.ggui.creator.pageEntry;

import com.panayotis.wt.wPanel;

public class ChoiceList extends ListNavigator
{
	private LabeledChoice cPage;

	public ChoiceList (String info, boolean selectedSupported)
	{
		super(info, selectedSupported);
	}

	public wPanel getVisualInterface ( )
	{
		cPage = new LabeledChoice (" Page name : ");

		cPage.addItemListener ( new ItemListener () {
			public void itemStateChanged (ItemEvent ev ) {
				changedPage ();
			}
		});
		return cPage;
	}

	/**
	* Use this method to create a new entry for this list
	*/
	public Object createNewEntry ()
	{
		return new pageEntry("0");
	}

	/**
	* Use this method to display the given entry on screen
	*/
	public void updateVisualList ( Object obj )
	{
		int pos;
		pos = transf.createInt ( ((pageEntry)obj).getValue(), null);
		if ( pos > 0 && pos <= cPage.getItemCount() ) {
			cPage.select ( pos);
		}
		else if (cPage.getItemCount() > 0 ){
			cPage.select (0);
		}
	}

	public void changedPage ()
	{
		int index;

		index = cPage.getSelectedIndex();
		((pageEntry)retrieveEntry()).setValue( String.valueOf (index));
	}


	public void insertPage ( String name, int index)
	{
		cPage.insert (name, index);
	}

	public void removePage ( int index)
	{
		cPage.remove (index);
	}

	public int getPageCount ()
	{
		return cPage.getItemCount();
	}

	public void selectPage (int index)
	{
		cPage.select (index);
	}

	public void selectPage (String pag)
	{
		cPage.select (pag);
	}

	public int getSelectedPageIndex()
	{
		return cPage.getSelectedIndex();
	}

	/**
	* WARNING! This method "thinks" that 1st page is page 1, not page 0!!!!
	*/
	public String getPageString ( int index )
	{
		return cPage.getItem (index);
	}

	/**
	* Get a Vector with all the given pages
	*/
	public Vector getPageList ()
	{
		Vector pages;

		pages = new Vector();
		for ( int i = 0 ; i < getDataSize(); i ++ ) {
			pages.addElement ( ((pageEntry)retrieveEntry (i)).getValue() );
		}
		return pages;
	}
}

