package com.example.db4a.dao;

import org.db4a.dao.impl.NativeDaoImpl;

import android.database.sqlite.SQLiteOpenHelper;

import com.example.db4a.pojo.Euser;

public class EuserNDao extends NativeDaoImpl<Euser>{

	public EuserNDao(SQLiteOpenHelper dbHelper) {
		super(dbHelper);
	}

}
