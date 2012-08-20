package org.db4a.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class NativeDBHelper extends SQLiteOpenHelper {
	private Class<?>[] modelClasses;

	public NativeDBHelper(Context context, String databaseName,
			SQLiteDatabase.CursorFactory factory, int databaseVersion,
			Class<?>[] modelClasses) {
		super(context, databaseName, factory, databaseVersion);
		this.modelClasses = modelClasses;
	}

	public void onCreate(SQLiteDatabase db) {
		NativeTableHelper.createTablesByClasses(db, this.modelClasses);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		NativeTableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
	public abstract void initDb();
}