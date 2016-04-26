<<<<<<< HEAD
package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author describe parameter return
 */
public class MyActivity extends Activity implements OnClickListener {

	protected static final int GPC = 0X5162513;
	private LinearLayout mine, my_dynamic, my_activity_my_product, nearPeople,
			yq_button;
	private String userid,username;
	private ImageView set, user_title;
	private TextView userName, userId;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GPC:
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
		setContentView(R.layout.activity_my);
		FrameActivity.getInstance().addActivity(this);

		initView();
		getData();
	}

	private void initView() {
		userid = SharePrefUtil.getString(MyActivity.this, "userId", "");
		username = SharePrefUtil.getString(MyActivity.this, "userName", "");
		mine = (LinearLayout) findViewById(R.id.mine);
		my_dynamic = (LinearLayout) findViewById(R.id.my_dynamic);
		my_activity_my_product = (LinearLayout) findViewById(R.id.my_activity_my_product);
	
		nearPeople = (LinearLayout) findViewById(R.id.nearPeople);
		yq_button = (LinearLayout) findViewById(R.id.yq_button);
		set = (ImageView) findViewById(R.id.set_button);
		user_title = (ImageView) findViewById(R.id.user_titile);
		userName = (TextView) findViewById(R.id.username);
		userId = (TextView) findViewById(R.id.user_password);
		userId.setText(userid);
		mine.setOnClickListener(this);
		set.setOnClickListener(this);
		my_dynamic.setOnClickListener(this);
		my_activity_my_product.setOnClickListener(this);
		nearPeople.setOnClickListener(this);
		yq_button.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		//我的信息
		case R.id.mine:
			intent.setClass(this, MineActivity.class);
			startActivity(intent);
			break;
			//设置
		case R.id.set_button:
			intent.setClass(this, SetActivity.class);
			startActivity(intent);
			break;
			//买卖
		case R.id.my_dynamic:
			intent.setClass(this, MyDealActivity.class);
			startActivity(intent);
			break;
			//物流
		case R.id.my_activity_my_product:
			intent.setClass(this, AdressManager.class);
			startActivity(intent);
			break;
			//车辆
		case R.id.nearPeople:
			intent.setClass(this, MyCarActivity.class);
			startActivity(intent);
			break;
			//邀请
		case R.id.yq_button:
			Intent intent1 = new Intent(Intent.ACTION_SEND);
//			intent.setType("image/*");
			intent1.setType("text/plain");
			intent1.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
			intent1.putExtra(Intent.EXTRA_TEXT,username+"邀请您使用'龙商家信'。下载地址：");
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent1, getTitle()));
			break;

		default:
			break;
		}

	}


	private void getData() {
		AsyncHttpClient client = new AsyncHttpClient();
		// String url = mConstantsUrl.get_users_info;
		RequestParams params = new RequestParams();
		params.add("uid", userid);

		client.post(MyActivity.this, ConstantsUrl.get_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("___________________myactivity"
								+ arg2.toString());
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");
							String url = data.getString("face").toString();
							System.out.println("----url----" + url);
							ImageLoader.getInstance().displayImage(url,
									user_title);
							userName.setText(data.getString("username"));
							userId.setText(data.getString("id"));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

import org.apache.http.Header;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author describe parameter return
 */
public class MyActivity extends Activity implements OnClickListener {

	protected static final int GPC = 0X5162513;
	private LinearLayout mine, my_dynamic, my_activity_my_product, nearPeople,
			yq_button;
	private String userid,username;
	private ImageView set, user_title;
	private TextView userName, userId;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GPC:
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
		setContentView(R.layout.activity_my);
		FrameActivity.getInstance().addActivity(this);

		initView();
		getData();
	}

	private void initView() {
		userid = SharePrefUtil.getString(MyActivity.this, "userId", "");
		username = SharePrefUtil.getString(MyActivity.this, "userName", "");
		mine = (LinearLayout) findViewById(R.id.mine);
		my_dynamic = (LinearLayout) findViewById(R.id.my_dynamic);
		my_activity_my_product = (LinearLayout) findViewById(R.id.my_activity_my_product);
		nearPeople = (LinearLayout) findViewById(R.id.nearPeople);
		yq_button = (LinearLayout) findViewById(R.id.yq_button);
		set = (ImageView) findViewById(R.id.set_button);
		user_title = (ImageView) findViewById(R.id.user_titile);
		userName = (TextView) findViewById(R.id.username);
		userId = (TextView) findViewById(R.id.user_password);
		userId.setText(userid);
		mine.setOnClickListener(this);
		set.setOnClickListener(this);
		my_dynamic.setOnClickListener(this);
		my_activity_my_product.setOnClickListener(this);
		nearPeople.setOnClickListener(this);
		yq_button.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		//我的信息
		case R.id.mine:
			intent.setClass(this, MineActivity.class);
			startActivity(intent);
			break;
			//设置
		case R.id.set_button:
			intent.setClass(this, SetActivity.class);
			startActivity(intent);
			break;
			//买卖
		case R.id.my_dynamic:
			intent.setClass(this, MyDealActivity.class);
			startActivity(intent);
			break;
			//物流
		case R.id.my_activity_my_product:
			intent.setClass(this, MyWlActivity.class);
			startActivity(intent);
			break;
			//车辆
		case R.id.nearPeople:
			intent.setClass(this, MyCarActivity.class);
			startActivity(intent);
			break;
			//邀请
		case R.id.yq_button:
			Intent intent1 = new Intent(Intent.ACTION_SEND);
//			intent.setType("image/*");
			intent1.setType("text/plain");
			intent1.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
			intent1.putExtra(Intent.EXTRA_TEXT,username+"邀请您使用'龙商家信'。下载地址：");
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent1, getTitle()));
			break;

		default:
			break;
		}

	}


	private void getData() {
		AsyncHttpClient client = new AsyncHttpClient();
		// String url = mConstantsUrl.get_users_info;
		RequestParams params = new RequestParams();
		params.add("uid", userid);

		client.post(MyActivity.this, ConstantsUrl.get_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("___________________myactivity"
								+ arg2.toString());
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");
							String url = data.getString("face").toString();
							System.out.println("----url----" + url);
							ImageLoader.getInstance().displayImage(url,
									user_title);
							userName.setText(data.getString("username"));
							userId.setText(data.getString("id"));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
