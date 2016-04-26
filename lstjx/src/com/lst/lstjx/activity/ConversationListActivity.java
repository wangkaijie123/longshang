<<<<<<< HEAD
package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.database.UserInfosDao;
import com.lst.lstjx.view.LoadingDialog;
import com.lst.yuewo.R;

/**
 * 会话界面 author describe parameter return
 */
public class ConversationListActivity extends FragmentActivity implements
		OnClickListener {
	private UserInfosDao mUserInfosDao;
	/**
	 * 对方id
	 */
	private Conversation.ConversationType mConversationType;
	private String targetId;
	private Fragment mConversationFragment;
	private ImageView chat_title_btn;
	private PopupWindow mPopupwindow;
	private ListView listView_pop;
	private List<String> mPopList = null;
	private LoadingDialog mDialog;
	private LinearLayout lin_chat, lin_add, lin_grop;
	private static final String TAG = ConversationListActivity.class
			.getSimpleName();
	private ConversationListFragment listFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		FrameActivity.getInstance().addActivity(this);

		initView();
		
		enterFragment();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		System.out
				.println("_____________ConversationListActivity_________onDetachedFromWindow______________");

	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

	}

	private void initView() {
		// TODO Auto-generated method stub
		chat_title_btn = (ImageView) findViewById(R.id.chat_title_btn);
		chat_title_btn.setOnClickListener(this);
		initmPopupWindowView();
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra("DEMO_COVERSATIONTYPE")
				&& intent.hasExtra("DEMO_TARGETID")
				&& intent.hasExtra("DEMO_COVERSATION")) {
			if (DemoContext.getInstance() != null) {
				String conversation = intent.getStringExtra("DEMO_COVERSATION");
				System.out.println("__________conversation___________"
						+ conversation);
				targetId = intent.getStringExtra("DEMO_TARGETID");
				String conversationType = intent
						.getStringExtra("DEMO_COVERSATIONTYPE");
				openConversationFragment(conversation, targetId,
						conversationType);
			}
		}

	}


	/**
	 * 收到 push 以后，打开会话页面
	 * 
	 * @param conversation
	 * @param targetId
	 * @param conversationType
	 */
	private void openConversationFragment(String conversation, String targetId,
			String conversationType) {

		String tag;
		if (conversation.equals("conversation")) {
			tag = "conversation";
			ConversationFragment conversationFragment = new ConversationFragment();
			Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
					.buildUpon().appendPath("conversation")
					.appendPath(conversationType.toLowerCase())
					.appendQueryParameter("targetId", targetId).build();
			conversationFragment.setUri(uri);
			mConversationType = Conversation.ConversationType
					.valueOf(conversationType);
			if (conversationFragment != null) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.add(R.id.de_content, conversationFragment, tag);
				transaction.addToBackStack(null).commitAllowingStateLoss();
			}
		}
	}

	public void initmPopupWindowView() {
		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popmenu, null,
				false);
		lin_chat = (LinearLayout) customView.findViewById(R.id.lin_chat);
		lin_add = (LinearLayout) customView.findViewById(R.id.lin_add);
		lin_grop = (LinearLayout) customView.findViewById(R.id.lin_grop);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		mPopupwindow = new PopupWindow(customView, 250,
				LayoutParams.WRAP_CONTENT);
		mPopupwindow.setFocusable(true);
		// 设置允许在外点击消失
		mPopupwindow.setOutsideTouchable(true);
		// 刷新状态（必须刷新否则无效）
		lin_chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ConversationListActivity.this,
						ChooseFriendToChat.class));
				mPopupwindow.dismiss();
			}
		});
		lin_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ConversationListActivity.this,
						SearchFriendActivity.class));
				mPopupwindow.dismiss();
			}
		});
		lin_grop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ConversationListActivity.this,
						CreatQunActivity.class));
				mPopupwindow.dismiss();

			}
		});

		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mPopupwindow != null && mPopupwindow.isShowing()) {
					mPopupwindow.dismiss();
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_title_btn:
			if (mPopupwindow != null && mPopupwindow.isShowing()) {
				mPopupwindow.dismiss();

			} else {
				// initmPopupWindowView();
				mPopupwindow.showAsDropDown(v, 10, 10);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 加载 会话列表 ConversationListFragment
	 */
	private void enterFragment() {
		if (mConversationFragment == null) {
			System.out.println("____________enterFragment__________");
			listFragment = ConversationListFragment.getInstance();
			Uri uri = Uri
					.parse("rong://" + getApplicationInfo().packageName)
					.buildUpon()
					.appendPath("conversationlist")
					.appendQueryParameter(
							Conversation.ConversationType.PRIVATE.getName(),
							"false") // 设置私聊会话是否聚合显示
					.appendQueryParameter(
							Conversation.ConversationType.GROUP.getName(),
							"false")// 群组
					.appendQueryParameter(
							Conversation.ConversationType.DISCUSSION.getName(),
							"false")
					// 讨论组
					.appendQueryParameter(
							Conversation.ConversationType.APP_PUBLIC_SERVICE
									.getName(),
							"false")// 应用公众服务。
					.appendQueryParameter(
							Conversation.ConversationType.PUBLIC_SERVICE
									.getName(),
							"false")// 公共服务号
					.appendQueryParameter(
							Conversation.ConversationType.SYSTEM.getName(),
							"false")// 系统
					.build();
			listFragment.setUri(uri);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.add(R.id.show_view_chat, listFragment);
			transaction.show(listFragment);
			transaction.commitAllowingStateLoss();

		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.show_view_chat, fragment);
		transaction.commitAllowingStateLoss();
	}

	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.remove(fragment);
		transaction.commitAllowingStateLoss();
	}

	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

	/**
	 * 收到push消息后做重连，重新连接融云
	 * 
	 * @param token
	 */
	private void reconnect(String token) {

		mDialog = new LoadingDialog(this);
		mDialog.setCancelable(false);
		mDialog.setText("正在连接中...");
		Log.e(TAG, "----reconnect------:" + token);
		mDialog.show();

		try {
			Log.e(TAG, "----reconnect----try--111111:");
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onTokenIncorrect() {
					Log.e(TAG, "----token－－－－－onTokenIncorrect------:");
				}

				@Override
				public void onSuccess(String userId) {
					Log.e(TAG, "----token－－－－－onSuccess------:");

				}

				@Override
				public void onError(RongIMClient.ErrorCode e) {
					Log.e(TAG, "----token－－－－－onError------:");

				}
			});
			Log.e(TAG, "----reconnect----try--222222:");
		} catch (Exception e) {
			Log.e(TAG, "----reconnect----catch--:");

		}

	}

}
=======
package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.database.UserInfosDao;
import com.lst.lstjx.view.LoadingDialog;
import com.lst.yuewo.R;

/**
 * 会话界面 author describe parameter return
 */
public class ConversationListActivity extends FragmentActivity implements
		OnClickListener {
	private UserInfosDao mUserInfosDao;
	/**
	 * 对方id
	 */
	private Conversation.ConversationType mConversationType;
	private String targetId;
	private Fragment mConversationFragment;
	private ImageView chat_title_btn;
	private PopupWindow mPopupwindow;
	private ListView listView_pop;
	private List<String> mPopList = null;
	private LoadingDialog mDialog;
	private LinearLayout lin_chat, lin_add, lin_grop;
	private static final String TAG = ConversationListActivity.class
			.getSimpleName();
	private ConversationListFragment listFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		FrameActivity.getInstance().addActivity(this);

		initView();
		
		enterFragment();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		System.out
				.println("_____________ConversationListActivity_________onDetachedFromWindow______________");

	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

	}

	private void initView() {
		// TODO Auto-generated method stub
		chat_title_btn = (ImageView) findViewById(R.id.chat_title_btn);
		chat_title_btn.setOnClickListener(this);
		initmPopupWindowView();
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra("DEMO_COVERSATIONTYPE")
				&& intent.hasExtra("DEMO_TARGETID")
				&& intent.hasExtra("DEMO_COVERSATION")) {
			if (DemoContext.getInstance() != null) {
				String conversation = intent.getStringExtra("DEMO_COVERSATION");
				System.out.println("__________conversation___________"
						+ conversation);
				targetId = intent.getStringExtra("DEMO_TARGETID");
				String conversationType = intent
						.getStringExtra("DEMO_COVERSATIONTYPE");
				openConversationFragment(conversation, targetId,
						conversationType);
			}
		}

	}


	/**
	 * 收到 push 以后，打开会话页面
	 * 
	 * @param conversation
	 * @param targetId
	 * @param conversationType
	 */
	private void openConversationFragment(String conversation, String targetId,
			String conversationType) {

		String tag;
		if (conversation.equals("conversation")) {
			tag = "conversation";
			ConversationFragment conversationFragment = new ConversationFragment();
			Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
					.buildUpon().appendPath("conversation")
					.appendPath(conversationType.toLowerCase())
					.appendQueryParameter("targetId", targetId).build();
			conversationFragment.setUri(uri);
			mConversationType = Conversation.ConversationType
					.valueOf(conversationType);
			if (conversationFragment != null) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.add(R.id.de_content, conversationFragment, tag);
				transaction.addToBackStack(null).commitAllowingStateLoss();
			}
		}
	}

	public void initmPopupWindowView() {
		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popmenu, null,
				false);
		lin_chat = (LinearLayout) customView.findViewById(R.id.lin_chat);
		lin_add = (LinearLayout) customView.findViewById(R.id.lin_add);
		lin_grop = (LinearLayout) customView.findViewById(R.id.lin_grop);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		mPopupwindow = new PopupWindow(customView, 250,
				LayoutParams.WRAP_CONTENT);
		mPopupwindow.setFocusable(true);
		// 设置允许在外点击消失
		mPopupwindow.setOutsideTouchable(true);
		// 刷新状态（必须刷新否则无效）
		lin_chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ConversationListActivity.this,
						ChooseFriendToChat.class));
				mPopupwindow.dismiss();
			}
		});
		lin_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ConversationListActivity.this,
						SearchFriendActivity.class));
				mPopupwindow.dismiss();
			}
		});
		lin_grop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ConversationListActivity.this,
						CreatQunActivity.class));
				mPopupwindow.dismiss();

			}
		});

		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mPopupwindow != null && mPopupwindow.isShowing()) {
					mPopupwindow.dismiss();
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_title_btn:
			if (mPopupwindow != null && mPopupwindow.isShowing()) {
				mPopupwindow.dismiss();

			} else {
				// initmPopupWindowView();
				mPopupwindow.showAsDropDown(v, 10, 10);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 加载 会话列表 ConversationListFragment
	 */
	private void enterFragment() {
		if (mConversationFragment == null) {
			System.out.println("____________enterFragment__________");
			listFragment = ConversationListFragment.getInstance();
			Uri uri = Uri
					.parse("rong://" + getApplicationInfo().packageName)
					.buildUpon()
					.appendPath("conversationlist")
					.appendQueryParameter(
							Conversation.ConversationType.PRIVATE.getName(),
							"false") // 设置私聊会话是否聚合显示
					.appendQueryParameter(
							Conversation.ConversationType.GROUP.getName(),
							"false")// 群组
					.appendQueryParameter(
							Conversation.ConversationType.DISCUSSION.getName(),
							"false")
					// 讨论组
					.appendQueryParameter(
							Conversation.ConversationType.APP_PUBLIC_SERVICE
									.getName(),
							"false")// 应用公众服务。
					.appendQueryParameter(
							Conversation.ConversationType.PUBLIC_SERVICE
									.getName(),
							"false")// 公共服务号
					.appendQueryParameter(
							Conversation.ConversationType.SYSTEM.getName(),
							"false")// 系统
					.build();
			listFragment.setUri(uri);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.add(R.id.show_view_chat, listFragment);
			transaction.show(listFragment);
			transaction.commitAllowingStateLoss();

		}
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.show_view_chat, fragment);
		transaction.commitAllowingStateLoss();
	}

	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.remove(fragment);
		transaction.commitAllowingStateLoss();
	}

	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.show(fragment);
		transaction.commitAllowingStateLoss();

	}

	/**
	 * 收到push消息后做重连，重新连接融云
	 * 
	 * @param token
	 */
	private void reconnect(String token) {

		mDialog = new LoadingDialog(this);
		mDialog.setCancelable(false);
		mDialog.setText("正在连接中...");
		Log.e(TAG, "----reconnect------:" + token);
		mDialog.show();

		try {
			Log.e(TAG, "----reconnect----try--111111:");
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onTokenIncorrect() {
					Log.e(TAG, "----token－－－－－onTokenIncorrect------:");
				}

				@Override
				public void onSuccess(String userId) {
					Log.e(TAG, "----token－－－－－onSuccess------:");

				}

				@Override
				public void onError(RongIMClient.ErrorCode e) {
					Log.e(TAG, "----token－－－－－onError------:");

				}
			});
			Log.e(TAG, "----reconnect----try--222222:");
		} catch (Exception e) {
			Log.e(TAG, "----reconnect----catch--:");

		}

	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
