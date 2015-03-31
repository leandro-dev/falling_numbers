package com.leandroideias.falling.numbers.tela;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.leandroideias.falling.numbers.R;

@SuppressWarnings("deprecation")
public class UserPreferences extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.user_preferences);
	}
}
