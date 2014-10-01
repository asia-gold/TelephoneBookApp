package ru.asia.mytelephonebookapp;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

	private LinearLayout linearLayout1;
	private ImageView ivPhotoDetail;
	private TextView tvNameDetail;
	private TextView tvGender;
	private TextView tvDateOfBirth;
	private TextView tvAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);


		linearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		tvNameDetail = (TextView) findViewById(R.id.tvNameDetail);
		tvGender = (TextView) findViewById(R.id.tvGender);
		tvDateOfBirth = (TextView) findViewById(R.id.tvDateofBirth);
		tvAddress = (TextView) findViewById(R.id.tvAddress);

		Intent intent = getIntent();
		long id = intent.getLongExtra("idContact", 1L);
		if (id != 1L) {
			Contact detailContact = MyTelephoneBookApplication.getDataSource().getContact(id);

			tvNameDetail.setText(detailContact.getName());
			tvGender.setText(detailContact.getGender());
			if (detailContact.getGender() == "Male") {
				linearLayout1.setBackgroundResource(R.color.male);
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
