package com.panayotis.ggui;

import com.panayotis.util.transf;
import com.panayotis.ggui.objects.gguiRoot;
import com.panayotis.ggui.objects.gguiPage;
import com.panayotis.ggui.objects.gguiObject;
import com.panayotis.wizard.Wizard;
import com.panayotis.wizard.WizardListener;

public class gguiWizard extends Wizard
{
	private gguiRoot root;

	public gguiWizard ( WizardListener wl)
	{
		super (wl);
		root = null;
	}

	public gguiRoot getRoot ()
	{
		return root;
	}

	public void setRoot ( gguiRoot rt)
	{
		gguiPage page;		// the current page
		int pages, objects;

		root = rt;
		setTitle ( root.getWindowTitle () );
		pages = root.countPages();
		setPages ( pages);
		for ( int i=1; i <= pages; i++ ) { 
			page = root.getPage (i-1);
			addPicture (i, page.getImage());
			setHelpText ( i, transf.stringToArray (page.getHelpText() ));
			setFinish (i, page.getCanFinish());

			//Count objects for this page
			objects = page.countObjects();
			for ( int j=1; j<= objects; j++ ) {
				addItem ( i, page.getObject (j-1).createGUI() );
 			} 
		} 
	}

	public String getAllParams ()
	{
		int i,j;
		StringBuffer dat;
		String par ;
		gguiObject obj;

		String space = (root.getLeaveSpaces()) ? " " : "";
		dat = new StringBuffer (root.getProgram());
		for (i=0; i < root.countPages(); i++) { 
			for (j=0; j < root.countObjects(i); j++) {
				obj = root.getObject (i,j);
				if (obj != null) {
					par = obj.getParam();
					if (par!= null && !par.equals("")) {
						dat.append( space + obj.getParam());
					}
				}
			}
		}
		dat.append ( root.getProgramTail() );
  		return dat.toString();
	}


	public boolean checkValidation (int page)
	{
		return true;
	}
}
