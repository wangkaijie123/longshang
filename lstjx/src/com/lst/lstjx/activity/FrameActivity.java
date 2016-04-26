package com.lst.lstjx.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lst.lstjx.app.App;
import com.lst.yuewo.R;

/**
 * 软件框架界面
 * 
 * */
public class FrameActivity extends ActivityGroup {
	public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
	public static final String ACTION_DMEO_GROUP_MESSAGE = "action_demo_group_message";
	public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
	private LinearLayout linear_chat, linear_contact, linear_more, linear_find,
			linear_my;
	private ImageView linear_chat_img, linear_contact_img, linear_more_img,
			linear_find_img, linear_my_img;
	private TextView linear_chat_tv, linear_contact_tv, linear_more_text,
			linear_find_tv, linear_my_tv;
	private List<View> list = new ArrayList<View>();// 相当于数据源
	private View view = null;
	private View view1 = null;
	private View view2 = null;
	private View view3 = null;
	private View view4 = null;
	private android.support.v4.view.ViewPager mViewPager;
	private PagerAdapter pagerAdapter = null;// 数据源和viewpager之间的桥梁
	private List<Activity> activityList = new LinkedList<Activity>();
	private static FrameActivity instance;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		App.getInstance().addActivity(FrameActivity.this);
		initView();

	}

	// 初始化控件
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.FramePager);
		// 查找以linearlayout为按钮作用的控件
		linear_chat = (LinearLayout) findViewById(R.id.linear_chat);
		linear_contact = (LinearLayout) findViewById(R.id.linear_contact);
		linear_more = (LinearLayout) findViewById(R.id.linear_more);
		linear_find = (LinearLayout) findViewById(R.id.linear_find);
		linear_my = (LinearLayout) findViewById(R.id.linear_my);
		// 查找linearlayout中的imageview
		linear_chat_img = (ImageView) findViewById(R.id.linear_chat_img);
		linear_contact_img = (ImageView) findViewById(R.id.linear_contact_img);
		linear_more_img = (ImageView) findViewById(R.id.linear_more_img);
		linear_find_img = (ImageView) findViewById(R.id.linear_find_img);
		linear_my_img = (ImageView) findViewById(R.id.linear_my_img);
		// 查找linearlayout中的textview
		linear_chat_tv = (TextView) findViewById(R.id.linear_chat_tv);
		linear_contact_tv = (TextView) findViewById(R.id.linear_contact_tv);
		linear_more_text = (TextView) findViewById(R.id.linear_more_text);
		linear_find_tv = (TextView) findViewById(R.id.linear_find_tv);
		linear_my_tv = (TextView) findViewById(R.id.linear_my_tv);
		mViewPager.setOffscreenPageLimit(3); // 设置 viewpager要缓存的页面数 默认的好像是两个？
												// 这个有待验证，缓存挺好，恩恩
		createView();
		// 写一个内部类pageradapter
		pagerAdapter = new PagerAdapter() {
			// 判断再次添加的view和之前的view 是否是同一个view
			// arg0新添加的view，arg1之前的
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			// 返回数据源长度
			@Override
			public int getCount() {
				return list.size();
			}

			// 销毁被滑动走的view
			/**
			 * ViewGroup 代表了我们的viewpager 相当于activitygroup当中的view容器， 添加之前先移除。
			 * position 第几条数据 Object 被移出的view
			 * */
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// 移除view
				container.removeView(list.get(position));
			}

			/**
			 * instantiateItem viewpager要现实的view ViewGroup viewpager position
			 * 第几条数据
			 * */
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// 获取view添加到容器当中，并返回
				View v = list.get(position);
				container.addView(v);
				return v;
			}
		};

		// 设置我们的adapter
		mViewPager.setAdapter(pagerAdapter);
		MyBtnOnclick mytouchlistener = new MyBtnOnclick();
		linear_chat.setOnClickListener(mytouchlistener);
		linear_contact.setOnClickListener(mytouchlistener);
		linear_more.setOnClickListener(mytouchlistener);
		linear_find.setOnClickListener(mytouchlistener);
		linear_my.setOnClickListener(mytouchlistener);
		// 设置viewpager界面切换监听,监听viewpager切换第几个界面以及滑动的
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			// arg0 获取 viewpager里面的界面切换到第几个的
			@Override
			public void onPageSelected(int arg0) {
				// 先清除按钮样式
				initBottemBtn();
				// 按照对应的view的tag来判断到底切换到哪个界面。
				// 更改对应的button状态

				int flag = (Integer) list.get((arg0)).getTag();
				if (flag == 0) {
					linear_find_img.setImageResource(R.drawable.home_pressed);
					linear_find_tv.setTextColor(Color.parseColor("#4CC2FF"));
				} else if (flag == 1) {
					linear_chat_img.setImageResource(R.drawable.weixin_pressed);
					linear_chat_tv.setTextColor(Color.parseColor("#4CC2FF"));
				}
				// else if (flag == 2) {
				// linear_more_img.setImageResource(R.drawable.more_press);
				// linear_more_text.setTextColor(Color.parseColor("#4CC2FF"));
				// }
				else if (flag == 2) {
					linear_contact_img
							.setImageResource(R.drawable.contact_list_pressed);
					linear_contact_tv.setTextColor(Color.parseColor("#4CC2FF"));
				} else if (flag == 3) {
					linear_my_img.setImageResource(R.drawable.profile_pressed);
					linear_my_tv.setTextColor(Color.parseColor("#4CC2FF"));
				}
			}

			/**
			 * 监听页面滑动的 arg0 表示当前滑动的view arg1 表示滑动的百分比 arg2 表示滑动的距离
			 * */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			/**
			 * 监听滑动状态 arg0 表示我们的滑动状态 0:默认状态 1:滑动状态 2:滑动停止
			 * */
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// 把viewpager里面要显示的view实例化出来，并且把相关的view添加到一个list当中
	@SuppressWarnings("deprecation")
	private void createView() {
		view = FrameActivity.this
				.getLocalActivityManager()
				.startActivity("my",
						new Intent(FrameActivity.this, HomeActivity.class))
				.getDecorView();
		view.setTag(0);
		list.add(view);
		view1 = this
				.getLocalActivityManager()
				.startActivity(
						"search",
						new Intent(FrameActivity.this,
								ConversationListActivity.class)).getDecorView();
		// 用来更改下面button的样式的标志
		view1.setTag(1);
		list.add(view1);

		// view2 = FrameActivity.this
		// .getLocalActivityManager()
		// .startActivity("sign",
		// new Intent(FrameActivity.this, MoreActivity.class))
		// .getDecorView();
		// view2.setTag(2);
		// list.add(view2);
		view3 = FrameActivity.this
				.getLocalActivityManager()
				.startActivity(
						"sign",
						new Intent(FrameActivity.this,
								ContactListActivity.class)).getDecorView();
		view3.setTag(2);
		list.add(view3);
		view4 = FrameActivity.this.getLocalActivityManager()
				.startActivity("tuan", // "more",
						new Intent(FrameActivity.this, MyActivity.class))
				.getDecorView();
		view4.setTag(3);
		list.add(view4);
	}


	/**
	 * 用linearlayout作为按钮的监听事件
	 * */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	private class MyBtnOnclick implements View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			int mBtnid = arg0.getId();
			switch (mBtnid) {
			case R.id.linear_find:
				mViewPager.setCurrentItem(0);
				initBottemBtn();
				linear_find_img.setImageResource(R.drawable.home_pressed);
				linear_find_tv.setTextColor(Color.parseColor("#4CC2FF"));
				break;
			case R.id.linear_chat:
				// //设置我们的viewpager跳转那个界面0这个参数和我们的list相关,相当于list里面的下标
				mViewPager.setCurrentItem(1);
				initBottemBtn();
				linear_chat_img.setImageResource(R.drawable.weixin_pressed);
				linear_chat_tv.setTextColor(Color.parseColor("#4CC2FF"));
				break;

			case R.id.linear_more:
				// mViewPager.setCurrentItem(2);
				// initBottemBtn();
				Intent intent = new Intent(FrameActivity.this,
						MoreActivity.class);
				startActivityForResult(intent, 1);
				linear_more_img.setImageResource(R.drawable.more_press);
				linear_more_text.setTextColor(Color.parseColor("#4CC2FF"));
				break;
			case R.id.linear_contact:
				mViewPager.setCurrentItem(2);
				initBottemBtn();
				linear_contact_img
						.setImageResource(R.drawable.contact_list_pressed);
				linear_contact_tv.setTextColor(Color.parseColor("#4CC2FF"));
				break;
			case R.id.linear_my:
				mViewPager.setCurrentItem(3);
				initBottemBtn();
				linear_my_img.setImageResource(R.drawable.profile_pressed);
				linear_my_tv.setTextColor(Color.parseColor("#4CC2FF"));
				break;
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			linear_more_img.setImageResource(R.drawable.more_normal);
			linear_more_text.setTextColor(Color.parseColor("#999999"));

		}
	}

	/**
	 * 初始化控件的颜色
	 * */
	private void initBottemBtn() {
		linear_chat_img.setImageResource(R.drawable.bottom_1_selector);
		linear_contact_img.setImageResource(R.drawable.bottom_2_selector);
		linear_more_img.setImageResource(R.drawable.more_selector);
		linear_find_img.setImageResource(R.drawable.bottom_3_selector);
		linear_my_img.setImageResource(R.drawable.bottom_4_selector);
		linear_chat_tv.setTextColor(getResources().getColor(R.color.mGray));
		linear_contact_tv.setTextColor(getResources().getColor(R.color.mGray));
		linear_more_text.setTextColor(getResources().getColor(R.color.mGray));
		linear_find_tv.setTextColor(getResources().getColor(R.color.mGray));
		linear_my_tv.setTextColor(getResources().getColor(R.color.mGray));
	}

	// 单例模式中获取唯一的MyApplication实例
	public static FrameActivity getInstance() {
		if (null == instance) {
			instance = new FrameActivity();
		}
		return instance;

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		// TODO Auto-generated method stub
		for (Activity activity : activityList) {
			activity.finish();
			System.out.println("________________activityList____________"
					+ activityList.size());
		}

	}

	// on backpress 这个方法主要是 解决了 按back无法返回的问题的 原因是 onsaveinstens 之后无法响应返回键的点击事件
	@Override
	public void onBackPressed() {
		finish();
	}
}
