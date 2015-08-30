package com.panayotis.ggui.objects; 

import java.util.Vector;

import com.panayotis.util.transf;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.DoubleList;
import com.panayotis.util.PropertiesSection;
import com.panayotis.util.PropertiesEntry;
import com.panayotis.ggui.objects.gguiObject;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wRadioButton;
import com.panayotis.wt.wButtonGroup;
import com.panayotis.wt.wComboBox;
import com.panayotis.wt.wList;
import com.panayotis.wt.wLabel;
import com.panayotis.wt.wListModel;


/** 
* This class defines the gguiChooser object. This object is for multi 
* choose commandline options.
*/ 
public class  gguiChooser extends gguiObject
{ 
	/**
	* Variables to store the state of this object
	*/
	private Vector Options;
	private Vector optionsText;
	private int defaultOption;
	private String helpText;
	private String GUIType;
 
	/**
	* Components to visually display this object
	*/
	private wRadioButton [] rb;
	private wButtonGroup cg;
	private wComboBox ch;
	private wList li;

	/**
	* Components to visually create this object
	*/
	LabeledTextField tfHelp;
	LabeledChoice cGUIType;
	DoubleList dlEntries;

	public gguiChooser ()
	{
		Options = new Vector();
		optionsText = new Vector ();
		defaultOption = 0;
		helpText = "" ;
		GUIType = "checkbox" ;
	}

	public void importData (PropertiesSection prop)
	{
		int countOptions;

		countOptions = transf.createInt (prop.getValue ("countoptions"), "countOptions is wrong or missing in Chooser.");
		for ( int f = 1 ; f <= countOptions ; f ++ ) {
			Options.addElement (transf.setString (prop.getValue("option"+f), "Option" + f + " not set in Chooser."));
			optionsText.addElement (transf.setString (prop.getValue("optiontext"+f), "optionText" + f + " not set in Chooser."));
		}

 		defaultOption = transf.createInt (prop.getValue ("defaultoption"), "defaultOption is wrong or missing in Chooser.");
		if (defaultOption > countOptions ) {
			defaultOption = countOptions;
		}
		if (defaultOption <= 0 ) {
			defaultOption = 1;
		}

		helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in Boolean.");

		GUIType = transf.setString (prop.getValue ("guitype"), "GUIType not defined in Chooser.");
		GUIType = transf.simplifyString(GUIType);
		if (GUIType.equals ("")) {
			GUIType = "checkbox";
		}
	}

	public wPanel createGUI ()
	{
		wPanel uiPanel;
		int countOptions;

		uiPanel = new wPanel ();
		countOptions = Options.size();
		uiPanel.setLayout ( new WizardLayout());
		uiPanel.add (new wLabel (helpText));
		if (GUIType.equals("choice")) {
			ch = new wComboBox();
			ch.setEditable(false);
			for ( int f = 0 ; f < countOptions ; f++ ) {
				ch.addItem ( (String) optionsText .elementAt (f) );
			}
			ch.setSelectedIndex ( defaultOption -1 );
			uiPanel.add (ch);
		}
		else if (GUIType.equals("list")) {
			wListModel model = new wListModel();
			li = new wList(model);
			for ( int f = 0 ; f < countOptions ; f++ ) {
				model.addElement (optionsText.elementAt (f) );
			}
			li.setSelectedIndex ( defaultOption -1 );
			uiPanel.add (li);
		}
		else {
			cg = new wButtonGroup ();
			rb = new wRadioButton [countOptions];
			for ( int f = 0; f < countOptions ; f++ ) {
				rb [f] = new wRadioButton( (String) optionsText.elementAt (f) , false);
				uiPanel.add (rb [f]);
				cg.add(rb[f]);
			}
			rb[defaultOption-1].setSelected (true);
		}
		return uiPanel;
	}


	public wPanel getCreatorPanel ()
	{
		wPanel p;

		p = new wPanel();
		tfHelp = new LabeledTextField ( "Information help text");
		cGUIType = new LabeledChoice( "Visual type of this object");
		dlEntries = new DoubleList ( "Define which are the available options", true);

		cGUIType.add ( "CheckBox" );
		cGUIType.add ( "Choice" );
		cGUIType.add ( "List" );
		if ( GUIType.equals("choice")) {
			cGUIType.select (1);
		}
		else if ( GUIType.equals ("list")) {
			cGUIType.select (2);
		}
		else {
			cGUIType.select (0);
		}
		tfHelp.setText (transf.stringCodes(helpText));

		for ( int i = 0; i <Options.size(); i++) {
			dlEntries.addEntry( i, new PropertiesEntry ( (String) optionsText.elementAt (i), (String) Options.elementAt(i) ));
		}
		dlEntries.setSelected ( defaultOption-1);
		dlEntries.deleteEntry (Options.size());		// by default, DoubleList has an entry already (now the last one)
		dlEntries.displayEntry ( 0);

		p.setLayout ( new WizardLayout ());
		p.add ( tfHelp );
		p.add ( cGUIType );
		p.add ( dlEntries);
		return p;
	}	

	public synchronized String getParam ()
	{
		return transf.convertSpecial((String) Options.elementAt ( defaultOption -1 ));
	}

	public int getInteractive ()
	{
		refreshValues();
		return defaultOption;
	}


	public PropertiesSection getPropertiesSection ( boolean onlyDefault)
	{
		PropertiesSection sect;
		
		refreshValues();
		sect = new PropertiesSection ( "gguiChooser");
		if (onlyDefault ) {
			sect.addEntry ("defaultOption" , String.valueOf(defaultOption ));
		}
		else {
			sect.addEntry ( "Type", "chooser");
			sect.addEntry ( "GUIType", GUIType );
			sect.addEntry ("countOptions" , String.valueOf( Options.size() ));
			for ( int i = 0; i < Options.size() ; i ++ ) {
				sect.addEntry ( "Option" + ( i+1) , transf.stringCodes((String) Options.elementAt (i)) );
				sect.addEntry ( "optionText" + ( i+1) , transf.stringCodes((String) optionsText.elementAt (i)) );
			}
			sect.addEntry ("defaultOption" , String.valueOf(defaultOption));
			sect.addEntry ( "helpText", transf.stringCodes(helpText));
		}
		return sect;
	}


	/**
	* Low level set variables to the actual value
	*/
	private synchronized void refreshValues ()
	{
		if ( rb != null ) {
			int countOptions = Options.size();
			for ( int f = 0; f < countOptions ; f++) {
				if ( rb[f].isSelected() ) {
					defaultOption = f+1 ;
				}
			}
		}
		else if ( ch != null) {
			defaultOption = ch.getSelectedIndex() + 1;
		}
		else if ( li != null) {
			defaultOption = li.getSelectedIndex() + 1;
		}
		else if ( dlEntries != null ) {
			defaultOption = dlEntries.getSelected() + 1;
			switch ( cGUIType.getSelectedIndex () ) {
			case 0:
				GUIType = "checkbox";
				break;
			case 1:
				GUIType = "choice";
				break;
			case 2:
				GUIType = "list";
				break;
			}
			helpText = tfHelp.getText ();
			optionsText = dlEntries.getKeywordList();
			Options = dlEntries.getValueList();
			while ( optionsText.size() > Options.size() ) {
				Options.addElement ( "" );
			}
			while ( optionsText.size() < Options.size() ) {
				optionsText.addElement ( "" );
			}
			if ( defaultOption > Options.size() ) {
				defaultOption = Options.size() ;
			}
		}
	}

}
