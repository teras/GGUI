package com.panayotis.awt;

import com.panayotis.awt.IntField;

/**
* Use this interface to listen when any of change events of a IntField happens
*/
public interface IntFieldListener
{
	// Called every time a value has changed;
	public void valueChanged ( IntField ifield);
}
