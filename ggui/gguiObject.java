package com.panayotis.ggui.objects;

import com.panayotis.util.PropertiesSection;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wLabel;

public class gguiObject
{
    String name;
    gguiObject parent;

	public gguiObject ()
	{
		name = "unknown";
	}


public void setName ( String n )
    {
        name = n;
    }

public String getName ()
    {
        return name;
    }

public void setParent ( gguiObject par)
    {
	parent = par;
    }
    
public gguiObject getParent ()
    {
	return parent;
    }
    
    /**
    * Use this method to import data from a PropertiesSection object
    */
public void importData (PropertiesSection data)
    {
    }

    /**
    * Create the graphica user interface for this object ( to display it on screen )
    */
public wPanel createGUI ()
    {
        return new wPanel();
    }


    /**
    * Create the graphics user interface for the creator program
    */
public wPanel getCreatorPanel ()
    {
        wPanel p = new wPanel();
        p.add ( new wLabel ( "This object doesn't have any parameters" ) );
        return p;
    }

    /**
    * Use this method if you want to get the current parameter for this object
    */
public String getParam ()
    {
        return "";
    }

    /**
    * For interactive ggui Objects. Should return the index number of
    * the selected object. If none can be selected, then it should
    * return 1. First object has index = 1
    */
public int getInteractive ()
    {
        return 1;
    }

    /**
    * Here return a PropertiesSection describing this object
    * @param onlyDefault true if we want only to save the current value of this entry and
    * not the entire data
    */
public PropertiesSection getPropertiesSection ( boolean onlyDefault)
    {
        PropertiesSection sect;
        String type;

        type = toString();
        type = type.substring ( type.indexOf ("ggui.objects.") + 17, type.indexOf ("@")).toLowerCase();
        sect = new PropertiesSection ( "ggui" + type.substring(0,1).toUpperCase() + type.substring (1 , type.length()));
        if ( ! onlyDefault ) {
            sect.addEntry ( "Type", type );
        }
        return sect;
    }
}
