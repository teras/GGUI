package com.panayotis.awt;

import java.awt.event.*;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wComboBox;
import com.panayotis.wt.wLabel;
import com.panayotis.wt.wComboBoxModel;

import java.awt.BorderLayout;

public class LabeledChoice extends wPanel
{
	wComboBox ch;
	wComboBoxModel model;
	
	public LabeledChoice ( String label )
	{
		model = new wComboBoxModel();
		ch = new wComboBox(model);
		ch.setEditable(false);
		setLayout ( new BorderLayout () );
		add ( "West", new wLabel ( label) );
		add ( "Center", ch );
	}

	public wComboBox getChoice ()
	{
		return ch;
	}

	public void add ( String item)
	{
		model.addElement ( item );
	}

	public void insert ( String item, int index)
	{
		model.insertElementAt ( item, index );
	}

	public void remove ( String item)
	{
		model.removeElement ( item );
	}

	public void remove ( int index)
	{
		model.removeElementAt ( index );
	}

	public int getItemCount ()
	{
		return ch.getItemCount();
	}

	public String getItem (int index)
	{
		return (String)model.getElementAt(index);
	}

	public void addItemListener ( ItemListener listener)
	{
		ch.addItemListener (listener);
	}

	public int getSelectedIndex ( )
	{
		return ch.getSelectedIndex ( );
	}

	public String getSelectedItem ( )
	{
		return (String)model.getSelectedItem ( );
	}

	public Object []  getSelectedObjects ( )
	{
		return ch.getSelectedObjects ( );
	}

	public void select ( int index)
	{
		ch.setSelectedIndex ( index );
	}

	public void select ( String item)
	{
		ch.setSelectedItem ( item );
	}

}
