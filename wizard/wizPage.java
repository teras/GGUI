package com.panayotis.wizard;

import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import com.panayotis.wt.wFrame;
import com.panayotis.wt.wPanel;

/**
* Wizard class uses this class in order to display the various wizard pages.
* Every wizard page is another instance of this class. Usually you
* don't have to use this class and it's methods directly, Wizard class
* does it for you.
*
* @see com.panayotis.wizard.Wizard#getWizPage
*/
public class wizPage extends wPanel
{

	/**
	* The help text of this wizard page. <I>Still experimental.</I>
	*/
	public String []HelpText; // somewhere to store the help text of this page
	
	// somewhere to store the previous page, useful with multi-paged wizards.
	// defined as "friendly"
	int prevPage;

	/**
	* True if wizard can end at this point, false if not.
	* @see com.panayotis.wizard.Wizard#setFinish
	*/
	public boolean canFinish; // true if the "finish" button can be clicked in this page

	private wFrame f_Help; // wFrame to display when clicking on Help button
	private wPanel pOptions; // wPanel where all the selectable items are stored
	private wizImage Img; // panel where the pictures are stored

	/**
	* Create a new wizard page.
	* @param prPage which is the previous page. Useful only with interactive
	* multi-paged wizards. Usually set just to previous page.
	*/
	public wizPage (int prPage)
	{
		pOptions = new wPanel();
		
		setLayout( new BorderLayout());
		pOptions.setLayout( new WizardLayout()); // the LayoutManager of the wizard page
		pOptions.setBorder( new EmptyBorder ( 40, 10, 20, 20 ));
		
		add( "Center", pOptions);
		canFinish = false;
	}
	
	/**
	* Add an object to the current page.
	* @see com.panayotis.wizard.Wizard#addItem
	*/
	public void addItem ( wPanel obj )
	{
		pOptions.add(obj);
	}
	
	
	/**
	* Add a picture to your wizard page
	*@see com.panayotis.wizard.Wizard#addPicture
	*/
	public void addPicture (String s_pic, wFrame wiz, Object caller)
	{
		Img = new wizImage (s_pic);
		add ("West", Img);		
	}
	
	/**
	* Get a handler for the wizard page handler.
	* @see com.panayotis.wizard.Wizard#getWizPage
	*/
	public wPanel getPanel()
	{
		return pOptions;
	}
	
}
