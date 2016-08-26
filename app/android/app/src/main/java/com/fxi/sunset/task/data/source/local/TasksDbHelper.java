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

package com.fxi.sunset.task.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.fxi.sunset.task.data.Task;
import com.fxi.sunset.task.data.TaskRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.fxi.sunset.task.data.TaskScheduler.*;

public class TasksDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "Tasks.db";

	private static final String TEXT_TYPE = " TEXT";

	private static final String INTEGER_TYPE = " INTEGER";

	private static final String COMMA_SEP = ",";

	public static final String TABLE_TASK_RECORD = "task_record";

	public static final String COLUMN_NAME_DAY = "day";

	public static final String COLUMN_NAME_TIME = "time";

	public static final String COLUMN_NAME_TASK_ID = "taskId";

	private static final String SQL_CREATE_TAKS_RECOARD_ENTRIES = "CREATE TABLE " + TABLE_TASK_RECORD + " ("
			+ COLUMN_NAME_DAY + INTEGER_TYPE + " , " + COLUMN_NAME_TIME + TEXT_TYPE + " , " + COLUMN_NAME_TASK_ID
			+ INTEGER_TYPE + ", " + " PRIMARY KEY(" + COLUMN_NAME_DAY + COMMA_SEP + COLUMN_NAME_TIME + COMMA_SEP
			+ COLUMN_NAME_TASK_ID + ") )";

	public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private static final String SQL_INSERT = "INSERT INTO " + TasksPersistenceContract.TaskEntry.TABLE_NAME + "("
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_SOURCE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_IMAGE_DESCIPTION + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUNM_NAME_VIDEO_DESCIPTION + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_REPEAT + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_RANGE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_CATEGORY + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_CREATE_TIME + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_ISONSCHEDULE + ")";
	// "VALUES (%s,%s,'%s','%s','%s','%s',%s,%s,%s,%s,%s);";

	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TasksPersistenceContract.TaskEntry.TABLE_NAME
			+ " (" + TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + INTEGER_TYPE + " PRIMARY KEY,"
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_SOURCE + INTEGER_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_IMAGE_DESCIPTION + TEXT_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUNM_NAME_VIDEO_DESCIPTION + TEXT_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_REPEAT + INTEGER_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_RANGE + INTEGER_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_CREATE_TIME + INTEGER_TYPE + COMMA_SEP
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_ISONSCHEDULE + INTEGER_TYPE + " )";

	private static final String SQL_CREATE_INDEX_SOURCE = "CREATE INDEX index_source ON "
			+ TasksPersistenceContract.TaskEntry.TABLE_NAME + " ("
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_SOURCE + ");";

	private static final String SQL_CREATE_INDEX_ONSCHEDULE = "CREATE INDEX index_isOnSchedule ON "
			+ TasksPersistenceContract.TaskEntry.TABLE_NAME + " ("
			+ TasksPersistenceContract.TaskEntry.COLUMN_NAME_ISONSCHEDULE + ");";

	public TasksDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public List<Task> getAllScheduleTask() {
		Cursor cursor = this.getReadableDatabase().rawQuery("select * from task where isOnSchedule = 1;", null);
		List<Task> tasks = new ArrayList<>();
		while (cursor.moveToNext()) {
			Task task = Task.from(cursor);
			Log.e("Tt", task.getTitle());
			tasks.add(task);
		}
		return tasks;
	}

	public void onCreate(SQLiteDatabase db) {
		Log.e("T",db.getPath());
		db.execSQL(SQL_CREATE_TAKS_RECOARD_ENTRIES);
		db.execSQL(SQL_CREATE_ENTRIES);
		db.execSQL(SQL_CREATE_INDEX_SOURCE);
		db.execSQL(SQL_CREATE_INDEX_ONSCHEDULE);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < defaultTaskList().size(); i++) {
			Task t = defaultTaskList().get(i);
			sb.append("  select ");
			sb.append(t.getId()).append(COMMA_SEP).append(t.getSource()).append(COMMA_SEP)
					.append("'" + t.getTitle() + "'").append(COMMA_SEP).append("'" + t.getDescription() + "'")
					.append(COMMA_SEP).append("'" + t.getImageDescription() + "'").append(COMMA_SEP)
					.append("'" + t.getVideoDescription() + "'").append(COMMA_SEP).append(t.getTimeRepeat())
					.append(COMMA_SEP).append(t.getTimeRange()).append(COMMA_SEP).append(t.getCategory())
					.append(COMMA_SEP).append(t.getCreateTime()).append(COMMA_SEP).append("'" + t.isOnSchedule() + "'");
			if (i != defaultTaskList().size() - 1) {
				sb.append(" union all ");
			}

		}
		Log.e("T", sb.toString());
		db.execSQL(SQL_INSERT + sb.toString());
	}

	public List<String> getTodayRecordKeyList() {
		String format = YYYYMMDD.format(new Date());
		Cursor cursor = this.getReadableDatabase().rawQuery("select * from task_record where day = ?;",
				new String[] { format });
		List<String> taskRecords = new ArrayList<>();
		while (cursor.moveToNext()) {
			String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TIME));
			Long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_TASK_ID));
			String key = time + id;
			Log.e("Tt", time);
			taskRecords.add(key);
		}
		return taskRecords;
	}

	public void saveTaskRecord(TaskRecord tr) {
		ContentValues contentValues = new ContentValues();
		String format = YYYYMMDD.format(new Date());
		contentValues.put(COLUMN_NAME_DAY, Integer.valueOf(format));
		contentValues.put(COLUMN_NAME_TIME, tr.getTime());
		contentValues.put(COLUMN_NAME_TASK_ID, tr.getTaskId());
		this.getWritableDatabase().insert(TABLE_TASK_RECORD, null, contentValues);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not required as at version 1
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not required as at version 1
	}

	public List<Task> defaultTaskList() {
		List<Task> list = new ArrayList<>();
		list.add(new Task("刷牙", "一天至少2次刷牙,好牙好胃口.", "", "", MASK_WEEK_ALL, MASK_AFTER_GET_UP | MASK_BEFORE_SLEEP,
				Task.TASK_CATEGORY_SPORTS, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(), Task.TASK_ON_SCHDULE,
				1L));
		list.add(new Task("喝水", "人体需要足够的水分,每天需要喝足量的水.", "", "", MASK_WEEK_ALL, MASK_AFTER_GET_UP | MASK_MORNING
				| MASK_NOON | MASK_AFTER_NOON | MASK_NIGHT, Task.TASK_CATEGORY_EAT, Task.TASK_SOURCE_NATIVE, System
				.currentTimeMillis(), Task.TASK_ON_SCHDULE, 2L));
		list.add(new Task("吃药-XXX", "关节炎,一日三次,饭前", "", "", MASK_WEEK_ALL, MASK_BEFORE_BREAKFAST | MASK_BEFORE_LUNCH
				| MASK_BEFORE_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_UN_SCHDULE, 3L));
		list.add(new Task("吃药-YYY", "123,一日三次,饭后", "", "", MASK_WEEK_ALL, MASK_AFTER_BREAKFAST | MASK_AFTER_LUNCH
				| MASK_AFTER_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_UN_SCHDULE, 4L));
		list.add(new Task("活动关节", "关节运动", "", "", MASK_WEEK_ALL, MASK_MORNING | MASK_NOON | MASK_AFTER_NOON
				| MASK_NIGHT, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_ON_SCHDULE, 5L));
		list.add(new Task("吃药-YYY", "123,一日三次,饭后", "", "", MASK_WEEK_ALL, MASK_AFTER_BREAKFAST | MASK_AFTER_LUNCH
				| MASK_AFTER_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_UN_SCHDULE, 6L));
		list.add(new Task("活动关节", "关节运动", "", "", MASK_WEEK_ALL, MASK_MORNING | MASK_NOON | MASK_AFTER_NOON
				| MASK_NIGHT, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_ON_SCHDULE, 7L));
		list.add(new Task("吃药-YYY", "123,一日三次,饭后", "", "", MASK_WEEK_ALL, MASK_AFTER_BREAKFAST | MASK_AFTER_LUNCH
				| MASK_AFTER_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_UN_SCHDULE, 8L));
		list.add(new Task("活动关节", "关节运动", "", "", MASK_WEEK_ALL, MASK_MORNING | MASK_NOON | MASK_AFTER_NOON
				| MASK_NIGHT, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_ON_SCHDULE, 9L));
		list.add(new Task("吃药-YYY", "123,一日三次,饭后", "", "", MASK_WEEK_ALL, MASK_AFTER_BREAKFAST | MASK_AFTER_LUNCH
				| MASK_AFTER_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_UN_SCHDULE, 10L));
		list.add(new Task("活动关节", "关节运动", "", "", MASK_WEEK_ALL, MASK_MORNING | MASK_NOON | MASK_AFTER_NOON
				| MASK_NIGHT, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
				Task.TASK_ON_SCHDULE, 11L));

		return list;
	}

}
