package ru.asia.mytelephonebookapp;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.dialogs.ImportExportDialog;
import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private Context context = this;
	private ListView lvContacts;
	private TextView tvEmpty;

	public ContactAdapter adapter;
	private ContactAdapter removeAdapter;
	private ArrayList<Contact> data;

	private ActionMode removeMode;
	private boolean removeModeActive = false;

	private SharedPreferences settings;
	private int gender = 2;
	private String colorTheme = "Blue/Pink";
	private boolean notify = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		settings = PreferenceManager.getDefaultSharedPreferences(context);
		String genderSetting = settings.getString("prefDisplayByGender",
				getResources().getString(R.string.pref_display_by_gender_default));
		Log.e("SharedPreferences",
				settings.getString("prefDisplayByGender", "Show all"));

		gender = getIntGender(genderSetting);
		data = MyTelephoneBookApplication.getDataSource()
				.getAllContactsByGender(gender);

		lvContacts = (ListView) findViewById(R.id.lvContacts);
		tvEmpty = (TextView) findViewById(R.id.tvEmpty);
		tvEmpty.setText(R.string.str_empty_view);
		lvContacts.setEmptyView(tvEmpty);

		adapter = new ContactAdapter(this, data, false);
		removeAdapter = new ContactAdapter(this, data, true);
		lvContacts.setAdapter(adapter);

		lvContacts.setItemsCanFocus(false);

		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (removeModeActive == true) {
					CheckBox checkBox = (CheckBox) view
							.findViewById(R.id.chbRemove);
					checkBox.setChecked(!checkBox.isChecked());
					// removeAdapter.changeState(position);
				} else {

					Intent intent = new Intent(MainActivity.this,
							DetailActivity.class);

					Contact cm = data.get(position);
					Log.e("------------", "Id item click " + cm.getId());
					intent.putExtra("idContact", cm.getId());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		updateData();
		super.onResume();
	}

	private boolean isGenderSettingChange() {
		String genderSetting = settings.getString("prefDisplayByGender",
				getResources().getString(R.string.pref_display_by_gender_default));
		int newGender = getIntGender(genderSetting);
		if (gender != newGender) {
			gender = newGender;
			return true;
		}
		return false;
	}

	private boolean isColorSettingChange() {
		String colorSetting = settings.getString("prefColorsByGender",
				getResources().getString(R.string.pref_colors_by_gender_default));
		if (!colorTheme.matches(colorTheme)) {
			colorTheme = colorSetting;
			return true;
		}
		return false;

	}

	private boolean isNotifyChange() {
		SharedPreferences sPref = getSharedPreferences("preferences", MODE_PRIVATE);
		notify = sPref.getBoolean("notify", false);
		if (notify) {
			Log.e("SharedPreferences", "Notify " + notify);
			settings = getPreferences(MODE_PRIVATE);
			Editor editor = settings.edit();
			editor.putBoolean("notify", false);
			editor.commit();
			adapter.updateAdapterData(data);
			invalidateOptionsMenu();
			return true;
		}
		return false;
	}

	public void updateData() {
		if (isGenderSettingChange() || isColorSettingChange()
				|| isNotifyChange()) {
			data = MyTelephoneBookApplication.getDataSource()
					.getAllContactsByGender(gender);
			adapter.updateAdapterData(data);
			invalidateOptionsMenu();
			Log.e("Main", "Update data ");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		if (adapter.isEmpty()) {
			menu.removeItem(R.id.action_remove);
		}
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
			new ImportExportDialog().show(getFragmentManager(), "dialog");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private int getIntGender(String genderSettings) {
		int genderInt = 2;
		if (genderSettings.matches("Show male only")) {
			genderInt = 1;
		} else if (genderSettings.matches("Show female only")) {
			genderInt = 0;
		} else {
			genderInt = 2;
		}
		return genderInt;
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
		lvContacts.setAdapter(adapter);
		lvContacts.setChoiceMode(ListView.CHOICE_MODE_NONE);
		data = MyTelephoneBookApplication.getDataSource()
				.getAllContactsByGender(gender);
		adapter.updateAdapterData(data);
		invalidateOptionsMenu();
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
				actionMode.finish();
				break;

			case R.id.action_cancel:
				actionMode.finish();
				break;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			endRemoveListItemMode();
		}
	}
}
