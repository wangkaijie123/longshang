package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
 * author describe parameter 添加好友人物的详情界面 return
 */
public class AddFriendPersonalDetail extends Activity implements
		OnClickListener {
	protected static final int ADDFRIEND = 0X55312;
	public static final int CHANGEMARK = 10;
	protected static final int PERSONDETAIL = 0X456548;
	protected static final int ISAGREE = 0;
	private PersonDetail mPersonDetail;
	private String userid;
	private String mUserid;
	private String  code;
	private String username;
	private TextView search_friend_username, search_friend_userid;
	private Button btn_refuse_add_friend, btn_agree_add_friend;
	private ImageView img_head_person_detail;
	private LinearLayout my_dynamic, my_activity_my_product, nearPeople;
	private int addFriendCode;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADDFRIEND:
				if (addFriendCode == 200) {
					WinToast.makeText(AddFriendPersonalDetail.this, "已添加")
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
				break;
			default:
				break;
			}
		};
	};
                                                                           
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend_person_detail);
		initView();
		getPersonDetails();
	}

	private void getPersonDetails() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(AddFriendPersonalDetail.this, ConstantsUrl.get_users_info,
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
		mUserid = SharePrefUtil.getString(AddFriendPersonalDetail.this,
				"userId", "");
		search_friend_username = (TextView) findViewById(R.id.search_friend_username);
		search_friend_userid = (TextView) findViewById(R.id.search_friend_userid);
		btn_refuse_add_friend = (Button) findViewById(R.id.btn_refuse_add_friend);
		btn_agree_add_friend = (Button) findViewById(R.id.btn_agree_add_friend);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		my_dynamic = (LinearLayout) findViewById(R.id.my_dynamic);
		my_activity_my_product = (LinearLayout) findViewById(R.id.my_activity_my_product);
		nearPeople = (LinearLayout) findViewById(R.id.nearPeople);

		my_dynamic.setOnClickListener(this);
		my_activity_my_product.setOnClickListener(this);
		nearPeople.setOnClickListener(this);
		btn_agree_add_friend.setOnClickListener(this);
		btn_refuse_add_friend.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("hisuserid", userid);
		switch (v.getId()) {
		// 拒绝 添加好友 2
		case R.id.btn_refuse_add_friend:
			isAgree(userid, mUserid, "2");
			break;
		// 同意添加好友 1
		case R.id.btn_agree_add_friend:
			isAgree(userid, mUserid, "1");
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
		
		default:
			break;
		}
	}

	private void isAgree(String userid2, String mUserid2, String i) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		System.out.println("________userid2___"+userid2+"_mUserid_____"+mUserid2.toString()+"isagree______"+i);		
		params.add("fromid", userid2);
		params.add("toid", mUserid2);
		params.put("agree", i);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(AddFriendPersonalDetail.this, ConstantsUrl.agree_add_users, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;

						System.out.println("_______________arg2.tosring"
								+ arg2.toString());
						try {
							obj = new JSONObject(arg2.toString());
							addFriendCode = Integer.parseInt(obj.getString("code"));
							System.out.println("!!!!!!!!!!!!!!!!!!!!!!!"+addFriendCode);
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
						Toast.makeText(AddFriendPersonalDetail.this,
								"网络忙请稍后重试", Toast.LENGTH_SHORT).show();
					}
				});
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

}
