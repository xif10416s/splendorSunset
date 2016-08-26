package com.fxi.sunset.task.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fxi.sunset.MainActivity;
import com.fxi.sunset.R;
import com.fxi.sunset.task.data.Task;
import com.fxi.sunset.task.data.TaskRecord;
import com.fxi.sunset.task.data.TaskScheduler;
import com.fxi.sunset.task.data.source.TaskValues;
import com.fxi.sunset.task.data.source.TasksDataSource;
import com.fxi.sunset.task.data.source.TasksRepository;
import com.fxi.sunset.task.data.source.local.TasksDbHelper;
import com.fxi.sunset.task.data.source.local.TasksLocalDataSource;
import com.fxi.sunset.task.data.source.local.TasksPersistenceContract;
import com.fxi.sunset.task.tasks.TasksActivity;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.fxi.sunset.task.data.TaskScheduler.*;
import static com.fxi.sunset.task.data.TaskScheduler.MASK_NIGHT;
import static com.fxi.sunset.task.data.TaskScheduler.getMockTaskList;

/**
 * Created by seki on 16/8/4.
 */
public class TaskService extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;

	public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
	public final static String INTENT_BUTTONID_TAG = "ButtonId";
	public ButtonBroadcastReceiver bReceiver;
	public final static int BUTTON_PREV_ID = 1;
	public final static int SCHEDULE_UPDATED = 2;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	public static Map<String, List<Task>> timeTaskMap ;
	public TasksDbHelper mTasksDbHelper;


	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// Normally we would do some work here, like download a file.
			// For our sample, we just sleep for 5 seconds.
			while (true) {
				try {
					Thread.sleep(60000);
//					Log.e("T", "TEST......" + System.currentTimeMillis());
					String currentTime = sdf.format(new Date());
					Log.e("T", "TEST...currentTime..2." + currentTime);
					TaskScheduler.printlnSchedule(timeTaskMap);
					//=======
//					List<Task> currentTasks =getMockTaskList();
//					currentTasks.get(0).setmStartTime(new Date().getTime()+"");
					//======
//					Task task = new Task("活动关节", "关节运动", "", "", MASK_WEEK_ALL, MASK_MORNING | MASK_NOON | MASK_AFTER_NOON
//							| MASK_NIGHT, Task.TASK_CATEGORY_MEDICAL, Task.TASK_SOURCE_NATIVE, System.currentTimeMillis(),
//							Task.TASK_ON_SCHDULE, 5L);
//					task.setmStartTime("10:"+new Random().nextInt());
//					showButtonNotify(task);
					//=======
					List<Task> currentTasks = timeTaskMap.get(currentTime);
					if(currentTasks != null && currentTasks.size() > 0 ){
						for(Task t : currentTasks){
							Log.e("T","task :"+t.getId()+"   " + t.getTitle() +"  "+ t.getmStartTime());
							showButtonNotify(t);
						}
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			// stopSelf(msg.arg1);
		}

//		public void showBigPicNotify() {
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
//					.setSmallIcon(R.drawable.ic_add).setContentTitle("My notification").setContentText("Hello World!")
//					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setTicker("测试通知来啦")
//					.setCategory(NotificationCompat.CATEGORY_ALARM);
//			mBuilder.mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
//
//			// NotificationCompat.InboxStyle inboxStyle = new
//			// NotificationCompat.InboxStyle();
//			// String[] events = new String[5];
//			// // Sets a title for the Inbox style big view
//			// inboxStyle.setBigContentTitle("测试通知来啦");
//			// inboxStyle.setSummaryText("测试通知来啦测试通知来啦测试通知来啦测试通知来啦测试通知来啦测试通知来啦测试通知来啦测试通知来啦");
//			// // Moves events into the big view
//			// for (int i = 0; i < events.length; i++) {
//			// inboxStyle.addLine("测试通知来啦测试通知"+i);
//			// }
//			// mBuilder.setStyle(inboxStyle);
//
//			NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//			bigPictureStyle.setBigContentTitle("测试通知来啦测试通知来啦测试通知来啦测试通知来啦");
//			bigPictureStyle.setSummaryText("测试通知来啦测试通知来啦测试通知来啦");
//			bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.sing_icon));
//			mBuilder.setStyle(bigPictureStyle);
//
//			mBuilder.setLights(0xff0000ff, 300, 0).setVibrate(
//					new long[] { 0, 300, 500, 700, 500, 700, 500, 700, 500, 700 });
//			;
//			// 设置通知的优先级
//			mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
//			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//			// 设置通知的提示音
//			mBuilder.setSound(alarmSound);
//
//			// Creates an explicit intent for an Activity in your app
//			Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//
//			// The stack builder object will contain an artificial back
//			// stack for the
//			// started Activity.
//			// This ensures that navigating backward from the Activity
//			// leads out of
//			// your application to the Home screen.
//			TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//			// Adds the back stack for the Intent (but not the Intent
//			// itself)
//			stackBuilder.addParentStack(TasksActivity.class);
//			// Adds the Intent that starts the Activity to the top of
//			// the stack
//			stackBuilder.addNextIntent(resultIntent);
//			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
//			mBuilder.setContentIntent(resultPendingIntent);
//			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//			// mId allows you to update the notification later on.
//			mNotificationManager.notify(1, mBuilder.build());
//		}

		public void showButtonNotify(Task t) {
//			int mId = Long.valueOf(System.currentTimeMillis()).intValue();
			int mId = t.getId().intValue();
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
			RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_custom_button);
			mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.drawable.sing_icon);
			mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, t.getmStartTime()+" " + t.getTitle());
//			Uri uri = new Uri.Builder()
//					.scheme("res") // "res"
//					.path(String.valueOf( R.drawable.sing_icon))
//					.build();
//			mRemoteViews.setImageViewUri(R.id.custom_song_icon,uri);

			if (getSystemVersion() <= 9) {
				mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.GONE);
			} else {
				mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE);
				//
			}

			Intent buttonIntent = new Intent(ACTION_BUTTON);
			buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
			buttonIntent.putExtra("noticeId", mId);
			buttonIntent.putExtra("taskId", t.getId());
			PendingIntent intent_prev = PendingIntent.getBroadcast(getApplicationContext(), 1, buttonIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);

			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

			// The stack builder object will contain an artificial back
			// stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity
			// leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
			// Adds the back stack for the Intent (but not the Intent
			// itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of
			// the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent).setWhen(System.currentTimeMillis())// ?????????????????????????
					.setSmallIcon(R.drawable.ic_add)
					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setTicker("到时间做任务了,快去看看!")
					.setCategory(NotificationCompat.CATEGORY_ALARM);
			mBuilder.mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;

			mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
//			mBuilder.setLights(0xff0000ff, 300, 0).setVibrate(
//					new long[] { 0, 300, 500, 700, 500, 700, 500, 700, 500, 700, 500, 700, 500, 700, 500, 700 });
//			// 设置通知的优先级
//			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//			// 设置通知的提示音
//			mBuilder.setSound(alarmSound);
			Notification notify = mBuilder.build();
			notify.bigContentView = mRemoteViews;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
//			mNotificationManager.notify(t.getId().intValue(), notify);
			mNotificationManager.notify(mId, notify);
		}
	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);

		mTasksDbHelper = new TasksDbHelper(getApplicationContext());

		timeTaskMap = TaskScheduler.getTaskSchedule(mTasksDbHelper.getAllScheduleTask(),getApplication());

		//--
//		SQLiteDatabase db =
//				new TasksDbHelper(getApplicationContext()).getReadableDatabase();
//		Cursor cursor = new TasksDbHelper(getApplicationContext()).getReadableDatabase().query("test", new String[]{"id"}, null, null, null, null, null);
//		Log.e("TT", "-------"+db.getPath());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		bReceiver = new ButtonBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BUTTON);
		registerReceiver(bReceiver, intentFilter);
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	public static int getSystemVersion() {
		int version = android.os.Build.VERSION.SDK_INT;
		return version;
	}

	public class ButtonBroadcastReceiver extends BroadcastReceiver {


		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.e("T", "task scheudle onReceive");
			String action = intent.getAction();
			if (action.equals(ACTION_BUTTON)) {
				int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
				switch (buttonId) {
					case BUTTON_PREV_ID:
						Log.e("onclick", "onclick");
						int noticeId = intent.getIntExtra("noticeId", 0);
						long taskId = intent.getLongExtra("taskId", 0);
						NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						mNotificationManager.cancel(noticeId);
						mTasksDbHelper.saveTaskRecord(new TaskRecord(null,  sdf.format(new Date()),taskId));
						break;
					case SCHEDULE_UPDATED:
						Log.e("T", "SCHEDULE_UPDATED");
//						timeTaskMap = TaskScheduler.getTaskSchedule(mTasksDbHelper.getAllScheduleTask(),getApplication());
					default:
						break;
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (bReceiver != null) {
			unregisterReceiver(bReceiver);
		}
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}
}
