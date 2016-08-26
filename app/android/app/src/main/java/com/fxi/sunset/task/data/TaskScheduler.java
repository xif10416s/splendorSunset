package com.fxi.sunset.task.data;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.fxi.sunset.MainActivity;
import com.fxi.sunset.common.util.MaskUtil;
import com.fxi.sunset.task.service.TaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by seki on 16/8/9. TIME_GET_UP 0 , 1 , 2 TIME_BREAKFAST 3 , 4, 5
 * TIME_LUNCH 6, 7 , 8 , 9 TIME_DINNER 10 , 11 ,12 TIME_SLEEP
 */
public class TaskScheduler {
	public static final int MASK_AFTER_GET_UP = 0x000001;
	public static final int MASK_EARLY = 0x000002;
	public static final int MASK_BEFORE_BREAKFAST = 0x000004;
	public static final int MASK_AFTER_BREAKFAST = 0x000008;
	public static final int MASK_MORNING = 0x000010;
	public static final int MASK_BEFORE_LUNCH = 0x000020;
	public static final int MASK_AFTER_LUNCH = 0x000040;
	public static final int MASK_NOON = 0x000080;
	public static final int MASK_AFTER_NOON = 0x000100;
	public static final int MASK_BEFORE_DINNER = 0x000200;
	public static final int MASK_AFTER_DINNER = 0x000400;
	public static final int MASK_NIGHT = 0x000800;
	public static final int MASK_BEFORE_SLEEP = 0x001000;

	public static final int MASK_WEEK_END = 0x000001;
	public static final int MASK_WEEK_1 = 0x000002;
	public static final int MASK_WEEK_2 = 0x000004;
	public static final int MASK_WEEK_3 = 0x000008;
	public static final int MASK_WEEK_4 = 0x000010;
	public static final int MASK_WEEK_5 = 0x000020;
	public static final int MASK_WEEK_6 = 0x000040;

	public static final Short MASK_WEEK_ALL = MASK_WEEK_END | MASK_WEEK_1 | MASK_WEEK_2 | MASK_WEEK_3 | MASK_WEEK_4
			| MASK_WEEK_5 | MASK_WEEK_6;

	public static final String TIME_GET_UP = "TIME_GET_UP";
	public static final String TIME_BREAKFAST = "TIME_BREAKFAST";
	public static final String TIME_LUNCH = "TIME_LUNCH";
	public static final String TIME_DINNER = "TIME_DINNER";
	public static final String TIME_SLEEP = "TIME_SLEEP";

	public static final Map<String, String> DEFAULT_BASE_TIME_MAP = new HashMap<String, String>() {
		{
			this.put(TIME_GET_UP, "07:00");
			this.put(TIME_BREAKFAST, "07:30");
			this.put(TIME_LUNCH, "11:30");
			this.put(TIME_DINNER, "17:30");
			this.put(TIME_SLEEP, "22:00");
		}
	};

	public static final Short GET_UP_AFTER_TIME_RANGE = 5;
	public static final Short BREAKFAST_BEFORE_TIME_RANGE = 5;
	public static final Short BREAKFAST_AFTER_TIME_RANGE = 10;
	public static final Short LUNCH_BEFORE_TIME_RANGE = 5;
	public static final Short LUNCH_AFTER_TIME_RANGE = 15;
	public static final Short DINNER_BEFORE_TIME_RANGE = 5;
	public static final Short DINNER_AFTER_TIME_RANGE = 15;
	public static final Short SLEEP_BEFORE_TIME_RANGE = 10;

	public static final Short TASK_MIN_BETWEEN_TIME = 10;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	public static final String PERFENENCR_NAME = "sunset_personal";

	/**
	 * 07:05 - 07:15 - 07:25
	 */
	public static String getTime(String time, int minutes) {
		try {
			Date timeDate = sdf.parse(time);
			Calendar instance = Calendar.getInstance();
			instance.setTime(timeDate);
			instance.add(Calendar.MINUTE, minutes);
			return sdf.format(instance.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMiddleTime(String fromTime, String toTime) {
		try {
			Date fromDate = sdf.parse(fromTime);
			Date toDate = sdf.parse(toTime);
			long diff = toDate.getTime() - fromDate.getTime();
			int diffMin = (int) (diff / (60 * 1000));

			Calendar instance = Calendar.getInstance();
			instance.setTime(fromDate);
			instance.add(Calendar.MINUTE, diffMin / 2);
			return sdf.format(instance.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getAfterTime(String time, int afterMinutes) {
		return getTime(time, afterMinutes);
	}

	public static String getBeforeTime(String time, int beforeMinutes) {
		return getTime(time, -beforeMinutes);
	}

	public static Map<String, List<Task>> getTaskSchedule(List<Task> scheduleTasks,Context ctx) {
		Map<String, List<Task>> scheduleMap = new HashMap<>();
		// mock tasks
		// mask task list
		Map<Integer, List<Task>> timeListMap = new HashMap<>();
		for (Task task : scheduleTasks) {
			Calendar todayCal = Calendar.getInstance();
			int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);
			Log.e("T", "==" + dayOfWeek + " " + MaskUtil.getMaskList(task.getTimeRepeat().intValue()));
			if (!MaskUtil.getMaskList(task.getTimeRepeat().intValue()).contains(MaskUtil.MASKS.get(dayOfWeek))) {
				continue;
			}
			for (Integer mask : MaskUtil.getMaskList(task.getTimeRange())) {
				List<Task> tasks = timeListMap.get(mask);
				if (tasks == null) {
					tasks = new ArrayList<>();
					timeListMap.put(mask, tasks);
				}
				tasks.add(task);
			}
		}

		// mask list to time list
		Map<Integer, String> maskTimeMap = getMaskTimeMap(ctx);
		for (Integer mask : timeListMap.keySet()) {
			List<Task> tasks = timeListMap.get(mask);
			String middleNoon = getMaskTimeMap(ctx).get(-1);
			switch (mask) {
				case MASK_AFTER_GET_UP:
				case MASK_EARLY:
				case MASK_BEFORE_BREAKFAST:
				case MASK_AFTER_BREAKFAST:
				case MASK_BEFORE_LUNCH:
				case MASK_AFTER_LUNCH:
				case MASK_BEFORE_DINNER:
				case MASK_AFTER_DINNER:
				case MASK_BEFORE_SLEEP:
					scheduleMap.put(maskTimeMap.get(mask), tasks);
					break;
				case MASK_MORNING:
					scheduleMap.putAll(scheduleTasks(maskTimeMap.get(MASK_AFTER_BREAKFAST),
							maskTimeMap.get(MASK_BEFORE_LUNCH), tasks));
					break;
				case MASK_NIGHT:
					scheduleMap.putAll(scheduleTasks(maskTimeMap.get(MASK_AFTER_DINNER),
							maskTimeMap.get(MASK_BEFORE_SLEEP), tasks));
					break;
				case MASK_NOON:
					scheduleMap.putAll(scheduleTasks(maskTimeMap.get(MASK_AFTER_LUNCH), middleNoon, tasks));
					break;
				case MASK_AFTER_NOON:
					scheduleMap.putAll(scheduleTasks(middleNoon, maskTimeMap.get(MASK_BEFORE_DINNER), tasks));
					break;
			}
		}

		for (String timeKey : scheduleMap.keySet()) {
			List<Task> tasks = scheduleMap.get(timeKey);
			for (Task t : tasks) {
				t.setmStartTime(timeKey);
			}
		}

		return scheduleMap;
	}

	public static List<Map<String, String>> getBeginSortedTaskSchedule(List<Task> scheduleTasks,Context ctx) {
		Map<String, List<Task>> taskSchedule = getTaskSchedule(scheduleTasks,ctx);
		List<Map<String, String>> resTask = new ArrayList<Map<String, String>>();
		Map<Long, String> map = new TreeMap<Long, String>();
		try {
			Long currTime = sdf.parse(sdf.format(new Date())).getTime();
			for (String time : taskSchedule.keySet()) {

				long timeLong = sdf.parse(time).getTime();
				if (timeLong >= currTime) {
					map.put(timeLong, time);
				}

//				 map.put(timeLong, time);

			}
		} catch (Exception e) {

		}
		Collection<String> coll = map.values();
		for (String s : coll) {
			System.out.println(s);
			List<Task> tasks = taskSchedule.get(s);
			for (Task t : tasks) {
				t.setmStartTime(s);
				resTask.add(t.toMap());
			}
		}
		return resTask;
	}

	private static Map<String, List<Task>> scheduleTasks(String startTime, String endTime, List<Task> tasks) {
		int taskSize = tasks.size();
		Map<String, List<Task>> rsMap = new HashMap<>();
		try {
			Date fromDate = sdf.parse(startTime);
			Date toDate = sdf.parse(endTime);
			long diff = toDate.getTime() - fromDate.getTime();
			int diffMin = (int) (diff / (60 * 1000));

			int diffAvg = diffMin / (taskSize + 1);
			Calendar instance = Calendar.getInstance();
			instance.setTime(fromDate);
			for (Task t : tasks) {
				List<Task> rsList = new ArrayList<>();
				rsList.add(t);
				instance.add(Calendar.MINUTE, diffAvg);
				rsMap.put(sdf.format(instance.getTime()), rsList);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rsMap;
	}

	public static Map<String, String> getBaseTimeMap(Context ctx) {
		if(ctx == null){
			return DEFAULT_BASE_TIME_MAP;
		}
		SharedPreferences sharedPreferences = ctx.getSharedPreferences(PERFENENCR_NAME,
				Activity.MODE_PRIVATE);
		HashMap<String, String> rsMap = new HashMap<>();
		rsMap.put(TIME_GET_UP, sharedPreferences.getString(TIME_GET_UP, DEFAULT_BASE_TIME_MAP.get(TIME_GET_UP)));
		rsMap.put(TIME_BREAKFAST, sharedPreferences.getString(TIME_BREAKFAST, DEFAULT_BASE_TIME_MAP.get(TIME_BREAKFAST)));
		rsMap.put(TIME_LUNCH, sharedPreferences.getString(TIME_LUNCH, DEFAULT_BASE_TIME_MAP.get(TIME_LUNCH)));
		rsMap.put(TIME_DINNER, sharedPreferences.getString(TIME_DINNER, DEFAULT_BASE_TIME_MAP.get(TIME_DINNER)));
		rsMap.put(TIME_SLEEP, sharedPreferences.getString(TIME_SLEEP, DEFAULT_BASE_TIME_MAP.get(TIME_SLEEP)));
		return rsMap;
	}

	public static Map<String, String> getBaseTimeMap() {
		return getBaseTimeMap(null);
	}

	public static Map<Integer, String> getMaskTimeMap(Context ctx) {
		Map<Integer, String> map = new LinkedHashMap<>();
		Map<String, String> timeMap = getBaseTimeMap(ctx); // TODO

		map.put(MASK_AFTER_GET_UP, getAfterTime(timeMap.get(TIME_GET_UP), GET_UP_AFTER_TIME_RANGE));
		map.put(MASK_EARLY,
				getMiddleTime(getAfterTime(timeMap.get(TIME_GET_UP), GET_UP_AFTER_TIME_RANGE),
						getBeforeTime(timeMap.get(TIME_BREAKFAST), BREAKFAST_BEFORE_TIME_RANGE)));
		map.put(MASK_BEFORE_BREAKFAST, getBeforeTime(timeMap.get(TIME_BREAKFAST), BREAKFAST_BEFORE_TIME_RANGE));
		map.put(MASK_AFTER_BREAKFAST, getAfterTime(timeMap.get(TIME_BREAKFAST), BREAKFAST_AFTER_TIME_RANGE));
		map.put(MASK_MORNING,
				getMiddleTime(getAfterTime(timeMap.get(TIME_BREAKFAST), BREAKFAST_AFTER_TIME_RANGE),
						getBeforeTime(timeMap.get(TIME_LUNCH), LUNCH_BEFORE_TIME_RANGE)));
		map.put(MASK_BEFORE_LUNCH, getBeforeTime(timeMap.get(TIME_LUNCH), LUNCH_BEFORE_TIME_RANGE));
		map.put(MASK_AFTER_LUNCH, getAfterTime(timeMap.get(TIME_LUNCH), LUNCH_AFTER_TIME_RANGE));
		map.put(MASK_BEFORE_DINNER, getBeforeTime(timeMap.get(TIME_DINNER), DINNER_BEFORE_TIME_RANGE));
		map.put(MASK_AFTER_DINNER, getAfterTime(timeMap.get(TIME_DINNER), DINNER_AFTER_TIME_RANGE));
		map.put(MASK_NIGHT,
				getMiddleTime(getAfterTime(timeMap.get(TIME_DINNER), DINNER_AFTER_TIME_RANGE),
						getBeforeTime(timeMap.get(TIME_SLEEP), SLEEP_BEFORE_TIME_RANGE)));
		map.put(MASK_BEFORE_SLEEP, getBeforeTime(timeMap.get(TIME_SLEEP), SLEEP_BEFORE_TIME_RANGE));
		// ---middlenoon time
		map.put(-1,
				getMiddleTime(getAfterTime(timeMap.get(TIME_LUNCH), LUNCH_AFTER_TIME_RANGE),
						getBeforeTime(timeMap.get(TIME_DINNER), DINNER_BEFORE_TIME_RANGE)));
		return map;
	}

	/**
	 * @Nullable String title, @Nullable String description, @Nullable String
	 *           imageDescription,
	 * @Nullable String videoDescription, @Nullable Short timeRepeat, Integer
	 *           timeRange, Short category ,Integer timeDuration
	 * @return
	 */
	public static List<Task> getMockTaskList() {
		List<Task> list = new ArrayList<>();
		list.add(new Task("刷牙", "一天至少2次刷牙,好牙好胃口.", "", "", MASK_WEEK_ALL, MASK_AFTER_GET_UP | MASK_BEFORE_SLEEP,
				Task.TASK_CATEGORY_SPORTS, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(), Task.TASK_UN_SCHDULE,
				1L,"10:29"));
		list.add(new Task("喝水", "人体需要足够的水分,每天需要喝足量的水.", "", "", MASK_WEEK_ALL, MASK_AFTER_GET_UP | MASK_MORNING
				| MASK_NOON | MASK_AFTER_NOON | MASK_NIGHT, Task.TASK_CATEGORY_EAT, Task.TASK_SOURCE_NATIVE, System
				.currentTimeMillis(), Task.TASK_UN_SCHDULE, 2L,"09:40"));
//		list.add(new Task("吃药-XXX", "关节炎,一日三次,饭前", "", "", MASK_WEEK_ALL, MASK_BEFORE_BREAKFAST | MASK_BEFORE_LUNCH
//				| MASK_BEFORE_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
//				Task.TASK_UN_SCHDULE, 3L,"09:43"));
//		list.add(new Task("吃药-YYY", "123,一日三次,饭后", "", "", MASK_WEEK_ALL, MASK_AFTER_BREAKFAST | MASK_AFTER_LUNCH
//				| MASK_AFTER_DINNER, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
//				Task.TASK_UN_SCHDULE, 4L));
//		list.add(new Task("活动关节", "关节运动", "", "", MASK_WEEK_ALL, MASK_MORNING | MASK_NOON | MASK_AFTER_NOON
//				| MASK_NIGHT, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
//				Task.TASK_UN_SCHDULE, 5L));
		return list;
	}

	public static void main(String[] args) {
		testTaskSchedule();
	}

	public static void testTaskSchedule() {
		println(TaskScheduler.getAfterTime(getBaseTimeMap().get(TaskScheduler.TIME_GET_UP),
				TaskScheduler.GET_UP_AFTER_TIME_RANGE));
		println(TaskScheduler.getBeforeTime(getBaseTimeMap().get(TaskScheduler.TIME_BREAKFAST),
				TaskScheduler.BREAKFAST_BEFORE_TIME_RANGE));
		println(TaskScheduler.getAfterTime(getBaseTimeMap().get(TaskScheduler.TIME_BREAKFAST),
				TaskScheduler.BREAKFAST_AFTER_TIME_RANGE));
		println(TaskScheduler.getBeforeTime(getBaseTimeMap().get(TaskScheduler.TIME_LUNCH),
				TaskScheduler.LUNCH_BEFORE_TIME_RANGE));
		println(TaskScheduler.getAfterTime(getBaseTimeMap().get(TaskScheduler.TIME_LUNCH),
				TaskScheduler.LUNCH_AFTER_TIME_RANGE));

		println(getMiddleTime(getAfterTime(getBaseTimeMap().get(TIME_GET_UP), GET_UP_AFTER_TIME_RANGE),
				getBeforeTime(getBaseTimeMap().get(TIME_BREAKFAST), BREAKFAST_BEFORE_TIME_RANGE)));

		println("------------");
		for (Integer mask : getMaskTimeMap(null).keySet()) {
			println(getMaskTimeMap(null).get(mask));
		}
		println("--------scheduleTasks----");

		Map<String, List<Task>> stringListMap = scheduleTasks(
				getAfterTime(getBaseTimeMap().get(TIME_BREAKFAST), BREAKFAST_AFTER_TIME_RANGE),
				getBeforeTime(getBaseTimeMap().get(TIME_LUNCH), LUNCH_BEFORE_TIME_RANGE), getMockTaskList());
		for (String s : stringListMap.keySet()) {
			println(s + " : " + stringListMap.get(s).get(0).getTitle());
		}
		println("-------getTaskSchedule-----");
		Map<String, List<Task>> taskSchedule = getTaskSchedule(getMockTaskList(),null);
		for (String s : taskSchedule.keySet()) {
			String taskStr = "";
			for (Task t : taskSchedule.get(s)) {
				taskStr += t.getTitle() + " :  ";
			}
			println(s + " : " + taskStr);
		}
		println("-------getTaskSchedule22-----");
		getBeginSortedTaskSchedule(getMockTaskList(),null);
	}

	public static void println(String a) {
		System.out.println(a);
	}

	public static void printlnSchedule(Map<String, List<Task>> taskSchedule) {
		for (String s : taskSchedule.keySet()) {
			String taskStr = "";
			for (Task t : taskSchedule.get(s)) {
				taskStr += t.getTitle() + " :  ";
			}
			Log.e("T", s + " : " + taskStr);
		}
	}

}
