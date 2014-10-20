package ru.asia.mytelephonebookapp;

import java.util.ArrayList;

import ru.asia.mytelephonebookapp.models.Contact;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Custom adapter for ListView. Inflates custom list item layout.
 * 
 * @author Asia
 *
 */
public class ContactAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Contact> contactsList;
	private static LayoutInflater inflater;
	private SparseBooleanArray checkedStates;
	private boolean isRemove;

	public ContactAdapter(Context contex, ArrayList<Contact> data,
			boolean isRemove) {
		this.context = contex;
		contactsList = data;
		this.isRemove = isRemove;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		checkedStates = new SparseBooleanArray(contactsList.size());
	}
	
	public SparseBooleanArray getCheckedStates() {
		return checkedStates;
	}

	/**
	 * Set new @param data to contactsList, notify adapter data has changed.
	 * 
	 * @param data
	 */
	public void updateAdapterData(ArrayList<Contact> data) {
		contactsList.clear();
		contactsList.addAll(data);
		this.notifyDataSetChanged();
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
	
//	public boolean isChecked(int position) {
//		return checkedStates.get(position, false);
//	}
//
//	public void setChecked(int position, boolean isChecked) {
//		checkedStates.put(position, isChecked);
//	}
//	
//	public void changeState(int position) {
//		setChecked(position, !isChecked(position));
//	}
	
	static class ViewHolder {
		public LinearLayout llItem;
		public ImageView ivPhoto;
		public TextView tvName;
		public CheckBox chbRemove;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			if (isRemove) {
				view = inflater.inflate(R.layout.item_remove, parent, false);
				ViewHolder removeHolder = new ViewHolder();
				removeHolder.llItem = (LinearLayout) view
						.findViewById(R.id.llItem);
				removeHolder.ivPhoto = (ImageView) view
						.findViewById(R.id.ivPhoto);
				removeHolder.tvName = (TextView) view.findViewById(R.id.tvName);
				removeHolder.chbRemove = (CheckBox) view
						.findViewById(R.id.chbRemove);
				view.setTag(removeHolder);
			} else {
				view = inflater.inflate(R.layout.item, parent, false);
				ViewHolder holder = new ViewHolder();
				holder.llItem = (LinearLayout) view.findViewById(R.id.llItem);
				holder.ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
				holder.tvName = (TextView) view.findViewById(R.id.tvName);
				view.setTag(holder);
			}
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();

		Contact tmpValue = (Contact) contactsList.get(position);
		
		Colors colors = new Colors(context);

		if (tmpValue.getIsMale()) {
			viewHolder.llItem.setBackgroundResource(colors.getMaleColorId());
		} else {
			viewHolder.llItem.setBackgroundResource(colors.getFemaleColorId());
		}
		
		byte[] photoArray = tmpValue.getPhoto();
		viewHolder.ivPhoto.setImageBitmap(BitmapFactory.decodeByteArray(
				photoArray, 0, photoArray.length));
		viewHolder.tvName.setText(tmpValue.getName());
		
		if (isRemove == true) {
			viewHolder.chbRemove.setTag(position);
			viewHolder.chbRemove.setChecked(checkedStates.get(position, false));
			viewHolder.chbRemove.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton button, boolean isChecked) {
					checkedStates.put((Integer)button.getTag(), isChecked);					
				}
			});
		}
		return view;
	}
}
