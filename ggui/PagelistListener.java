package com.panayotis.awt;

import com.panayotis.ggui.objects.gguiObject;

public interface PagelistListener
{

	/**
	* Use this method when we want to change the display of an object
	*/
	public void updateObject ( String name );

	/**
	* Use this object to add the specified object to the displaying list
	*/
	public void appendObject ( gguiObject obj);

	/**
	* Set the title of the current creator window
	*/
	public void setTitle ( String title);

}
