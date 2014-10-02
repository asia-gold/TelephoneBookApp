package ru.asia.mytelephonebookapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.asia.mytelephonebookapp.models.Contact;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddEditActivity extends ActionBarActivity {

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_SELECT = 2;

	private PackageManager pm;
	
	private Calendar calendar;
	
	private int year;
	private int month;
	private int day;

	private ImageView ivPhotoAddEdit;
	private EditText etName;
	private Spinner spGender;
	private EditText etDateOfBirth;
	private EditText etAddress;

	private String currentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit);

		pm = getPackageManager();
		
		calendar = Calendar.getInstance();
		
		year = calendar.get(Calendar.YEAR); 
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		ivPhotoAddEdit = (ImageView) findViewById(R.id.ivPhotoAddEdit);
		etName = (EditText) findViewById(R.id.etName);
		spGender = (Spinner) findViewById(R.id.spGender);
		etDateOfBirth = (EditText) findViewById(R.id.etDateOfBirth);
		etDateOfBirth.setClickable(true);
		etDateOfBirth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {				
				
				DatePickerDialog datePicker = new DatePickerDialog(
						AddEditActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int selectedYear,
									int monthOfYear, int dayOfMonth) {
								
								year = selectedYear; 
								month = monthOfYear;
								day = dayOfMonth;
								
								calendar.set(Calendar.YEAR, selectedYear);
								calendar.set(Calendar.MONTH, monthOfYear); 
								calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								
								String dateFormat = "dd/MM/yyyy";
								SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
								etDateOfBirth.setText(sdf.format(calendar.getTime()));
							}
						}, year, month, day);
				datePicker.show();
			}
		});

		etAddress = (EditText) findViewById(R.id.etAddress);

		registerForContextMenu(ivPhotoAddEdit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_done) {
			String name = etName.getText().toString();
			String gender = spGender.getSelectedItem().toString();
			String dateBirth = etDateOfBirth.getText().toString();
			String address = etAddress.getText().toString();
			long contactId = MyTelephoneBookApplication.getDataSource()
					.addContact(name, gender, dateBirth, address);
			Contact newContact = MyTelephoneBookApplication.getDataSource()
					.getContact(contactId);
			MyTelephoneBookApplication.getAdapter().notifyDataSetChanged();
			Intent intent = new Intent(this, DetailActivity.class);
			intent.putExtra("idContact", (long) newContact.getId());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		if (view.getId() == R.id.ivPhotoAddEdit) {
			getMenuInflater().inflate(R.menu.photo_menu, menu);
			if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
				menu.removeItem(R.id.action_photo_by_camera);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_photo_by_camera:
			photoByCamera();
			return true;
		case R.id.action_photo_from_gallery:
			photoFromGallery();
			return true;
		case R.id.action_photo_from_network:
		default:
			return super.onContextItemSelected(item);
		}

	}

	private void photoByCamera() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
				currentPhotoPath = photoFile.getAbsolutePath();
			} catch (IOException e) {
				// Error occurred while creating the File
				e.printStackTrace();
				photoFile = null;
				currentPhotoPath = null;
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss")
				.format(new Date());
		String imageFile = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFile, ".jgp", storageDir);
		// Save a file: path for use with ACTION_VIEW intents
		currentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE
				&& currentPhotoPath != null) {
			setPic();
			galleryAddPicture();
			currentPhotoPath = null;
		}
		if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_SELECT) {
			Uri selectedImageUri = data.getData();
			currentPhotoPath = getPath(selectedImageUri);
			setPic();
		}
	}

	private void setPic() {
		// Get the dimensions of the View
		int targetWidth = ivPhotoAddEdit.getWidth();
		int targetHeight = ivPhotoAddEdit.getHeight();

		// Get the dimension of the bitmap
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(currentPhotoPath, bfOptions);
		int photoWidth = bfOptions.outWidth;
		int photoHeigth = bfOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoWidth / targetWidth, photoHeigth
				/ targetHeight);

		// Decode the image file into a Bitmap sized to fill the View
		bfOptions.inJustDecodeBounds = false;
		bfOptions.inSampleSize = scaleFactor;
		bfOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bfOptions);
		ivPhotoAddEdit.setImageBitmap(bitmap);
	}

	private void galleryAddPicture() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(currentPhotoPath);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private void photoFromGallery() {
		Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
		galleryIntent.setType("image/*");
		startActivityForResult(
				Intent.createChooser(galleryIntent, "Select Picture"),
				REQUEST_IMAGE_SELECT);
	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.getContentResolver().query(uri, projection, null,
				null, null);
		int columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(columnIndex);
	}
}
