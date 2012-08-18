package com.example.db4a;

import org.db4a.util.RemoteDBHelper;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.db4a.dao.EuserNDao;
import com.example.db4a.dao.EuserRDao;
import com.example.db4a.pojo.Euser;

public class MainActivity extends Activity {
	SQLiteDatabase sqliteDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		NDBHelper db = new NDBHelper(this);
		sqliteDb = db.getWritableDatabase();
		EuserNDao uNdao = new EuserNDao(db);
		for(Euser u:uNdao.find()){
			show("native",u);
		}
		new Thread() {

			public void run() {
				RemoteDBHelper rdb = new RemoteDBHelper("211.87.227.10",
						"ebag", "sa", "@!!*&@@&%^");
				try {
					EuserRDao uRdao = new EuserRDao(rdb);
					for(Euser u:uRdao.find(new String[]{"id"}, "id=?", new String[]{"1"}, null, null, null, null)){
						show("remote",u);
					}
					show("remote",uRdao.get(1));
				} catch (Exception e) {
					Log.e("qq", "error");
					e.printStackTrace();
				}
				Log.e("qq", "finish");
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			sqliteDb.close();
		} finally {

		}
	}

	public void show(String tag,Object u){
		Log.i("qq",tag+": "+u.toString());
	}
}
