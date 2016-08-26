/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.fxi.sunset.task.tasks;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.fxi.sunset.R;
import com.fxi.sunset.architecture.blueprints.todoapp.Injection;
import com.fxi.sunset.common.util.ActivityUtils;
import com.fxi.sunset.common.util.EspressoIdlingResource;
import com.fxi.sunset.task.data.source.LoaderProvider;

public class TasksActivity extends AppCompatActivity {

	private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY2";

	private TasksPresenter mTasksPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_act);

		// Set up the toolbar.
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle("任务管理");

		TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		if (tasksFragment == null) {
			// Create the fragment
			tasksFragment = TasksFragment.newInstance();
			ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
		}

		// Create the presenter
		LoaderProvider loaderProvider = new LoaderProvider(this);

		// Load previously saved state, if available.
		TaskFilter taskFilter = TaskFilter.from(TasksFilterType.ALL_TASKS);
		if (savedInstanceState != null) {
			TasksFilterType currentFiltering = (TasksFilterType) savedInstanceState
					.getSerializable(CURRENT_FILTERING_KEY);
			taskFilter = TaskFilter.from(currentFiltering);
		}

		mTasksPresenter = new TasksPresenter(loaderProvider, getSupportLoaderManager(),
				Injection.provideTasksRepository(getApplicationContext()), tasksFragment, taskFilter);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("T", "task act nav onOptionsItemSelected");
		switch (item.getItemId()) {
			case android.R.id.home:
				// Write your logic here
//				this.finishActivity(1);
				super.onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	// private void setupDrawerContent(NavigationView navigationView) {
	// navigationView.setNavigationItemSelectedListener(new
	// NavigationView.OnNavigationItemSelectedListener() {
	// @Override
	// public boolean onNavigationItemSelected(MenuItem menuItem) {
	// switch (menuItem.getItemId()) {
	// case R.id.list_navigation_menu_item:
	// // Do nothing, we're already on that screen
	// break;
	// case R.id.statistics_navigation_menu_item:
	// Intent intent = new Intent(TasksActivity.this, StatisticsActivity.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
	// Intent.FLAG_ACTIVITY_CLEAR_TASK);
	// startActivity(intent);
	// break;
	// default:
	// break;
	// }
	// // Close the navigation drawer when an item is selected.
	// menuItem.setChecked(true);
	// mDrawerLayout.closeDrawers();
	// return true;
	// }
	// });
	// }

	@VisibleForTesting
	public IdlingResource getCountingIdlingResource() {
		return EspressoIdlingResource.getIdlingResource();
	}

//	@Override
//	public boolean onSupportNavigateUp() {
//		Log.e("T", "task act nav onSupportNavigateUp");
//		onBackPressed();
//		return true;
//	}

}
