package ru.asia.mytelephonebookapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDBHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "telephonebook.db";
	private static final int DATADASE_VERSION = 1;
	
	public static final String TABLE_NAME = "contacts";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_GENDER = "gender";
	public static final String COLUMN_DATE_BIRTH = "dateBorn";
	public static final String COLUMN_ADDRESS = "address";
			
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, " + COLUMN_GENDER + " text, " 
			+ COLUMN_DATE_BIRTH + " text, " + COLUMN_ADDRESS + " text" + ");";
	
	
	public ContactDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATADASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		Log.e("-------------------", "DB created");
	}
	// Мне не нравится эта реализация
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXESTS " + TABLE_NAME);
		onCreate(db);		
	}

}
