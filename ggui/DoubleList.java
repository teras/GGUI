package com.panayotis.awt;

import java.awt.event.*;
import java.util.Vector;

import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.ListNavigator;
import com.panayotis.util.PropertiesEntry;

import com.panayotis.wt.wPanel;

public class DoubleList extends ListNavigator
{
	LabeledTextField tfDisplay, tfParameter;


	public DoubleList (String info, boolean selectedSupported)
	{
		super(info, selectedSupported);
	}

	public wPanel getVisualInterface ( )
	{
		wPanel p;

		p = new wPanel();
		tfDisplay = new LabeledTextField ("Entry display help text");
		tfParameter = new LabeledTextField ("Entry actual parameter");
		p.setLayout ( new WizardLayout());
		p.add( tfDisplay );
		p.add( tfParameter );
		tfDisplay.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				changedDisplay ( );
			}
		} ) ;
		tfParameter.addActionListener ( new ActionListener () {
			public void actionPerformed (ActionEvent ev) {
				changedParameter ( );
			}
		} ) ;
		return p;
	}

	/**
	* Use this method to create a new entry for this list
	*/
	public Object createNewEntry ()
	{
		return new PropertiesEntry("","");
	}

	/**
	* Use this method to display the given entry on screen
	*/
	public void updateVisualList ( Object obj )
	{
		tfDisplay.setText ( ((PropertiesEntry)obj).getKeyword());
		tfParameter.setText ( ((PropertiesEntry)obj).getValue());
	}


	private void changedDisplay ( )
	{
		getEntry ().setKeyword (tfDisplay.getText());
	}

	private void changedParameter ( )
	{
		getEntry().setValue (tfParameter.getText());
	}

	private PropertiesEntry getEntry ()
	{
		return (PropertiesEntry) retrieveEntry();
	}

	private PropertiesEntry getEntry (int i )
	{
		return (PropertiesEntry) retrieveEntry(i);
	}

	/**
	* Get a Vector with all the given keywords
	*/
	public Vector getKeywordList ()
	{
		Vector keyword;

		keyword = new Vector();
		for ( int i = 0 ; i < getDataSize(); i ++ ) {
			keyword.addElement ( getEntry (i).getKeyword() );
		}
		return keyword;
	}

	/**
	* Get a Vector with all the given values
	*/
	public Vector getValueList ()
	{
		Vector value;

		value = new Vector();
		for ( int i = 0 ; i < getDataSize(); i ++ ) {
			value.addElement ( getEntry (i).getValue() );
		}
		return value;
	}
}
