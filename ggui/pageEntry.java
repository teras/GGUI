package com.panayotis.ggui.creator;

public class pageEntry
{
	private String data;

	public pageEntry (String d)
	{
		data = d;
	}

	public void setValue ( String d)
	{
		data = d;
	}

	public String getValue ()
	{
		return data;
	}
}
