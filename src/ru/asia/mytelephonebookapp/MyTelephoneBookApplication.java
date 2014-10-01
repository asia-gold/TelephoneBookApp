package ru.asia.mytelephonebookapp;

import android.app.Application;
import android.util.Log;

public class MyTelephoneBookApplication extends Application{
		
	private static ContactsDataSource dataSource;

	@Override
	public void onCreate() {
		super.onCreate();
		dataSource = new ContactsDataSource(this);
		dataSource.open();
		Log.e("MyApp", "DataSource initialize");
	}
	
	public static ContactsDataSource getDataSource() {
		return dataSource;
	}
	@Override
	public void onTerminate() {
		dataSource.close();
		super.onTerminate();
	}
}
