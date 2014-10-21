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

/**
 * Main Activity of application. Show ListView of contacts.
 * 
 * @author Asia
 *
 */
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

		gender = getIntGender(genderSetting);
		data = MyTelephoneBookApplication.getDataProvider()
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
				} else {

					Intent intent = new Intent(MainActivity.this,
							DetailActivity.class);

					Contact cm = data.get(position);
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
	
	/**
	 * Check contacts filter by gender state.
	 * 
	 * @return          <code>true</code> if gender filtering mode was changed.
	 */
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
	
	/**
	 * Check color preference state.
	 * 
	 * @return          <code>true</code> if color preference state was changed.
	 */
	private boolean isColorSettingChange() {
		String colorSetting = settings.getString("prefColorsByGender",
				getResources().getString(R.string.pref_colors_by_gender_default));
		if (!colorTheme.matches(colorSetting)) {
			colorTheme = colorSetting;
			return true;
		}
		return false;
	}

	/**
	 * Check notify preference state.
	 * 
	 * @return          <code>true</code> if notify preference was changed.	 */
	private boolean isNotifyChange() {
		SharedPreferences sPref = getSharedPreferences("preferences", MODE_PRIVATE);
		notify = sPref.getBoolean("notify", false);
		if (notify) {
			Editor editor = sPref.edit();
			editor.putBoolean("notify", false);
			editor.commit();
			return true;
		}
		return false;
	}

	/**
	 * Update data of activity, adapter and invalidate options menu
	 * if one of the preferences has been changed.
	 */
	public void updateData() {
		if (isGenderSettingChange() || isColorSettingChange()
				|| isNotifyChange()) {
			data = MyTelephoneBookApplication.getDataProvider()
					.getAllContactsByGender(gender);
			
			Log.e("MainActivity", "data " + data.toString());
			
			adapter.updateAdapterData(data);
			invalidateOptionsMenu();
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

	/**
	 * Get int representation of String genderSettings argument.
	 * 
	 * @param genderSettings
	 * @return int representation of String gender.
	 */
	private int getIntGender(String genderSettings) {
		int genderInt = 2;
		if (genderSettings.matches(getResources().getString(R.string.str_male_only))) {
			genderInt = 1;
		} else if (genderSettings.matches(getResources().getString(R.string.str_female_only))) {
			genderInt = 0;
		} else {
			genderInt = 2;
		}
		return genderInt;
	}

	/**
	 * Customize activity for action mode.
	 */
	private void startRemoveListItemMode() {
		removeModeActive = true;
		removeMode = this
				.startActionMode(new RemoveListItemActionModeCallback());
		removeAdapter = new ContactAdapter(this, MyTelephoneBookApplication
				.getDataProvider().getAllContact(), true);
		lvContacts.setAdapter(removeAdapter);
		lvContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	/**
	 * Return default activity state. 
	 */
	private void endRemoveListItemMode() {
		removeModeActive = false;
		lvContacts.setAdapter(adapter);
		lvContacts.setChoiceMode(ListView.CHOICE_MODE_NONE);
		data = MyTelephoneBookApplication.getDataProvider()
				.getAllContactsByGender(gender);
		adapter.updateAdapterData(data);
		invalidateOptionsMenu();
	}

	/**
	 * Action mode for removing contacts from ListView.
	 * 
	 * @author Asia
	 *
	 */
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
						.getDataProvider().getAllContact();
				ArrayList<Contact> contactsToRemove = new ArrayList<Contact>();
				ContactAdapter adapter = (ContactAdapter) lvContacts
						.getAdapter();
				SparseBooleanArray checkedItems = adapter.getCheckedStates();

				for (int i = 0; i < allContacts.size(); ++i) {
					if (checkedItems.get(i)) {
						contactsToRemove.add(allContacts.get(i));
					}
				}
				MyTelephoneBookApplication.getDataProvider().deleteAllContacts(
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
