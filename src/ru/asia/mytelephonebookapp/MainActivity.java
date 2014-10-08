package ru.asia.mytelephonebookapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.asia.mytelephonebookapp.models.Contact;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private Context context = this;
	private ListView lvContacts;

	// private ContactAdapter adapter;
	private ContactAdapter removeAdapter;
	private ArrayList<Contact> data;

	private ActionMode removeMode;
	private boolean removeModeActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Sets default values if app launched for the first time
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		data = MyTelephoneBookApplication.getDataSource().getAllContact();

		// SharedPreferences sharedPreferences = PreferenceManager
		// .getDefaultSharedPreferences(this);
		// String gender = sharedPreferences.getString(
		// "prefDisplayByGender",
		// getResources().getString(
		// R.string.pref_display_by_gender_default));
		// data = MyTelephoneBookApplication.getDataSource()
		// .getAllContactsByGender(gender);

		lvContacts = (ListView) findViewById(R.id.lvContacts);

		// adapter = new ContactAdapter(this, data, false);
		removeAdapter = new ContactAdapter(this, data, true);
		lvContacts.setAdapter(MyTelephoneBookApplication.getAdapter());

		lvContacts.setItemsCanFocus(false);

		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MyTelephoneBookApplication.getAdapter().changeState(position);

				Intent intent = new Intent(MainActivity.this,
						DetailActivity.class);

				Contact cm = data.get(position);
				Log.e("------------", "Id item click " + cm.getId());
				intent.putExtra("idContact", cm.getId());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		data = MyTelephoneBookApplication.getDataSource().getAllContact();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.action_add:
			intent.setClass(this, AddEditActivity.class);
			startActivity(intent);
			break;
		case R.id.action_remove:
			startRemoveListItemMode();
			break;
		case R.id.action_settings:
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.action_import_export:
			// new dialog
			final Dialog importExportDialog = new Dialog(context,
					R.style.CustomDialogTheme);
			importExportDialog.setContentView(R.layout.import_export_dialog);

			Button btnImport = (Button) importExportDialog
					.findViewById(R.id.btnImport);
			Button btnExport = (Button) importExportDialog
					.findViewById(R.id.btnExport);

			btnImport.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// is there file?
					importExportDialog.dismiss();
				}
			});

			btnExport.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					new DBContactsExportTask(context).execute();
					importExportDialog.dismiss();
				}
			});
			importExportDialog.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startRemoveListItemMode() {
		removeModeActive = true;
		removeMode = this
				.startActionMode(new RemoveListItemActionModeCallback());
		removeAdapter = new ContactAdapter(this, MyTelephoneBookApplication
				.getDataSource().getAllContact(), true);
		lvContacts.setAdapter(removeAdapter);
		lvContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	private void endRemoveListItemMode() {
		removeModeActive = false;
		lvContacts.setAdapter(MyTelephoneBookApplication.getAdapter());
		lvContacts.setChoiceMode(ListView.CHOICE_MODE_NONE);
		MyTelephoneBookApplication.getAdapter().notifyDataSetChanged();
	}

	private class RemoveListItemActionModeCallback implements
			ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
			actionMode.getMenuInflater().inflate(R.menu.remove_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode actionMode, MenuItem menu) {
			switch (menu.getItemId()) {
			case R.id.action_delete:
				ArrayList<Contact> allContacts = MyTelephoneBookApplication
						.getDataSource().getAllContact();
				ArrayList<Contact> contactsToRemove = new ArrayList<Contact>();
				ContactAdapter adapter = (ContactAdapter) lvContacts
						.getAdapter();
				SparseBooleanArray checkedItems = adapter.getCheckedStates();

				for (int i = 0; i < allContacts.size(); ++i) {
					if (checkedItems.get(i)) {
						Log.e("----------------", "checked items: "
								+ checkedItems.get(i));
						contactsToRemove.add(allContacts.get(i));
					}
				}
				Log.e("----------------", "Contact to remove: "
						+ contactsToRemove.toString());
				MyTelephoneBookApplication.getDataSource().deleteAllContacts(
						contactsToRemove);
				adapter.notifyDataSetChanged();
				actionMode.finish();
				break;
			case R.id.action_cancel:
				actionMode.finish();
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			endRemoveListItemMode();
		}
	}
}
