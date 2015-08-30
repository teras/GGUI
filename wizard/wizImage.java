package com.panayotis.wizard;

import java.net.URL;
import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import com.panayotis.wt.wLabel;
import com.panayotis.wt.wImageIcon;

/**
* Wizard class uses this component in order to add a picture to the left of your wizard.
* Usually you don't need to have access to this class.
*/
class wizImage extends wLabel

{
	
	/**
	* Create a new wizImage.
	*
	* @param s_img The filename of the picture to display
	* @param wFrame the parent wFrame of this page (usually the Wizard)
	* @param caller the user caller class, the class which implemented the WizardListener interface. It is the same caller class as with Wizard.
	*/
	wizImage (String s_img)
	{
		super ();
		URL imgURL;
		try {
			imgURL = new URL("file://"+s_img);
			setIcon(new wImageIcon(imgURL));
		}
		catch (Exception e){
			System.err.println("Couldn't find URL file: " + s_img);
			return;
		}
	}

}
