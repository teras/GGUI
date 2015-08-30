package com.panayotis.awt;

import java.awt.event.ActionListener;

import java.awt.BorderLayout;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wTextField;
import com.panayotis.wt.wLabel;

public class LabeledTextField extends wPanel
{
	wTextField tf;

	public LabeledTextField ( String label )
	{
		tf = new wTextField();
		setLayout ( new BorderLayout () );
		add ( "West", new wLabel ( label) );
		add ( "Center", tf );
	}

	public String getText ()
	{
		return tf.getText ();
	}

	public void setText ( String text)
	{
		tf.setText ( text );
	}

	public void addActionListener ( ActionListener l )
	{
		tf.addActionListener ( l );
	}

	public wTextField getTextField ()
	{
		return tf;
	}

}
