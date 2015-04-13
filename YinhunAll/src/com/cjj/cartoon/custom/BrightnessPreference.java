/*******************************************************************************
 * Copyright 2009 Robot Media SL
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.cjj.cartoon.custom;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.cjj.cartoon.R;

// TODO: Reset value when dialog is dismissed

public class BrightnessPreference extends DialogPreference implements
		SeekBar.OnSeekBarChangeListener, OnCheckedChangeListener {
	private static final String androidns = "http://schemas.android.com/apk/res/android";

	private SeekBar mSeekBar;
	private CheckBox mCheckBox;
	private int mDefault, mValue = -1;

	
	public BrightnessPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", -1);
	}

	@Override
	protected View onCreateDialogView() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.brightness_preference, null);

		mSeekBar = (SeekBar) layout.findViewById(R.id.brightness_seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);

		mCheckBox = (CheckBox) layout.findViewById(R.id.brightness_checkbox);
		mCheckBox.setOnCheckedChangeListener(this);

		if (shouldPersist()) {
			mValue = getPersistedInt(mDefault);
		}
			
		if (mValue < 0) {
			mCheckBox.setChecked(true);
			mSeekBar.setEnabled(false);
		} else {
			mCheckBox.setChecked(false);
			mSeekBar.setEnabled(true);
			mSeekBar.setProgress(mValue);
		}
		
		return layout;
	}

	private void updateBrightness(int value) {
		if (shouldPersist()) { 
			persistInt(value);
		}
		callChangeListener(new Integer(value));
		/*
		float brightness = value;
		WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
		 if (brightness == 0) { // 0 renders the phone unusable
			 brightness = 1f/100f;
		 }
		 lp.screenBrightness = brightness; 
		 getDialog().getWindow().setAttributes(lp); */
	}
	
	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		super.onSetInitialValue(restore, defaultValue);
		if (restore) {
			mValue = shouldPersist() ? getPersistedInt(mDefault) : -1;
		} else {
			mValue = (Integer) defaultValue;
		}
	}

	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
		updateBrightness(value);
	}

	public void onStartTrackingTouch(SeekBar seek) {
	}

	public void onStopTrackingTouch(SeekBar seek) {
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final int value;
		if (isChecked) {
			mSeekBar.setEnabled(false);
			value = -1;
		} else {
			mSeekBar.setEnabled(true);
			value = mSeekBar.getProgress();
		}
		updateBrightness(value);
	}

}
