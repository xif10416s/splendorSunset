package com.fxi.sunset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.fxi.sunset.common.util.ActivityUtils;
import com.fxi.sunset.common.util.EspressoIdlingResource;
import com.fxi.sunset.info.view.BannerFragment;
import com.fxi.sunset.personal.activity.PersonalInfoSettingActivity;
import com.fxi.sunset.task.addedittask.AddEditTaskActivity;
import com.fxi.sunset.task.data.Task;
import com.fxi.sunset.task.data.TaskRecord;
import com.fxi.sunset.task.data.TaskScheduler;
import com.fxi.sunset.task.data.source.local.TasksDbHelper;
import com.fxi.sunset.task.service.TaskService;
import com.fxi.sunset.task.taskdetail.TaskDetailActivity;
import com.fxi.sunset.task.tasks.TasksActivity;

/**
 * Created by seki on 16/8/7.
 */
public class MainActivity extends AppCompatActivity {
	private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

	private DrawerLayout mDrawerLayout;

	private ScrollView scrollView;

	private SchduleTaskAdapter mSchedule;
	private LinearLayout noScheduleTask;
	private ListView listView;
	private TasksDbHelper mTasksDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_act);

		// Set up the toolbar.
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setTitle("主页-健康养生");

		// Set up the navigation drawer.
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			setupDrawerContent(navigationView);
		}

		BannerFragment bannerFragment = (BannerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.contentFrame);
		if (bannerFragment == null) {
			// Create the fragment
			bannerFragment = BannerFragment.newInstance();
			ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), bannerFragment, R.id.contentFrame);
		}

		Log.i("test", TaskService.class.getName());
		if (ActivityUtils.isServiceRun(getApplicationContext(), TaskService.class.getName())) {
			Toast.makeText(this, "service is running...." + TaskService.class.getName(), Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(this, TaskService.class);
			startService(intent);
		}

		// myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
		// String.valueOf(AudioManager.STREAM_ALARM));

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_task);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// toAddTask();
				// for (int i=0;i<50;i++){

				// }
			}
		});

		listView = (ListView) findViewById(R.id.tasks_list);
		noScheduleTask = (LinearLayout) findViewById(R.id.no_schedule_task);

		List<Map<String, String>> latestScheduleData = getLatestScheduleData();
		if (latestScheduleData != null && latestScheduleData.size() > 0) {
			mSchedule = new SchduleTaskAdapter(this, // 没什么解释
					latestScheduleData,// 数据来源
					R.layout.task_item,// ListItem的XML实现

					// 动态数组与ListItem对应的子项
					new String[] { "title", "id", "startTime" },

					// ListItem的XML文件里面的两个TextView ID
					new int[] { R.id.title }, new TaskItemListenerImpl(this));
			listView.setAdapter(mSchedule);
			noScheduleTask.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.GONE);
			noScheduleTask.setVisibility(View.VISIBLE);
		}

		scrollView = (ScrollView) findViewById(R.id.main_sv);
		// scrollTop();

		Button toSchedule = (Button) findViewById(R.id.toSchedule);
		toSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				toTaskManager();
			}
		});

		mTasksDbHelper = new TasksDbHelper(getApplicationContext());

	}

	private void scrollTop() {
		scrollView.smoothScrollTo(0, 0);
	}

	@Override
	protected void onResume() {
		Log.e("T", "main activity on resume.....");
		super.onResume();
		reloadAllData();
		scrollTop();
	}

	private void toAddTask() {
		Intent intent = new Intent(this, AddEditTaskActivity.class);
		startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
	}

	private void toTaskManager() {
		Intent intent1 = new Intent(this, TasksActivity.class);
		startActivityForResult(intent1, 1);
	}

	private void toPersonalInfoSetting() {
		Intent intent = new Intent(this, PersonalInfoSettingActivity.class);
		startActivityForResult(intent, 1);
	}

	private List<Map<String, String>> getLatestScheduleData() {
		mTasksDbHelper = new TasksDbHelper(getApplicationContext());
		Cursor cursor = mTasksDbHelper.getReadableDatabase().rawQuery("select * from task where isOnSchedule = 1;",
				null);
		List<Task> tasks = new ArrayList<>();
		while (cursor.moveToNext()) {
			Task task = Task.from(cursor);
			Log.e("Tt", task.getTitle());
			tasks.add(task);
		}
		List<Map<String, String>> beginSortedTaskSchedule = TaskScheduler.getBeginSortedTaskSchedule(tasks, this);
		List<Map<String, String>> filterList = new ArrayList<>();
		List<String> todayRecordKeyList = mTasksDbHelper.getTodayRecordKeyList();
		for (Map<String, String> map : beginSortedTaskSchedule) {
			String key = map.get("startTime") + map.get("id");
			if (!todayRecordKeyList.contains(key)) {
				filterList.add(map);
			}
		}
		return filterList;
	}

	private void reloadAllData() {
		// get new modified random data
		List<Map<String, String>> latestScheduleData = getLatestScheduleData();
		if (latestScheduleData != null && latestScheduleData.size() > 0) {
			mSchedule.setmData(getLatestScheduleData());
			// fire the event
			mSchedule.notifyDataSetChanged();
			listView.setVisibility(View.VISIBLE);
			noScheduleTask.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.GONE);
			noScheduleTask.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// Open the navigation drawer when the home icon is selected
				// from the toolbar.
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.list_navigation_menu_item:
						// Do nothing, we're already on that screen
						toTaskManager();
						break;
					// case R.id.statistics_navigation_menu_item:
					// Intent intent = new Intent(getApplication(),
					// StatisticsActivity.class);
					// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
					// Intent.FLAG_ACTIVITY_CLEAR_TASK);
					// startActivity(intent);
					// break;
					case R.id.personal_menu_item:
						toPersonalInfoSetting();
						break;
					default:
						break;
				}
				// Close the navigation drawer when an item is selected.
				menuItem.setChecked(true);
				mDrawerLayout.closeDrawers();
				return true;
			}
		});
	}

	public interface TaskItemListener {

		void onTaskClick(Long taskId);

		void onCompleteTaskClick(Task completedTask);

		void onActivateTaskClick(Task activatedTask);
	}

	class TaskItemListenerImpl implements TaskItemListener {

		private Context ctx;

		public TaskItemListenerImpl(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		public void onTaskClick(Long taskId) {
			Intent intent = new Intent(ctx, TaskDetailActivity.class);
			intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, Long.valueOf(taskId));
			startActivity(intent);
		}

		@Override
		public void onCompleteTaskClick(Task completedTask) {
			reloadAllData();
		}

		@Override
		public void onActivateTaskClick(Task activatedTask) {
			// mPresenter.activateTask(activatedTask);
		}
	}

	;

	@VisibleForTesting
	public IdlingResource getCountingIdlingResource() {
		return EspressoIdlingResource.getIdlingResource();
	}

	public static class SchduleTaskAdapter extends SimpleAdapter {
		/**
		 * 保存map的list,map保存数据
		 */
		private List<? extends Map<String, ?>> mData;

		/**
		 * 解析的xml资源文件id
		 */
		private int mResource;

		/**
		 * RemindViewAdapter所需的key数组
		 */
		private String[] mFrom;

		/**
		 * RemindViewAdapter所需的id数组
		 */
		private int[] mTo;

		/**
		 * 解析器
		 */
		private LayoutInflater mInflater;

		/**
		 * 属兔绑定
		 */
		private ViewBinder mViewBinder;

		private TasksDbHelper taskDbHelper;

		private final TaskItemListener mItemListener;

		public void setmData(List<? extends Map<String, ?>> mData) {
			this.mData = mData;
		}

		public SchduleTaskAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
				int[] to, TaskItemListener listener) {
			super(context, data, resource, from, to);
			mData = data;
			mResource = resource;
			mFrom = from;
			mTo = to;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mItemListener = listener;
			taskDbHelper = new TasksDbHelper(context.getApplicationContext());
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_schedule_item, parent, false);
				ViewHolder viewHolder = new ViewHolder(view);
				view.setTag(viewHolder);
			} else {
				view = convertView;
			}

			ViewHolder viewHolder = (ViewHolder) view.getTag();

			// Active/completed task UI
			if (position >= mData.size()) {
				position = 0;
			}
			final Map<String, ?> stringMap = mData.get(position);
			// viewHolder.completeCB.setChecked(true);
			viewHolder.completeCB.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("T", "completeCB....onClick" + stringMap.get(mFrom[1]));
					String id = stringMap.get(mFrom[1]).toString();
					taskDbHelper.saveTaskRecord(new TaskRecord(null, stringMap.get(mFrom[2]).toString(), Long
							.valueOf(id)));
					mItemListener.onCompleteTaskClick(null);
				}
			});
			viewHolder.titleTV.setText((String) stringMap.get(mFrom[2]) + " " + (String) stringMap.get(mFrom[0]));

			viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// mItemListener.onTaskClick(task);
					Log.e("T", "row....onClick");
					mItemListener.onTaskClick(Long.valueOf((String) stringMap.get(mFrom[1])));

				}
			});
			return view;
		}
	}

	public static class ViewHolder {
		public final View rowView;
		public final TextView titleTV;
		public final Button completeCB;

		public ViewHolder(View view) {
			rowView = view;
			titleTV = (TextView) view.findViewById(R.id.title);
			completeCB = (Button) view.findViewById(R.id.complete);
		}
	}
}
