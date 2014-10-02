package ru.asia.mytelephonebookapp;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.models.Contact;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Contact> contactsList;
	private static LayoutInflater inflater;
	
	public ContactAdapter(Context contex, ArrayList<Contact> data) {
		this.context = contex;
		contactsList = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void notifyDataSetChanged() {
		contactsList = MyTelephoneBookApplication.getDataSource().getAllContact();
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return contactsList.size();
	}

	@Override
	public Object getItem(int position) {
		return contactsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	// Holder works faster
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.item, parent, false);
		}
		
		LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llItem);
		ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		
		Contact tmpValue = (Contact) contactsList.get(position);
		
		if (tmpValue.getGender().matches("Male")) {
			llItem.setBackgroundResource(R.color.male);
		}
		tvName.setText(tmpValue.getName());
		
		return view;
	}

}
