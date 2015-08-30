package com.panayotis.ggui.objects; 

import java.awt.FlowLayout;

import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.wizard.WizardLayout;
import com.panayotis.ggui.objects.gguiObject;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wTextField;
import com.panayotis.wt.wTextArea;
import com.panayotis.wt.wLabel;

/** 
* This class defines the gguiInfo object. This object is for displaying 
* information
*/ 
public class  gguiInfo extends gguiObject
{ 
	/**
	* Variables to store the state of this object
	*/
	private String helpText;
	private String GUIType;

	/**
	* Components to visually create this object
	*/
	private LabeledTextField tfHelp;
	private LabeledChoice cGUIType;

	public gguiInfo ()
	{
		helpText = "";
		GUIType = "";
	}

	public void importData (PropertiesSection prop)
	{
		helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in Info.");

		GUIType = transf.setString (prop.getValue ("guitype"), null);
 		GUIType = transf.simplifyString(GUIType);
 		if (GUIType.equals ("")) {
			GUIType = "label";
		}
	}


	public wPanel createGUI ()
	{
		wPanel uiPanel;

		uiPanel = new wPanel ();
		if (GUIType.equals("textfield")) {
			uiPanel.setLayout ( new WizardLayout());
			wTextField tf = new wTextField (helpText); 
			tf.setEditable (false);
			uiPanel.add ( tf );
		}
		else if (GUIType.equals("textarea")) {
			uiPanel.setLayout ( new WizardLayout());
			wTextArea ta = new wTextArea(helpText, 3, 40);
			ta.setEditable (false);
			uiPanel.add (ta);
		}
		else {
			uiPanel.setLayout ( new FlowLayout());
			uiPanel.add (new wLabel (helpText));
		}
		return uiPanel;
	}

	public wPanel getCreatorPanel ()
	{
		wPanel p;

		p = new wPanel();
		p.setLayout ( new WizardLayout ());
		cGUIType = new LabeledChoice( "Visual type of this object");
		tfHelp = new LabeledTextField ( "Information help text");

		cGUIType.add ( "Label" );
		cGUIType.add ( "TextField" );
		cGUIType.add ( "TextArea" );
		if ( GUIType.equals("textfield")) {
			cGUIType.select (1);
		}
		else if ( GUIType.equals ("textarea")) {
			cGUIType.select (2);
		}
		else {
			cGUIType.select (0);
		}
		tfHelp.setText (transf.stringCodes(helpText));

		p.add ( tfHelp );
		p.add ( cGUIType );
		return p;
	}	

	public PropertiesSection getPropertiesSection ( boolean onlyDefault)
	{
		PropertiesSection sect;

		refreshValues();
		sect = new PropertiesSection ( "gguiInfo");
		if ( ! onlyDefault ) {
			sect.addEntry ( "Type", "info");
			sect.addEntry ( "GUIType", GUIType );
			sect.addEntry ( "helpText", transf.stringCodes(helpText));
		}
		return sect;
	}

	/**
	* Low level set variables to the actual value
	*/
	private void refreshValues ()
	{
		if ( tfHelp != null ) {
			switch ( cGUIType.getSelectedIndex () ) {
			case 0:
				GUIType = "label";
				break;
			case 1:
				GUIType = "textfield";
				break;
			case 2:
				GUIType = "textarea";
				break;
			}
			helpText = tfHelp.getText ();
		}
	}
}
