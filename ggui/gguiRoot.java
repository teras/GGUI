package com.panayotis.ggui.objects;

import java.util.Vector;

import com.panayotis.util.PropertiesSection;
import com.panayotis.util.transf;
import com.panayotis.awt.LabeledTextField;
import com.panayotis.awt.LabeledChoice;
import com.panayotis.wizard.WizardLayout;
import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.ggui.objects.gguiPage;

import com.panayotis.wt.wPanel;

public class  gguiRoot extends gguiObject
{

    /**
    * Variables to store the state of this object
    */
private String program, programTail, windowTitle;
private boolean windowed;
private boolean leavespaces;
private Vector pages;
private String wizardName;

    /**
    * Visual components to define the attributes of this object to be created
    */
private LabeledTextField tfProgram, tfProgramTail, tfWindowTitle;
private LabeledChoice cWindowed, cLeaveSpaces;


public gguiRoot ()
    {
        program = "";
        programTail = "";
		wizardName= "unknown";
        windowTitle = "GGUI Wizard";
        windowed = true;
		leavespaces = true;
		setName ("general");
        pages = new Vector();
    }

public int countPages ()
    {
        return pages.size();
    }

public int countObjects ( int page)
    {
        return ( page >= countPages() || page <0 )  ? -1 : getPage (page).countObjects ();
    }

public void addPage ( gguiPage pg )
    {
        addPage ( countPages(), pg);
    }

public void addPage ( int index, gguiPage pg)
    {
        if ( index >=0 && index <= countPages() ) {	// We CAN add to the countPages() position
            pages.insertElementAt ( pg, index );
        }
    }

public void addObject ( int page, gguiObject obj)
    {
        if ( page >=0 && page < countPages() ) {	// We CAN'T add to the countPages() position
            addObject ( page, getPage (page).countObjects(), obj);
        }
    }

public void addObject ( int page, int index, gguiObject obj)
    {
        if ( page >=0 && page < countPages() ) {	// We CAN'T add to the countPages() position
            getPage (page).addObject ( index, obj);
        }
    }


public gguiPage getPage ( int page )
    {
        return ( page < 0 || page >= countPages() ) ? null : (gguiPage) pages.elementAt (page) ;	// MUST return a gguiPage
    }

public gguiObject getObject ( int page, int obj)
    {
        return ( page < 0 || page >= countPages() )  ? this : getPage (page).getObject ( obj );	// Don't care for return, can return gguiRoot or gguiPage too.
    }

public void removePage ( int page)
    {
        if ( page >=0 && page < countPages() ) {
            pages.removeElementAt (page);
        }
    }

public void removeObject ( int page, int index)
    {
	if  ( page >=0 && page < countPages() ) {
		if ( index < 0  ) {
			removePage ( page );
		}
		else {
			getPage(page).removeObject (index);
		}
	}
	else {
		System.err.println ( "Error: Can't remove root object. { page="+page+", object="+index+"}");
	}
    }

	public String getWizardName ()
	{
		return wizardName;
	}

	public void setWizardName (String newname)
	{
		wizardName = newname;
	}

public int getInteractivePage ( int page )
    {
        return ( page > countPages() )  ? -1 : getPage(page).getInteractivePage();
    }

public String getWindowTitle ()
    {
        return windowTitle;
    }

public String getProgram()
    {
        return program;
    }

public String getProgramTail()
    {
        return programTail;
    }

public boolean getWindowed()
    {
        return windowed;
    }
public boolean getLeaveSpaces()
    {
        return leavespaces;
    }



public void importData (PropertiesSection prop)
    {
        program = transf.setString (prop.getValue ("program"), "Program not defined in General.");
        programTail = transf.setString (prop.getValue ("programtail"), "programTail not defined in General.");
        windowTitle = transf.setString (prop.getValue ("windowtitle"), "windowTitle not defined in General.");
        windowed = transf.setBoolean (prop.getValue ("windowed"), true, "Windowed is wrong or missing in General.");
        leavespaces = transf.setBoolean (prop.getValue ("leavespaces"), true, "leaveSpaces is wrong or missing in General.");
    }

public wPanel getCreatorPanel()
    {
        wPanel p;

        p = new wPanel();
        p.setLayout ( new WizardLayout () );
        tfProgram = new LabeledTextField ( "The actual filename of the program to execute");
        tfProgramTail = new LabeledTextField ( "The ending text of the executed target program" );
        tfWindowTitle = new LabeledTextField ( "The Window title of the Wizard");
		
        cWindowed = new LabeledChoice ( "Where should the results be shown?");
        cWindowed.add ("In a window");
        cWindowed.add ("On standard I/O stream");
        cWindowed.select ( (windowed) ? 0 : 1 );

		cLeaveSpaces = new LabeledChoice ( "Spaces between arguments");
        cLeaveSpaces.add ("Automatically");
        cLeaveSpaces.add ("User defined");
        cLeaveSpaces.select ( (leavespaces) ? 0 : 1 );
		
        tfProgram.setText ( transf.stringCodes(program));
        tfProgramTail.setText (transf.stringCodes(programTail));
        tfWindowTitle.setText ( transf.stringCodes(windowTitle));

        p.add ( tfProgram );
        p.add ( tfProgramTail );
        p.add ( tfWindowTitle );
        p.add ( cWindowed );
        p.add ( cLeaveSpaces );
        return p;
    }

public PropertiesSection getPropertiesSection ( boolean onlyDefault )
    {
        PropertiesSection sect;

        refreshValues();
        sect = new PropertiesSection ( "General" );
        if ( ! onlyDefault ) {
            if ( tfProgram != null ) {
                sect.addEntry ( "Program", program);
                sect.addEntry ( "ProgramTail", programTail);
                sect.addEntry ( "WindowTitle", windowTitle);
                sect.addEntry ( "Windowed", (windowed) ? "true" : "false" );
                sect.addEntry ( "leaveSpaces", (leavespaces) ? "true" : "false" );
            }
        }
        return sect;
    }

private void refreshValues()
    {
        if ( tfProgram != null ) {
            program = tfProgram.getText();
            programTail = tfProgramTail.getText();
            windowTitle = tfWindowTitle.getText();
            windowed = (cWindowed.getSelectedIndex() == 0);
            leavespaces = (cLeaveSpaces.getSelectedIndex() == 0);
        }
    }
}
