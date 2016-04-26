package com.lst.lstjx.activity;

import com.lst.lstjx.fragment.MyGroupListFragment;
import com.lst.yuewo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * author        
 * 我的群组
 * return
 */
public class GroupActivity extends FragmentActivity {
	private MyGroupListFragment myDeal;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_qun);
		myDeal = new MyGroupListFragment();
		addFragment(myDeal);
		showFragment(myDeal);
		
	}
	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.show_mQunView, fragment);
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
		

		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}
}
