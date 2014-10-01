package ru.asia.mytelephonebookapp;

import java.util.ArrayList;
import java.util.List;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContactsDataSource {
	
	private SQLiteDatabase database;
	private ContactDBHelper dbHelper;
	private String[] allColumns = {ContactDBHelper.COLUMN_ID, ContactDBHelper.COLUMN_NAME,
			ContactDBHelper.COLUMN_GENDER, ContactDBHelper.COLUMN_DATE_BIRTH, 
			ContactDBHelper.COLUMN_ADDRESS };
	
	public ContactsDataSource(Context context) {
		dbHelper = new ContactDBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase(); 
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Contact addContact(String name, String gender, String dateBorn, String address) {
		ContentValues values = new ContentValues();
		values.put(ContactDBHelper.COLUMN_NAME, name);
		values.put(ContactDBHelper.COLUMN_GENDER, gender);
		values.put(ContactDBHelper.COLUMN_DATE_BIRTH, dateBorn);
		values.put(ContactDBHelper.COLUMN_ADDRESS, address);
		long insertID = database.insert(ContactDBHelper.TABLE_NAME, null, values);
		Log.e("---------------", "id create = " + insertID);
		Cursor cursor = database.query(ContactDBHelper.TABLE_NAME, allColumns, 
				ContactDBHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
		cursor.moveToFirst();
		Contact newContact = cursorToContact(cursor);
		cursor.close();
		return newContact;
	}
	
	public void deleteContact(Contact contact) {
		long id = contact.getId();
		database.delete(ContactDBHelper.TABLE_NAME, ContactDBHelper.COLUMN_ID + " = " + id, null);
	}
	
	public Contact getContact(long id) {
		//List<Contact> contact = new ArrayList<>();
		Contact newContact = null;
		Cursor cursor = database.query(ContactDBHelper.TABLE_NAME, allColumns, ContactDBHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		
			newContact = cursorToContact(cursor);
			//contact.add(newContact);
			//cursor.moveToNext();
		
		cursor.close();
		return newContact;
	}
	
	public List<Contact> getAllContact() {
		List<Contact> contacts = new ArrayList<>();
		
		Cursor cursor = database.query(ContactDBHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return	contacts;
	}
	
	private Contact cursorToContact(Cursor cursor) {
		Contact contact = new Contact();
		contact.setId(cursor.getLong(0));
		contact.setName(cursor.getString(1));
		contact.setGender(cursor.getString(2));
		contact.setDateOfBirth(cursor.getString(3));
		contact.setAddress(cursor.getString(4));
		return contact;
	}

}
