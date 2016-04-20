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
import com.lst.lstjx.fragment.FriendCarAddFragment;
import com.lst.lstjx.fragment.FriendCarFragment;
import com.lst.lstjx.fragment.FriendCarsFragment;
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
public class FriendCarActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String hisuserid;
	private boolean b;
	private FriendCarsFragment myCar;
	private FriendCarAddFragment myCarAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_car_no);
		Intent intent = getIntent();
		hisuserid = intent.getStringExtra("hisuserid");
		postInfo();
		intview();
	}

	private void intview() {
		b = SharePrefUtil.getBoolean(FriendCarActivity.this, "hHasClData", false);
		if (!b) {
			myCarAdd = new FriendCarAddFragment();
			addFragment(myCarAdd);
			showFragment(myCarAdd);
		} else {
			myCar = new FriendCarsFragment(hisuserid);
			addFragment(myCar);
			showFragment(myCar);
		}
		goback_wd = (ImageView) findViewById(R.id.shopcar_go_back);
		
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
		params.put("uid", hisuserid);// 动态id
		client.post(FriendCarActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(FriendCarActivity.this,
									"hHasClData", false);
						} else {
							SharePrefUtil.saveBoolean(FriendCarActivity.this,
									"hHasClData", true);
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
