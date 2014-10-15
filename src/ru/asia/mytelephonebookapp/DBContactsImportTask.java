package ru.asia.mytelephonebookapp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class DBContactsImportTask extends AsyncTask<Void, Void, ArrayList<Contact>> {

	private static final String IMPORT_FILE_NAME = "/database.xml";

	private Context context;
	private InputSource inputSource;
	private ArrayList<Contact> contacts;

	public DBContactsImportTask(Context context) {
		this.context = context;
	}

	@Override
	protected ArrayList<Contact> doInBackground(Void... params) {

		File sdCard = Environment.getExternalStorageDirectory();
		File importFile = new File(sdCard, IMPORT_FILE_NAME);
		contacts = new ArrayList<Contact>();

		FileInputStream xmlFileiInput = null;
		try {
			xmlFileiInput = new FileInputStream(importFile);
			inputSource = new InputSource(xmlFileiInput);

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();

			XMLContentHandler handler = new XMLContentHandler();
			reader.setContentHandler(handler);

			reader.parse(inputSource);

			contacts = handler.getParsedData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}

	@Override
	protected void onPostExecute(ArrayList<Contact> result) {
		String message = null;
		MyTelephoneBookApplication.getDataSource().deleteAllContacts();
		if (result == null) {
			message = context.getResources()
					.getString(R.string.str_import_result_false);
		} else {
			for (Contact contact : result) {
				MyTelephoneBookApplication.getDataSource().addContact(contact);
			}
			MyTelephoneBookApplication.getAdapter().notifyDataSetChanged();
			((MainActivity)context).updateData();
			message = context.getResources()
					.getString(R.string.str_import_result_true);
		}
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
