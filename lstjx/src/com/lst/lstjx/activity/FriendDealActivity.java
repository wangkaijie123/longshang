<<<<<<< HEAD
package com.lst.lstjx.activity;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.FriendDealAddFragment;
import com.lst.lstjx.fragment.FriendDealFragment;
import com.lst.lstjx.fragment.MyDealAddFragment;
import com.lst.lstjx.fragment.MyDealFragment;
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
public class FriendDealActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String userid,hisuserid;
	private boolean b;
	private FriendDealFragment myDeal;
	private FriendDealAddFragment myDealAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_deal_no);
		Intent intent = getIntent();
		hisuserid = intent.getStringExtra("hisuserid");
		postInfo();
		intview();

	}

	private void intview() {
		b = SharePrefUtil.getBoolean(FriendDealActivity.this, "hHasData", false);

		if (!b) {
			myDealAdd = new FriendDealAddFragment();
			addFragment(myDealAdd);
			showFragment(myDealAdd);
		} else {
			myDeal = new FriendDealFragment(hisuserid);
			addFragment(myDeal);
			showFragment(myDeal);
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
		client.post(FriendDealActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(FriendDealActivity.this,
									"hHasData", false);
						} else {
							SharePrefUtil.saveBoolean(FriendDealActivity.this,
									"hHasData", true);
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
		transaction.add(R.id.show_mview, fragment);
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

		if (myDeal != null) {
			transaction.hide(myDeal);
		}
		if (myDealAdd != null) {
			transaction.hide(myDealAdd);
		}

		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
=======
package com.lst.lstjx.activity;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.FriendDealAddFragment;
import com.lst.lstjx.fragment.FriendDealFragment;
import com.lst.lstjx.fragment.MyDealAddFragment;
import com.lst.lstjx.fragment.MyDealFragment;
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
public class FriendDealActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String userid,hisuserid;
	private boolean b;
	private FriendDealFragment myDeal;
	private FriendDealAddFragment myDealAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_deal_no);
		Intent intent = getIntent();
		hisuserid = intent.getStringExtra("hisuserid");
		postInfo();
		intview();

	}

	private void intview() {
		b = SharePrefUtil.getBoolean(FriendDealActivity.this, "hHasData", false);

		if (!b) {
			myDealAdd = new FriendDealAddFragment();
			addFragment(myDealAdd);
			showFragment(myDealAdd);
		} else {
			myDeal = new FriendDealFragment(hisuserid);
			addFragment(myDeal);
			showFragment(myDeal);
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
		client.post(FriendDealActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(FriendDealActivity.this,
									"hHasData", false);
						} else {
							SharePrefUtil.saveBoolean(FriendDealActivity.this,
									"hHasData", true);
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
		transaction.add(R.id.show_mview, fragment);
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

		if (myDeal != null) {
			transaction.hide(myDeal);
		}
		if (myDealAdd != null) {
			transaction.hide(myDealAdd);
		}

		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
