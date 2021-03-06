package ru.asia.mytelephonebookapp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Activity with settings. Contacts filter and color settings.
 * 
 * @author Asia
 *
 */
public class SettingsActivity extends PreferenceActivity {
	
	public static final String KEY_PREF_DISPLAY_BY_GENDER = "prefDisplayByGender";
	public static final String KEY_PREF_COLORS_BY_GENDER = "prefColorsByGender";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}
	
	public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(KEY_PREF_DISPLAY_BY_GENDER)) {
				Preference genderPreference = findPreference(key);
				genderPreference.setSummary(sharedPreferences.getString(key, ""));
			}	
			if (key.equals(KEY_PREF_COLORS_BY_GENDER)) {
				Preference colorsPreference = findPreference(key);
				colorsPreference.setSummary(sharedPreferences.getString(key, ""));
			}
		}
		
		@Override
		public void onResume() {
		    super.onResume();
		    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		}

		@Override
		public void onPause() {
		    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		    super.onPause();
		}
	}	
}
