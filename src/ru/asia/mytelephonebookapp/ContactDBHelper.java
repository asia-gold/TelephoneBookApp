package ru.asia.mytelephonebookapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manage contacts database creation and version changing.
 * 
 * @author Asia
 *
 */
public class ContactDBHelper extends SQLiteOpenHelper{
	
	/**
	 * Database name and version.
	 */
	public static final String DATABASE_NAME = "telephonebook.db";
	private static final int DATADASE_VERSION = 1;
	
	/**
	 * Table name.
	 */
	public static final String TABLE_NAME = "contacts";
	
	/**
	 * Columns names.
	 */
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PHOTO = "photo";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_GENDER = "isMale";
	public static final String COLUMN_DATE_BIRTH = "dateBorn";
	public static final String COLUMN_ADDRESS = "address";
			
	/**
	 * Database creation SQL statement.
	 */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PHOTO + " BLOB, " + COLUMN_NAME + " text not null, " 
			+ COLUMN_GENDER + " integer default 0, " + COLUMN_DATE_BIRTH + " text, " 
			+ COLUMN_ADDRESS + " text" + ");";
	
	
	public ContactDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATADASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);		
	}

}
