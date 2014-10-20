package ru.asia.mytelephonebookapp.dataproviders;

import java.util.ArrayList;
import java.util.Date;

import ru.asia.mytelephonebookapp.models.Contact;

/**
 * Work with contacts. Add, update, delete, get contacts.
 * 
 * @author Asia
 *
 */
public interface DataProvider {

	
	/**
	 * Add contact.
	 * 
	 * Contact data to add.
	 * @param photo
	 * @param name
	 * @param isMale
	 * @param dateBorn
	 * @param address
	 * @return id of new contact
	 */
	long addContact(byte[] photo, String name, boolean isMale,
			Date dateBorn, String address);
	
	/**
	 * Add contact.
	 * 
	 * @param contact - contact to add.
	 */
	void addContact(Contact contact);
	
	
	/**
	 * Update contact, specified by @param id.
	 * 
	 * @param id	- id of contact.
	 * 
	 * Contact data to update.
	 * @param photo
	 * @param name
	 * @param isMale
	 * @param dateBorn
	 * @param address
	 */
	void updateContact(long id, byte[] photo, String name,
			boolean isMale, Date dateBorn, String address);
	
	/**
	 * Delete contact, specified by @param contact.
	 * 
	 * @param contact	- contact to delete.
	 */
	void deleteContact(Contact contact);
	
	/**
	 * Delete contacts, specified by @param data.
	 * 
	 * @param data	- contacts to delete
	 */
	void deleteAllContacts(ArrayList<Contact> data);
	
	/**
	 * Delete all contacts.
	 */
	void deleteAllContacts();
	
	/**
	 * Get contact from provider, using id.
	 * 
	 * @param id	- id of contact.
	 * @return contact.
	 */
	Contact getContact(long id);	
	
	/**
	 * Get all contacts from provider.
	 * 
	 * @return ArrayList of contacts.
	 */
	ArrayList<Contact> getAllContact();
	
	/**
	 * Get contacts from provider, specified by @param gender.
	 * 
	 * @param gender
	 * @return ArrayList of contacts.
	 */
	ArrayList<Contact> getAllContactsByGender(int gender);
}
