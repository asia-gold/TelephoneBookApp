package ru.asia.mytelephonebookapp.dataproviders;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Context;
import android.util.Log;

public class MemoryDataProvider implements DataProvider {
	
	private static final int READ = 1;
	private static final int WRITE = 2;
	
	private ArrayList<Contact> dataInMemory;
	private Context context;
	private String fileName = "dataInMemory";
	
	public MemoryDataProvider(Context context) {
		this.context = context;
		dataInMemory = new ArrayList<Contact>();
	}

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
		
		Log.e("addContact", "Id " + id);
		
		newContact.setId(id);
		dataInMemory.set((int) id, newContact);
		return id;
	}

	@Override
	public void addContact(Contact contact) {
		dataInMemory.add(contact);
		long id = (long)dataInMemory.indexOf(contact);
		
		Log.e("addContact(Contact)", "Id " + id);
		
		contact.setId(id);
		dataInMemory.set((int) id, contact);
		
	}

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

	@Override
	public void deleteContact(Contact contact) {
		dataInMemory.remove(contact);
		
	}

	@Override
	public void deleteAllContacts(ArrayList<Contact> data) {
		dataInMemory.removeAll(data);		
	}
	
	@Override
	public void deleteAllContacts() {
		dataInMemory.clear();		
	}

	@Override
	public Contact getContact(long id) {
		Contact contact = dataInMemory.get((int) id);
		return contact;
	}

	@Override
	public ArrayList<Contact> getAllContact() {
		return new ArrayList<Contact>(dataInMemory);
	}

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
		
		Log.e("MemoryDataProvider", "contactsByGender " + contactsByGender.toString());
		
		return contactsByGender;
	}


	
//	private static ArrayList<Contact> readFromFile(Context context, String fileName) throws IOException,
//    ClassNotFoundException {
//		FileInputStream fis = context.openFileInput(fileName);
//		ObjectInputStream ois = new ObjectInputStream(fis);
//		ArrayList<Contact> contacts = (ArrayList<Contact>)ois.readObject();
//		ois.close();
//		fis.close();
//		return contacts;
//	}
//	
//	private static void writeToFile(Context context, String fileName, ArrayList<Contact> data) throws IOException {
//		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
//		ObjectOutputStream oos = new ObjectOutputStream(fos);
//		oos.writeObject(data);
//		oos.close();
//		fos.close();
//	}

}
