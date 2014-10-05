package ru.asia.mytelephonebookapp;

import java.util.ArrayList;
import java.util.List;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class ContactsDataSource {

	private SQLiteDatabase database;
	private ContactDBHelper dbHelper;
	private String[] allColumns = { ContactDBHelper.COLUMN_ID,
			ContactDBHelper.COLUMN_PHOTO, ContactDBHelper.COLUMN_NAME, 
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

	// Return id of new data row
	public long addContact(byte[] photo, String name, String gender, String dateBorn,
			String address) {
		ContentValues values = new ContentValues();
		values.put(ContactDBHelper.COLUMN_PHOTO, photo);
		values.put(ContactDBHelper.COLUMN_NAME, name);
		values.put(ContactDBHelper.COLUMN_GENDER, gender);
		values.put(ContactDBHelper.COLUMN_DATE_BIRTH, dateBorn);
		values.put(ContactDBHelper.COLUMN_ADDRESS, address);
		long insertID = database.insert(ContactDBHelper.TABLE_NAME, null,
				values);
		Log.e("---------------", "id create = " + insertID);
		return insertID;
	}

	public void deleteContact(Contact contact) {
		long id = contact.getId();
		Log.e("----------------", "deleted with id: " + id);
		database.delete(ContactDBHelper.TABLE_NAME, ContactDBHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public void deleteAllContacts(ArrayList<Contact> data) {
		for (int i = 0; i < data.size(); i++) {
			Contact c = data.get(i);
			deleteContact(c);
		}
	}

	public Contact getContact(long id) {
		// List<Contact> contact = new ArrayList<>();
		Contact newContact = null;
		Cursor cursor = database.query(ContactDBHelper.TABLE_NAME, allColumns,
				ContactDBHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();

		newContact = cursorToContact(cursor);
		// contact.add(newContact);
		// cursor.moveToNext();

		cursor.close();
		return newContact;
	}

	public ArrayList<Contact> getAllContact() {
		ArrayList<Contact> contacts = new ArrayList<>();

		Cursor cursor = database.query(ContactDBHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return contacts;
	}

	public ArrayList<Contact> getAllContactsByGender(String gender) {
		ArrayList<Contact> contacts = new ArrayList<>();
		if (TextUtils.equals(gender, "Both")) {
			contacts = getAllContact();			
		} else {
			Cursor cursor = database.query(ContactDBHelper.TABLE_NAME,
					allColumns, ContactDBHelper.COLUMN_GENDER + " = " + gender,
					null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Contact contact = cursorToContact(cursor);
				contacts.add(contact);
				cursor.moveToNext();
			}
			cursor.close();			
		}
		return contacts;
	}

	private Contact cursorToContact(Cursor cursor) {
		Contact contact = new Contact();
		contact.setId(cursor.getLong(0));
		contact.setPhoto(cursor.getBlob(1));
		contact.setName(cursor.getString(2));
		contact.setGender(cursor.getString(3));
		contact.setDateOfBirth(cursor.getString(4));
		contact.setAddress(cursor.getString(5));
		return contact;
	}

}
