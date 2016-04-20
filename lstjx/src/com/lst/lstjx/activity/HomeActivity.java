package com.lst.lstjx.activity;

import com.lst.lstjx.fragment.HomeFragment;
import com.lst.yuewo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;


/**
 * 首页
 * author describe parameter return
 */
public class HomeActivity extends FragmentActivity {

	private HomeFragment homeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homes);
		FrameActivity.getInstance().addActivity(this);
		initView();
	}
	

	private void initView() {
		homeFragment = new HomeFragment();
		addFragment(homeFragment);
		showFragment(homeFragment);
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.home_view, fragment);
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
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
