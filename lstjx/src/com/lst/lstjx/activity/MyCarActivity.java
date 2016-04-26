<<<<<<< HEAD
package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyCarAddFragment;
import com.lst.lstjx.fragment.MyCarFragment;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * author describe parameter return
 */
public class MyCarActivity extends FragmentActivity {
	private ImageView goback_wd;
	private ImageView main_title_tv_ed;
	private String userid;
	private boolean b;
	private MyCarFragment myCar;
	private MyCarAddFragment myCarAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_car_no);
		userid = SharePrefUtil.getString(MyCarActivity.this, "userId", null);
		postInfo();
		intview();
	}

	private void intview() {
		b = SharePrefUtil.getBoolean(MyCarActivity.this, "hasClData", false);
		if (!b) {
			myCarAdd = new MyCarAddFragment();
			addFragment(myCarAdd);
			showFragment(myCarAdd);
		} else {
			myCar = new MyCarFragment();
			addFragment(myCar);
			showFragment(myCar);
		}
		goback_wd = (ImageView) findViewById(R.id.shopcar_go_back);
		main_title_tv_ed = (ImageView) findViewById(R.id.main_title_tv_ed);
		main_title_tv_ed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCarActivity.this,
						AddCarActivity.class);
				startActivity(intent);
				finish();
			}
		});
		goback_wd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void postInfo() {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", userid);// 动态id
		client.post(MyCarActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(MyCarActivity.this,
									"hasClData", false);
						} else {
							SharePrefUtil.saveBoolean(MyCarActivity.this,
									"hasClData", true);
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});

	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.show_mView, fragment);
		transaction.commitAllowingStateLoss();
	}

	//
	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.remove(fragment);
		transaction.commitAllowingStateLoss();
	}

	//
	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		if (myCar != null) {
			transaction.hide(myCar);
		}
		if (myCarAdd != null) {
			transaction.hide(myCarAdd);
		}

		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
=======
package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyCarAddFragment;
import com.lst.lstjx.fragment.MyCarFragment;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * author describe parameter return
 */
public class MyCarActivity extends FragmentActivity {
	private ImageView goback_wd;
	private ImageView main_title_tv_ed;
	private String userid;
	private boolean b;
	private MyCarFragment myCar;
	private MyCarAddFragment myCarAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_car_no);
		userid = SharePrefUtil.getString(MyCarActivity.this, "userId", null);
		postInfo();
		intview();
	}

	private void intview() {
		b = SharePrefUtil.getBoolean(MyCarActivity.this, "hasClData", false);
		if (!b) {
			myCarAdd = new MyCarAddFragment();
			addFragment(myCarAdd);
			showFragment(myCarAdd);
		} else {
			myCar = new MyCarFragment();
			addFragment(myCar);
			showFragment(myCar);
		}
		goback_wd = (ImageView) findViewById(R.id.shopcar_go_back);
		main_title_tv_ed = (ImageView) findViewById(R.id.main_title_tv_ed);
		main_title_tv_ed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCarActivity.this,
						AddCarActivity.class);
				startActivity(intent);
				finish();
			}
		});
		goback_wd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void postInfo() {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", userid);// 动态id
		client.post(MyCarActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(MyCarActivity.this,
									"hasClData", false);
						} else {
							SharePrefUtil.saveBoolean(MyCarActivity.this,
									"hasClData", true);
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});

	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.show_mView, fragment);
		transaction.commitAllowingStateLoss();
	}

	//
	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.remove(fragment);
		transaction.commitAllowingStateLoss();
	}

	//
	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		if (myCar != null) {
			transaction.hide(myCar);
		}
		if (myCarAdd != null) {
			transaction.hide(myCarAdd);
		}

		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
