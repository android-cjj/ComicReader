package com.cjj.cartoon;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingLightActivity extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.light_setting);
	}
}
