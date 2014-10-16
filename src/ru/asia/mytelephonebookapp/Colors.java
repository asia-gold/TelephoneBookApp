package ru.asia.mytelephonebookapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class Colors {
	
	private static final int[] maleColors = { R.color.male_blue, R.color.male_violet };	
	private static final int[] femaleColors = { R.color.female_pink, R.color.female_yellow };
	
	private int maleColor;
	private int femaleColor;
	
	private SharedPreferences colorSettings;
	
	private Context context;
	
	public Colors(Context context) {
		this.context = context;
	}
	
	private void getColorsFromSettings() {
		int i = 0;
		colorSettings = PreferenceManager.getDefaultSharedPreferences(context);
		String colorTheme = colorSettings.getString(
				"prefColorsByGender", "Blue/Pink");
		Log.e("Colors Preferences", colorSettings.getString(
				"prefColorsByGender", "Blue/Pink"));
		if (colorTheme.matches("Blue/Pink")) {
			i = 0;			
		} else {
			i = 1; 
		}
		setMaleColor(maleColors[i]);
		setFemaleColor(femaleColors[i]);
	}
	
	private void setMaleColor(int maleColor) {
		this.maleColor = maleColor;
	}
	
	private void setFemaleColor(int femaleColor) {
		this.femaleColor = femaleColor;
	}
	
	public int getMaleColor() {
		getColorsFromSettings();
		return maleColor;
	}
	
	public int getFemaleColor() {
		getColorsFromSettings();
		return femaleColor;
	}

}
