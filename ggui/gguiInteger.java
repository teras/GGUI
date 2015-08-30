package com.panayotis.ggui.objects; 

import com.panayotis.wizard.WizardLayout;
import com.panayotis.awt.IntField;
import com.panayotis.awt.IntFieldListener;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.util.transf;
import com.panayotis.util.PropertiesSection;
import com.panayotis.ggui.objects.gguiObject;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wLabel;

/** 
* This class defines the gguiFreetext object. This object is for entering 
* free text.
*/ 
public class  gguiInteger extends gguiObject implements IntFieldListener
{ 
	/**
	* Variables to store the state of this object
	*/
	private String leadText;
	private String trailText;
	private int defaultInt;
	private int minimum;
	private int maximum;
	private int smallStep;
	private int bigStep;
	private String helpText;

	/**
	* Components to visually display this object
	*/
	private IntField intf;

	/**
	* Components to visually create this object
	*/
	private IntField ifMinimum, ifMaximum, ifSStep, ifBStep, ifDefault;
	private LabeledTextField tfLead, tfTrail, tfHelp;

	/** 
	* Creates a new gguiBoolean object.  
	*/ 
	public gguiInteger () 
	{ 
		leadText = "";
		trailText = "";
		defaultInt = 0;
		minimum = IntField.ABSOLUT_MIN;
		maximum = IntField.ABSOLUT_MAX;
		smallStep = 1;
		bigStep = IntField.ABSOLUT_STEP;
		helpText = "";
	}

	public void importData (PropertiesSection prop)
	{
		leadText = transf.setString (prop.getValue ("leadtext"), "leadText not set in Integer.");
		trailText = transf.setString (prop.getValue ("trailtext"), "trailText not set in Integer.");
		defaultInt = transf.createInt (prop.getValue ("defaultint"), "defaultInt not set in Integer.");
		helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in Integer.");
		minimum = transf.createInt (prop.getValue ("minimum"), "minimum not set in Integer.");
		maximum = transf.createInt (prop.getValue ("maximum"), "maximum not set in Integer.");
		smallStep = transf.createInt (prop.getValue ("smallstep"), "smallStep not set in Integer.");
		bigStep = transf.createInt (prop.getValue ("bigstep"), "bigStep not set in Integer.");
	}


	public wPanel createGUI ()
	{
		wPanel p;

		p = new wPanel();
		p.setLayout ( new WizardLayout());
		intf = new IntField ( minimum, maximum, smallStep, bigStep, defaultInt);
		p.add ( new wLabel ( helpText ));
		p.add ( intf);
		return p;
	}

	public wPanel getCreatorPanel ()
	{
		wPanel p;

		p = new wPanel();
		tfLead = new LabeledTextField( "The leading text of this parameter");
		tfTrail = new LabeledTextField ( "The ending text of this parameter");
		tfHelp = new LabeledTextField ( "Information help text");
		ifMinimum = new IntField ();
		ifMaximum = new IntField ();
		ifSStep = new IntField ();
		ifBStep = new IntField();
		ifDefault = new IntField ();

		ifMinimum.updateData ( minimum);
		ifMaximum.updateData (maximum);
		ifSStep.setMinimum (1);
		ifSStep.updateData (smallStep);
		ifBStep.setMinimum (1);
		ifBStep.updateData (bigStep);
		ifDefault.setMinimum ( minimum );
		ifDefault.setMaximum (maximum);
		ifDefault.updateData ( defaultInt );

		ifMinimum.setIntFieldListener ( this);
		ifMaximum.setIntFieldListener ( this);

		tfLead.setText ( transf.stringCodes(leadText));
		tfTrail.setText ( transf.stringCodes(trailText));
		tfHelp.setText (transf.stringCodes(helpText));

		p.setLayout ( new WizardLayout ());
		p.add ( tfHelp );
		p.add ( tfLead );
		p.add ( tfTrail );
		p.add ( new wLabel ( "Minimum possible value" ));
		p.add ( ifMinimum );
		p.add ( new wLabel ( "Maximum possible value" ));
		p.add ( ifMaximum );
		p.add ( new wLabel ( "Incremental step for small advance" ));
		p.add ( ifSStep );
		p.add ( new wLabel ( "Incremental step for large advance" ));
		p.add ( ifBStep );
		p.add ( new wLabel ( "Default value" ));
		p.add ( ifDefault );
		return p;
	}	

	public String getParam ()
	{
		String res;

		refreshValues();
		return transf.convertSpecial(leadText + String.valueOf (defaultInt) +trailText) ;
	}

	public PropertiesSection getPropertiesSection( boolean onlyDefault )
	{
		PropertiesSection sect;
		
		refreshValues();
		sect = new PropertiesSection("gguiFreeText");
		if ( onlyDefault ) {
			sect.addEntry ( "defaultInt", String.valueOf (defaultInt));
		}
		else {
			sect.addEntry ( "Type", "integer");
			sect.addEntry ( "defaultInt", String.valueOf (defaultInt));
			sect.addEntry ("helpText", transf.stringCodes(helpText));
			sect.addEntry ("leadText", transf.stringCodes(leadText));
			sect.addEntry ("trailText", transf.stringCodes(trailText));
			sect.addEntry ( "minimum", String.valueOf (minimum));
			sect.addEntry ( "maximum", String.valueOf (maximum));
			sect.addEntry ( "smallStep", String.valueOf (smallStep));
			sect.addEntry ( "bigStep", String.valueOf (bigStep));
		}
		return sect;
	}


	/**
	* Low level set variables to the actual value
	*/
	private void refreshValues ()
	{
		if ( intf != null ) {
			defaultInt = intf.getInt();
		}
		else if ( ifDefault != null ) {
			leadText = tfLead.getText ();
			trailText = tfTrail.getText ();
			helpText = tfHelp.getText();
			defaultInt = ifDefault.getInt();
			minimum = ifDefault.getMinimum();
			maximum = ifDefault.getMaximum();
			smallStep = ifDefault.getSmallStep();
			bigStep = ifDefault.getBigStep();
		}
	}

	public void valueChanged ( IntField ifield)
	{
		// Test that minimum<= maximum
		if ( ifield == ifMinimum ) {
			if ( ifMinimum.getInt() > ifMaximum.getInt() ) {
				ifMinimum.updateData (ifMaximum.getInt());
			}
		}
		else if ( ifield == ifMaximum ) {
			if ( ifMaximum.getInt() < ifMinimum.getInt() ) {
				ifMaximum.updateData ( ifMinimum.getInt());
			}
		}

		// Rearrange floor & cieling & remeber old values
		int cur = ifDefault.getInt();
		ifDefault.setMinimum ( IntField.ABSOLUT_MIN );
		ifDefault.setMaximum ( IntField.ABSOLUT_MAX );
		ifDefault.updateData ( ifMaximum.getInt());

		// apply current data + old values
		ifDefault.setMinimum ( ifMinimum.getInt());
		ifDefault.setMaximum ( ifMaximum.getInt());
		ifDefault.updateData ( cur );
	}
}
