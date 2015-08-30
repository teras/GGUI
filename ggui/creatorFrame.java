package com.panayotis.ggui.creator;

import java.awt.event.*;
import java.util.*;

import com.panayotis.awt.ToolbarListener;
import com.panayotis.awt.PagelistListener;
import com.panayotis.ggui.gguiFileData;
import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.ggui.objects.gguiRoot;
import com.panayotis.ggui.creator.creatorPagelist;
import com.panayotis.ggui.creator.creatorToolbar;


import com.panayotis.wt.wFrame;
import com.panayotis.wt.wPanel;
import com.panayotis.wt.wButton;
import com.panayotis.wt.wLabel;
import com.panayotis.wt.wScrollPane;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;

public class creatorFrame extends wFrame implements ToolbarListener, PagelistListener
{
    creatorPagelist navig;
    wLabel Tooltip;
    creatorToolbar tools;

    CardLayout displayer;
    wPanel pControls;

	// here we store the data files directory, in order to save the new datafile
	private String dataDir;

public creatorFrame ( String imageDir )
    {
        wButton cancel, ok, remove, insert;
        wPanel bottom, centr, left, topButtons, botButtons, topAll ;
		wScrollPane leftscroll;

        bottom = new wPanel();
        left = new wPanel();
        leftscroll = new wScrollPane();
        centr = new wPanel();
        topButtons = new wPanel();
        botButtons = new wPanel();
        topAll = new wPanel();
        cancel = new wButton ("Cancel");
        ok = new wButton ("OK");
        remove = new wButton ("Remove");
        insert = new wButton (" Add ");

        pControls = new wPanel();
        displayer = new CardLayout();
        pControls.setLayout ( displayer );	// pControls layout should be cards-layout before the " new creatorPageList (this) " argument

        Tooltip = new wLabel ("");
        navig = new creatorPagelist ( this);
        tools = new creatorToolbar(this, imageDir );

        getContentPane().setLayout ( new BorderLayout());
        bottom.setLayout ( new BorderLayout ());
        botButtons.setLayout ( new FlowLayout ());
        centr.setLayout ( new BorderLayout ());
		left.setLayout ( new BorderLayout() );
        topButtons.setLayout ( new FlowLayout());
        topAll.setLayout ( new BorderLayout());

        setBackground(SystemColor.control);
        setForeground( SystemColor.controlText);

        bottom.add ( "East", botButtons);
        bottom.add ("Center", Tooltip);

        botButtons.add ( cancel);
        botButtons.add ( new wLabel ( "      " ) );
        botButtons.add (ok);

        leftscroll.getViewport().add ( navig );
		left.add ( "Center", leftscroll);
        left.add ( "South", tools);

        topButtons.add (insert);
        topButtons.add (remove);

        topAll.add ( "North", topButtons);
 //       topAll.add ( "South", new Line3D () );

        centr.add ("West", new wLabel (""));
        centr.add ("North", topAll);
        centr.add ( "Center", pControls);

        getContentPane().add ("South", bottom);
        getContentPane().add ("West", left);
        getContentPane().add ("Center", centr);

        addWindowListener (new WindowAdapter () {
                           public void windowClosing ( WindowEvent e) {
                                   closeWindow ();
                               }	} );
        cancel.addActionListener (new ActionListener () {
                                  public void actionPerformed (ActionEvent evt)  {
                                          a_cancel ();
                                      }	} );
        ok.addActionListener (new ActionListener () {
                              public void actionPerformed (ActionEvent evt)  {
                                      a_ok ();
                                  }	} );
        remove.addActionListener (new ActionListener () {
                                  public void actionPerformed (ActionEvent evt)  {
                                          a_remove ();
                                      }	} );
        insert.addActionListener (new ActionListener () 	{
                                  public void actionPerformed (ActionEvent evt)  {
                                          a_insert ();
                                      }	} );

        setSize (630, 450);

		setTitle("GGUI Wizard Editor");
    }


    /**
    * Load a file to a creatorPagelist from a given filename
    */
public void setRoot ( gguiRoot rt )
    {
        navig.setRoot (rt);
    }

public void setDataDir ( String dir)
{
	dataDir = dir;
}


private void closeWindow ()
    {
        a_cancel();
    }

private void a_cancel ()
    {
        System.exit (21);
    }

private void a_ok ()
    {
        System.exit ( gguiFileData.exportFile ( navig.getRoot(), dataDir + navig.getRoot().getWizardName() +".ggui" ));
    }

private void a_insert ()
    {
        tools.addListItem ( navig );
    }

private void a_remove ()
    {
        navig.removeCurrentObject ();
    }

public void toolChanged (int tool)
    {
		Tooltip.setText ( tools.getToolName(tool) );
    }

public void updateObject ( String name )
    {
        displayer.show ( pControls, name);
    }

public void appendObject (gguiObject object)
    {
        pControls.add ( object.getName(), object.getCreatorPanel () );
    }

}
