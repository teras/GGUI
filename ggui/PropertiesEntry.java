package com.panayotis.util;

public class PropertiesEntry
{
	private String keyword, value;

	public PropertiesEntry ( String key, String val)
	{
		keyword = key;
		value = val;
	}

	public String getKeyword ()
	{
		return keyword;
	}

	public String getValue ()
	{
		return value;
	}

	public void setKeyword ( String key)
	{
		keyword = key;
	}

	public void setValue ( String val)
	{
		value = val;
	}

	public boolean keywordEquals ( String key )
	{
		return ( key.equals (keyword));
	}

	public boolean valueEquals ( String val)
	{
		return ( val.equals(value));
	}

}

