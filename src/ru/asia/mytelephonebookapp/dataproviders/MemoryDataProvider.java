package ru.asia.mytelephonebookapp.dataproviders;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import ru.asia.mytelephonebookapp.models.Contact;

/**
 * Work with contacts from ArrayList. Add, update, delete and get
 * contacts.
 * 
 * @author Asia
 *
 */
public class MemoryDataProvider implements DataProvider {
	
	private ArrayList<Contact> dataInMemory;
	
	public MemoryDataProvider() {
		dataInMemory = new ArrayList<Contact>();
	}

	/**
	 * Add contact to ArrayList.
	 * 
	 * Contact data for adding.
	 * @param photo
	 * @param name
	 * @param isMale
	 * @param dateBorn
	 * @param address
	 * @return id of new contact
	 */
	@Override
	public long addContact(byte[] photo, String name, boolean isMale,
			Date dateBorn, String address) {
		Contact newContact = new Contact();
		newContact.setPhoto(photo);
		newContact.setName(name);
		newContact.setIsMale(isMale);
		newContact.setDateOfBirth(dateBorn);
		newContact.setAddress(address);
		dataInMemory.add(newContact);
		long id = (long)dataInMemory.indexOf(newContact);

		newContact.setId(id);
		dataInMemory.set((int) id, newContact);
		return id;
	}

	/**
	 * Add contact to ArrayList.
	 * 
	 * @param contact - contact for adding.
	 */
	@Override
	public void addContact(Contact contact) {
		dataInMemory.add(contact);
		long id = (long)dataInMemory.indexOf(contact);
		
		contact.setId(id);
		dataInMemory.set((int) id, contact);		
	}

	/**
	 * Update contact specified by id argument.
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
	@Override
	public void updateContact(long id, byte[] photo, String name,
			boolean isMale, Date dateBorn, String address) {
		Contact updateContact = dataInMemory.get((int)id);
		updateContact.setPhoto(photo);
		updateContact.setName(name);
		updateContact.setIsMale(isMale);
		updateContact.setDateOfBirth(dateBorn);
		updateContact.setAddress(address);
		dataInMemory.set((int) id, updateContact);		
	}

	/**
	 * Delete contact specified by contact argument from ArrayList.
	 * 
	 * @param contact	- contact to delete.
	 */
	@Override
	public void deleteContact(Contact contact) {
		dataInMemory.remove(contact);
		
	}

	/**
	 * Delete contacts specified by data argument from ArrayList.
	 * 
	 * @param data	- contacts to delete
	 */
	@Override
	public void deleteAllContacts(ArrayList<Contact> data) {
		dataInMemory.removeAll(data);		
	}
	
	/**
	 * Delete all contacts from ArrayList.
	 */
	@Override
	public void deleteAllContacts() {
		dataInMemory.clear();		
	}

	/**
	 * Get contact from ArrayList using id.
	 * 
	 * @param id	- id of contact.
	 * @return contact.
	 */
	@Override
	public Contact getContact(long id) {
		Contact contact = dataInMemory.get((int) id);
		return contact;
	}

	/**
	 * Get all contacts from ArrayList.
	 * 
	 * @return ArrayList of contacts.
	 */
	@Override
	public ArrayList<Contact> getAllContact() {
		return new ArrayList<Contact>(dataInMemory);
	}

	/**
	 * Get contacts from ArrayList specified by gender argument.
	 * 
	 * @param gender
	 * @return ArrayList of contacts.
	 */
	@Override
	public ArrayList<Contact> getAllContactsByGender(int gender) {
		ArrayList<Contact> contactsByGender = new ArrayList<Contact>();
		Iterator<Contact> iterator = dataInMemory.iterator();
		if (gender == 2) {
			contactsByGender = getAllContact();
		} else if (gender == 1) {
			while (iterator.hasNext()) {
				Contact contact = (Contact) iterator.next();
				if (contact.getIsMale()) {
					contactsByGender.add(contact);
				}
			}
		} else if (gender == 0) {
			while (iterator.hasNext()) {
				Contact contact = (Contact) iterator.next();
				if (!contact.getIsMale()) {
					contactsByGender.add(contact);
				}
			}
		}
		
		return contactsByGender;
	}

}
