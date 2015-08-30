package com.panayotis.ggui.creator;

import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.event.*;
import java.util.Vector;

import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.ggui.objects.gguiRoot;
import com.panayotis.ggui.objects.gguiPage;
import com.panayotis.awt.PagelistListener;

import com.panayotis.wt.wList;
import com.panayotis.wt.wListModel;

/**
* This class displays the contend of a datafile in a tree-like list
* It stores too the newely created objects
*/
public class creatorPagelist extends wList
{

	private gguiRoot root;
	private PagelistListener listen;
	private String dataDir;
	private wListModel model;

	public creatorPagelist (PagelistListener pll)
	{
		model = new wListModel();
		setModel(model);
		listen = pll;
		root = null ;
		dataDir = null;
		addListSelectionListener ( new ListSelectionListener () {
			public void valueChanged ( ListSelectionEvent ev ) {
				l_change ( ev ) ;
			}
		} ) ;
	}

	/**
	* Get the root GGUI object
	*/
	public gguiRoot getRoot ()
	{
		return root;
	}

	/**
	* Set the root GGUI object and apply the data
	*/
	public void setRoot ( gguiRoot rt)
	{
		root =rt;
		listen.appendObject (root);
		for ( int i = 0; i < root.countPages(); i ++ ) {
			listen.appendObject (root.getPage(i));
			for ( int j = 0 ; j < root.countObjects(i); j ++ ) {
				listen.appendObject ( root.getObject (i,j));
			}
		}
		refreshDisplay();
		displayObject (-1, -1);
	}

	public void refreshDisplay()
	{
		model.removeAllElements();
		model.addElement ( root.getName() );
		for ( int i = 0; i < root.countPages() ; i++ ) {
			model.addElement ( root.getPage(i).getName() );
			for ( int j = 0; j < root.countObjects(i) ; j ++ ) {
				model.addElement ( "    " + root.getObject ( i,j ).getName() );
			}
		}
	}

	public void displayObject(int page, int object)
	{
		int pos;

		if ( page<0 ) {
			page = -1;
			object = -1;
		}
		else if ( page >= root.countPages() ) {
				page = root.countPages() -1;
				object = -1;
		}
		else if ( object <0 ) {
				object = -1;
		}
		else if ( object >= root.countObjects ( page) ) {
			object = root.countObjects (page) -1;
		}
		pos = findListIndex (page, object);
		setSelectedIndex ( pos);
		listen.updateObject (root.getObject(page,object).getName());
	}


	public void removeCurrentObject ()
	{
		Dimension pos;

		pos = findIndex ( getSelectedIndex() );
		root.removeObject (pos.width, pos.height);
		refreshDisplay();
		displayObject (pos.width, pos.height);
	}

	public boolean addCurrentObject ( gguiObject obj)
	{
		Dimension pos;
		int page, object;

		pos = findIndex ( getSelectedIndex() );
		page = pos.width;
		object = pos.height;

		if ( obj instanceof gguiPage ) {
			page++;
			obj.setParent ( root );
			root.addPage ( page, (gguiPage) obj);
		}
		else if ( page != -1 ) {
			object++;
			root.addObject ( page, object, obj );
			obj.setParent ( root.getPage ( page) );
		}
		else {
			System.err.println ("Error: can't add object {page="+page+" object="+object+"}");
			return false;
		}
		listen.appendObject (obj);
		refreshDisplay();
		displayObject (page, object);
		return true;
	}

	private Dimension findIndex ( int listindex)
	{
		Dimension res;
		int page, index;

		page=-1;
		index=-1;
		listindex--;
		while ( listindex >= 0 && (page+1) < root.countPages() ) {
			listindex--;
			page++;
			index=-1;
			while ( listindex >= 0 && (index+1) < root.countObjects(page) ) {
				listindex --;
				index++;
			}
		}
		res = new Dimension ( page, index);
		return res;
	}


	private int findListIndex ( int page, int index )
	{
		int counter;

		counter = 0;
		for ( int i = 0; i <= page && i < root.countPages() ; i++ ) {
			counter++;
			for ( int j = 0; (page == i) ? j <= index : j < root.countObjects (i) ; j++ ) {
				counter++;
			}
		}
		return counter;
	}


	private void l_change (ListSelectionEvent ev )
	{
		int obj ;
		Dimension position;

		obj = getSelectedIndex();
		if ( obj < 0 ) return;

		position = findIndex (obj);
		listen.updateObject ( root.getObject(position.width,position.height).getName() );
	}

}
