package com.example.db4a.dao;

import org.db4a.dao.impl.RemoteDaoImpl;
import org.db4a.util.RemoteDBHelper;

import com.example.db4a.pojo.Euser;

public class EuserRDao extends RemoteDaoImpl<Euser>{

	public EuserRDao(RemoteDBHelper rdb) {
		super(rdb);
	}

	

}
