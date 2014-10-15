package ru.asia.mytelephonebookapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
	public long addContact(byte[] photo, String name, boolean isMale,
			Date dateBorn, String address) {
		ContentValues values = new ContentValues();
		values.put(ContactDBHelper.COLUMN_PHOTO, photo);
		values.put(ContactDBHelper.COLUMN_NAME, name);
		int gender = (isMale == true ? 1 : 0);
		values.put(ContactDBHelper.COLUMN_GENDER, gender);
		String dateBornString = formatDateToString(dateBorn);
		values.put(ContactDBHelper.COLUMN_DATE_BIRTH, dateBornString);
		values.put(ContactDBHelper.COLUMN_ADDRESS, address);
		long insertID = database.insert(ContactDBHelper.TABLE_NAME, null,
				values);
		Log.e("---------------", "id create = " + insertID);
		return insertID;
	}
	
	public void addContact(Contact contact) {
		byte[] photo = contact.getPhoto();
		String name = contact.getName();
		boolean isMale = contact.getIsMale();
		Date dateBorn = contact.getDateOfBirth();
		String address = contact.getAddress();
		addContact(photo, name, isMale, dateBorn, address);
	}

	public void updateContact(long id, byte[] photo, String name,
			boolean isMale, Date dateBorn, String address) {
		ContentValues values = new ContentValues();
		values.put(ContactDBHelper.COLUMN_PHOTO, photo);
		values.put(ContactDBHelper.COLUMN_NAME, name);
		int gender = (isMale == true ? 1 : 0);
		values.put(ContactDBHelper.COLUMN_GENDER, gender);
		String dateBornString = formatDateToString(dateBorn);
		values.put(ContactDBHelper.COLUMN_DATE_BIRTH, dateBornString);
		values.put(ContactDBHelper.COLUMN_ADDRESS, address);
		String whereClause = null;
		if (id != 0) {
			whereClause = ContactDBHelper.COLUMN_ID + " = " + id;
		}
		database.update(ContactDBHelper.TABLE_NAME, values, whereClause, null);
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
	
	public void deleteAllContacts() {
		database.delete(ContactDBHelper.TABLE_NAME, null, null);
	}

	public Contact getContact(long id) {
		Contact newContact = null;
		Cursor cursor = database.query(ContactDBHelper.TABLE_NAME, allColumns,
				ContactDBHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();

		newContact = cursorToContact(cursor);

		cursor.close();
		return newContact;
	}

	public ArrayList<Contact> getAllContact() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();

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

//	public ArrayList<Contact> getAllContactsByGender(String gender) {
//		ArrayList<Contact> contacts = new ArrayList<Contact>();
//		if (TextUtils.equals(gender, "Both")) {
//			contacts = getAllContact();
//		} else {
//			Cursor cursor = database.query(ContactDBHelper.TABLE_NAME,
//					allColumns, ContactDBHelper.COLUMN_GENDER + " = " + gender,
//					null, null, null, null);
//
//			cursor.moveToFirst();
//			while (!cursor.isAfterLast()) {
//				Contact contact = cursorToContact(cursor);
//				contacts.add(contact);
//				cursor.moveToNext();
//			}
//			cursor.close();
//		}
//		return contacts;
//	}

	public static String formatDateToString(Date date) {
		String dateString = null;
		if (date == null) {
			dateString = "";
		} else {
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,
					Locale.ENGLISH);
			dateString = sdf.format(date);
		}
		return dateString;
	}

	public static Date formatStringToDate(String dateString)
			throws ParseException {
		Date date = new Date();
		if (dateString == null) {
			date = null;
		} else {
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,
					Locale.ENGLISH);
			date = sdf.parse(dateString);
		}
		return date;
	}

	private Contact cursorToContact(Cursor cursor) {
		Contact contact = new Contact();
		contact.setId(cursor.getLong(0));
		contact.setPhoto(cursor.getBlob(1));
		contact.setName(cursor.getString(2));
		boolean isMale = (cursor.getInt(3) == 1 ? true : false);
		contact.setIsMale(isMale);
		Date tmpDate = null;
		try {
			tmpDate = formatStringToDate(cursor.getString(4));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		contact.setDateOfBirth(tmpDate);
		contact.setAddress(cursor.getString(5));
		return contact;
	}
}
