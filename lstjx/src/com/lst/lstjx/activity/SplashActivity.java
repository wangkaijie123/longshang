package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import com.lst.lstjx.app.App;
import com.lst.lstjx.bean.LoginBean;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

/**
 * 开屏页
 * 
 */

public class SplashActivity extends Activity {
	// private TextView versionText;
	private String token;
	private ImageView splash;
	private static final int sleepTime = 2000;
	protected static final int LOGIN = 0x894651;
	private String username;
	private String password;
	private LoginBean mLoginBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initView();
		initAnim();
	}

	private void initAnim() {
		// TODO Auto-generated method stub
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(4000);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(username)
						&& !TextUtils.isEmpty(password) && token != null) {
					// goLogin();
					connect(token);
				} else {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				boolean boolean1 = SharePrefUtil.getBoolean(
						getApplicationContext(), "isConnectSuccess", false);
				if (boolean1) {
					startActivity(new Intent(SplashActivity.this,
							FrameActivity.class));
					finish();
				} else {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
				}
			}
		});
		splash.startAnimation(animation);
	}

	private void initView() {
		// TODO Auto-generated method stub
		username = SharePrefUtil.getString(SplashActivity.this, "username", "");
		password = SharePrefUtil.getString(SplashActivity.this, "password", "");
		token = SharePrefUtil.getString(SplashActivity.this, "token", "");
		splash = (ImageView) findViewById(R.id.splash_img);

	}

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号错误";
		}
	}

	/**
	 * 建立与融云服务器的连接
	 * 
	 * @param token
	 */
	private void connect(String token) {

		if (getApplicationInfo().packageName.equals(App
				.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 * Token
				 */
				@Override
				public void onTokenIncorrect() {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
					Log.d("LoginActivity", "--onTokenIncorrect");
				}

				/**
				 * 连接融云成功
				 * 
				 * @param userid
				 *            当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					System.out
							.println("________onSuccess______________splash________________");
					Log.d("LoginActivity", "--onSuccess" + userid);
					SharePrefUtil.saveBoolean(getApplicationContext(),
							"isConnectSuccess", true);
				}

				/**
				 * 连接融云失败
				 * 
				 * @param errorCode
				 *            错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
					Log.d("LoginActivity", "--onError" + errorCode);
				}
			});
		}
	}
}
