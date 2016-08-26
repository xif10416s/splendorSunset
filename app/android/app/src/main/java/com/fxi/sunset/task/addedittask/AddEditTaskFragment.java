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

package com.fxi.sunset.task.addedittask;

import static com.google.common.base.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.fxi.sunset.R;
import com.fxi.sunset.task.data.Task;
import com.fxi.sunset.task.data.TaskScheduler;
import com.fxi.sunset.task.service.TaskService;
import com.fxi.sunset.task.tasks.TaskFilter;
import com.fxi.sunset.task.tasks.TasksFilterType;
import com.lid.lib.LabelTextView;

import java.util.Date;
import java.util.Map;

/**
 * Main UI for the add task screen. Users can enter a task title and
 * description.
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

	public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";
	public static final String ARGUMENT_EDIT_TASK_INTERNAL_ID = "EDIT_TASK_INTERNAL_ID";

	private AddEditTaskContract.Presenter mPresenter;

	private TextView mTitle;

	private TextView mDescription;

	private RadioButton eatRadio;
	private RadioButton sportRadio;
	private RadioButton medicalRadio;
	private CheckBox repeatAllBox;
	private CheckBox repeat0;
	private CheckBox repeat1;
	private CheckBox repeat2;
	private CheckBox repeat3;
	private CheckBox repeat4;
	private CheckBox repeat5;
	private CheckBox repeat6;

	private CheckBox cbAfterGetup;
	private CheckBox cbEarly;
	private CheckBox cbBeforeBreakfast;
	private CheckBox cbAfterBreakfast;
	private CheckBox cbMorning;
	private CheckBox cbBeforeLunch;
	private CheckBox cbAfterLunch;
	private CheckBox cbNoon;
	private CheckBox cbAfterNoon;
	private CheckBox cbBeforeDinner;
	private CheckBox cbAfterDinner;
	private CheckBox cbEvening;
	private CheckBox cbBeforeSleep;

	public AddEditTaskFragment() {
		// Required empty public constructor
	}

	public static AddEditTaskFragment newInstance() {
		return new AddEditTaskFragment();
	}

	@Override
	public void onResume() {
		super.onResume();
		mPresenter.start();
	}

	@Override
	public void setPresenter(@NonNull AddEditTaskContract.Presenter presenter) {
		mPresenter = checkNotNull(presenter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

//		FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
//		fab.setImageResource(R.drawable.ic_done);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.e("T", "create task...");
//				mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString(), null, null,
//						getTimeRepeat(), getTimeRange(), getCategory(), Task.TASK_SOURCE_CREATE,
//						System.currentTimeMillis(), Task.TASK_ON_SCHDULE);
//			}
//		});

		eatRadio = (RadioButton) getActivity().findViewById(R.id.radioBtnEat);
		sportRadio = (RadioButton) getActivity().findViewById(R.id.radioBtnSport);
		medicalRadio = (RadioButton) getActivity().findViewById(R.id.radioBtnMedical);
		eatRadio.setChecked(true);

		LabelTextView getupLabel = (LabelTextView) getActivity().findViewById(R.id.getUpLabel);
		Map<Integer, String> maskTimeMap = TaskScheduler.getMaskTimeMap(this.getContext());
		getupLabel.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_GET_UP));

		TextView labelAfterGetUpTime = (TextView) getActivity().findViewById(R.id.labelAfterGetUpTime);
		labelAfterGetUpTime.setText(maskTimeMap.get(TaskScheduler.MASK_AFTER_GET_UP));

		TextView labelTimeEarly = (TextView) getActivity().findViewById(R.id.labelTimeEarly);
		labelTimeEarly.setText(maskTimeMap.get(TaskScheduler.MASK_EARLY));

		TextView labelTimeBeforeBreakfast = (TextView) getActivity().findViewById(R.id.labelTimeBeforeBreakfast);
		labelTimeBeforeBreakfast.setText(maskTimeMap.get(TaskScheduler.MASK_BEFORE_BREAKFAST));

		LabelTextView breakfastLabel1 = (LabelTextView) getActivity().findViewById(R.id.breakfastLabel1);
		breakfastLabel1.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_BREAKFAST));

		LabelTextView breakfastLabel2 = (LabelTextView) getActivity().findViewById(R.id.breakfastLabel2);
		breakfastLabel2.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_BREAKFAST));

		TextView labelTimeAfterBreakfast = (TextView) getActivity().findViewById(R.id.labelTimeAfterBreakfast);
		labelTimeAfterBreakfast.setText(maskTimeMap.get(TaskScheduler.MASK_AFTER_BREAKFAST));

		TextView labelTimeBeforeLunch = (TextView) getActivity().findViewById(R.id.labelTimeBeforeLunch);
		labelTimeBeforeLunch.setText(maskTimeMap.get(TaskScheduler.MASK_BEFORE_LUNCH));

		LabelTextView lunchLabel1 = (LabelTextView) getActivity().findViewById(R.id.lunchLabel1);
		lunchLabel1.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_LUNCH));

		LabelTextView lunchLabel2 = (LabelTextView) getActivity().findViewById(R.id.lunchLabel2);
		lunchLabel2.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_LUNCH));

		TextView labelTimeAfterlunch = (TextView) getActivity().findViewById(R.id.labelTimeAfterlunch);
		labelTimeAfterlunch.setText(maskTimeMap.get(TaskScheduler.MASK_AFTER_LUNCH));

		TextView timeMiddle = (TextView) getActivity().findViewById(R.id.timeMiddle);
		timeMiddle.setText(maskTimeMap.get(-1));

		TextView labelTimeBeforeDinner = (TextView) getActivity().findViewById(R.id.labelTimeBeforeDinner);
		labelTimeBeforeDinner.setText(maskTimeMap.get(TaskScheduler.MASK_BEFORE_DINNER));

		LabelTextView dinnerLabel1 = (LabelTextView) getActivity().findViewById(R.id.dinnerLabel1);
		dinnerLabel1.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_DINNER));

		LabelTextView dinnerLabel2 = (LabelTextView) getActivity().findViewById(R.id.dinnerLabel2);
		dinnerLabel2.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_DINNER));

		TextView labelTimeAfterDinner = (TextView) getActivity().findViewById(R.id.labelTimeAfterDinner);
		labelTimeAfterDinner.setText(maskTimeMap.get(TaskScheduler.MASK_AFTER_DINNER));

		TextView labelTimeBeforeSleep = (TextView) getActivity().findViewById(R.id.labelTimeBeforeSleep);
		labelTimeBeforeSleep.setText(maskTimeMap.get(TaskScheduler.MASK_BEFORE_SLEEP));

		LabelTextView sleepLabel = (LabelTextView) getActivity().findViewById(R.id.sleepLabel);
		sleepLabel.setLabelText(TaskScheduler.getBaseTimeMap(this.getContext()).get(TaskScheduler.TIME_SLEEP));

		repeatAllBox = (CheckBox) getActivity().findViewById(R.id.repeatAll);
		repeatAllBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				repeat0.setChecked(b);
				repeat1.setChecked(b);
				repeat2.setChecked(b);
				repeat3.setChecked(b);
				repeat4.setChecked(b);
				repeat5.setChecked(b);
				repeat6.setChecked(b);
			}
		});
	}

	private Short getTimeRepeat() {
		Integer timeRepeat = 0;
		if (repeat0.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_END;
		}
		if (repeat1.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_1;
		}
		if (repeat2.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_2;
		}

		if (repeat3.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_3;
		}
		if (repeat4.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_4;
		}
		if (repeat5.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_5;
		}
		if (repeat6.isChecked()) {
			timeRepeat |= TaskScheduler.MASK_WEEK_6;
		}

		return timeRepeat.shortValue();
	}

	private Integer getTimeRange() {
		Integer timeRange = 0;
		if (cbAfterGetup.isChecked()) {
			timeRange |= TaskScheduler.MASK_AFTER_GET_UP;
		}
		if (cbEarly.isChecked()) {
			timeRange |= TaskScheduler.MASK_EARLY;
		}
		if (cbBeforeBreakfast.isChecked()) {
			timeRange |= TaskScheduler.MASK_BEFORE_BREAKFAST;
		}

		if (cbAfterBreakfast.isChecked()) {
			timeRange |= TaskScheduler.MASK_AFTER_BREAKFAST;
		}

		if (cbMorning.isChecked()) {
			timeRange |= TaskScheduler.MASK_MORNING;
		}

		if (cbBeforeLunch.isChecked()) {
			timeRange |= TaskScheduler.MASK_BEFORE_LUNCH;
		}

		if (cbAfterLunch.isChecked()) {
			timeRange |= TaskScheduler.MASK_AFTER_LUNCH;
		}

		if (cbNoon.isChecked()) {
			timeRange |= TaskScheduler.MASK_NOON;
		}

		if (cbAfterNoon.isChecked()) {
			timeRange |= TaskScheduler.MASK_AFTER_NOON;
		}

		if (cbBeforeDinner.isChecked()) {
			timeRange |= TaskScheduler.MASK_BEFORE_DINNER;
		}

		if (cbAfterDinner.isChecked()) {
			timeRange |= TaskScheduler.MASK_AFTER_DINNER;
		}

		if (cbEvening.isChecked()) {
			timeRange |= TaskScheduler.MASK_NIGHT;
		}

		if (cbBeforeSleep.isChecked()) {
			timeRange |= TaskScheduler.MASK_BEFORE_SLEEP;
		}

		return timeRange;
	}

	private short getCategory() {
		if (eatRadio.isChecked()) {
			return Task.TASK_CATEGORY_EAT;
		}
		if (sportRadio.isChecked()) {
			return Task.TASK_CATEGORY_SPORTS;
		}
		if (medicalRadio.isChecked()) {
			return Task.TASK_CATEGORY_MEDICAL;
		}
		return -1;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.addtask_frag, container, false);
		mTitle = (TextView) root.findViewById(R.id.add_task_title);
		mDescription = (TextView) root.findViewById(R.id.add_task_description);

		RadioGroup categoryGroup = (RadioGroup) root.findViewById(R.id.categoryGroup);

		// categoryGroup.setOnCheckedChangeListener(new
		// RadioGroup.OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// // TODO Auto-generated method stub
		// if (checkedId == eatRadio.getId()) {
		//
		// }
		// }
		// });

		repeat0 = (CheckBox) root.findViewById(R.id.repeat_0);
		repeat1 = (CheckBox) root.findViewById(R.id.repeat_1);
		repeat2 = (CheckBox) root.findViewById(R.id.repeat_2);
		repeat3 = (CheckBox) root.findViewById(R.id.repeat_3);
		repeat4 = (CheckBox) root.findViewById(R.id.repeat_4);
		repeat5 = (CheckBox) root.findViewById(R.id.repeat_5);
		repeat6 = (CheckBox) root.findViewById(R.id.repeat_6);

		cbAfterGetup = (CheckBox) root.findViewById(R.id.cbAfterGetup);
		cbEarly = (CheckBox) root.findViewById(R.id.cbEarly);
		cbBeforeBreakfast = (CheckBox) root.findViewById(R.id.cbBeforeBreakfast);
		cbAfterBreakfast = (CheckBox) root.findViewById(R.id.cbAfterBreakfast);
		cbMorning = (CheckBox) root.findViewById(R.id.cbMorning);
		cbBeforeLunch = (CheckBox) root.findViewById(R.id.cbBeforeLunch);
		cbAfterLunch = (CheckBox) root.findViewById(R.id.cbAfterLunch);
		cbNoon = (CheckBox) root.findViewById(R.id.cbNoon);
		cbAfterNoon = (CheckBox) root.findViewById(R.id.cbAfterNoon);
		cbBeforeDinner = (CheckBox) root.findViewById(R.id.cbBeforeDinner);
		cbAfterDinner = (CheckBox) root.findViewById(R.id.cbAfterDinner);
		cbEvening = (CheckBox) root.findViewById(R.id.cbEvening);
		cbBeforeSleep = (CheckBox) root.findViewById(R.id.cbBeforeSleep);

		setHasOptionsMenu(true);
		return root;
	}

	@Override
	public void showEmptyTaskError() {
		Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void showTasksList() {
		Log.e("T", "send borad cast");
		Intent intent = new Intent();
		intent.setAction(TaskService.ACTION_BUTTON);
		intent.putExtra(TaskService.INTENT_BUTTONID_TAG, TaskService.SCHEDULE_UPDATED);
		getActivity().sendBroadcast(intent);
		getActivity().setResult(Activity.RESULT_OK);
		getActivity().finish();
	}

	@Override
	public void setTitle(String title) {
		mTitle.setText(title);
	}

	@Override
	public void setDescription(String description) {
		mDescription.setText(description);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.save_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_save:
				mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString(), null, null,
						getTimeRepeat(), getTimeRange(), getCategory(), Task.TASK_SOURCE_CREATE,
						System.currentTimeMillis(), Task.TASK_ON_SCHDULE);
				break;
			case android.R.id.home:
				getActivity().onBackPressed();
				return true;
		}
		return true;
	}

}
