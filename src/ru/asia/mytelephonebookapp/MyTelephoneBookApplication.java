package ru.asia.mytelephonebookapp;

import ru.asia.mytelephonebookapp.dataproviders.DataProvider;
import ru.asia.mytelephonebookapp.dataproviders.SQLiteDataProvider;
import android.app.Application;

public class MyTelephoneBookApplication extends Application{
		
	private static DataProvider dataProvider;

	@Override
	public void onCreate() {
		super.onCreate();
		// Use this to work with SQLiteDatabase
		dataProvider = new SQLiteDataProvider(this);
		// Use this to work with ArrayList
		//dataProvider = new MemoryDataProvider();
		//dataProvider.deleteAllContacts();
	}

	public static DataProvider getDataProvider() {
		return dataProvider;
	}
	
}
