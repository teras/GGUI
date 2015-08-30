package com.panayotis.ggui.objects; 

import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.ggui.objects.gguiObject;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wTextField;
import com.panayotis.wt.wTextArea;
import com.panayotis.wt.wLabel;


/** 
* This class defines the gguiFreetext object. This object is for entering 
* free text.
*/ 
public class  gguiFreetext extends gguiObject
{ 
	/**
	* Variables to store the state of this object
	*/
	private String leadText;
	private String trailText;
	private String defaultText;
	private boolean appearAlways;
	private String helpText;
	private String GUIType;

	/**
	* Components to visually display this object
	*/
	private wTextField tf;
	private wTextArea ta;

	/**
	* Components to visually create this object
	*/
	private LabeledTextField tfLead, tfTrail, tfDefault, tfHelp;
	private LabeledChoice cAlways, cGUIType;

	/** 
	* Creates a new gguiBoolean object.  
	*/ 
	public gguiFreetext () 
	{ 
		leadText = "";
		trailText = "";
		defaultText = "";
		appearAlways = false;
		helpText = "";
		GUIType = "textfield" ;
	}

	public void importData (PropertiesSection prop)
	{
		leadText = transf.setString (prop.getValue ("leadtext"), "leadText not set in FreeText.");
		trailText = transf.setString (prop.getValue ("trailtext"), "trailText not set in FreeText.");
		defaultText = transf.setString (prop.getValue ("defaulttext"), "defaultText not set in FreeText.");
		appearAlways = transf.setBoolean (prop.getValue ("appearalways"), true, "appearAlways is wrong or missing in FreeText.");
		helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in FreeText.");

		GUIType = transf.setString (prop.getValue ("guitype"), "GUIType not defined in FreeText.");
		GUIType = transf.simplifyString(GUIType);
		if (GUIType.equals ("")) {
			GUIType = "textfield";
		}
	}


	public wPanel createGUI ()
	{
		wPanel uiPanel;

		uiPanel = new wPanel ();
		uiPanel.setLayout ( new WizardLayout());
		uiPanel.add (new wLabel (helpText));
		if (GUIType.equals("textarea")) {
			ta = new wTextArea(defaultText, 3, 40);
			uiPanel.add (ta);
		}
		else {
			tf = new wTextField(defaultText);
			uiPanel.add (tf);
		}
		return uiPanel;
	}

	public wPanel getCreatorPanel ()
	{
		wPanel p;

		p = new wPanel();
		cAlways = new LabeledChoice ( "Appear this parameter, even if not exist");
		cGUIType = new LabeledChoice( "Visual type of this object");
		tfLead = new LabeledTextField( "The leading text of this parameter");
		tfTrail = new LabeledTextField ( "The ending text of this parameter");
		tfHelp = new LabeledTextField ( "Information help text");
		tfDefault = new LabeledTextField ( "The default internal text of the parameter");

		cAlways.add ("True");
		cAlways.add ("False");
		cGUIType.add ( "TextField" );
		cGUIType.add ( "TextArea" );
		cAlways.select ( (appearAlways) ? 0 : 1 );
		cGUIType.select ( (GUIType.equals("textarea")) ? 1 : 0 );
		tfLead.setText ( transf.stringCodes(leadText));
		tfTrail.setText ( transf.stringCodes(trailText));
		tfHelp.setText (transf.stringCodes(helpText));
		tfDefault.setText (transf.stringCodes(defaultText));

		p.setLayout ( new WizardLayout ());
		p.add ( tfHelp );
		p.add ( cAlways );
		p.add ( cGUIType );
		p.add ( tfLead );
		p.add ( tfDefault );
		p.add ( tfTrail );
		return p;
	}	

	public String getParam ()
	{
		String res;

		refreshValues();
		if (defaultText.equals("") && !appearAlways) {
			return "";
		}
		return transf.convertSpecial(leadText + defaultText +trailText) ;
	}

	public PropertiesSection getPropertiesSection( boolean onlyDefault )
	{
		PropertiesSection sect;
		
		refreshValues();
		sect = new PropertiesSection("gguiFreeText");
		if ( onlyDefault ) {
			sect.addEntry ( "defaultText", defaultText);
		}
		else {
			sect.addEntry ( "Type", "freetext");
			sect.addEntry ( "defaultText", transf.stringCodes(defaultText));
			sect.addEntry ("helpText", transf.stringCodes(helpText));
			sect.addEntry ( "GUIType", transf.stringCodes(GUIType));
			sect.addEntry ("leadText", transf.stringCodes(leadText));
			sect.addEntry ("trailText", transf.stringCodes(trailText));
			sect.addEntry ("appearAlways", (appearAlways) ? "true" : "false" );
		}
		return sect;
	}


	/**
	* Low level set variables to the actual value
	*/
	private void refreshValues ()
	{
		if ( tf != null ) {
			defaultText = tf.getText();
		}
		else if ( ta != null ) {
			defaultText = transf.stringCodes (ta.getText());
		}
		else if ( tfDefault != null ) {
			defaultText = tfDefault.getText();
			GUIType =  ( cGUIType.getSelectedIndex() == 0 ) ? "textfield" : "textarea" ;
			leadText = tfLead.getText ();
			trailText = tfTrail.getText ();
			appearAlways = ( cAlways.getSelectedIndex() == 0 )  ;
			helpText = tfHelp.getText();
		}
	}
}
