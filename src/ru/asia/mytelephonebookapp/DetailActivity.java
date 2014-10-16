package ru.asia.mytelephonebookapp;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

	private ScrollView scrollDetail;
	private ImageView ivPhotoDetail;
	private TextView tvNameDetail;
	private TextView tvGender;
	private TextView tvDateOfBirth;
	private TextView tvAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		scrollDetail = (ScrollView) findViewById(R.id.scrollDetail);
		ivPhotoDetail = (ImageView) findViewById(R.id.ivPhotoDetail);
		tvNameDetail = (TextView) findViewById(R.id.tvNameDetail);
		tvGender = (TextView) findViewById(R.id.tvGender);
		tvDateOfBirth = (TextView) findViewById(R.id.tvDateofBirth);
		tvAddress = (TextView) findViewById(R.id.tvAddress);

		Intent intent = getIntent();
		long id = intent.getLongExtra("idContact", 0);
		Log.e("-------------", "ID from Intent" + id);
		if (id != 0) {
			Contact detailContact = MyTelephoneBookApplication.getDataSource()
					.getContact(id);
			byte[] photoArray = detailContact.getPhoto();
			ivPhotoDetail.setImageBitmap(BitmapFactory.decodeByteArray(
					photoArray, 0, photoArray.length));
			tvNameDetail.setText(detailContact.getName());
			setTitle(detailContact.getName());
			
			Colors colors = new Colors(this);
			
			if (detailContact.getIsMale()) {
				tvGender.setText(getResources().getString(R.string.str_male));
				scrollDetail.setBackgroundResource(colors.getMaleColorId());
			} else {
				tvGender.setText(getResources().getString(R.string.str_female));
				scrollDetail.setBackgroundResource(colors.getFemaleColorId());
			}
			
			String dateString = ContactsDataSource.formatDateToString(detailContact.getDateOfBirth());
			if (dateString == null) {
				tvDateOfBirth.setText("");
			} else {
				tvDateOfBirth.setText(dateString);
			}			
			tvAddress.setText(detailContact.getAddress());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		int id = item.getItemId();
		switch(id) {
		case R.id.action_edit:
			intent = getIntent();
			long idContact = intent.getLongExtra("idContact", 0);
			Intent editIntent = new Intent(this, AddEditActivity.class);
			editIntent.putExtra("idContact", idContact);
			startActivity(editIntent);	
			break;
		case R.id.action_settings:
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
