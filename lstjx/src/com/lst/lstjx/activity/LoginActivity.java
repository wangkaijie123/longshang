package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.app.App;
import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.app.RongCloudEvent;
import com.lst.lstjx.bean.GetFriendBean;
import com.lst.lstjx.bean.GetGroupBean;
import com.lst.lstjx.bean.LoginBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.database.DBManager;
import com.lst.lstjx.database.GroupInfos;
import com.lst.lstjx.database.GroupInfosDao;
import com.lst.lstjx.database.UserInfos;
import com.lst.lstjx.database.UserInfosDao;
import com.lst.lstjx.model.ApiResult;
import com.lst.lstjx.model.Friends;
import com.lst.lstjx.model.Groups;
import com.lst.lstjx.model.User;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.EditTextHolder;
import com.lst.lstjx.view.LoadingDialog;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/1/30.
 */
public class LoginActivity extends BaseApiActivity implements
		View.OnClickListener, Handler.Callback,
		EditTextHolder.OnEditTextFocusChangeListener {
	private String userid;
	private String passWord;
	private GetFriendBean mGetFriendBean;
	private static final String TAG = "LoginActivity";
	/**
	 * 用户账户
	 */
	private EditText mUserNameEt;
	/**
	 * 密码
	 */
	private EditText mPassWordEt;
	/**
	 * 登录button
	 */
	private Button mSignInBt;
	/**
	 * 设备id
	 */
	private String mDeviceId;
	/**
	 * 忘记密码
	 */
	private TextView mFogotPassWord;
	/**
	 * 注册
	 */
	private TextView mRegister;
	/**
	 * 输入用户名删除按钮
	 */
	private FrameLayout mFrUserNameDelete;
	/**
	 * 输入密码删除按钮
	 */
	private FrameLayout mFrPasswordDelete;
	/**
	 * logo
	 */
	// private ImageView mLoginImg;
	/**
	 * 软键盘的控制
	 */
	private InputMethodManager mSoftManager;
	/**
	 * 是否展示title
	 */
	private RelativeLayout mIsShowTitle;
	/**
	 * 左侧title
	 */
	private TextView mLeftTitle;
	/**
	 * 右侧title
	 */
	private TextView mRightTitle;
	private LoginBean mLoginBean;

	private static final int REQUEST_CODE_REGISTER = 200;
	public static final String INTENT_IMAIL = "intent_email";
	public static final String INTENT_PASSWORD = "intent_password";
	private static final int HANDLER_LOGIN_SUCCESS = 1;
	private static final int HANDLER_LOGIN_FAILURE = 2;
	private static final int HANDLER_LOGIN_HAS_FOCUS = 3;
	private static final int HANDLER_LOGIN_HAS_NO_FOCUS = 4;
	// 登录
	protected static final int LOGIN = 0X45;
	protected static final int GETFRIENDLIST = 0x45623;
	protected static final int GETGROUPLIST = 0x78945;

	private LoadingDialog mDialog;
	private AbstractHttpRequest<User> loginHttpRequest;
	private AbstractHttpRequest<User> getTokenHttpRequest;
	private AbstractHttpRequest<Friends> getUserInfoHttpRequest;
	private AbstractHttpRequest<Groups> mGetMyGroupsRequest;

	private Handler mHandler;
	private List<User> mUserList;
	private List<ApiResult> mResultList;
	private ImageView mImgBackgroud;
	EditTextHolder mEditUserNameEt;
	EditTextHolder mEditPassWordEt;

	List<UserInfos> friendsList = new ArrayList<UserInfos>();
	UserInfosDao mUserInfosDao;
	String userName;
	private boolean isFirst = false;
	private boolean isSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.de_ac_login);
		initView();
		initData();
	}

	protected void initView() {

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mUserInfosDao = DBManager.getInstance(LoginActivity.this)
				.getDaoSession().getUserInfosDao();
		mUserNameEt = (EditText) findViewById(R.id.app_username_et);
		mPassWordEt = (EditText) findViewById(R.id.app_password_et);
		mSignInBt = (Button) findViewById(R.id.app_sign_in_bt);
		mRegister = (TextView) findViewById(R.id.de_login_register);
		mFogotPassWord = (TextView) findViewById(R.id.de_login_forgot);
		mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
		mFrUserNameDelete = (FrameLayout) findViewById(R.id.fr_username_delete);
		mFrPasswordDelete = (FrameLayout) findViewById(R.id.fr_pass_delete);
		mIsShowTitle = (RelativeLayout) findViewById(R.id.de_merge_rel);
		mLeftTitle = (TextView) findViewById(R.id.de_left);
		mRightTitle = (TextView) findViewById(R.id.de_right);
		mUserList = new ArrayList<User>();
		mResultList = new ArrayList<ApiResult>();
		mSignInBt.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		mLeftTitle.setOnClickListener(this);
		mRightTitle.setOnClickListener(this);
		mHandler = new Handler(this);
		mDialog = new LoadingDialog(this);

		mEditUserNameEt = new EditTextHolder(mUserNameEt, mFrUserNameDelete,
				null);
		mEditPassWordEt = new EditTextHolder(mPassWordEt, mFrPasswordDelete,
				null);

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Animation animation = AnimationUtils.loadAnimation(
						LoginActivity.this, R.anim.translate_anim);
				mImgBackgroud.startAnimation(animation);
			}
		});

	}

	protected void initData() {

		if (DemoContext.getInstance() != null) {
			String email = DemoContext.getInstance().getSharedPreferences()
					.getString(INTENT_IMAIL, "");
			String password = DemoContext.getInstance().getSharedPreferences()
					.getString(INTENT_PASSWORD, "");
			mUserNameEt.setText(email);
			mPassWordEt.setText(password);
		}

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mUserNameEt.setOnClickListener(LoginActivity.this);
				mPassWordEt.setOnClickListener(LoginActivity.this);
				mEditPassWordEt
						.setmOnEditTextFocusChangeListener(LoginActivity.this);
				mEditUserNameEt
						.setmOnEditTextFocusChangeListener(LoginActivity.this);
			}
		}, 200);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_sign_in_bt:// 登录
			// initnn();
			userName = mUserNameEt.getEditableText().toString();
			passWord = mPassWordEt.getEditableText().toString();
			String name = null;
			if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
				WinToast.toast(this, R.string.login_erro_is_null);
				return;
			}
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}

			// 登录

			goLogin(userName, passWord);
			break;
		case R.id.de_left:// 注册
		case R.id.de_login_register:// 注册
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, REQUEST_CODE_REGISTER);
			break;
		case R.id.de_login_forgot:// 忘记密码
			WinToast.toast(this, "忘记密码");
			break;
		case R.id.de_right:// 忘记密码
			Intent intent1 = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent1, REQUEST_CODE_REGISTER);
			break;

		case R.id.app_username_et:
		case R.id.app_password_et:
			Message mess = Message.obtain();
			mess.what = HANDLER_LOGIN_HAS_FOCUS;
			mHandler.sendMessage(mess);
			break;

		}
	}

	// 登录的接口
	private void goLogin(String userName2, String passWord) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("username", userName2);
		params.add("userpwd", passWord);

		AsyncHttpClient http = new AsyncHttpClient();
		http.post(LoginActivity.this, ConstantsUrl.loginUrl, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("loginUrl_______________" + arg2);
						JSONObject obj;

						try {
							obj = new JSONObject(arg2.toString());
							mLoginBean = new LoginBean();
							mLoginBean.setCode(obj.getInt("code"));
							mLoginBean.setUserId(obj.getString("userId"));
							mLoginBean.setToken(obj.getString("token"));
							System.out.println("登陆的返回值code == ==== =="
									+ obj.getInt("code"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (mLoginBean.getCode() == 200) {
							Message msg = mHandler.obtainMessage();
							msg.what = LOGIN;
							msg.obj = mLoginBean;
							mHandler.sendMessage(msg);
						} else if (mLoginBean.getCode() == 1001) {

							Toast.makeText(LoginActivity.this, "密码错误",
									Toast.LENGTH_SHORT).show();
							mDialog.dismiss();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						System.out.println("LoginActivity_onFailure_arg0"
								+ arg0 + "arg2" + arg2 + "arg3" + arg3);

						Toast.makeText(LoginActivity.this, "用户名不存在",
								Toast.LENGTH_SHORT).show();

					}
				});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == HANDLER_LOGIN_FAILURE) {
			if (mDialog != null)
				mDialog.dismiss();
			WinToast.toast(LoginActivity.this, R.string.login_failure);

		} else if (msg.what == LOGIN) {

			if (mLoginBean != null && mLoginBean.getCode() == 200) {

				httpGetTokenSuccess(mLoginBean.getToken().toString());
				SharePrefUtil.saveString(LoginActivity.this, "userId",
						mLoginBean.getUserId());
				SharePrefUtil.saveString(LoginActivity.this, "token",
						mLoginBean.getToken());
				System.out.println("______________userid"
						+ mLoginBean.getUserId());
				// 如果登录成功把 账户名和密码都储存起来
				SharePrefUtil.saveString(LoginActivity.this, "username",
						userName);
				SharePrefUtil.saveString(LoginActivity.this, "password",
						passWord);
				userid = SharePrefUtil.getString(LoginActivity.this, "userId",
						"");
				// 获取好友的信息 头像昵称zzf
				getFriendInfo();
//				getGroupInfo();
				
				System.out.println("我运行了" + "getfrient方法");
				startActivity(new Intent(this, FrameActivity.class));
				finish();

			}

		} else if (msg.what == HANDLER_LOGIN_HAS_FOCUS) {
			mRegister.setVisibility(View.GONE);
			mFogotPassWord.setVisibility(View.GONE);
			mIsShowTitle.setVisibility(View.VISIBLE);
			mLeftTitle.setText(R.string.app_sign_up);
			mRightTitle.setText(R.string.app_fogot_password);
		} else if (msg.what == HANDLER_LOGIN_HAS_NO_FOCUS) {
			mRegister.setVisibility(View.VISIBLE);
			mFogotPassWord.setVisibility(View.VISIBLE);
			mIsShowTitle.setVisibility(View.GONE);
		} else if (msg.what == GETFRIENDLIST) {
			if (mGetFriendBean.data != null && mGetFriendBean.data.size() != 0) {
				for (int i = 0; i < mGetFriendBean.data.size(); i++) {
					UserInfos f = new UserInfos();
					f.setUserid(mGetFriendBean.data.get(i).getFid());
					f.setUsername(mGetFriendBean.data.get(i).getUsername());
					f.setPortrait(mGetFriendBean.data.get(i).getFace());
					mUserInfosDao.insertOrReplace(f);
				}

			}

		}
		return false;
	}

	// 获取好友的列表
	private void getFriendInfo() {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		System.out
				.println("___________________userid________login___" + userid);
		http.post(LoginActivity.this, ConstantsUrl.getFriendList, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("__________arg2login______"
								+ arg2.toString());
						Gson mGson = new Gson();
						mGetFriendBean = mGson.fromJson(arg2,
								GetFriendBean.class);
						Message msg = mHandler.obtainMessage();
						msg.what = GETFRIENDLIST;
						System.out.println("----msg.what=======" + msg.what);
						msg.obj = mGetFriendBean;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_REGISTER
				&& resultCode == Activity.RESULT_OK) {
			if (data != null) {
				mUserNameEt.setText(data.getStringExtra(INTENT_IMAIL));
				mPassWordEt.setText(data.getStringExtra(INTENT_PASSWORD));
			}
		}
	}

	private void httpGetTokenSuccess(final String token) {
		try {
			/**
			 * IMKit SDK调用第二步
			 * 
			 * 建立与服务器的连接
			 * 
			 * 详见API http://docs.rongcloud.cn/api/android/imkit/index.html
			 */
			System.out.println("_____________________________"
					+ token.toString());
			if (getApplicationInfo().packageName.equals(App
					.getCurProcessName(getApplicationContext()))) {
				RongIM.connect(token, new RongIMClient.ConnectCallback() {

					/**
					 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
					 * Token
					 */
					@Override
					public void onTokenIncorrect() {
						// goLogin(userName, passWord);

						Log.d("LoginActivity",
								"--onTokenIncorrect_____________________");
					}

					/**
					 * 连接融云成功
					 * 
					 * @param userid
					 *            当前 token
					 */
					@Override
					public void onSuccess(String userid) {
						if (mDialog != null)
							mDialog.dismiss();
						WinToast.makeText(LoginActivity.this, "登录成功").show();
						Log.e("LoginActivity",
								"--++++++++++++++++++++++++++++++++++++++onSuccess"
										+ userid);
						SharePrefUtil.saveString(LoginActivity.this, "token",
								token);
						 getFriendInfo();
//						 getGroupInfo();
					}

					/**
					 * 连接融云失败
					 * 
					 * @param errorCode
					 *            错误码，可到官网 查看错误码对应的注释
					 */
					@Override
					public void onError(RongIMClient.ErrorCode errorCode) {

						Log.d("LoginActivity", "--onError" + errorCode);
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (DemoContext.getInstance() != null) {
			mGetMyGroupsRequest = DemoContext.getInstance().getDemoApi()
					.getMyGroups(LoginActivity.this);
		}

		if (DemoContext.getInstance() != null) {
			SharedPreferences.Editor editor = DemoContext.getInstance()
					.getSharedPreferences().edit();
			editor.putString(INTENT_PASSWORD, mPassWordEt.getText().toString());
			editor.putString(INTENT_IMAIL, mUserNameEt.getText().toString());
			editor.apply();
		}
	}

	@Override
	public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
		if (mGetMyGroupsRequest != null && mGetMyGroupsRequest.equals(request)) {
			getMyGroupApiSuccess(obj);
		} else if (loginHttpRequest != null && loginHttpRequest.equals(request)) {
			loginApiSuccess(obj);
		} else if (getTokenHttpRequest != null
				&& getTokenHttpRequest.equals(request)) {
			// getTokenApiSuccess(obj);
		} else if (getUserInfoHttpRequest != null
				&& getUserInfoHttpRequest.equals(request)) {
			getFriendsApiSuccess(obj);
		}
	}

	@Override
	public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
		System.out.println("___________onCallApiFailure_______________");
		if (loginHttpRequest != null && loginHttpRequest.equals(request)) {
			if (mDialog != null)
				mDialog.dismiss();
		} else if (getTokenHttpRequest != null
				&& getTokenHttpRequest.equals(request)) {
			if (mDialog != null)
				mDialog.dismiss();
		}
	}

	/**
	 * 获得好友列表
	 * 
	 * @param obj
	 */
	private void getFriendsApiSuccess(Object obj) {
		// 获取好友列表接口 返回好友数据 (注：非融云SDK接口，是demo接口)
		if (obj instanceof Friends) {
			final Friends friends = (Friends) obj;
			if (friends.getCode() == 200) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						ArrayList<UserInfo> friendLists = new ArrayList<UserInfo>();

						for (int i = 0; i < friends.getResult().size(); i++) {
							UserInfos userInfos = new UserInfos();

							userInfos.setUserid(friends.getResult().get(i)
									.getId());
							userInfos.setUsername(friends.getResult().get(i)
									.getUsername());
							userInfos.setStatus("1");
							if (friends.getResult().get(i).getPortrait() != null)
								userInfos.setPortrait(friends.getResult()
										.get(i).getPortrait());
							friendsList.add(userInfos);
						}

						UserInfos addFriend = new UserInfos();
						addFriend.setUsername("新好友消息");
						addFriend.setUserid("10000");
						addFriend.setPortrait("test");
						addFriend.setStatus("0");
						UserInfos customer = new UserInfos();
						customer.setUsername("客服");
						customer.setUserid("kefu114");
						customer.setPortrait("http://jdd.kefu.rongcloud.cn/image/service_80x80.png");
						customer.setStatus("0");

						friendsList.add(customer);
						friendsList.add(addFriend);

						if (friendsList != null) {
							for (UserInfos friend : friendsList) {
								UserInfos f = new UserInfos();
								f.setUserid(friend.getUserid());
								f.setUsername(friend.getUsername());
								f.setPortrait(friend.getPortrait());
								f.setStatus(friend.getStatus());
								mUserInfosDao.insertOrReplace(f);
							}
						}
						mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS)
								.sendToTarget();
					}

				});
			}
		}
	}

	private void getMyGroupApiSuccess(Object obj) {
		if (obj instanceof Groups) {
			final Groups groups = (Groups) obj;

			if (groups.getCode() == 200) {
				List<Group> grouplist = new ArrayList<Group>();
				if (groups.getResult() != null) {
					for (int i = 0; i < groups.getResult().size(); i++) {

						String id = groups.getResult().get(i).getId();
						String name = groups.getResult().get(i).getName();
						if (groups.getResult().get(i).getPortrait() != null) {
							Uri uri = Uri.parse(groups.getResult().get(i)
									.getPortrait());
							grouplist.add(new Group(id, name, uri));
						} else {
							grouplist.add(new Group(id, name, null));
						}
					}
					HashMap<String, Group> groupM = new HashMap<String, Group>();
					for (int i = 0; i < grouplist.size(); i++) {
						groupM.put(groups.getResult().get(i).getId(),
								grouplist.get(i));
						Log.e("login", "------get Group id---------"
								+ groups.getResult().get(i).getId());
					}

					if (DemoContext.getInstance() != null)
						DemoContext.getInstance().setGroupMap(groupM);
					if (isSuccess) {
						if (grouplist.size() > 0) {
							RongIM.getInstance()
									.getRongIMClient()
									.syncGroup(
											grouplist,
											new RongIMClient.OperationCallback() {
												@Override
												public void onSuccess() {
													Log.e(TAG,
															"---syncGroup-onSuccess---");
												}

												@Override
												public void onError(
														RongIMClient.ErrorCode errorCode) {
													Log.e(TAG,
															"---syncGroup-onError---");
												}
											});
						}
					}
				}
			} else {
				// WinToast.toast(this, groups.getCode());
			}
		}
	}

	private void loginApiSuccess(Object obj) {

		if (obj instanceof User) {

			final User user = (User) obj;

			if (user.getCode() == 200) {
				if (DemoContext.getInstance() != null
						&& user.getResult() != null) {
					SharedPreferences.Editor edit = DemoContext.getInstance()
							.getSharedPreferences().edit();
					edit.putString("DEMO_USER_ID", user.getResult().getId());
					edit.putString("DEMO_USER_NAME", user.getResult()
							.getUsername());
					edit.putString("DEMO_USER_PORTRAIT", user.getResult()
							.getPortrait());
					edit.apply();
					Log.e(TAG, "-------login success------");

				}
			} else if (user.getCode() == 103) {

				if (mDialog != null)
					mDialog.dismiss();

				WinToast.toast(LoginActivity.this, "密码错误");
			} else if (user.getCode() == 104) {

				if (mDialog != null)
					mDialog.dismiss();

				WinToast.toast(LoginActivity.this, "账号错误");
			}
		}
	}

	@Override
	public void onEditTextFocusChange(View v, boolean hasFocus) {
		Message mess = Message.obtain();
		switch (v.getId()) {
		case R.id.app_username_et:
		case R.id.app_password_et:
			if (hasFocus) {
				mess.what = HANDLER_LOGIN_HAS_FOCUS;
			}
			mHandler.sendMessage(mess);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				Message mess = Message.obtain();
				mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
				mHandler.sendMessage(mess);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		event.getKeyCode();
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_ESCAPE:
			Message mess = Message.obtain();
			mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
			mHandler.sendMessage(mess);
			break;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	protected void onPause() {
		super.onPause();
		if (mSoftManager == null) {
			mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		if (getCurrentFocus() != null) {
			mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);// 隐藏软键盘
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {

			final AlertDialog.Builder alterDialog = new AlertDialog.Builder(
					this);
			alterDialog.setMessage("确定退出应用？");
			alterDialog.setCancelable(true);

			alterDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (RongIM.getInstance() == null)
								RongIM.getInstance().logout();

							App.getInstance().exit();
							Process.killProcess(Process.myPid());
						}
					});
			alterDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alterDialog.show();
		}

		return false;
	}

}
