package com.cjj.cartoon.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/**
 * 
 * @author Cjj
 *
 */
public class SettingUtils {
	public static String getSetting(Context context, String name,
			String defaultValue) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String value = prefs.getString(name, defaultValue);
		return value;
	}

	public static boolean setSetting(Context context, String name, String value) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(name, value);
		return editor.commit();
	}

	public static boolean getSetting(Context context, String name,
			boolean defaultValue) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean value = prefs.getBoolean(name, defaultValue);
		return value;
	}

	public static boolean setSetting(Context context, String name, boolean value) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean(name, value);
		return editor.commit();
	}

	public static int getSetting(Context context, String name, int defaultValue) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		int value = prefs.getInt(name, defaultValue);
		return value;
	}

	public static boolean setSetting(Context context, String name, int value) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putInt(name, value);
		return editor.commit();
	}

}
