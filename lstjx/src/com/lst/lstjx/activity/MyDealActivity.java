<<<<<<< HEAD
package com.lst.lstjx.activity;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyDealAddFragment;
import com.lst.lstjx.fragment.MyDealFragment;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
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
public class MyDealActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String userid;
	private boolean b;
	private MyDealFragment myDeal;
	private MyDealAddFragment myDealAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_deal_no);
		intview();
		postInfo();

	}

	private void intview() {
		userid = SharePrefUtil.getString(MyDealActivity.this, "userId", null);
		b = SharePrefUtil.getBoolean(MyDealActivity.this, "hasData", false);

		if (!b) {
			myDealAdd = new MyDealAddFragment();
			addFragment(myDealAdd);
			showFragment(myDealAdd);
		} else {
			myDeal = new MyDealFragment();
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
		params.put("uid", userid);// 动态id
		client.post(MyDealActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(MyDealActivity.this,
									"hasData", false);
						} else {
							SharePrefUtil.saveBoolean(MyDealActivity.this,
									"hasData", true);
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
import com.lst.lstjx.fragment.MyDealAddFragment;
import com.lst.lstjx.fragment.MyDealFragment;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
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
public class MyDealActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String userid;
	private boolean b;
	private MyDealFragment myDeal;
	private MyDealAddFragment myDealAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_deal_no);
		intview();
		postInfo();

	}

	private void intview() {
		userid = SharePrefUtil.getString(MyDealActivity.this, "userId", null);
		b = SharePrefUtil.getBoolean(MyDealActivity.this, "hasData", false);

		if (!b) {
			myDealAdd = new MyDealAddFragment();
			addFragment(myDealAdd);
			showFragment(myDealAdd);
		} else {
			myDeal = new MyDealFragment();
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
		params.put("uid", userid);// 动态id
		client.post(MyDealActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(MyDealActivity.this,
									"hasData", false);
						} else {
							SharePrefUtil.saveBoolean(MyDealActivity.this,
									"hasData", true);
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
