package ru.asia.mytelephonebookapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.asia.mytelephonebookapp.models.Contact;
import android.util.Base64;

public class XMLContentHandler extends DefaultHandler{
	
	private boolean inContact = false;
	
	private StringBuilder builder = new StringBuilder();
	private Contact contact = new Contact();
	private ArrayList<Contact> listOfContact = new ArrayList<Contact>();
	
	/**
	 * Called when parsed data is requested.
	 */
	public ArrayList<Contact> getParsedData() {
		return listOfContact;
	}
	
	/**
	 * Receive notification of the start of an element.
	 * 
	 * Called in opening tag <contact>
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (localName.equals("contact")) {
			contact = new Contact();
			inContact = true;
		}
	}
	
	/**
	 * Receive notification of the end of an element.
	 * 
	 * Called in end tag </contact>
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if (inContact == true && localName.equals("contact")) {
			listOfContact.add(contact);
			inContact = false;
		} else if (inContact == true && localName.equals("id")) {
			contact.setId(Long.valueOf(builder.toString().trim()));
		} else if (inContact == true && localName.equals("photo")) {
			String photo = builder.toString().trim();
			byte[] photoArray = Base64.decode(photo, 1);
			contact.setPhoto(photoArray);
		} else if (inContact == true && localName.equals("name")) {
			contact.setName(builder.toString().trim());
		} else if (inContact == true && localName.equals("dateOfBirth")) {
			Date date = null;
			try {
				date = ContactsDataSource.formatStringToDate(builder.toString().trim());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			contact.setDateOfBirth(date);
		} else if (inContact == true && localName.equals("gender")) {
			contact.setGender(builder.toString().trim());
		} else if (inContact == true && localName.equals("address")) {
			contact.setAddress(builder.toString().trim());
		}
		builder.setLength(0);
	}
	
	/**
	 * Receive notification of character data inside an element
	 * 
	 * Called on the following structure <tag>characters</tag>
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		builder.append(ch, start, length);
	}
}
