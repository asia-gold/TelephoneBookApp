package ru.asia.mytelephonebookapp.dataproviders;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.ContactsDataSource;
import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Context;

public class SQLiteDataProvider implements DataProvider {

	ArrayList<Contact> dataInDatabase;
	ContactsDataSource dataSource;

	public SQLiteDataProvider(Context context) {
		dataInDatabase = new ArrayList<Contact>();
		dataSource = new ContactsDataSource(context);
	}

	@Override
	public void getData() {
		dataInDatabase = dataSource.getAllContact();
	}

	@Override
	public void deleteData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void query() {
		// TODO Auto-generated method stub

	}

}
