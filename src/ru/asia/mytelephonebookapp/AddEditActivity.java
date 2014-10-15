package ru.asia.mytelephonebookapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import ru.asia.mytelephonebookapp.models.Contact;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEditActivity extends ActionBarActivity {

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_SELECT = 2;

	private static final String[] urlImages = {
			"http://img1.wikia.nocookie.net/__cb20101014052403/en.futurama/images/4/45/Dr._John_A._Zoidberg.png",
			"http://upload.wikimedia.org/wikipedia/ru/9/97/Philip_J._Fry.png",
			"http://upload.wikimedia.org/wikipedia/ru/archive/d/d4/20110107211840!Turanga_Leela.png",
			"http://upload.wikimedia.org/wikipedia/en/thumb/0/0f/FuturamaProfessorFarnsworth.png/175px-FuturamaProfessorFarnsworth.png",
			"https://do4a.com/data/MetaMirrorCache/080941ca2c2790d3ad0c96ceb3abd3fc.png",
			"http://upload.wikimedia.org/wikipedia/en/f/fd/FuturamaAmyWong.png",
			"http://fc00.deviantart.net/fs71/i/2013/031/2/7/bender_bending_rodriguez_by_car0003-d5tdyps.png" };

	private Context context = this;

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
		
		setTitle("Add contact");

		Intent intent = getIntent();
		long id = intent.getLongExtra("idContact", 0);

		pm = getPackageManager();

		calendar = Calendar.getInstance();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		ivPhotoAddEdit = (ImageView) findViewById(R.id.ivPhotoAddEdit);
		etName = (EditText) findViewById(R.id.etName);
		spGender = (Spinner) findViewById(R.id.spGender);
		etDateOfBirth = (EditText) findViewById(R.id.etDateOfBirth);
		etAddress = (EditText) findViewById(R.id.etAddress);

		ivPhotoAddEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				DialogFragment dialog = new DialogFragment() {

					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setTitle(R.string.title_add_photo).setItems(
								R.array.array_add_photo,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case 0:
											takePhotoByCamera();
											break;
										case 1:
											takePhotoFromGallery();
											break;
										case 2:
											Random random = new Random();
											new DownloadImageTask(context,
													ivPhotoAddEdit).execute(urlImages[random
													.nextInt(urlImages.length)]);
										}
									}
								});
						return builder.create();
					}
				};
				dialog.show(getFragmentManager(), "dialog");
			}
		});

		etDateOfBirth.setClickable(true);
		etDateOfBirth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				DatePickerDialog datePicker = new DatePickerDialog(
						AddEditActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view,
									int selectedYear, int monthOfYear,
									int dayOfMonth) {

								year = selectedYear;
								month = monthOfYear;
								day = dayOfMonth;

								calendar.set(Calendar.YEAR, selectedYear);
								calendar.set(Calendar.MONTH, monthOfYear);
								calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

								String dateFormat = "dd/MM/yyyy";
								SimpleDateFormat sdf = new SimpleDateFormat(
										dateFormat, Locale.ENGLISH);
								etDateOfBirth.setText(sdf.format(calendar
										.getTime()));
							}
						}, year, month, day);
				datePicker.show();
			}
		});

		if (savedInstanceState == null) {
			if (id != 0) {
				Contact editContact = MyTelephoneBookApplication
						.getDataSource().getContact(id);
				byte[] photoArray = editContact.getPhoto();
				ivPhotoAddEdit.setImageBitmap(BitmapFactory.decodeByteArray(
						photoArray, 0, photoArray.length));
				etName.setText(editContact.getName());
				setTitle("Edit contact " + editContact.getName());

				setSpinnerGenderSelection(editContact.getIsMale());		
				
				Date date = editContact.getDateOfBirth();
				String dateString = ContactsDataSource.formatDateToString(date);
				if (dateString.matches("")) {
					etDateOfBirth.setText("");
				} else {
					etDateOfBirth.setText(dateString);
					calendar.setTime(date);
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);
				}
				etAddress.setText(editContact.getAddress());
			}
		} else {
			byte[] photoArray = savedInstanceState
					.getByteArray("ivPhotoAddEdit");
			String name = savedInstanceState.getString("etName");
			boolean isMale = savedInstanceState.getBoolean("spGender");
			String dateOfBirth = savedInstanceState.getString("etDateOfBirth");

			if (dateOfBirth == null) {
				etDateOfBirth.setText("");
			} else {
				etDateOfBirth.setText(dateOfBirth);
				Date date = null;
				try {
					date = ContactsDataSource.formatStringToDate(dateOfBirth);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date != null) {
					calendar.setTime(date);
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);
				}
			}
			String address = savedInstanceState.getString("etAddress");

			ivPhotoAddEdit.setImageBitmap(BitmapFactory.decodeByteArray(
					photoArray, 0, photoArray.length));
			etName.setText(name);
			setSpinnerGenderSelection(isMale);
			etAddress.setText(address);
		}
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putByteArray("ivPhotoAddEdit", getByteArrayFromImageView());
		outState.putString("etName", etName.getText().toString());		
		String gender = spGender.getSelectedItem().toString();		
		outState.putBoolean("spGender", getBooleanFromString(gender));
		outState.putString("etDateOfBirth", etDateOfBirth.getText().toString());
		outState.putString("etAddress", etAddress.getText().toString());

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_edit, menu);
		return true;
	}

	private byte[] getByteArrayFromImageView() {
		Bitmap photo = ((BitmapDrawable) ivPhotoAddEdit.getDrawable())
				.getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.PNG, 0, bos);
		byte[] photoArray = bos.toByteArray();
		return photoArray;
	}

	private void setSpinnerGenderSelection(boolean isMale) {
		if (isMale) {
			spGender.setSelection(0);
		} else {
			spGender.setSelection(1);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_done) {

			byte[] photoArray = getByteArrayFromImageView();
			String name = etName.getText().toString();
			
			if (name.matches("")) {
				Toast.makeText(context, getResources().getString(R.string.str_no_name_typed), Toast.LENGTH_LONG).show();
				return false;
			}
			
			String gender = spGender.getSelectedItem().toString();
			boolean isMale = getBooleanFromString(gender);
			String dateString = etDateOfBirth.getText().toString();
			Date dateBirth = null;
			try {
				dateBirth = ContactsDataSource.formatStringToDate(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String address = etAddress.getText().toString();

			Intent editIntent = getIntent();
			long idContact = editIntent.getLongExtra("idContact", 0);

			if (idContact != 0) {
				MyTelephoneBookApplication.getDataSource()
						.updateContact(idContact, photoArray, name, isMale,
								dateBirth, address);
			} else {

				idContact = MyTelephoneBookApplication.getDataSource()
						.addContact(photoArray, name, isMale, dateBirth,
								address);
			}
			MyTelephoneBookApplication.getAdapter().notifyDataSetChanged();
			Intent intent = new Intent(this, DetailActivity.class);
			intent.putExtra("idContact", (long) idContact);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean getBooleanFromString(String gender){
		boolean isMale = false;
		if (gender.matches(getResources().getString(R.string.str_male))) {
			isMale = true;
		}	
		return isMale;
	}

	private void takePhotoByCamera() {
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
		File image = File.createTempFile(imageFile, ".jpg", storageDir);
		// Save a file: path for use with ACTION_VIEW intents
		currentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE
				&& currentPhotoPath != null) {
			setPic();
			// galleryAddPicture();
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
		int targetWidth = (int) getResources().getDimension(
				R.dimen.iv_photo_add_width);
		int targetHeight = (int) getResources().getDimension(
				R.dimen.iv_photo_add_height);

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

	// private void galleryAddPicture() {
	// Intent mediaScanIntent = new Intent(
	// Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	// File file = new File(currentPhotoPath);
	// Uri contentUri = Uri.fromFile(file);
	// mediaScanIntent.setData(contentUri);
	// this.sendBroadcast(mediaScanIntent);
	// }

	private void takePhotoFromGallery() {
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
