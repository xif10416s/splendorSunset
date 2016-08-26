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

package com.fxi.sunset.task.data.source;

import android.content.ContentValues;

import com.fxi.sunset.task.data.Task;
import com.fxi.sunset.task.data.source.local.TasksPersistenceContract;

public class TaskValues {

    public static ContentValues from(Task task) {
        ContentValues values = new ContentValues();
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ISONSCHEDULE, task.isOnSchedule());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_IMAGE_DESCIPTION, task.getImageDescription());
        values.put(TasksPersistenceContract.TaskEntry.COLUNM_NAME_VIDEO_DESCIPTION, task.getVideoDescription());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_RANGE, task.getTimeRange());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TIME_REPEAT, task.getTimeRepeat());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_CATEGORY, task.getCategory());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_CREATE_TIME, task.getCreateTime());
        values.put(TasksPersistenceContract.TaskEntry.COLUMN_NAME_SOURCE, task.getSource());

        return values;
    }

}
