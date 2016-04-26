package com.lst.lstjx.app;

import io.rong.imkit.RongIM;
import io.rong.imlib.ipc.RongExceptionHandler;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.lst.lstjx.message.ContactNotificationMessageProvider;
import com.lst.lstjx.message.DeAgreedFriendRequestMessage;
import com.lst.lstjx.message.DemoCommandNotificationMessage;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by bob on 2015/1/30.
 */
public class App extends Application {
	public static final String APP_PIC_STORAGE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/pic/";
	private List<ActivityGroup> activityList = new LinkedList<ActivityGroup>();
	private static App instance;

	@Override
	public void onCreate() {
		super.onCreate();
		/**
		 * 注意：
		 * 
		 * IMKit SDK调用第一步 初始化
		 * 
		 * context上下文
		 * 
		 * 只有两个进程需要初始化，主进程和 push 进程
		 */
		if ("com.lst.yuewo".equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {

			RongIM.init(this);

			System.out.println("______________________init+++++++++++++++++"
					+ getCurProcessName(getApplicationContext()).toString());
			
			/**
			 * 融云SDK事件监听处理
			 * 
			 * 注册相关代码，只需要在主进程里做。
			 */

			if ("com.lst.yuewo"
					.equals(getCurProcessName(getApplicationContext()))) {
				
				DemoContext.init(this);
				System.out
						.println("___________________DemoContext___init+++++++++++++++++");

				RongCloudEvent.init(this);
				Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(
						this));
				try {
					RongIM.registerMessageType(DemoCommandNotificationMessage.class);
					RongIM.registerMessageType(DeAgreedFriendRequestMessage.class);
					RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
					RongIM.getInstance().setMessageAttachedUserInfo(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		initImageLoader(getApplicationContext());
	}

	// 獲得當前的進程的名字
	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	// 初始化imageloader
	public static void initImageLoader(Context context) {
		File cacheDir = new File(APP_PIC_STORAGE_PATH);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				// .discCache(new UnlimitedDiscCache(cacheDir))
				// default
				// .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.build();

		ImageLoader.getInstance().init(config); // 初始化

	}

	// 单例模式中获取唯一的MyApplication实例
	public static App getInstance() {
		if (null == instance) {
			instance = new App();
		}
		return instance;

	}

	// 添加Activity到容器中
	public void addActivity(ActivityGroup activity) {
		activityList.add(activity);
	}

	public void exit() {
		// TODO Auto-generated method stub
		for (ActivityGroup activity : activityList) {
			System.out
					.println("____________activityList" + activityList.size());
			activity.finish();
		}

	}

}
