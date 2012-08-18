package com.example.db4a;

import org.db4a.util.NativeDBHelper;

import com.example.db4a.pojo.Euser;

import android.content.Context;

public class NDBHelper extends NativeDBHelper {
	private static final String DBNAME = "gis.db";
	private static final int DBVERSION = 4;
	private static final Class<?>[] classes = {Euser.class};
	
	public Context context;
	public NDBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, classes);
		this.context=context;
	}
	@Override
	public void initDb() {
		
	}


}