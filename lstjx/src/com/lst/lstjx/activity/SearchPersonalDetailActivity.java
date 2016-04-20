package com.lst.lstjx.activity;
import io.rong.imkit.RongIM;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class SearchPersonalDetailActivity extends Activity implements OnClickListener {
	protected static final int ADDFRIEND = 0X55312;
	public static final int CHANGEMARK = 10;
	protected static final int PERSONDETAIL = 0X456548;
	private PersonDetail mPersonDetail;
	private String userid;
	private String mUserid;
	private String username;
	private int addFriendCode;
	private TextView search_friend_username, search_friend_userid;
	private Button btn_search_friend_add_friend, send_message_btn;
	private ImageView img_head_person_detail;
	private LinearLayout my_dynamic, my_activity_my_product, nearPeople;
	private RelativeLayout go_back_person_detail; //返回按钮
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADDFRIEND:
				if (addFriendCode == 200) {
					WinToast.makeText(SearchPersonalDetailActivity.this, "已发送请求")
							.show();
				}else{
					WinToast.makeText(SearchPersonalDetailActivity.this, "添加失败，请稍后重试")
					.show();
				}			
				break;
			case PERSONDETAIL:
				if (mPersonDetail.data.face!=null) {
					ImageLoader.getInstance().displayImage(mPersonDetail.data.face.toString(),img_head_person_detail);
				}
			
				if (mPersonDetail.data.username!=null) {
					search_friend_username.setText(mPersonDetail.data.username.toString());
				}
				break;
			default:
				break;
			}
		};
	};

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.search_friend_details);
		initView();
		getPersonDetails();
	}
	private void getPersonDetails() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		System.out.println("______________________________________userid______"+userid.toString());
		params.add("uid", userid);		
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(SearchPersonalDetailActivity.this, ConstantsUrl.get_users_info, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {				
							Gson mGson = new Gson();
							mPersonDetail = mGson.fromJson(arg2,
									PersonDetail.class);					
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
		img_head_person_detail  = (ImageView) findViewById(R.id.img_head_person_detail);
		go_back_person_detail =  (RelativeLayout) findViewById(R.id.go_back_person_detail);		
		mUserid = SharePrefUtil.getString(SearchPersonalDetailActivity.this, "userId", "");
		search_friend_username = (TextView) findViewById(R.id.search_friend_username);
		search_friend_userid = (TextView) findViewById(R.id.search_friend_userid);
		btn_search_friend_add_friend = (Button) findViewById(R.id.btn_search_friend_add_friend);
		send_message_btn = (Button) findViewById(R.id.send_message_btn);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		search_friend_userid.setText(userid);
		my_dynamic = (LinearLayout) findViewById(R.id.my_dynamic);
		my_activity_my_product = (LinearLayout) findViewById(R.id.my_activity_my_product);
		nearPeople = (LinearLayout) findViewById(R.id.nearPeople);
		my_dynamic.setOnClickListener(this);
		my_activity_my_product.setOnClickListener(this);
		nearPeople.setOnClickListener(this);
		btn_search_friend_add_friend.setOnClickListener(this);
		send_message_btn.setOnClickListener(this);
		go_back_person_detail.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("hisuserid", userid);
		switch (v.getId()) {
		
		//加好友
		case R.id.btn_search_friend_add_friend:			
			AddFriend();
			break;
			// 发消息
		case R.id.send_message_btn:
			SharePrefUtil.saveString(SearchPersonalDetailActivity.this,"conversationUsername", username);
			RongIM.getInstance().startPrivateChat(SearchPersonalDetailActivity.this,
					userid, null);
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
		case R.id.go_back_person_detail:
		
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CHANGEMARK:
			Intent i = getIntent();
			String name = i.getStringExtra("mark");
			search_friend_username.setText(name);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void AddFriend() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("uid", mUserid);
		params.add("touid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(SearchPersonalDetailActivity.this, ConstantsUrl.addFriend, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;
						System.out.println("_______________arg2.tosring"+arg2.toString());
						try {
							obj = new JSONObject(arg2.toString());
							addFriendCode = obj.getInt("code");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = ADDFRIEND;
						msg.obj = addFriendCode;
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
