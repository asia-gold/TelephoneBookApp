package ru.asia.mytelephonebookapp;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	
	private static final long SPLASH_TIME = 2000;
	private static final int SPLASH_STOP = 0;
	
	private ImageView ivSplash;
	private ListView lvContacts;
	
	private Handler splashHandler;
	private ContactAdapter adapter;
	private ArrayList<Contact> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Sets default values if app launched for the first time
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		ivSplash = (ImageView) findViewById(R.id.ivSplash);
		lvContacts = (ListView) findViewById(R.id.lvContacts);
		splashHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SPLASH_STOP:
					ivSplash.setVisibility(View.GONE);
					lvContacts.setVisibility(View.VISIBLE);
					break;
				}
				super.handleMessage(msg);
			}
		};
		Message message = new Message();
		message.what = SPLASH_STOP;
		splashHandler.sendMessageDelayed(message, SPLASH_TIME);
		
		
		Contact cm1 = new Contact("Ivanov", "Ivan", "Male");
		Contact cm2 = new Contact("Bennet", "Jane", "Female");
		Contact cm3 = new Contact("Petrov", "Petr", "Male");
		
		data = new ArrayList<>();
		data.add(cm1);
		data.add(cm2);
		data.add(cm3);		
		//lvContacts = (ListView) findViewById(R.id.lvContacts);
		adapter = new ContactAdapter(this, data);
		lvContacts.setAdapter(adapter);
		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
				
				Contact cm = data.get(position);
				String surname = cm.getSurname();
				String name = cm.getName();
				String gender = cm.getGender();
				
				intent.putExtra("surname", surname);
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
