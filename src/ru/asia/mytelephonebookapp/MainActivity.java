package ru.asia.mytelephonebookapp;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private ImageView ivSplash;
	private ListView lvContacts;

	private ContactAdapter adapter;
	private ArrayList<Contact> data;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Sets default values if app launched for the first time
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		//ivSplash = (ImageView) findViewById(R.id.ivSplash);
		lvContacts = (ListView) findViewById(R.id.lvContacts);

		Contact cm1 = new Contact("Ivanov Ivan", "Male");
		Contact cm2 = new Contact("Bennet Jane", "Female");
		Contact cm3 = new Contact("Petrov Petr", "Male");

		data = new ArrayList<>();
		data.add(cm1);
		data.add(cm2);
		data.add(cm3);
		
		adapter = new ContactAdapter(this, data);
		lvContacts.setAdapter(adapter);
		
		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						DetailActivity.class);

				Contact cm = data.get(position);
				String name = cm.getName();
				String gender = cm.getGender();

				intent.putExtra("name", name);
				intent.putExtra("gender", gender);
				startActivity(intent);
			}
		});
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
			break;
		case R.id.action_settings:
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.action_import_export:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
