package com.panayotis.awt;

import java.awt.event.*;
import java.util.*;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Image;
import java.net.URL;

import com.panayotis.wt.wPanel;
import com.panayotis.wt.wImageIcon;

public class Toolbar extends wPanel
{
	private Vector tools;
	private int X, Y;
	private int selected;
	private ToolItem nullitem;
	private ToolbarListener listener;
	private String imageDir;

	private static final int PICX = 32;
	private static final int PICY = 32;
	private static final int BORDER = 1;
	private static final int XLEN = PICX + BORDER*2;
	private static final int YLEN = PICY + BORDER*2;
	private static final String NULL_IMG = "null.gif";


	public Toolbar ( ToolbarListener tl, String imgDir )
	{
		super();
		listener = tl;
		imageDir = imgDir;
		tools = new Vector();
		selected = -1;
		setBackground (SystemColor.control);
		setForeground(SystemColor.controlShadow);
		addMouseListener ( new MouseAdapter () {
			public void mouseClicked (MouseEvent e) {
				toolClick ( e );
			}
		}) ;
		nullitem = new ToolItem ("null item", imageDir + "null.png");
		selected =0;
	}


	public void addTool ( String name, String file)
	{
		tools.addElement ( new ToolItem ( name, imageDir + file ) );
		reshape();
	}

	public int howMany()
	{
		return tools.size();
	}

	public Dimension getPreferredSize ()
	{
		return new Dimension (X*XLEN + 2, Y*YLEN + 2);
	}

	private void reshape()
	{
		int hm, x, y;

		hm = howMany();
		x = (int)Math.ceil(Math.sqrt (hm));
		if ( x <3 ) { x = 3; }
		if ( x > 5) { x = 5; }
		y = hm / x;
		if ( y < 1) { y = 1; }
		if ( (x*y) < hm) { y ++; }
		X = x;
		Y = y;
		setSize (X*XLEN + 2, Y*YLEN + 2);
	}

	private void toolClick (MouseEvent e)
	{
		int x,y;
		int current;

		x = (e.getX() - BORDER)  / XLEN;
		y = (e.getY() - BORDER)  / YLEN;
		current = x + y*X;
		if (current < howMany()) {
			if ( listener != null ) {
				listener.toolChanged (current);
			}
			setSelected(current);
		}
	}

	public void paint (Graphics g)
	{
	//	if(true) return;
		int hm, current;

		g.setColor ( SystemColor.controlShadow);
		//g.draw3DRect (0,0, X*XLEN + BORDER, Y*YLEN + BORDER, true);
		g.drawRect(0,0,getWidth(),getHeight());
		hm = howMany();
		g.setColor ( SystemColor.controlShadow);
		g.fill3DRect (0, 0, X*XLEN+2, Y*YLEN+2, false);

		for ( int y = 0; y < Y; y++) {
			for ( int x = 0; x < X ; x++) {
				current = x + y*X;
				if ( current == selected ) {
					g.setColor ( SystemColor.controlHighlight);
					g.fill3DRect (x*XLEN + BORDER, y*YLEN + BORDER, XLEN-BORDER, YLEN-BORDER, false);
				}
				else {
					g.setColor ( SystemColor.controlShadow);
					g.draw3DRect (x*XLEN + BORDER*2, y*YLEN + BORDER*2, PICX, PICY, false);
				}
				if ( current < hm) {
					g.drawImage ( getTool ( current).getImage(), x*XLEN + BORDER*2, y*YLEN + BORDER*2, this);
 				}
				else {
					g.drawImage ( nullitem.getImage(), x*XLEN + BORDER*2, y*YLEN + BORDER*2, this);
				}
			}
		}
 	}

	/**
	* Retun the selected tool
	*/
	public int getSelected ()
	{
		return selected;
	}

	/**
	* Select a specified Tool
	*/
	public void setSelected (int sel)
	{
		selected = sel;
		repaint();
	}

	/**
	* Get the name of a specified tool
	*/
	public String getToolName ( int which )
	{
		return getTool (which).getName ();
	}


	/**
	* Low level get tool from a list
	*/
	private ToolItem getTool ( int which )
	{
		return (ToolItem) tools.elementAt ( which );
	}


}


class ToolItem
{
	String name;
	wImageIcon img;

	ToolItem ( String nam, String imag )
	{
		name = nam;
		try {URL imgURL =  new URL("file://"+imag);
			if ( imgURL != null ) {
				img = new wImageIcon(imgURL);
			}
		}
		catch (Exception e) {}
	}
	
	Image getImage () {
		return img.getImage();
	}
	
	String getName ()
	{
		return name;
	}

	void setName ( String nam )
	{
		name = nam;
	}

	void setImage ( wImageIcon imag )
	{
		img = imag;
	}
}
