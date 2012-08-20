package com.example.db4a;

import org.db4a.util.RemoteDBHelper;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.db4a.dao.EuserRDao;
import com.example.db4a.pojo.Euser;

public class MainActivity extends Activity {
	SQLiteDatabase sqliteDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//insert.start();
		//select.start();
		//update.start();
		//delete.start();
	}

	final Thread select=new Thread() {
		public void run() {
			RemoteDBHelper rdb = new RemoteDBHelper("211.87.227.10",
					"ebag", "sa", "@!!*&@@&%^");
			try {
				EuserRDao uRdao = new EuserRDao(rdb);
				for(Euser u:uRdao.find()){
					show("remote",u);
				}
			} catch (Exception e) {
				Log.e("qq", "error");
				e.printStackTrace();
			}
			Log.e("qq", "finish");
		}
	};
	final Thread insert=new Thread() {
		public void run() {
			RemoteDBHelper rdb = new RemoteDBHelper("211.87.227.10",
					"ebag", "sa", "@!!*&@@&%^");
			try {
				EuserRDao uRdao = new EuserRDao(rdb);
				Euser s2=new Euser();
				s2.setName("s2");
				s2.setPwd("pwd2");
				uRdao.insert(s2);
				select.start();
			} catch (Exception e) {
				Log.e("qq", "error");
				e.printStackTrace();
			}
			Log.e("qq", "finish");
		}
	};
	final Thread update=new Thread() {
		public void run() {
			RemoteDBHelper rdb = new RemoteDBHelper("211.87.227.10",
					"ebag", "sa", "@!!*&@@&%^");
			try {
				EuserRDao uRdao = new EuserRDao(rdb);
				Euser s2=new Euser();
				s2.setId(3);
				s2.setName("student2");
				s2.setPwd("pwd2");
				uRdao.update(s2);
				select.start();
			} catch (Exception e) {
				Log.e("qq", "error");
				e.printStackTrace();
			}
			Log.e("qq", "finish");
		}
	};
	final Thread delete=new Thread() {
		public void run() {
			RemoteDBHelper rdb = new RemoteDBHelper("211.87.227.10",
					"ebag", "sa", "@!!*&@@&%^");
			try {
				EuserRDao uRdao = new EuserRDao(rdb);
				uRdao.delete(6);
			} catch (Exception e) {
				Log.e("qq", "error");
				e.printStackTrace();
			}
			select.start();
			Log.e("qq", "finish");
		}
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if(sqliteDb!=null)
			sqliteDb.close();
		} finally {

		}
	}

	public void show(String tag,Object u){
		Log.i("qq",tag+": "+u.toString());
	}
}
