package com.lst.lstjx.activity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.app.App;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

public class SetActivity extends Activity implements OnClickListener {
	private static final String SHARE_APP_TAG = null;
	private LinearLayout goback_wd,lianxikefu;
	private ToggleButton dark_light;
	private Button loginout;
	private Dialog mDialog;

	// 取得当前亮度
	int screenBrightness = 255;
	private LinearLayout set_possword, about_me, clear;
	// 计时器
	private Timer timer;
	private long t1 = 0;// 记录上一次单击的时间，初始值为0
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	int flag = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (flag == 2) {
					Toast.makeText(SetActivity.this, "缓存清除成功", 0).show();
					timer.cancel(); // 退出计时器
					mDialog.dismiss();

				}
				flag++;
				System.out.println("----flag----" + flag);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_set);

		initView();
	}

	private void initView() {

		goback_wd = (LinearLayout) findViewById(R.id.goback_wd);
		dark_light = (ToggleButton) findViewById(R.id.dark_light);
		loginout = (Button) findViewById(R.id.loginout);
		set_possword = (LinearLayout) findViewById(R.id.set_password);
		about_me = (LinearLayout) findViewById(R.id.aboutme);
		clear = (LinearLayout) findViewById(R.id.clear);
		lianxikefu = (LinearLayout) findViewById(R.id.lianxikefu);
	
		goback_wd.setOnClickListener(this);
		dark_light.setOnClickListener(this);
		loginout.setOnClickListener(this);
		set_possword.setOnClickListener(this);
		about_me.setOnClickListener(this);
		clear.setOnClickListener(this);
		try {
			if (Settings.System.getInt(
			getContentResolver(),
			Settings.System.SCREEN_BRIGHTNESS) ==50) {
				dark_light.setChecked(true);
							
			} else {

				dark_light.setChecked(false);
			}
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.goback_wd:
			this.finish();
			break;
		case R.id.set_password:
			intent.setClass(SetActivity.this, SetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.aboutme:
			intent.setClass(SetActivity.this, AboutMeActivity.class);
			startActivity(intent);
			break;
		case R.id.clear:
			if (t1 == 0) {// 第一次单击，初始化为本次单击的时间
				t1 = (new Date()).getTime();
				mDialog = DialogUtil.createProgressDialog(SetActivity.this,
						"缓存清除中..");
				mDialog.show();
				timer = new Timer(true);
				timer.schedule(task, 1000, 1000);// 延时1000ms后执行，1000ms执行一次
			} else {
				long curTime = (new Date()).getTime();// 本地单击的时间
				System.out.println("两次单击间隔时间：" + (curTime - t1));// 计算本地和上次的时间差
				if (curTime - t1 > 5 * 1000) {
					// 间隔5秒允许点击，可以根据需要修改间隔时间
					t1 = curTime;// 当前单击事件变为上次时间
				}
			}

			break;
		case R.id.dark_light:

			// 当按钮第一次被点击时候响应的事件
			if (dark_light.isChecked()) {
				
				
				Settings.System.putInt(getContentResolver(),

				Settings.System.SCREEN_BRIGHTNESS_MODE,

				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

				// 取得当前亮度
				try {
					screenBrightness = Settings.System.getInt(
							getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS);
					SharePrefUtil.saveInt(SetActivity.this,
							"SCREEN_BRIGHTNESS", screenBrightness);
				} catch (SettingNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 设置当前的系统亮度为50,（最亮为255）
				Settings.System.putInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS, 50);
			
				Toast.makeText(SetActivity.this, "已经打开夜间模式", Toast.LENGTH_SHORT)
						.show();

			}
			// 当按钮再次被点击时候响应的事件
			else {
				// 恢复成之前亮度
			
				Settings.System.putInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS, SharePrefUtil
								.getInt(SetActivity.this, "SCREEN_BRIGHTNESS",
										screenBrightness));

				Toast.makeText(SetActivity.this, "已经关闭夜间模式", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.loginout:
			Builder dialog = new AlertDialog.Builder(SetActivity.this);
			dialog.setTitle("是否退出应用程序");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							loginout();

						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			dialog.create();
			dialog.show();
			break;
		default:
			break;
		}

	}

	/**
	 * 注销并退出应用程序
	 */
	private void loginout() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(SetActivity.this, ConstantsUrl.loginout, null,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("----aaaaaa---" + arg2);
						App.getInstance().exit();
						SharePrefUtil.saveString(SetActivity.this, "username",
								"");
						SharePrefUtil.saveString(SetActivity.this, "password",
								"");
						SharePrefUtil.saveString(SetActivity.this, "token", "");
						SharePrefUtil
								.saveString(SetActivity.this, "userId", "");
						Intent intent = new Intent(SetActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

}
