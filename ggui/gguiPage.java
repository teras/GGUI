package com.panayotis.ggui.objects;

import java.util.Vector;

import com.panayotis.util.PropertiesSection;
import com.panayotis.util.transf;
import com.panayotis.io.util.Files;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.wizard.WizardLayout;
import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.ggui.objects.gguiInteractive;

import com.panayotis.wt.wPanel;

public class  gguiPage extends gguiObject
{

    /**
    * Variables to store the state of this object
    */
private String helpText;
private String image;
private boolean canFinish;
private Vector objects;
private gguiInteractive interactive;
private gguiRoot parent;

    /**
    * Visual components to define the attributes of this object to be created
    */
private LabeledTextField tfImage, tfHelp;
private LabeledChoice cFinish;

public gguiPage ()
    {
        helpText = "";
        image = "";
        parent = null;
        canFinish = false;
        objects = new Vector();
        interactive = new gguiInteractive();
        interactive.setParent ( this);
    }


public int countObjects ()
    {
        return objects.size();
    }

public void addObject ( gguiObject obj )
    {
        addObject ( countObjects(), obj) ;
    }

public void addObject ( int index, gguiObject obj )
    {
        if ( index >=0 && index <= countObjects() ) {	// We CAN add to the countObjects() position
            objects.insertElementAt ( obj, index );
        }
    }

public gguiObject getObject ( int index )
    {
        return ( index < 0 || index >= countObjects() ) ? this : (gguiObject) objects.elementAt (index) ;
    }


public void removeObject ( int index)
    {
        if ( index >=0 && index < countObjects() ) {
            objects.removeElementAt (index);
        }
    }

public void setInteractiveData ( PropertiesSection sect )
    {
        interactive.importData ( sect );
    }

public gguiInteractive getInteractiveData ( )
    {
        return interactive;
    }

public int getInteractivePage ()
    {
        return interactive.getInteractivePage( this );
    }

public String getImage ()
    {
        return image;
    }

public String getHelpText ()
    {
        return helpText;
    }

public boolean getCanFinish ()
    {
        return canFinish;
    }

public void importData (PropertiesSection prop)
    {
        helpText = transf.setString (prop.getValue ("helptext"), "helpText not defined in Page.");
        image = transf.setString (prop.getValue ("image"), "Image not defined in Page.");
        canFinish = transf.setBoolean (prop.getValue ("canfinish"), false, "canFinish is wrong or missing in Page.");
    }

public wPanel getCreatorPanel()
    {
        wPanel p;

        p = new wPanel();
        p.setLayout ( new WizardLayout () );
        tfImage = new LabeledTextField ( "The filename of the image to display on the left");
        tfHelp = new LabeledTextField ( "The help text to display in the dialog.");
        cFinish = new LabeledChoice ( "The wizard can finish on this page");

        cFinish.add ("True");
        cFinish.add ("False");
        cFinish.select ( (canFinish) ? 0 : 1 );
        tfHelp.setText ( transf.stringCodes(helpText));
        tfImage.setText (Files.leaveOnlyFilename(image));

        p.add ( tfHelp);
        p.add ( tfImage );
        p.add ( cFinish );
        p.add ( interactive.getCreatorPanel() );
        return p;
    }

public PropertiesSection getPropertiesSection ( boolean onlyDefault )
    {
        PropertiesSection sect;

        refreshValues();
        sect = new PropertiesSection ( "Page" );
        if ( ! onlyDefault ) {
            if ( tfImage != null ) {
                sect.addEntry ( "helpText", transf.stringCodes(helpText));
                sect.addEntry ( "Image", Files.leaveOnlyFilename(image));
                sect.addEntry ( "canFinish", (canFinish) ? "true" : "false");
	sect.addEntry ( "Interactive", ( ! interactive.getPropertiesSection(false).isEmpty() ) ? "true" : "false" );
             }
        }
        return sect;
    }

private void refreshValues()
    {
        if ( tfHelp != null) {
            helpText = tfHelp.getText();
            image = tfImage.getText();
            canFinish = ( cFinish.getSelectedIndex() == 0);
        }
    }
}
