package com.lst.lstjx.activity;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyWlAddFragment;
import com.lst.lstjx.fragment.MyWlFragment;
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
public class MyWlActivity extends FragmentActivity {
	private ImageView goback_wd;
	private String userid;
	private boolean b;
	private MyWlFragment myWl;
	private MyWlAddFragment myWlAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wl_no);
		userid = SharePrefUtil.getString(MyWlActivity.this, "userId", null);

		postInfo();
		intview();
	}

	private void intview() {
		b = SharePrefUtil.getBoolean(MyWlActivity.this, "hasWlData", false);

		System.out.println("=======b======" + b);
		if (!b) {
			myWlAdd = new MyWlAddFragment();
			addFragment(myWlAdd);
			showFragment(myWlAdd);
		} else {
			myWl = new MyWlFragment();
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
		params.put("uid", userid);// 动态id
		client.post(MyWlActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);
						Gson gson = new Gson();
						DynamicBean json = gson.fromJson(arg2,
								DynamicBean.class);
						if (json.getData() == null || json.getData().equals("")) {
							SharePrefUtil.saveBoolean(MyWlActivity.this,
									"hasWlData", false);
						} else {
							SharePrefUtil.saveBoolean(MyWlActivity.this,
									"hasWlData", true);
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
