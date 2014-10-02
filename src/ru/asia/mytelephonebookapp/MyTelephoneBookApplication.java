package ru.asia.mytelephonebookapp;

import android.app.Application;
import android.util.Log;

public class MyTelephoneBookApplication extends Application{
		
	private static ContactsDataSource dataSource;
	private static ContactAdapter adapter;

	@Override
	public void onCreate() {
		super.onCreate();
		dataSource = new ContactsDataSource(this);
		dataSource.open();
		adapter = new ContactAdapter(getApplicationContext(), dataSource.getAllContact());
		Log.e("MyApp", "DataSource initialize");
	}
	
	public static ContactsDataSource getDataSource() {
		return dataSource;
	}
	
	public static ContactAdapter getAdapter() {
		return adapter;
	}
	@Override
	public void onTerminate() {
		dataSource.close();
		super.onTerminate();
	}
}
