<<<<<<< HEAD
package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

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
import android.widget.Toast;

public class SexActivity extends Activity implements OnClickListener {
	private ImageView man, woman;
	private LinearLayout is_man, is_woman, goback_grxx;
	private TextView save_sex;
	private int sex = 1;
	private int flag = 0;
	private String userid;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				Toast.makeText(SexActivity.this, "保存成功", 0).show();

			} else if (msg.what == 1) {
				Toast.makeText(SexActivity.this, "保存失败", 0).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_sex);
		initView();
	}

	private void initView() {
		userid = SharePrefUtil.getString(SexActivity.this, "userId", "");
		goback_grxx = (LinearLayout) findViewById(R.id.goback_grxx);
		man = (ImageView) findViewById(R.id.man);
		woman = (ImageView) findViewById(R.id.woman);
		is_man = (LinearLayout) findViewById(R.id.is_man);
		is_woman = (LinearLayout) findViewById(R.id.is_woman);
		save_sex = (TextView) findViewById(R.id.save_sex);

		goback_grxx.setOnClickListener(this);
		is_man.setOnClickListener(this);
		is_woman.setOnClickListener(this);
		save_sex.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_grxx:
			if (flag == 1) {
				Intent intent = new Intent();
				if (sex == 1) {
					intent.putExtra("sex", "男");
				} else {
					intent.putExtra("sex", "女");
				}
				SexActivity.this.setResult(2, intent);

			} else {
				SexActivity.this.finish();
			}
			break;
		case R.id.is_man:
			man.setVisibility(View.VISIBLE);
			woman.setVisibility(View.GONE);
			sex = 1;
			break;
		case R.id.is_woman:
			woman.setVisibility(View.VISIBLE);
			man.setVisibility(View.GONE);
			sex = 0;
			break;
		case R.id.save_sex:
			saveSex();

			Intent intent = new Intent();
			if (sex == 1) {
				intent.putExtra("sex", "男");
			} else {
				intent.putExtra("sex", "女");
			}
			SexActivity.this.setResult(2, intent);
			this.finish();
			break;
		default:
			break;
		}

	}

	private void saveSex() {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("field", "sex");
		if (man.getVisibility() == View.VISIBLE) {
			params.put("value", "1");
		} else {
			params.put("value", "0");
		}
		params.put("uid", userid);
		client.post(this, ConstantsUrl.modify_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("_________sex_____" + arg2);
						try {
							JSONObject json = new JSONObject(arg2.toString());
							String success = json.getString("success");
							if ("1".equals(success)) {
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							} else {
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						flag=1;

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				});

	}
}
=======
package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

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
import android.widget.Toast;

public class SexActivity extends Activity implements OnClickListener {
	private ImageView man, woman;
	private LinearLayout is_man, is_woman, goback_grxx;
	private TextView save_sex;
	private int sex = 1;
	private int flag = 0;
	private String userid;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				Toast.makeText(SexActivity.this, "保存成功", 0).show();

			} else if (msg.what == 1) {
				Toast.makeText(SexActivity.this, "保存失败", 0).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_sex);
		initView();
	}

	private void initView() {
		userid = SharePrefUtil.getString(SexActivity.this, "userId", "");
		goback_grxx = (LinearLayout) findViewById(R.id.goback_grxx);
		man = (ImageView) findViewById(R.id.man);
		woman = (ImageView) findViewById(R.id.woman);
		is_man = (LinearLayout) findViewById(R.id.is_man);
		is_woman = (LinearLayout) findViewById(R.id.is_woman);
		save_sex = (TextView) findViewById(R.id.save_sex);

		goback_grxx.setOnClickListener(this);
		is_man.setOnClickListener(this);
		is_woman.setOnClickListener(this);
		save_sex.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_grxx:
			if (flag == 1) {
				Intent intent = new Intent();
				if (sex == 1) {
					intent.putExtra("sex", "男");
				} else {
					intent.putExtra("sex", "女");
				}
				SexActivity.this.setResult(2, intent);

			} else {
				SexActivity.this.finish();
			}
			break;
		case R.id.is_man:
			man.setVisibility(View.VISIBLE);
			woman.setVisibility(View.GONE);
			sex = 1;
			break;
		case R.id.is_woman:
			woman.setVisibility(View.VISIBLE);
			man.setVisibility(View.GONE);
			sex = 0;
			break;
		case R.id.save_sex:
			saveSex();

			Intent intent = new Intent();
			if (sex == 1) {
				intent.putExtra("sex", "男");
			} else {
				intent.putExtra("sex", "女");
			}
			SexActivity.this.setResult(2, intent);
			this.finish();
			break;
		default:
			break;
		}

	}

	private void saveSex() {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("field", "sex");
		if (man.getVisibility() == View.VISIBLE) {
			params.put("value", "1");
		} else {
			params.put("value", "0");
		}
		params.put("uid", userid);
		client.post(this, ConstantsUrl.modify_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("_________sex_____" + arg2);
						try {
							JSONObject json = new JSONObject(arg2.toString());
							String success = json.getString("success");
							if ("1".equals(success)) {
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							} else {
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						flag=1;

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				});

	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
