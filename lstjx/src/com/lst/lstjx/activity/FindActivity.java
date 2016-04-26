package com.lst.lstjx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lst.lstjx.fragment.AllBuyDynamicFragment;
import com.lst.lstjx.fragment.AllCarSxFragment;
import com.lst.lstjx.fragment.AllGoodsSxFragment;
import com.lst.lstjx.fragment.AllSellDynamicFragment;
import com.lst.lstjx.fragment.FriendBuyDynamicFragment;
import com.lst.lstjx.fragment.FriendCarFragment;
import com.lst.lstjx.fragment.FriendGoodsFragment;
import com.lst.lstjx.fragment.FriendSellDynamicFragment;
import com.lst.lstjx.fragment.QunLiaoFragment;
import com.lst.yuewo.R;

/**
 * 首页动态的框架
 */
public class FindActivity extends FragmentActivity implements OnClickListener {

	// 动态的页面

	private AllGoodsSxFragment allGoodsSx;
	private FriendGoodsFragment friendGoods;
	private AllBuyDynamicFragment allBuyDynamic;
	private FriendBuyDynamicFragment friendBuyDynamic;
	private AllSellDynamicFragment allSellDynamic;
	private FriendSellDynamicFragment friendSellDynamic;
	private AllCarSxFragment allCarSx;
	private FriendCarFragment friendCar;
	private TextView onclick_tv_dynamic, onclick_tv_goods, main_title_tv,
			onclick_tv_qunliao;
	private QunLiaoFragment qun;
	private Button bn_fb ;
	private ImageView prodetail_goback, shaixuan;
	private String flg, starting, ending, time;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		FrameActivity.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		onclick_tv_dynamic = (TextView) findViewById(R.id.onclick_tv_dynamic);
		main_title_tv = (TextView) findViewById(R.id.main_title_tv);
		prodetail_goback = (ImageView) findViewById(R.id.prodetail_goback);
		shaixuan = (ImageView) findViewById(R.id.shaixuan);
		shaixuan.setOnClickListener(this);
		bn_fb = (Button)findViewById(R.id.bn_fb);
		bn_fb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FindActivity.this,MoreActivity.class);
				startActivity(intent);
			}
		});
		onclick_tv_goods = (TextView) findViewById(R.id.onclick_tv_goods);
		onclick_tv_qunliao = (TextView) findViewById(R.id.onclick_tv_qunliao);
		prodetail_goback.setOnClickListener(this);
		onclick_tv_qunliao.setOnClickListener(this);
		onclick_tv_goods.setOnClickListener(this);
		onclick_tv_dynamic.setOnClickListener(this);
		Intent intent = getIntent();
		flg = intent.getStringExtra("TYBEFLG");
		if ("0".equals(flg)) {
			onclick_tv_goods.setText("好友动态");
			onclick_tv_dynamic.setText("卖家动态");
			main_title_tv.setText("买家");
			allSellDynamic = new AllSellDynamicFragment();
			addFragment(allSellDynamic);
			showFragment(allSellDynamic);

		} else if ("1".equals(flg)) {
			onclick_tv_goods.setText("好友动态");
			onclick_tv_dynamic.setText("买家动态");
			main_title_tv.setText("卖家");
			allBuyDynamic = new AllBuyDynamicFragment();
			addFragment(allBuyDynamic);
			showFragment(allBuyDynamic);
		} else if ("2".equals(flg)) {

			onclick_tv_goods.setText("好友动态");
			onclick_tv_dynamic.setText("最新车源");
			main_title_tv.setText("发货");
			shaixuan.setVisibility(View.VISIBLE);

			allCarSx = new AllCarSxFragment(starting, ending, time);
			addFragment(allCarSx);
			showFragment(allCarSx);
		} else if ("3".equals(flg)) {
			onclick_tv_goods.setText("好友动态");
			onclick_tv_dynamic.setText("最新货源");
			main_title_tv.setText("运货");
			shaixuan.setVisibility(View.VISIBLE);

			allGoodsSx = new AllGoodsSxFragment(starting, ending, time);
			addFragment(allGoodsSx);
			showFragment(allGoodsSx);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	// @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.onclick_tv_dynamic:
			if ("0".equals(flg)) {
				if (allSellDynamic == null) {
					allSellDynamic = new AllSellDynamicFragment();
					addFragment(allSellDynamic);
					showFragment(allSellDynamic);
				} else {
					showFragment(allSellDynamic);
				}
			} else if ("1".equals(flg)) {
				if (allBuyDynamic == null) {
					allBuyDynamic = new AllBuyDynamicFragment();
					addFragment(allBuyDynamic);
					showFragment(allBuyDynamic);
				} else {
					showFragment(allBuyDynamic);
				}
			} else if ("2".equals(flg)) {
				shaixuan.setVisibility(View.VISIBLE);

				if (allCarSx == null) {
					allCarSx = new AllCarSxFragment(starting, ending, time);
					addFragment(allCarSx);
					showFragment(allCarSx);
				} else {
					showFragment(allCarSx);
				}

			} else if ("3".equals(flg)) {
				shaixuan.setVisibility(View.VISIBLE);

				if (allGoodsSx == null) {

					allGoodsSx = new AllGoodsSxFragment(starting, ending, time);
					addFragment(allGoodsSx);
					showFragment(allGoodsSx);
				} else {
					showFragment(allGoodsSx);

				}

			}
			onclick_tv_goods.setTextColor(getResources()
					.getColor(R.color.black));

			onclick_tv_qunliao.setTextColor(getResources().getColor(
					R.color.black));

			onclick_tv_dynamic.setTextColor(getResources().getColor(
					R.color.titele_color_more));

			break;
		case R.id.onclick_tv_goods:
			if ("0".equals(flg)) {
				if (friendSellDynamic == null) {
					friendSellDynamic = new FriendSellDynamicFragment();
					addFragment(friendSellDynamic);
					showFragment(friendSellDynamic);
					System.out.println("我的好友卖货动态");
				} else {
					showFragment(friendSellDynamic);
				}
			} else if ("1".equals(flg)) {
				if (friendBuyDynamic == null) {
					friendBuyDynamic = new FriendBuyDynamicFragment();
					addFragment(friendBuyDynamic);
					showFragment(friendBuyDynamic);
				} else {
					showFragment(friendBuyDynamic);
				}
			} else if ("2".equals(flg)) {
				if (friendCar == null) {
					friendCar = new FriendCarFragment();
					addFragment(friendCar);
					showFragment(friendCar);
				} else {
					showFragment(friendCar);
				}
			} else if ("3".equals(flg)) {
				if (friendGoods == null) {
					friendGoods = new FriendGoodsFragment();
					addFragment(friendGoods);
					showFragment(friendGoods);
				} else {
					showFragment(friendGoods);
				}
			}
			onclick_tv_qunliao.setTextColor(getResources().getColor(
					R.color.black));

			onclick_tv_goods.setTextColor(getResources().getColor(
					R.color.titele_color_more));
			onclick_tv_dynamic.setTextColor(getResources().getColor(
					R.color.black));
			break;
		case R.id.onclick_tv_qunliao:
			if (qun == null) {
				qun = new QunLiaoFragment();
				addFragment(qun);
				showFragment(qun);
			} else {
				showFragment(qun);
			}
			onclick_tv_qunliao.setTextColor(getResources().getColor(
					R.color.titele_color_more));

			onclick_tv_goods.setTextColor(getResources()
					.getColor(R.color.black));
			onclick_tv_dynamic.setTextColor(getResources().getColor(
					R.color.black));
			break;
		case R.id.prodetail_goback:
			finish();
			break;
		case R.id.shaixuan:
			Intent intent = new Intent(FindActivity.this, SxActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) { // 处理代码在此地
					starting = bundle.getString("starting");
					ending = bundle.getString("ending");
					time = bundle.getString("time");

					System.out.println("----------" + starting + "/" + ending
							+ "/" + time);
					if ("2".equals(flg)) {
						allCarSx = new AllCarSxFragment(starting, ending, time);
						addFragment(allCarSx);
						showFragment(allCarSx);
					} else if ("3".equals(flg)) {
						allGoodsSx = new AllGoodsSxFragment(starting, ending,
								time);
						addFragment(allGoodsSx);
						showFragment(allGoodsSx);
					}
				}
			}
		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.show_view, fragment);
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
		if ("1".equals(flg)) {
			if (allBuyDynamic != null) {
				transaction.hide(allBuyDynamic);
			}
			if (qun != null) {
				transaction.hide(qun);
			}
			if (friendBuyDynamic != null) {
				transaction.hide(friendBuyDynamic);
			}
		} else if ("0".equals(flg)) {
			if (allSellDynamic != null) {
				transaction.hide(allSellDynamic);
			}
			if (qun != null) {
				transaction.hide(qun);
			}
			if (friendSellDynamic != null) {
				transaction.hide(friendSellDynamic);
			}
		} else if ("2".equals(flg)) {
			if (allCarSx != null) {
				transaction.hide(allCarSx);
			}

			if (qun != null) {
				transaction.hide(qun);
			}
			if (friendCar != null) {
				transaction.hide(friendCar);
			}
		} else if ("3".equals(flg)) {
			if (allGoodsSx != null) {
				transaction.hide(allGoodsSx);
			}
			if (qun != null) {
				transaction.hide(qun);
			}
			if (friendGoods != null) {
				transaction.hide(friendGoods);
			}
		}
		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

}
