package com.panayotis.ggui.objects; 


import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.browseButton;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.ggui.objects.gguiObject;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wLabel;


/** 
* This class defines the gguiFreetext object. This object is for entering 
* free text.
*/ 
public class  gguiFilechooser extends gguiObject
{ 
	/**
	* Variables to store the state of this object
	*/
	private String leadText;
	private String trailText;
	private String defaultFile;
	private boolean appearAlways;
	private String helpText;

	/**
	* Components to visually display this object
	*/
	private browseButton bb;

	/**
	* Components to visually create this object
	*/
	private LabeledTextField tfLead, tfTrail, tfDefault, tfHelp;
	private LabeledChoice cAlways;

	public gguiFilechooser ()
	{
		leadText = "" ;
		trailText = "";
		defaultFile = "";
		appearAlways = false;
		helpText = "";
	}

	public void importData (PropertiesSection prop)
	{
		leadText = transf.setString (prop.getValue ("leadtext"), "leadText not set in FileChooser.");
		trailText = transf.setString (prop.getValue ("trailtext"), "trailText not set in FileChooser.");
		defaultFile = transf.setString (prop.getValue ("defaultfile"), "defaultFile not set in FileChooser.");
		appearAlways = transf.setBoolean (prop.getValue ("appearalways"), true, "appearAlways is wrong or missing in FileChooser.");

		helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in FileChooser.");
	}


	public wPanel createGUI ()
	{
		wPanel uiPanel;

		uiPanel = new wPanel ();
		bb = new browseButton();
		uiPanel.setLayout ( new WizardLayout());
		uiPanel.add (new wLabel (helpText));
		uiPanel.add (bb);
		bb.setFile (defaultFile);
		return uiPanel;
	}

	public wPanel getCreatorPanel ()
	{
		wPanel p;

		p = new wPanel();
		cAlways = new LabeledChoice ( "Appear this parameter, even if not exist");
		tfLead = new LabeledTextField( "The leading text of this file parameter");
		tfTrail = new LabeledTextField ( "The ending text of this file parameter");
		tfHelp = new LabeledTextField ( "Information help text");
		tfDefault = new LabeledTextField ( "The default filename");

		cAlways.add ("True");
		cAlways.add ("False");
		cAlways.select ( (appearAlways) ? 0 : 1 );
		tfLead.setText (transf.stringCodes(leadText));
		tfTrail.setText (transf.stringCodes(trailText));
		tfHelp.setText (transf.stringCodes(helpText));
		tfDefault.setText (transf.stringCodes(defaultFile));

		p.setLayout ( new WizardLayout ());
		p.add ( tfHelp );
		p.add ( cAlways );
		p.add ( tfLead );
		p.add ( tfDefault );
		p.add ( tfTrail );
		return p;
	}	

	public String getParam ()
	{
		String res;

		refreshValues();
		if (defaultFile.equals("") && !appearAlways) {
			return "";
		}
		return transf.convertSpecial(leadText + defaultFile +trailText) ;
	}

	public PropertiesSection getPropertiesSection ( boolean  onlyDefault )
	{
		PropertiesSection sect;
		
		refreshValues();
		sect = new PropertiesSection("gguiFileChooser");
		if ( onlyDefault ) {
			sect.addEntry ( "defaultFile", defaultFile);
		}
		else {
			sect.addEntry ( "Type", "filechooser");
			sect.addEntry ( "defaultFile", defaultFile);
			sect.addEntry ("helpText", transf.stringCodes(helpText));
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
		if ( bb != null ) {
			defaultFile = bb.getFilename();
		}
		else if ( tfDefault != null ) {
			defaultFile = tfDefault.getText();
			appearAlways = ( cAlways.getSelectedIndex() == 0 )  ;
			leadText = tfLead.getText ();
			trailText = tfTrail.getText ();
			helpText = tfHelp.getText();
		}
	}
}
