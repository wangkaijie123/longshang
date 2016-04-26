<<<<<<< HEAD
package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.AddToBlackCallback;
import io.rong.imlib.RongIMClient.BlacklistStatus;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.RemoveFromBlacklistCallback;
import io.rong.imlib.RongIMClient.ResultCallback;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.PersonDetail;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author describe parameter 人物的详情界面 return
 */
public class PersonalDetailActivity extends Activity implements OnClickListener {
	protected static final int ADDFRIEND = 0X55312;
	public static final int CHANGEMARK = 10;
//	public static final int IN_BLACK_LIST =1;
	protected static final int PERSONDETAIL = 0X456548;
	private PersonDetail mPersonDetail;
	private String userid;
	private String mUserid;
	private ImageView goback_fjdr;
	private TextView search_friend_username, usernickname, title_mask;
	private Button btn_search_friend_del_friend, send_message_btn;
	private ImageView img_head_person_detail;
	// private LinearLayout lin_dynamic_person_detail;
	private int delFriendCode;
	private LinearLayout my_dynamic, my_activity_my_product, nearPeople;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADDFRIEND:
				if (delFriendCode == 1) {
					WinToast.makeText(PersonalDetailActivity.this, "删除成功")
							.show();
				} else {
					WinToast.makeText(PersonalDetailActivity.this, "删除失败,请重试")
							.show();
				}
				break;
			case PERSONDETAIL:
				if (mPersonDetail.data.face != null) {
					ImageLoader.getInstance().displayImage(
							mPersonDetail.data.face.toString(),
							img_head_person_detail);
				}
				if (mPersonDetail.data.username != null) {
					search_friend_username.setText(mPersonDetail.data.username
							.toString());
				}
				
			default:
				break;
			}
		};
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_person_detail);
		Intent intent = getIntent();
		if (intent.getStringExtra("SEARCH_USERID") != null) {
			userid = intent.getStringExtra("SEARCH_USERID");

		} else {
			userid = intent.getStringExtra("userid");
		}
		initView();
		getPersonDetails();

	}

	private void getPersonDetails() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(PersonalDetailActivity.this, ConstantsUrl.get_users_info,
				params, new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {

						Gson mGson = new Gson();
						mPersonDetail = mGson
								.fromJson(arg2, PersonDetail.class);
						Message msg = mHandler.obtainMessage();
						msg.what = PERSONDETAIL;
						msg.obj = mPersonDetail;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});

	}

	private void initView() {

		img_head_person_detail = (ImageView) findViewById(R.id.img_head_person_detail);
		goback_fjdr = (ImageView) findViewById(R.id.goback_fjdr);
		mUserid = SharePrefUtil.getString(PersonalDetailActivity.this,
				"userId", "");
		search_friend_username = (TextView) findViewById(R.id.search_friend_username);
		usernickname = (TextView) findViewById(R.id.usernickname);
		usernickname.setText(mUserid);
		btn_search_friend_del_friend = (Button) findViewById(R.id.btn_search_friend_del_friend);
		title_mask = (TextView) findViewById(R.id.title_mask);
		RongIM.getInstance()
				.getRongIMClient()
				.getBlacklistStatus(userid,
						new ResultCallback<RongIMClient.BlacklistStatus>() {

							@Override
							public void onSuccess(BlacklistStatus arg0) {
								// TODO Auto-generated method stub
								int value = arg0.getValue();
								System.out.println("黑名单状态吗"+value);
								if (value==0) {
									title_mask.setText("取消屏蔽");
								}else {
									title_mask.setText("屏蔽好友");
									
								}
							}

							@Override
							public void onError(ErrorCode arg0) {
								// TODO Auto-generated method stub

							}
						});
		
		send_message_btn = (Button) findViewById(R.id.send_message_btn);
		my_dynamic = (LinearLayout) findViewById(R.id.my_dynamic);
		my_activity_my_product = (LinearLayout) findViewById(R.id.my_activity_my_product);
		nearPeople = (LinearLayout) findViewById(R.id.nearPeople);

		title_mask.setOnClickListener(this);
		my_dynamic.setOnClickListener(this);
		my_activity_my_product.setOnClickListener(this);
		nearPeople.setOnClickListener(this);
		goback_fjdr.setOnClickListener(this);
		btn_search_friend_del_friend.setOnClickListener(this);
		send_message_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("hisuserid", userid);
		switch (v.getId()) {
		// 删除好友
		case R.id.btn_search_friend_del_friend:

			final AlertDialog.Builder alterDialog = new AlertDialog.Builder(
					this);
			alterDialog.setMessage("确定删除好友？");
			alterDialog.setCancelable(true);
			alterDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							delFriend();
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
			break;
		// 发消息
		case R.id.send_message_btn:

			RongIM.getInstance().startPrivateChat(PersonalDetailActivity.this,
					userid, search_friend_username.getText().toString().trim());

			break;
		case R.id.goback_fjdr:
			finish();
			break;
		case R.id.my_dynamic:
			intent.setClass(this, FriendDealActivity.class);
			startActivity(intent);
			break;
		// 物流
		case R.id.my_activity_my_product:
			intent.setClass(this, FriendWlActivity.class);
			startActivity(intent);
			break;
		// 车辆
		case R.id.nearPeople:
			intent.setClass(this, FriendCarActivity.class);
			startActivity(intent);
			break;
		case R.id.title_mask:
			if ("屏蔽好友".equals(title_mask.getText().toString())) {

				RongIM.getInstance().getRongIMClient()
						.addToBlacklist(userid, new AddToBlackCallback() {

							@Override
							public void onSuccess() {
								title_mask.setText("取消屏蔽");
							}

							@Override
							public void onError(ErrorCode arg0) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				RongIM.getInstance()
						.getRongIMClient()
						.removeFromBlacklist(userid,
								new RemoveFromBlacklistCallback() {

									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										title_mask.setText("屏蔽好友");
									}

									@Override
									public void onError(ErrorCode arg0) {
										// TODO Auto-generated method stub

									}
								});
			}
			break;
		default:
			break;
		}
	}

	private void delFriend() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("uid", mUserid);
		params.add("fid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(PersonalDetailActivity.this, ConstantsUrl.del_friend, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;
						System.out.println("_______________arg2.tosring"
								+ arg2.toString());
						try {
							obj = new JSONObject(arg2.toString());
							delFriendCode = obj.getInt("success");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = ADDFRIEND;
						msg.obj = delFriendCode;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});
	}
}
=======
package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.AddToBlackCallback;
import io.rong.imlib.RongIMClient.BlacklistStatus;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.RemoveFromBlacklistCallback;
import io.rong.imlib.RongIMClient.ResultCallback;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.PersonDetail;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author describe parameter 人物的详情界面 return
 */
public class PersonalDetailActivity extends Activity implements OnClickListener {
	protected static final int ADDFRIEND = 0X55312;
	public static final int CHANGEMARK = 10;
//	public static final int IN_BLACK_LIST =1;
	protected static final int PERSONDETAIL = 0X456548;
	private PersonDetail mPersonDetail;
	private String userid;
	private String mUserid;
	private ImageView goback_fjdr;
	private TextView search_friend_username, usernickname, title_mask;
	private Button btn_search_friend_del_friend, send_message_btn;
	private ImageView img_head_person_detail;
	// private LinearLayout lin_dynamic_person_detail;
	private int delFriendCode;
	private LinearLayout my_dynamic, my_activity_my_product, nearPeople;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADDFRIEND:
				if (delFriendCode == 1) {
					WinToast.makeText(PersonalDetailActivity.this, "删除成功")
							.show();
				} else {
					WinToast.makeText(PersonalDetailActivity.this, "删除失败,请重试")
							.show();
				}
				break;
			case PERSONDETAIL:
				if (mPersonDetail.data.face != null) {
					ImageLoader.getInstance().displayImage(
							mPersonDetail.data.face.toString(),
							img_head_person_detail);
				}
				if (mPersonDetail.data.username != null) {
					search_friend_username.setText(mPersonDetail.data.username
							.toString());
				}
				
			default:
				break;
			}
		};
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_person_detail);
		Intent intent = getIntent();
		if (intent.getStringExtra("SEARCH_USERID") != null) {
			userid = intent.getStringExtra("SEARCH_USERID");

		} else {
			userid = intent.getStringExtra("userid");
		}
		initView();
		getPersonDetails();

	}

	private void getPersonDetails() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(PersonalDetailActivity.this, ConstantsUrl.get_users_info,
				params, new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {

						Gson mGson = new Gson();
						mPersonDetail = mGson
								.fromJson(arg2, PersonDetail.class);
						Message msg = mHandler.obtainMessage();
						msg.what = PERSONDETAIL;
						msg.obj = mPersonDetail;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});

	}

	private void initView() {

		img_head_person_detail = (ImageView) findViewById(R.id.img_head_person_detail);
		goback_fjdr = (ImageView) findViewById(R.id.goback_fjdr);
		mUserid = SharePrefUtil.getString(PersonalDetailActivity.this,
				"userId", "");
		search_friend_username = (TextView) findViewById(R.id.search_friend_username);
		usernickname = (TextView) findViewById(R.id.usernickname);
		usernickname.setText(mUserid);
		btn_search_friend_del_friend = (Button) findViewById(R.id.btn_search_friend_del_friend);
		title_mask = (TextView) findViewById(R.id.title_mask);
		RongIM.getInstance()
				.getRongIMClient()
				.getBlacklistStatus(userid,
						new ResultCallback<RongIMClient.BlacklistStatus>() {

							@Override
							public void onSuccess(BlacklistStatus arg0) {
								// TODO Auto-generated method stub
								int value = arg0.getValue();
								System.out.println("黑名单状态吗"+value);
								if (value==0) {
									title_mask.setText("取消屏蔽");
								}else {
									title_mask.setText("屏蔽好友");
									
								}
							}

							@Override
							public void onError(ErrorCode arg0) {
								// TODO Auto-generated method stub

							}
						});
		
		send_message_btn = (Button) findViewById(R.id.send_message_btn);
		my_dynamic = (LinearLayout) findViewById(R.id.my_dynamic);
		my_activity_my_product = (LinearLayout) findViewById(R.id.my_activity_my_product);
		nearPeople = (LinearLayout) findViewById(R.id.nearPeople);

		title_mask.setOnClickListener(this);
		my_dynamic.setOnClickListener(this);
		my_activity_my_product.setOnClickListener(this);
		nearPeople.setOnClickListener(this);
		goback_fjdr.setOnClickListener(this);
		btn_search_friend_del_friend.setOnClickListener(this);
		send_message_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("hisuserid", userid);
		switch (v.getId()) {
		// 删除好友
		case R.id.btn_search_friend_del_friend:

			final AlertDialog.Builder alterDialog = new AlertDialog.Builder(
					this);
			alterDialog.setMessage("确定删除好友？");
			alterDialog.setCancelable(true);
			alterDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							delFriend();
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
			break;
		// 发消息
		case R.id.send_message_btn:

			RongIM.getInstance().startPrivateChat(PersonalDetailActivity.this,
					userid, search_friend_username.getText().toString().trim());

			break;
		case R.id.goback_fjdr:
			finish();
			break;
		case R.id.my_dynamic:
			intent.setClass(this, FriendDealActivity.class);
			startActivity(intent);
			break;
		// 物流
		case R.id.my_activity_my_product:
			intent.setClass(this, FriendWlActivity.class);
			startActivity(intent);
			break;
		// 车辆
		case R.id.nearPeople:
			intent.setClass(this, FriendCarActivity.class);
			startActivity(intent);
			break;
		case R.id.title_mask:
			if ("屏蔽好友".equals(title_mask.getText().toString())) {

				RongIM.getInstance().getRongIMClient()
						.addToBlacklist(userid, new AddToBlackCallback() {

							@Override
							public void onSuccess() {
								title_mask.setText("取消屏蔽");
							}

							@Override
							public void onError(ErrorCode arg0) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				RongIM.getInstance()
						.getRongIMClient()
						.removeFromBlacklist(userid,
								new RemoveFromBlacklistCallback() {

									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										title_mask.setText("屏蔽好友");
									}

									@Override
									public void onError(ErrorCode arg0) {
										// TODO Auto-generated method stub

									}
								});
			}
			break;
		default:
			break;
		}
	}

	private void delFriend() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("uid", mUserid);
		params.add("fid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(PersonalDetailActivity.this, ConstantsUrl.del_friend, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;
						System.out.println("_______________arg2.tosring"
								+ arg2.toString());
						try {
							obj = new JSONObject(arg2.toString());
							delFriendCode = obj.getInt("success");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = ADDFRIEND;
						msg.obj = delFriendCode;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
