package com.lst.lstjx.activity;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.FriendWlAddFragment;
import com.lst.lstjx.fragment.FriendWlFragment;
import com.lst.lstjx.fragment.MyWlAddFragment;
import com.lst.lstjx.fragment.MyWlFragment;
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
public class FriendWlActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String hisuserid;
	private boolean b;
	private FriendWlFragment myWl;
	private FriendWlAddFragment myWlAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_wl_no);
		Intent intent = getIntent();
		hisuserid = intent.getStringExtra("hisuserid");
		postInfo();
		intview();
	}

	private void intview() {
		b = SharePrefUtil.getBoolean(FriendWlActivity.this, "hHasWlData", false);

		System.out.println("=======b======" + b);
		if (!b) {
			myWlAdd = new FriendWlAddFragment();
			addFragment(myWlAdd);
			showFragment(myWlAdd);
		} else {
			myWl = new FriendWlFragment(hisuserid);
			addFragment(myWl);
			showFragment(myWl);
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
		client.post(FriendWlActivity.this, ConstantsUrl.my_fid_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);
						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(FriendWlActivity.this,
									"hHasWlData", false);
						} else {
							SharePrefUtil.saveBoolean(FriendWlActivity.this,
									"hHasWlData", true);
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
		transaction.add(R.id.show_mWlView, fragment);
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

		if (myWl != null) {
			transaction.hide(myWl);
		}
		if (myWlAdd != null) {
			transaction.hide(myWlAdd);
		}

		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
