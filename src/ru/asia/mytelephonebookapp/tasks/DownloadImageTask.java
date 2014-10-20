package ru.asia.mytelephonebookapp.tasks;

import java.io.InputStream;
import java.net.URL;

import ru.asia.mytelephonebookapp.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Downloads image from String url, returns the bitmap.
 * If error occurred, shows toast to inform user.
 * 
 * @author Asia
 *
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
	
	private Context context;
	private ImageView imageView;
	private ProgressDialog progressDialog;
	
	public DownloadImageTask(Context context, ImageView imageView) {
		this.context = context;
		this.imageView = imageView;
	}
	
	@Override
	protected void onPreExecute() {		
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Download Image From Network");
		progressDialog.setMessage("Loading Image...");
		progressDialog.show();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		
		String url = params[0];
		Bitmap bitmap = null;
		try {
			InputStream input = new URL(url).openStream();
			bitmap = BitmapFactory.decodeStream(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			imageView.setImageBitmap(result);
			progressDialog.dismiss();
		} else {
			progressDialog.dismiss();
			Toast.makeText(context, context.getResources().getString(R.string.str_error_download), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}

}
