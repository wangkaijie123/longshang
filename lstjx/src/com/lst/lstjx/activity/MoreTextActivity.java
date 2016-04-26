package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

/**
 * author describe parameter return
 */
public class MoreTextActivity extends Activity implements OnClickListener {
	protected static final int PUBLISHTEXT = 0X848558;
	private TextView tv_more_text_cancel, tv_more_text_publish;
	private EditText publish_text_dynamic_tv;
	private String etContent;
	private int result;
	private String userid;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PUBLISHTEXT:
				if (result == 1) {
					Toast.makeText(MoreTextActivity.this, "发表成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(MoreTextActivity.this, "发表失败，请重新发表",
							Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_more_text);
		initView();

	}

	// 发表文字动态
	private void publishText() {
		// TODO Auto-generated method stub
		etContent = publish_text_dynamic_tv.getText().toString().trim();
		RequestParams params = new RequestParams();
		params.add("content", etContent);
		params.add("uid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(MoreTextActivity.this, ConstantsUrl.publishTextDynamic,
				params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						try {
							JSONObject obj = new JSONObject(arg2.toString());
							
							result = obj.getInt("success");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = PUBLISHTEXT;
						msg.obj = result;
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
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(MoreTextActivity.this, "userId", "");
		tv_more_text_cancel = (TextView) findViewById(R.id.tv_more_text_cancel);
		tv_more_text_publish = (TextView) findViewById(R.id.tv_more_text_publish);
		publish_text_dynamic_tv = (EditText) findViewById(R.id.publish_text_dynamic_tv);
		tv_more_text_cancel.setOnClickListener(this);
		tv_more_text_publish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_more_text_cancel:
			finish();
			break;
		case R.id.tv_more_text_publish:
			publishText();
			break;
		default:
			break;
		}
	}
}
