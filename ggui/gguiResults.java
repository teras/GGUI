package com.panayotis.ggui;

import java.awt.event.*;
import java.io.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;

import com.panayotis.util.transf;

import com.panayotis.wt.wFrame;
import com.panayotis.wt.wTextArea;
import com.panayotis.wt.wTextField;
import com.panayotis.wt.wPanel;
import com.panayotis.wt.wButton;
import com.panayotis.wt.wLabel;

public class gguiResults extends wFrame
{
	wTextArea tError, tOutput, tCommand;
	wTextField tCode;
	int code;

	public gguiResults ( BufferedReader out, BufferedReader err, int code, String command, boolean windowed) throws java.io.IOException
	{
		this ( transf.ReaderToString (out), transf.ReaderToString (err), code, command, windowed);
	}

	public gguiResults (String output, String errors, int result_code, String command, boolean windowed)
	{
		code = result_code;
		if ( windowed ) {
			createGUI ();
			tOutput.setText (output);
			tError.setText (errors);
			tCode.setText ("Result code: " + result_code);
			tCommand.setText (command);
			setVisible (true);
		}
		else {
			//System.out.println ("Command :\n"+command);
			//System.out.println ("Result code :\n"+result_code);
			//System.out.println ("* Output *");
			System.out.print (output);
			//System.err.println ("* Errors *");
			System.err.print(errors);
			finish();
		}
	}

	private void finish()
	{
		System.exit (code);
	}

	private void createGUI ()
	{
		wPanel pError, pOutput, pCommand, pCenter, pBottom;
		wButton bOK;

		setBackground(SystemColor.control);
		setForeground( SystemColor.controlText);

		pError = new wPanel();
		pOutput = new wPanel();
		pCommand = new wPanel();
		pCenter = new wPanel();
		pBottom = new wPanel();

		bOK = new wButton ( "OK");

		tError = new wTextArea ();
		tOutput = new wTextArea();
		tCommand = new wTextArea(2,2);
		tCode = new wTextField();
		tError.setEditable (false);
		tOutput.setEditable (false);
		tCode.setEditable (false);
		tCommand.setEditable (false);

		pOutput.setLayout ( new BorderLayout());
		pError.setLayout ( new BorderLayout());
		pCommand.setLayout ( new BorderLayout ());
		pCenter.setLayout (new GridLayout ( 2,1 ));
		pBottom.setLayout ( new BorderLayout ());
		getContentPane().setLayout ( new BorderLayout ());

		pOutput.add ( "North", new wLabel ("Output"));
		pOutput.add ( "Center", tOutput);
		pError.add ( "North", new wLabel ("Error"));
		pError.add ( "Center", tError);
		pCommand.add ("North", new wLabel ("Command executed"));
		pCommand.add ("Center", tCommand);

		pCenter.add (pOutput);
		pCenter.add (pError);
		pBottom.add ( "North", tCode);
		pBottom.add ("South", bOK);

		getContentPane().add ( "North", pCommand);
		getContentPane().add ("South", pBottom);
		getContentPane().add ("Center", pCenter);

		setSize ( 600, 450);
		setTitle ("Results");

		addWindowListener ( new WindowAdapter () {
			public void windowClosing ( WindowEvent ev ) {
				windowClose();
			}
		} ) ;
		bOK.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e) {
				OKclicked ();
			}
		} ) ;
	}

	private void windowClose ()
	{
		setVisible (false);
		dispose();
		finish();
	}

	private void OKclicked ()
	{
		windowClose();
	}

}
