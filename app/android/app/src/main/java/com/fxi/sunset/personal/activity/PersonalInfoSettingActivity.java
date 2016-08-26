/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fxi.sunset.personal.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fxi.sunset.R;
import com.fxi.sunset.task.data.TaskScheduler;
import com.fxi.sunset.task.service.TaskService;

/**
 * Show statistics for tasks.
 */
public class PersonalInfoSettingActivity extends AppCompatActivity {

	private DrawerLayout mDrawerLayout;

	private TextView getUpTimeLabel;
	private TextView breakfastTime;
	private TextView lunchTime;
	private TextView dinnerTime;
	private TextView sleepTime;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.personal_act);

		// Set up the toolbar.
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle(R.string.personal_title);

		// Set up the navigation drawer.
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

		sharedPreferences = getSharedPreferences(TaskScheduler.PERFENENCR_NAME, Activity.MODE_PRIVATE);

		Button getupTimeBtn = (Button) findViewById(R.id.btnGetupTime);
		getUpTimeLabel = (TextView) findViewById(R.id.getUpTime);
		getUpTimeLabel.setText(sharedPreferences.getString(TaskScheduler.TIME_GET_UP,
				TaskScheduler.DEFAULT_BASE_TIME_MAP.get(TaskScheduler.TIME_GET_UP)));
		getupTimeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openTimePickerDialog(getUpTimeLabel);
			}
		});

		Button btnBreakfastTime = (Button) findViewById(R.id.btnBreakfastTime);
		breakfastTime = (TextView) findViewById(R.id.breakfastTime);
		breakfastTime.setText(sharedPreferences.getString(TaskScheduler.TIME_BREAKFAST,
				TaskScheduler.DEFAULT_BASE_TIME_MAP.get(TaskScheduler.TIME_BREAKFAST)));
		btnBreakfastTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openTimePickerDialog(breakfastTime);
			}
		});

		Button btnLunchTime = (Button) findViewById(R.id.btnLunchTime);
		lunchTime = (TextView) findViewById(R.id.lunchTime);
		lunchTime.setText(sharedPreferences.getString(TaskScheduler.TIME_LUNCH,
				TaskScheduler.DEFAULT_BASE_TIME_MAP.get(TaskScheduler.TIME_LUNCH)));
		btnLunchTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openTimePickerDialog(lunchTime);
			}
		});

		Button btnDinnerTime = (Button) findViewById(R.id.btnDinnerTime);
		dinnerTime = (TextView) findViewById(R.id.dinnerTime);
		dinnerTime.setText(sharedPreferences.getString(TaskScheduler.TIME_DINNER,
				TaskScheduler.DEFAULT_BASE_TIME_MAP.get(TaskScheduler.TIME_DINNER)));
		btnDinnerTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openTimePickerDialog(dinnerTime);
			}
		});

		Button btnSleepTime = (Button) findViewById(R.id.btnSleepTime);
		sleepTime = (TextView) findViewById(R.id.sleepTime);
		sleepTime.setText(sharedPreferences.getString(TaskScheduler.TIME_SLEEP,
				TaskScheduler.DEFAULT_BASE_TIME_MAP.get(TaskScheduler.TIME_SLEEP)));
		btnSleepTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openTimePickerDialog(sleepTime);
			}
		});

	}

	private void openTimePickerDialog(final TextView label) {
		String[] times = label.getText().toString().split(":");
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Log.e("T", hourOfDay + "");
				label.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
			}
		}, Integer.valueOf(times[0]), Integer.valueOf(times[1]), true);

		timePickerDialog.show();
	}

	private void saveInfo(){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(TaskScheduler.TIME_GET_UP,getUpTimeLabel.getText().toString());
		editor.putString(TaskScheduler.TIME_BREAKFAST,breakfastTime.getText().toString());
		editor.putString(TaskScheduler.TIME_LUNCH,lunchTime.getText().toString());
		editor.putString(TaskScheduler.TIME_DINNER,dinnerTime.getText().toString());
		editor.putString(TaskScheduler.TIME_SLEEP,sleepTime.getText().toString());
		editor.commit();

		Intent intent = new Intent();
		intent.setAction(TaskService.ACTION_BUTTON);
		intent.putExtra(TaskService.INTENT_BUTTONID_TAG, TaskService.SCHEDULE_UPDATED);
		sendBroadcast(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("T", "task act nav onOptionsItemSelected");
		switch (item.getItemId()) {
			case android.R.id.home:
				super.onBackPressed();
				return true;
			case R.id.menu_save:
				saveInfo();
				onBackPressed();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.save_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
