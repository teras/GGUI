package com.panayotis.ggui.objects; 

import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.ggui.objects.gguiObject;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wCheckBox;
import com.panayotis.wt.wComboBox;
import com.panayotis.wt.wLabel;

/** 
* This class defines the gguiBoolean object. This object is for boolean 
* commandline options.
*/ 
public class  gguiBoolean extends gguiObject
{ 
	/**
	* Variables to store the state of this object
	*/
	private String forceEnabled;
	private String forceDisabled;
	private boolean defaultState;
	private String helpText;
	private String enabledText;
	private String disabledText;
	private String GUIType;

	/**
	* Components to visually display this object
	*/
	private wCheckBox cb;
	private wComboBox ch;

	/**
	* Components to visually create this object
	*/
	private LabeledChoice cState, cGUIType;
	private LabeledTextField tfEnabled, tfDisabled, tfHelp, tfEnabledText, tfDisabledText;


	public gguiBoolean ()
	{
		forceEnabled = "";
		forceDisabled = "";
		defaultState = false;
		helpText = "";
		enabledText = "";
		disabledText = "";
		GUIType = "checkbox";
	}


	public void importData (PropertiesSection prop)
	{
		forceEnabled = transf.setString (prop.getValue("forceenabled"), "forceEnabled not set in Boolean.");
 		forceDisabled = transf.setString (prop.getValue ("forcedisabled"), "forceDisabled not set in Boolean.");
 		defaultState = transf.setBoolean (prop.getValue ("defaultstate"), false, "defaultState is wrong or missing in Boolean.");

		helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in Boolean.");
		enabledText = transf.setString (prop.getValue ("enabledtext"), "enabledText not defined in Boolean.");
		disabledText = transf.setString (prop.getValue ("disabledtext"), "disabledText not defined in Boolean.");

		GUIType = transf.setString (prop.getValue ("guitype"), "GUIType not defined in Boolean.");
		GUIType = transf.simplifyString(GUIType);
		if (GUIType.equals ("")) {
			GUIType = "checkbox";
		}
	}


	public wPanel createGUI ()
	{
		wPanel uiPanel;

		uiPanel = new wPanel ();
		uiPanel.setLayout ( new WizardLayout());
		uiPanel.add (new wLabel (helpText));
		if (GUIType.equals("choice")) {
			ch = new wComboBox();
			ch.setEditable(false);
			ch.addItem (enabledText);
			ch.addItem (disabledText);
			if (! defaultState ) ch.setSelectedIndex (1);
			uiPanel.add (ch);
		}
		else {
			cb = new wCheckBox(enabledText);
			cb.setSelected (defaultState);
			uiPanel.add (cb);
		}
		return uiPanel;
	}

	public wPanel getCreatorPanel ()
	{
		wPanel p;

		p = new wPanel();
		cState = new LabeledChoice ( "Default state for this option");
		cGUIType = new LabeledChoice( "Visual type of this object");
		tfEnabled = new LabeledTextField( "Parameter when this option is selected");
		tfDisabled = new LabeledTextField ( "Parameter when this option is not selected");
		tfHelp = new LabeledTextField ( "Information help text");
		tfEnabledText = new LabeledTextField ( "Information text for the selected option" );
		tfDisabledText = new LabeledTextField ("Information text for the unselected option" );

		cState.add ("Enabled");
		cState.add ("Disabled");
		cGUIType.add ( "CheckBox" );
		cGUIType.add ( "Choice" );

		cState.select ( (defaultState) ? 0 : 1 );
		cGUIType.select ( (GUIType.equals("choice")) ? 1 : 0 );
		tfEnabled.setText (transf.stringCodes(forceEnabled));
		tfDisabled.setText (transf.stringCodes(forceDisabled));
		tfHelp.setText (transf.stringCodes(helpText));
		tfEnabledText.setText (transf.stringCodes(enabledText));
		tfDisabledText.setText (transf.stringCodes(disabledText));

		p.setLayout ( new WizardLayout ());
		p.add ( tfHelp );
		p.add ( cState );
		p.add ( cGUIType );
		p.add ( tfEnabled );
		p.add ( tfEnabledText );
		p.add ( tfDisabled );
		p.add ( tfDisabledText );
		return p;
	}	

	public String getParam ()
	{
		refreshValues ();
		return ( defaultState ) ? transf.convertSpecial(forceEnabled) : transf.convertSpecial(forceDisabled);
	}


	public int getInteractive ()
	{
		refreshValues ();
		return ( defaultState ) ? 1 : 2 ;
	}

	public PropertiesSection getPropertiesSection ( boolean onlyDefault)
	{
		PropertiesSection sect;

		refreshValues();
		sect = new PropertiesSection ( "gguiBoolean");
		if (onlyDefault ) {
			sect.addEntry ( "defaultState", ( defaultState ) ? "true" : "false" );
		}
		else {
			sect.addEntry ( "Type", "boolean");
			sect.addEntry ( "defaultState", ( defaultState ) ? "true" : "false" );
			sect.addEntry ( "GUIType", GUIType );
			sect.addEntry ( "forceEnabled", transf.stringCodes(forceEnabled));
			sect.addEntry ( "enabledText", transf.stringCodes(enabledText));
			sect.addEntry ( "forceDisabled", transf.stringCodes(forceDisabled));
			sect.addEntry ( "disabledText", transf.stringCodes(disabledText));
			sect.addEntry ( "helpText", transf.stringCodes(helpText));
		}
		return sect;
	}

	/**
	* Low level set variables to the actual value
	*/
	private void refreshValues ()
	{
		if ( cb != null ) {
			defaultState = cb.isSelected();
		}
		else if ( ch != null ) {
			defaultState = ( ch.getSelectedIndex() == 0 );
		}
		else if ( cState != null ) {
			defaultState = (cState.getSelectedIndex () == 0 );
			GUIType =  ( cGUIType.getSelectedIndex() == 0 ) ? "chechbox" : "choice" ;
			forceEnabled = tfEnabled.getText() ;
			forceDisabled = tfDisabled.getText() ;
			helpText = tfHelp.getText ();
			enabledText = tfEnabledText.getText ();
			disabledText = tfDisabledText.getText ();
		}
	}

}
