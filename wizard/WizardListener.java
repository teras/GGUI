package com.panayotis.wizard;


/**
* This is the interface model, in order for a class to recieve action
* events from the wizard. You have to implement this interface to your
* class, or else you can not use the Wizard.<BR>
* You should implement all of those methods in your class, or your
* class will be abstract.
*/
public interface WizardListener
{
	/** 
	* This method is called when the Finish or the Cancel button is clicked.
	* @param status true if the Finish button is clicked, false if the Cancel
	* button is clicked.
	*/
	public void exitWizard (boolean finished);

	/**
	* This method is called every time the Next button is clicked.
	* @param pg the old page number (first page has pagenumber 1).
	* @return the new page to jump. It <I><B>should</I></B> equal to 0, or the wizard will jump to
	* the specified page.
	* @see com.panayotis.wizard.Wizard#gotoPage
	*/	
	public int nextPage( int pg );
	
}
