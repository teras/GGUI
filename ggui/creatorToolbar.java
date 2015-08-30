package com.panayotis.ggui.creator;

import com.panayotis.awt.Toolbar;
import com.panayotis.awt.ToolbarListener;
import com.panayotis.ggui.creator.creatorPagelist;

import com.panayotis.ggui.objects.gguiPage;
import com.panayotis.ggui.objects.gguiInfo;
import com.panayotis.ggui.objects.gguiBoolean;
import com.panayotis.ggui.objects.gguiChooser;
import com.panayotis.ggui.objects.gguiFilechooser;
import com.panayotis.ggui.objects.gguiInteger;
import com.panayotis.ggui.objects.gguiFreetext;
import com.panayotis.ggui.objects.gguiObject;

/**
* This class creates a new visual item.
* It has the form of clicked buttons
*/
public class creatorToolbar extends Toolbar
{
private int counter [ ] = { 1, 1, 1, 1, 1, 1, 1, 1} ;

public creatorToolbar ( ToolbarListener tl, String imageDir )
    {
        super (tl, imageDir + "icons/" );
        addTool ("Page", "page.png");
        addTool ("Info", "info.png");
        addTool ("Boolean", "boolean.png");
        addTool ("Chooser", "chooser.png");
        addTool ("FileChooser", "filechooser.png");
        addTool ("FreeText", "freetext.png");
		addTool ("Integer", "integer.png");
		addTool ("Cosmetics", "3dline.png");
    }

public void addListItem ( creatorPagelist pl )
    {
        boolean result;
        int which;
        gguiObject what;
        String name;

        result =false;
        which = getSelected();

        switch ( which ) {
        case 0 :
            what = new gguiPage();
            break;
        case 1 :
            what = new gguiInfo();
            break;
        case 2 :
            what = new gguiBoolean ();
            break;
        case 3 :
            what = new gguiChooser() ;
            break;
        case 4 :
            what = new gguiFilechooser ();
            break;
        case 5 :
            what = new gguiFreetext ();
		break;
	case 6 :
		what = new gguiInteger ();
		break;
	default:
            what = new gguiObject ();
            break;
        }
        what.setName ( getToolName(getSelected())  + " new " +counter [which]  );
        result = pl.addCurrentObject (what);
        if ( result ) {
            counter [which]  ++;
        }
    }

}
