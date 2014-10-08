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
		if (id != 0) {
			Contact detailContact = MyTelephoneBookApplication.getDataSource()
					.getContact(id);
			byte[] photoArray = detailContact.getPhoto();
			ivPhotoDetail.setImageBitmap(BitmapFactory.decodeByteArray(
					photoArray, 0, photoArray.length));
			tvNameDetail.setText(detailContact.getName());
			tvGender.setText(detailContact.getGender());
			Log.e("Gender", detailContact.getGender());
			if (detailContact.getGender().matches("Male")) {
				scrollDetail.setBackgroundResource(R.color.male);
			}
			tvDateOfBirth.setText(detailContact.getDateOfBirth());
			tvAddress.setText(detailContact.getAddress());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
		case R.id.action_edit:
			Intent intent = getIntent();
			long idContact = intent.getLongExtra("idContact", 0);
			Intent editIntent = new Intent(this, AddEditActivity.class);
			editIntent.putExtra("idContact", idContact);
			startActivity(editIntent);			
		}
		return super.onOptionsItemSelected(item);
	}
}
