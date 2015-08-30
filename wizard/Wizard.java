package com.panayotis.wizard;

import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Container;

import com.panayotis.wt.wContainer;
import com.panayotis.wt.wCardLayout;
import com.panayotis.wt.wBorderLayout;
import com.panayotis.wt.wFlowLayout;
import com.panayotis.wt.wButton;
import com.panayotis.wt.wFrame;
import com.panayotis.wt.wLabel;
import com.panayotis.wt.wPanel;
import com.panayotis.wt.wOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Vector;


//import com.panayotis.wizard.WizardListener;

/**
* This is the main class of the Wizard component. A normal developer
* should use ONLY the methods provided by this class and communicate
* ONLY with this class. All other classes are helper-classes and they
* shouldn't be used.<BR>
*
* @author Panos Katsaloulis
* @version 0.9.4
*/
public class Wizard extends wFrame
{
	
	/**
	* The currently displaying wizard page. First page has number <B>1</B> not 0.
	*/
	public int currentPage ; // which is the current page
	
	private Vector v_pages; //somewhere to store the pages of this wizard
	private wPanel p_Card ; // panel for the wizard pages
	private wCardLayout cards ; // layout for the wizard pages
	private wButton b_Begin ; // buttin to go to the first card
	private wButton b_Prev ;  // button to go to the previous card
	private wButton b_Next ; // button to go to the next card
	private wButton b_Finish ; // button to finish the wizard
	private wButton b_Cancel ; // button to cancel the wizard
	private wButton b_Help ; // button to display a help frame
	
	private WizardListener caller ;	// pointer to store the current object, which called
								//  this wizard. This pointer is used in order to
								// inform this object of any wizard events.

	/**
	* Create a new Wizard with default size
	*
	* @param apl the component which recieves the Wizard events.
	* Usually it is the caller class. If you intent to use the Wizard
	* in a browser, you <I>must</I> provide the applet class as a parameter,
	* or else the Wizard couldn't display any pictures.
	*/
	public Wizard (WizardListener apl) // default constructor
	{
		this(apl, 620, 300 );
	}
	
	
	/**
	* Create a new Wizard with specified size
	*
	* @param apl the component which recieves the Wizard events.
	* Usually it is the caller class. If you intent to use the Wizard
	* in a browser, you <I>must</I> provide the applet class as a parameter,
	* or else the Wizard couldn't display any pictures.
	* @param xLen the width of the Wizard
	* @param yLen the height of the Wizard
	*/
	public Wizard (WizardListener wl, int xLen, int yLen)
	{
		v_pages = new Vector(0,1);  // only one start page and then increasement +1
		
		// define the wPanel for the Wizard Pages
		// It MUST be defined here and not in createFirstCard, 
		// because addPage() needs it ...
		cards = new wCardLayout(); // the Layout for the wizard pages
		p_Card = new wPanel();
		p_Card.setLayout (cards);
		
		addPage (); // Add the first page
		createFirstCard (xLen, yLen); 	// this is the first page to be added => create layout etc.
		caller = wl; // set the parent caller, in order to send an event there
		setActionListeners();
	}


	// Here we try to add the various action listeners for Java 1.1
	private void setActionListeners ()
	{

		b_Begin.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{
				gotoPage ( 0, 1);
			}
		} );
 
		b_Prev.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{
				wizPage wp;

				wp = (wizPage)v_pages.elementAt (currentPage-1);
				gotoPage ( 0, wp.prevPage );
			}
		} );
 
		b_Next.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{
				int next_page;

				next_page = caller.nextPage ( currentPage );
				if (next_page <= 0) next_page = currentPage + 1;
				gotoPage ( currentPage, next_page);
			}
		} );
 
		b_Finish.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{
				hideWizard();
				caller.exitWizard( true );
			}
		} );
 
		b_Cancel.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{
				hideWizard ();
				caller.exitWizard ( false );
			}
		} );
 
		b_Help.addActionListener (new ActionListener ()
		{ public void actionPerformed (ActionEvent evt)
			{
				displayHelp ();
			}
		} );
	}
	

	private void displayHelp ()
	{
		wizPage wp;
		String title = "Help for wizard page #" + currentPage; 
		wp = (wizPage)v_pages.elementAt (currentPage-1); 
		wOptionPane.showMessageDialog((wFrame)this, wp.HelpText, title, wOptionPane.QUESTION_MESSAGE);
	}		


	/**
	* Use this method ONLY if you want to jump to another page that
	* the next one. It is used for interactive navigation only.
	* @param newPage the number of the page number to jump to. First page has
	* number 1.
	* @param oldPage the number of the previous page number ( to go back when the
	* the Previous button is clicked). If equals zero, not changes have been done
	* (this means that it keeps the previous situation of the "Previous" button of this page).
	*  <BR>-<BR>
	* Warning : if you want to use this wizard in the normal way (e.g. collect
	* information in a serial way) you don't ever need to use this method. On the
	* other hand, if you want to interactively jump to a special page, use this method.
	* For example, if you want to have some options and jump to a special page
	* every time the user selects a special option, then that's what you can do:
	* <BR> 1. "Trap" the nextPage method in your caller application.
	* <BR> 2. Check if the old page (current) is the one wanted to interact ( the user
	* pressed the "Next" button).
	* <BR> 3. Read the value of the option component.
	* <BR> 4. Return the new page number. Wizard will automatically jump there.
	* <BR> Remember that every time you set a page like this, the wizard 'remembers'
	* what was the previous page, and by clicking on the "Previous" button the wizard 
	* automatically jumps there and NOT on the real back page. Of course, if the "previous"
	* page was also the real previous page, the wizard jumps correctly to it.
	*/
	public void gotoPage ( int oldPage, int newPage )
	{
		wizPage wp;

		if ( newPage > pages() || oldPage > pages() || newPage <=0 || oldPage <0 ) return;
		cards.show (p_Card, "WizPage" + newPage);
		setButtons ( newPage );
		currentPage = newPage ;
		if (oldPage > 0 )
		{
			wp = (wizPage)v_pages.elementAt( newPage -1);
			if ( newPage != oldPage ) wp.prevPage = oldPage;
		}
	}


	// set the condition of the navigation buttons
	private void setButtons ( int page )
	{
		if ( ! existsPage (page) ) return;	// exit if this page doesn't exist
		if ( page < pages() )	b_Next.setEnabled(true);	else	b_Next.setEnabled(false);
		if ( page > 1)			b_Prev.setEnabled(true);	else	b_Prev.setEnabled(false);
		if ( page > 1)			b_Begin.setEnabled(true); 	else	b_Begin.setEnabled(false);
		wizPage wp = (wizPage)v_pages.elementAt(page-1);
		if ( wp.canFinish )		b_Finish.setEnabled(true);	else	b_Finish.setEnabled(false);
	}

	
	/**
	* With this method you can set the finish button to any page.
	* 
	* @param page the page number you want to change the finish button state
	* @param finish state of the finish button. If <I>true</I> the
	* finish button is anabled and you exit the Wizard at this page. If
	* <I>false</I> the finish button is desabled and you can not exit
	* the Wizard at this point.
	* <BR><BR>
	* By default the finish button is disabled for all the wizard pages
	* except the last one. Sometimes there is a need to finish the
	* wizard earlier. With this method you can set manually which pages
	* you can exit with the finish button.<BR>
	* Remember that in the last page the finish button is <B>always</B> enabled.
	*/
	public void setFinish ( int page, boolean finish)
	{
		wizPage wp;
		if ( !existsPage (page) ) return; // exit if this page don't exists
		wp = (wizPage)v_pages.elementAt(page-1);
		wp.canFinish = finish;
	}		

	/**
	* With this method you can set a little help text for every Wizard
	* page. This feature is still experimental and it is possible to
	* change dramatically in the future.<BR>Please note: if the width of the wizard is small
	* it is possible that the help button will NOT appear on screen! 	
	* @param page the page number to display the supplied text
	* @param txt[] the help text in a String array. Every line of text is an array element
	*/
	public void setHelpText ( int page, String []txt)
	{
		wizPage wp;
		if ( ! existsPage (page) ) return; // exit if this page don't exists
		wp = (wizPage)v_pages.elementAt (page-1);
		wp.HelpText = txt;
	}

	/**
	* With this method you can set the number of pages for this Wizard.<BR>
	* 
	* @param hm_pages the absolute number of pages. <BR>
	* That means if you call mywiz.setPages (5), in your program just
	* after the constructor, you create 5 pages and not 6, as opposed
	* to the addPage method. <BR>
	* If you use it for a second time with a greater number of pages as
	* before, you can add pages with this method. It is a quicker method
	* than addPage().<BR>
	* Remember that by creating a Wizard, you create it's first page too.
	* So if you use the addPage() method, after you created a wizard, you
	* will have <B>2</B> pages instead of 1.
	*
	* @see #addPage
	*/
	public void setPages (int hm_pages)
	{
		if (hm_pages <= 0 || hm_pages > 100 ) return ;// check that number of pages is between the limits
		if ( hm_pages > pages() )	// check that we ADDING pages. We cannot delete pages
		{									// with this command
			int n=hm_pages - pages();
			for ( int f = 1 ; f <= n ; f++) // for every page we want to add ...
			{
				this.addPage();
			}
		}
	}
	
		
	/**
	* With this method you can add another page to the Wizard.
	* 
	* @see #setPages
	*/
	public void addPage ()
	{
		v_pages.addElement (new wizPage ( pages()));
		int pos = pages();
		p_Card.add( "WizPage" + pos , (wPanel)v_pages.elementAt (pos-1) ); // store the card data for this page
	}


	/**
	* With this method you can get back the number of pages this
	* wizard has.
	* @return the number of pages
	*/
	public int pages ()
	{
		return v_pages.size();		
	}

	
	/**
	* With this method you can check if the given page number exists.
	*
	* @param pag the page number we want to check if it extsts
	* @return true if exists, false if it doesn't
	*/
	public boolean existsPage ( int pag )
	{
		return ( pages() >= pag && pag>0 );
	}
	

	/** This method returns a handler for a wizard page. It is not recommended though to use this method.
	*
	* @param the page number you hand to get a handler
	* @return the wizard wPanel. On ths wPanel usually the Wizard adds the
	* various components. You can use this handler to change the LaoutManager
	* for example, or add components not supported by the Wizard.
	* @since changed on 0.9.2
	*/
	public wPanel getWizPage (int page)
	{
		wizPage wp;
		if ( existsPage (page) == false ) return null ; // exit if this page don't exists
		else
		{
			wp = (wizPage)v_pages.elementAt(page-1);
			return wp.getPanel();
		}
	}
	


	// This method is called when we create a card for first time 
	// in order to manupulate the layout of the navigation buttons etc.
	private void createFirstCard(int xLen, int yLen) // you are not supposed to have access to this method
	{
		Container RootPane;
		wPanel p_Nav ; // panel for the navigation buttons
		wPanel p_lower ; // panel for the wizard bottom

		RootPane = getContentPane();
		RootPane.setLayout ( new wBorderLayout());

		RootPane.setBackground(SystemColor.control);
		RootPane.setForeground(SystemColor.controlText);
		
		// set Navigation wPanel
		p_lower = new wPanel ();
		p_Nav = new wPanel ();
		
		p_lower.setLayout( new wBorderLayout ());
		p_lower.add( p_Nav, "Center");
		p_Nav.setLayout ( new wFlowLayout());

		// set Navigation buttons
		b_Begin = new wButton (" << Begin ");
		b_Prev = new wButton (" < Previous ");
		b_Next = new wButton (" > Next ");
		b_Finish = new wButton (" Finish ");
		b_Cancel = new wButton (" Cancel ");
		b_Help = new wButton (" Help ");

		// add navigation buttons
		p_Nav.add (b_Begin);
		p_Nav.add ( new wLabel (""));
		p_Nav.add (b_Prev);
		p_Nav.add (b_Next);
		p_Nav.add ( new wLabel (""));
		p_Nav.add (b_Finish);
		p_Nav.add ( new wLabel (""));
		p_Nav.add (b_Cancel);
		p_Nav.add ( new wLabel (""));
		p_Nav.add (b_Help);
		
		wPanel cv = new wPanel ();
		cv.setSize (15,1);

		RootPane.add ( "Center", p_Card);
		RootPane.add ( "South", p_lower );
		RootPane.add ( "East" , cv );
		setResizable (false) ;
		setSize(xLen, yLen);
		currentPage = 1;
	}
	
	
	
	/**
	* You can attach an object to a specific page.
	*
	* @param pag the page number you want to add an object
	* @param obj the Component you want to add to this page.
	*/
	public void addItem ( int pag, wPanel obj )
	{
		wizPage cpage ;
		if ( existsPage (pag) == false ) return; // exit if this page don't exists
		cpage = (wizPage)v_pages.elementAt(pag-1); // first page is 0, so we must substract
		cpage.addItem(obj);
	}
	

	/**
	* With this method you can add a picture to a specific page on
	* the left of the wizard.
	*
	* @see #addPicture(java.lang.String)
	* @param pag the page you want to add the picture. All other pages are not affected.
	* @param s_pic the filename of the page. You can use relative or absolute path.
	* <BR><BR><B>Warning!</B><BR>
	* Remember that by adding an image to page already having an image,
	* doesn't destroy the old image but only overlaps it.
	*/
	public void addPicture ( int pag, String s_pic )
	{
		wizPage cpage ;
		if ( ! existsPage (pag))
			return; // exit if this page don't exists
		cpage = (wizPage)v_pages.elementAt(pag-1); // first page is 0, so we must substract
		cpage.addPicture(s_pic, this, caller);//support with information such as the frame to be redrawn & the (possible) caller applet
	}

	/**
	* With this method you can add the same picture to every page on
	* the left of the wizard.
	*
	* @see #addPicture(int,java.lang.String)
	* @param s_pic the filename of the page. You can use relative or absolute path.
	* <BR><BR><B>Warning!</B><BR>
	* Remember that by adding an image to page already having an image,
	* doesn't destroy the old image but only overlaps it.
	*/
	public void addPicture (String s_pic )
	{
		for ( int f = 1; f <= pages(); f++)
		{
			addPicture (f, s_pic);
		}
	}

	/**
	* Use this method in order to display the wizard.<BR>
	* It is obligatory to use this method instead of <CODE>show()</CODE>.
	*/
	public void displayWizard ()
	{
		wizPage wp ;
		super.setVisible(true);

		wp = (wizPage)v_pages.lastElement (); // get last page
		wp.canFinish = true ; // last page can be "Finish"-ed

		cards.first(p_Card);
		currentPage = 1;
		setButtons ( currentPage );
	}

		
	/**
	* Use this method to cleanly hide this wizard. <BR>
	* If you exit the wizard with the "Cancel" or "Finish" button you don't
	* need to call this method. This is ONLY for interactive wizard usage.
	*/
	public void hideWizard ()
	{
		setVisible(false);
	}
}
