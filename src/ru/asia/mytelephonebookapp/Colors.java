package ru.asia.mytelephonebookapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Colors {
	
	private static final int[] maleColors = { R.color.male_0, R.color.male_1 };	
	private static final int[] femaleColors = { R.color.female_0, R.color.female_1 };
	
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
		String defaultValue = context.getResources().getString(R.string.pref_colors_by_gender_default);
		String colorTheme = colorSettings.getString(
				"prefColorsByGender", defaultValue);
		if (colorTheme.matches(defaultValue)) {
			i = 0;			
		} else {
			i = 1; 
		}
		setMaleColorId(maleColors[i]);
		setFemaleColorId(femaleColors[i]);
	}
	
	private void setMaleColorId(int maleColor) {
		this.maleColor = maleColor;
	}
	
	private void setFemaleColorId(int femaleColor) {
		this.femaleColor = femaleColor;
	}
	
	public int getMaleColorId() {
		getColorsFromSettings();
		return maleColor;
	}
	
	public int getFemaleColorId() {
		getColorsFromSettings();
		return femaleColor;
	}

}
