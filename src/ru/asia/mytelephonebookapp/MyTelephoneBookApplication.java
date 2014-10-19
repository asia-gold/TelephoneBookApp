package ru.asia.mytelephonebookapp;

import ru.asia.mytelephonebookapp.dataproviders.DataProvider;
import ru.asia.mytelephonebookapp.dataproviders.SQLiteDataProvider;
import android.app.Application;

public class MyTelephoneBookApplication extends Application{
		
	private static DataProvider dataProvider;

	@Override
	public void onCreate() {
		super.onCreate();
		dataProvider = new SQLiteDataProvider(this);
		//dataProvider = new MemoryDataProvider(this);
	}

	public static DataProvider getDataProvider() {
		return dataProvider;
	}
	
	@Override
	public void onTerminate() {
		if (dataProvider instanceof SQLiteDataProvider) {
			//dataProvider.deleteAllContacts();
			((SQLiteDataProvider) dataProvider).close();
		} else {
			dataProvider = null;
		}
		super.onTerminate();
	}
}
