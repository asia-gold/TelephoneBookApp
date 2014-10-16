package ru.asia.mytelephonebookapp;

import ru.asia.mytelephonebookapp.dataproviders.DataProvider;
import ru.asia.mytelephonebookapp.dataproviders.SQLiteDataProvider;
import android.app.Application;
import android.util.Log;

public class MyTelephoneBookApplication extends Application{
		
	private static ContactsDataSource dataSource;
	public DataProvider dataProvider;

	@Override
	public void onCreate() {
		super.onCreate();
		dataSource = new ContactsDataSource(this);
		dataSource.open();
		Log.e("MyApp", "DataSource initialize");
		dataProvider = new SQLiteDataProvider(this);
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
