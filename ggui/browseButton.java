package com.panayotis.awt;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

import com.panayotis.wt.wButton;
import com.panayotis.wt.wFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;


/**
* With this class you can display a browse button in your
* wizard. <BR>
* A browse button is a button with a JTextField attached to it.
* If you click on the button then a file dialog box appears
* and waits for you to select a file name. By clicking in the
* Open button of the file dialog then the contents of theJTextField
* are replaced with the directory and file name of the file 
* chosen. <BR>
* Remember that by creating a browseButton class usually you
* create a JTextField too, except if you give as parameter an
* already created JTextField. Default caption text is "Browse...".
*
* @author Panos Katsaloulis
* @see #setFrame
*/
public class browseButton extends JPanel
{
	
	/** The handler for the combined Textfield
	*/
	private JTextField bTextField;
	private wButton bButton;
	/** The handler of the wFrame to be modal
	*/
	private wFrame bFrame;
	private JFileChooser FChooser;

	private boolean filetype;

	/**
	* Create a new browseButton with default caption text.
	*/
	public browseButton ()
	{
		this ( "Browse...", new JTextField(25));
	}
	/**
	* Create a new browseButton with a specified JTextField and default caption text
	* @param tf the Textfield to combine with this wButton
	*/
	public browseButton ( JTextField tf )
	{
		this ( "Browse..." , tf );
	}
	/**
	* Create a new browseButton with specified caption text
	* @param str the caption text of the browseButton
	*/
	public browseButton ( String str )
	{
		this ( str , new JTextField (25) );
	}
	/**
	* Create a new browseButton with a specified JTextField and caption text
	* @param str the caption text of the browseButton
	* @param tf the Textfield to combine with this wButton
	*/
	public browseButton ( String str, JTextField tf)
	{
		bButton = new wButton ( str );
		bTextField = tf;
		filetype = true;
		FChooser = new JFileChooser ();
		setLayout(new BorderLayout());
		add(bTextField, "Center");
		add(bButton, "East");

		// Add the event handling routine for Java 1.1
		bButton.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{ callBrowser (evt); }
		} );

	}

	
	// The button is clicked, so we have to display the dialog
	private void callBrowser ( ActionEvent ev )
	{
		int returnVal = 0;
		
		FChooser.setDialogTitle("Please select output directory");
		try {
			if ( filetype ) {
				FChooser.setFileSelectionMode ( JFileChooser.FILES_ONLY );
			}
			else {
				FChooser.setFileSelectionMode ( JFileChooser.DIRECTORIES_ONLY );
			}
			returnVal = FChooser.showDialog(bFrame, "Select");
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				bTextField.setText ( FChooser.getSelectedFile().getAbsolutePath());
			}
		}
		catch ( Exception e ) 
		{}
	}

	/**
	* Get the chosen filename (the text in the Textfield) of this browseButton.
	*
	* @return a String with the chosen filename
	*/
	public String getFilename ()
	{
		return bTextField.getText();
	}
	
	/**
	* Assign a frame to the browseButton, in order for the file
	* dialog to be modal.<BR>
	* It is strongly recommended to use this method with the wizard,
	* so that the wizard must wait for the JFileChooser to be finished.<BR>
	* 
	* @param  fr the frame which would be modal, when you call
	* the JFileChooser. Usually give the wizard as parameter. If
	* you don't execute this method an invisible wFrame will be
	* created, in order to display the JFileChooser.<BR>
	* Example:<BR>
	* <CODE>browseButton brButton.setFrame( myWiz )</CODE>
	*
	*/
	public void setFrame (wFrame fr)
	{
		bFrame = fr;
	}

	/** Set the default filename 
	 * <br>
	 * WARNING: only sets the textfiles, not the browser
	 *
	 * @param filename the selected filename
	 */
	public void setFile (String filename) {
		bTextField.setText(filename);
	}
	
	/**
	 * Set if only files are able to be selected or only directories.
	 * Note that the mixed mode (files+directories) is not supported
	 *
	 * @param type If only files (true) or only directories (false) are
	 * able to be selected.
	 */
	public void setFileSelect ( boolean type) {
		filetype = type;
	}
}
