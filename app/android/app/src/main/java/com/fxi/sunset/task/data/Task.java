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

package com.fxi.sunset.task.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.fxi.sunset.task.data.source.local.TasksPersistenceContract;
import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable model class for a Task.
 */
public final class Task {

	public static final Short TASK_CATEGORY_EAT = 0;
	public static final Short TASK_CATEGORY_SPORTS = 1;
	public static final Short TASK_CATEGORY_MEDICAL = 2;

	public static final Short TASK_SOURCE_NATIVE = 0;
	public static final Short TASK_SOURCE_CREATE = 1;

	public static final Short TASK_UN_SCHDULE = 0;
	public static final Short TASK_ON_SCHDULE = 1;

	private final Long mId;
	private final String mTitle;
	private final String mDescription;
	private final Short mIsOnSchedule;
	private final Short mSource;
	private int mInternalId;

	private String mImageDescription;
	private String mVideoDescription;
	// 每周几
	private Short mTimeRepeat;
	private Integer mTimeRange;
	private Short mCategory;
	private Long mCreateTime;
	private String mStartTime;

	public Task(@Nullable String title, @Nullable String description, @Nullable String imageDescription,
			@Nullable String videoDescription, @Nullable Short timeRepeat, Integer timeRange, Short category,
			Short source, Long createTime, Short isOnSchedule, Long id) {
		mId = id;
		mTitle = title;
		mDescription = description;
		mIsOnSchedule = isOnSchedule;
		mImageDescription = imageDescription;
		mVideoDescription = videoDescription;
		mTimeRange = timeRange;
		mTimeRepeat = timeRepeat;
		mCategory = category;
		mSource = source;
		mCreateTime = createTime;
	}

	public Task(@Nullable String title, @Nullable String description, @Nullable String imageDescription,
				@Nullable String videoDescription, @Nullable Short timeRepeat, Integer timeRange, Short category,
				Short source, Long createTime, Short isOnSchedule, Long id,String startTime) {
		mId = id;
		mTitle = title;
		mDescription = description;
		mIsOnSchedule = isOnSchedule;
		mImageDescription = imageDescription;
		mVideoDescription = videoDescription;
		mTimeRange = timeRange;
		mTimeRepeat = timeRepeat;
		mCategory = category;
		mSource = source;
		mCreateTime = createTime;
		mStartTime = startTime;
	}

	/**
	 * Use this constructor to return a Task from a Cursor
	 *
	 * @return
	 */
	public static Task from(Cursor cursor) {
		Long entryId = cursor.getLong(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID));
		String title = cursor.getString(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE));
		String description = cursor.getString(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
		String imgDesc = cursor.getString(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_IMAGE_DESCIPTION));
		String videoDesc = cursor.getString(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUNM_NAME_VIDEO_DESCIPTION));
		Short timeRepeat = cursor.getShort(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_REPEAT));
		Integer timeRange = cursor.getInt(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_RANGE));
		Short category = cursor.getShort(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_CATEGORY));
		Short source = cursor.getShort(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_SOURCE));

		Long createTime = cursor.getLong(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_CREATE_TIME));
		Short isOnSchedule = cursor.getShort(cursor
				.getColumnIndexOrThrow(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ISONSCHEDULE));
		return new Task(title, description, imgDesc, videoDesc, timeRepeat, timeRange, category, source, createTime,
				isOnSchedule, entryId);
	}

	public static Task from(ContentValues values) {
		Long entryId = values.getAsLong(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID);
		String title = values.getAsString(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE);
		String description = values.getAsString(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION);
		Short isOnSchedule = values.getAsShort(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ISONSCHEDULE) ;
		String imgDesc = values.getAsString(TasksPersistenceContract.TaskEntry.COLUMN_NAME_IMAGE_DESCIPTION);
		String videoDesc = values.getAsString(TasksPersistenceContract.TaskEntry.COLUNM_NAME_VIDEO_DESCIPTION);
		Short timeRepeat = values.getAsShort(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_REPEAT);
		Integer timeRange = values.getAsInteger(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_RANGE);
		Short category = values.getAsShort(TasksPersistenceContract.TaskEntry.COLUMN_NAME_CATEGORY);
		Short source = values.getAsShort(TasksPersistenceContract.TaskEntry.COLUMN_NAME_SOURCE);
		Long createTime = values.getAsLong(TasksPersistenceContract.TaskEntry.COLUMN_NAME_CREATE_TIME);
		return new Task(title, description, imgDesc, videoDesc, timeRepeat, timeRange, category, source, createTime,
				isOnSchedule, entryId);
	}

	public Map<String,String> toMap(){
		Map<String,String> m = new HashMap<>();
		m.put("id",this.mId.toString());
		m.put("title",this.getTitle());
		m.put("startTime",this.getmStartTime());
		return m;
	}

	public Long getId() {
		return mId;
	}

	@Nullable
	public String getTitle() {
		return mTitle;
	}

	@Nullable
	public String getTitleForList() {
		if (mTitle != null && !mTitle.equals("")) {
			return mTitle;
		} else {
			return mDescription;
		}
	}

	@Nullable
	public String getDescription() {
		return mDescription;
	}

	public Short isOnSchedule() {
		return mIsOnSchedule;
	}

	public boolean isActive() {
		return false;
	}

	public boolean isEmpty() {
		return (mTitle == null || "".equals(mTitle)) && (mDescription == null || "".equals(mDescription));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Task task = (Task) o;
		return Objects.equal(mId, task.mId) && Objects.equal(mTitle, task.mTitle)
				&& Objects.equal(mDescription, task.mDescription);
	}

	public String getVideoDescription() {
		return mVideoDescription;
	}

	public void setVideoDescription(String mVideoDescription) {
		this.mVideoDescription = mVideoDescription;
	}

	public Short getTimeRepeat() {
		return mTimeRepeat;
	}

	public void setTimeRepeat(Short mTimeRepeat) {
		this.mTimeRepeat = mTimeRepeat;
	}

	public Integer getTimeRange() {
		return mTimeRange;
	}

	public void setTimeRange(Integer mTimeRange) {
		this.mTimeRange = mTimeRange;
	}

	public Short getCategory() {
		return mCategory;
	}

	public void setCategory(Short mCategory) {
		this.mCategory = mCategory;
	}

	public Long getCreateTime() {
		return mCreateTime;
	}

	public void setCreateTime(Long mCreateTime) {
		this.mCreateTime = mCreateTime;
	}

	public Short getSource() {
		return mSource;
	}

	public String getImageDescription() {
		return mImageDescription;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(mId, mTitle, mDescription);
	}

	@Override
	public String toString() {
		return "Task with title " + mTitle;
	}

	public void setmStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}

	public String getmStartTime() {
		return mStartTime;
	}
}
