package com.panayotis.awt;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.*;
import java.util.Vector;

import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.LabeledTextField;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wTextField;
import com.panayotis.wt.wButton;
import com.panayotis.wt.wTextArea;
import com.panayotis.wt.wLabel;

public abstract class ListNavigator extends wPanel
{
	private Vector entries;
	wTextField tfInfo;

	private int selected, current;
	private boolean supportSelected;

	public ListNavigator ( String text, boolean supSelected)
	{
		wPanel pButtons;
		wTextArea taButtons;
		wButton bBegin, bPrev, bInsert, bAdd, bDelete, bNext, bEnd, bSelect;

		supportSelected = supSelected;
		entries = new Vector();

		bBegin = new wButton ("<<");
		bPrev = new wButton ("<");
		bInsert = new wButton ("^");
		bAdd = new wButton ("+");
		bDelete = new wButton ("X");
		bNext = new wButton (">");
		bEnd = new wButton (">>");

		pButtons = new wPanel ();
		tfInfo = new wTextField ();

		String taText = ( supportSelected) ? "\n*  Make current entry the default one" : "" ;
		taText = " Usage of navigation buttons:\n<<  Go to the begining\n<  Go to previous entry\n^  Insert entry before current entry\n+  Insert entry after current entry\nX  Delete current entry\n>  Go to next Entry\n>>  Go to lastEntry" + taText;
		taButtons = new wTextArea ( taText , 5, 10);

		pButtons.setLayout ( new GridLayout ( 1,7 + ((supportSelected) ? 1 : 0) ));
		setLayout ( new WizardLayout ());
		taButtons.setEditable ( false );
		tfInfo.setEditable ( false );

		pButtons.add ( bBegin );
		pButtons.add ( bPrev );
		pButtons.add ( bInsert );
		pButtons.add ( bAdd );
		pButtons.add ( bDelete );
		pButtons.add ( bNext );
		pButtons.add ( bEnd );

		add ( new wLabel ( text ) );
		add ( getVisualInterface() );
		add ( pButtons);
		add ( tfInfo );
		add ( taButtons );

		bBegin.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickBegin ();
			}
		} ) ;
		bPrev.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickPrev ();
			}
		} ) ;
		bInsert.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickInsert ();
			}
		} ) ;
		bAdd.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickAdd ();
			}
		} ) ;
		bDelete.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickDelete ();
			}
		} ) ;
		bNext.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickNext ();
			}
		} ) ;
		bEnd.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				clickEnd ();
			}
		} ) ;
		if ( supportSelected) {
			bSelect = new wButton ("*");
			pButtons.add ( bSelect );
			bSelect.addActionListener ( new ActionListener () {
				public void actionPerformed (ActionEvent ev) {
					clickSelect ();
				}
			} ) ;
			setSelected (0) ;
		}

		current = 0;
		addEntry (current);
	}

	abstract wPanel getVisualInterface();
	abstract Object createNewEntry();
	abstract void updateVisualList(Object obj);

	private void clickBegin ()
	{
		selectEntry ( 0 );
	}

	private void clickPrev ()
	{
		selectEntry (current-1);
	}

	private void clickInsert ()
	{
		addEntry (current);
	}

	private void clickAdd ()
	{
		addEntry (current+1);
	}

	private void clickDelete ()
	{
		deleteEntry (current);
	}

	private void clickNext ()
	{
		selectEntry (current+1);
	}

	private void clickEnd ()
	{
		selectEntry (getDataSize()-1);
	}

	private void clickSelect ()
	{
		setSelected (current );
		setInfoText();
	}

	public void addEntry ( int index)
	{
		addEntry ( index, createNewEntry () );
	}

	/**
	* Use this method to select the given entry on screen
	*/
	private void selectEntry ( int index )
	{
		if ( index <0 || index >= getDataSize() ) {
			return;
		}
		displayEntry ( index );
	}

	/**
	* Use this method to delete a specified entry
	*/
	public void deleteEntry ( int index )
	{
		if ( index < 0 || index >= getDataSize() || ( index == 0 && getDataSize() == 1 ) ) {
			return;
		}
		removeEntry ( index );
		if ( index == getDataSize() ) {
			index--;
		}
		if ( supportSelected) {
			if ( getSelected() == getDataSize() ) {
				setSelected ( getDataSize() -1 );
			}
		}
		displayEntry ( index );
	}

	/**
	* Low-level : Use this method to display the given entry on screen
	*/
	public void displayEntry ( int index )
	{
		current = index;
		updateVisualList ( retrieveEntry (index) ) ;
		setInfoText ();
	}


	/**
	* Use this method to produce the information line at the bottom of the buttons
	*/
	private void setInfoText ()
	{
		String isSelected = ( current == getSelected() ) ? "SELECTED" : "unselected";
		tfInfo.setText (" Entry #" + (current+1) + " (out of " + getDataSize()  + "). " + ( (supportSelected) ? " (" + isSelected + ") Selected entry #" + ( getSelected() +1) + "." : "" ) );
	}

	/**
	* Set the selected item to the given one
	*/
	public void setSelected ( int index )
	{
		if ( index > 0 && index >= getDataSize() ) {
			index --;
		}
		selected = index;
	}

	/**
	* Get the selected index number
	*/
	public int getSelected ( )
	{
		return selected;
	}

	/**
	* Low level delete entry of a specified position
	*/
	private void removeEntry ( int index)
	{
		entries.removeElementAt ( index);
	}

	/**
	* Low level get the specified entry
	*/
	public Object retrieveEntry ()
	{
		return retrieveEntry (current) ;
	}
	public Object retrieveEntry ( int index)
	{
		return entries.elementAt ( index);
	}

	/**
	* Low - Level add an entry to the specified position
	*/
	public void addEntry ( int index, Object entry )
	{
		if ( index >=0 && index <= getDataSize()) {	// Wa CAN add an entry to the getDataSize() position - at far end!
			entries.insertElementAt ( entry , index);
			displayEntry ( index );
		}
		else {
			return;
		}
	}

	/**
	* Low level count the number of entries
	*/
	public int getDataSize ()
	{
		return entries.size();
	}
}
