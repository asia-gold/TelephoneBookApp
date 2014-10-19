package ru.asia.mytelephonebookapp.dataproviders;

import java.util.ArrayList;
import java.util.Date;

import ru.asia.mytelephonebookapp.models.Contact;

public interface DataProvider {

	long addContact(byte[] photo, String name, boolean isMale,
			Date dateBorn, String address);
	void addContact(Contact contact);
	void updateContact(long id, byte[] photo, String name,
			boolean isMale, Date dateBorn, String address);
	void deleteContact(Contact contact);
	void deleteAllContacts(ArrayList<Contact> data);
	void deleteAllContacts();
	Contact getContact(long id);
	ArrayList<Contact> getAllContact();
	ArrayList<Contact> getAllContactsByGender(int gender);
}
