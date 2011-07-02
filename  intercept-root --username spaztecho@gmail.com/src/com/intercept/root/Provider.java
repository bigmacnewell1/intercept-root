package com.intercept.root;


//import android.os.Process;
import android.content.ContentValues;
import android.content.ContentProvider;
import android.database.Cursor;
import android.net.Uri;

public class Provider extends ContentProvider
{
	public static void main(String args[])
	{
	}

	public static final Uri CONTENT_URI = Uri.parse("content://com.intercept.root.Provider");

	public boolean onCreate()
	{
		System.loadLibrary("Zysploit");
		return true;
	}

	public int update(Uri u, ContentValues c, String s1, String[] s2)
	{
		return 0;
	}

	public int delete(Uri u, String s1, String[] s2)
	{
		return 0;
	}

	public Uri insert(Uri u, ContentValues c)
	{
		return null;
	}

	public String getType (Uri uri)
	{
		return null;
	}

	public Cursor query(Uri u, String[] s1, String s2, String[] s3, String s4)
	{
		return null;
	}

}

