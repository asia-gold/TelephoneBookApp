package ru.asia.mytelephonebookapp.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import ru.asia.mytelephonebookapp.ContactDBHelper;
import ru.asia.mytelephonebookapp.ContactsUtils;
import ru.asia.mytelephonebookapp.MyTelephoneBookApplication;
import ru.asia.mytelephonebookapp.R;
import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Xml;
import android.widget.Toast;

/**
 * Export contacts to xml file on SD card. If error occurred show toast to inform user.
 * @author Asia
 *
 */
public class DataContactsExportTask extends AsyncTask<Void, Void, Boolean> {


	private Context context;

	private static final String EXPORT_FILE_NAME = "/database.xml";

	public DataContactsExportTask(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		ArrayList<Contact> contactsList = MyTelephoneBookApplication
				.getDataProvider().getAllContact();

		String xmlString = writeXML(contactsList);
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					xmlString)));
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);

			if (isExternalStorageWritable()) {
				File file = new File(context.getExternalFilesDir(null), EXPORT_FILE_NAME);
				file.createNewFile();
				file.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				StreamResult result = new StreamResult(fos);
				transformer.transform(source, result);
				fos.close();
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	protected void onPostExecute(Boolean result) {
		String message = null;
		if (result) {
			message = context.getResources()
					.getString(R.string.str_export_result_true);
		} else {
			message = context.getResources().getString(
					R.string.str_export_result_false);
		}
		showToast(message);
	}

	/**
	 * Serialize ArrayList of contacts to String.
	 * 
	 * @param contacts	- ArrayList for serialization
	 * @return String representation of ArrayList.
	 */
	private String writeXML(ArrayList<Contact> contacts) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();		
		
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "database");
			serializer.attribute("", "name", ContactDBHelper.DATABASE_NAME);
			serializer.startTag("", "table");
			serializer.attribute("", "name", ContactDBHelper.TABLE_NAME);
			for (Contact contact : contacts) {
				serializer.startTag("", "contact");
				// ID
				serializer.startTag("", "id");
				serializer.text(String.valueOf(contact.getId()));
				serializer.endTag("", "id");
				// Photo
				String photo = Base64.encodeToString(contact.getPhoto(), 1);
				serializer.startTag("", "photo");
				serializer.text(photo);
				serializer.endTag("", "photo");
				// Name
				serializer.startTag("", "name");
				serializer.text(contact.getName());
				serializer.endTag("", "name");
				// Date of Birth
				String dateString = ContactsUtils.formatDateToString(contact.getDateOfBirth());
				
				serializer.startTag("", "dateOfBirth");
				serializer.text(dateString);
				serializer.endTag("", "dateOfBirth");
				// Gender
				serializer.startTag("", "isMale");
				serializer.text(String.valueOf(contact.getIsMale()));
				serializer.endTag("", "isMale");
				// Address
				serializer.startTag("", "address");
				serializer.text(contact.getAddress());
				serializer.endTag("", "address");
				
				serializer.endTag("", "contact");
			}
			serializer.endTag("", "table");
			serializer.endTag("", "database");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Check external storage state.
	 * 
	 * @return          <code>true</code> if external storage writable.
	 */
	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		showToast(context.getResources().getString(R.string.str_no_sd_card));
		return false;
	}
	
	/**
	 * Inform user.
	 * 
	 * @param message
	 */
	private void showToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
