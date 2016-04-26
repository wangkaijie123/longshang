package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TelePhoneActivity extends Activity implements OnClickListener{
	private EditText telephone;
	private LinearLayout goback_grxx;
	private TextView save_number;
	private int flag = 0;
	private String userid;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				Toast.makeText(TelePhoneActivity.this, "保存成功", 0).show();
			}else if(msg.what == 1){
				Toast.makeText(TelePhoneActivity.this, "保存失败", 0).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telephone);
		initVeiw();
	}
	
	
	private void initVeiw() {
		userid = SharePrefUtil.getString(TelePhoneActivity.this, "userId", "");
		telephone = (EditText) findViewById(R.id.telephone);
		goback_grxx = (LinearLayout) findViewById(R.id.goback_grxx);
		save_number = (TextView) findViewById(R.id.save_telphone);
		
		goback_grxx.setOnClickListener(this);
		save_number.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_grxx:
			if (flag == 1) {
				Intent intent = new Intent();
				intent.putExtra("telephone", telephone.getText().toString().trim());
				TelePhoneActivity.this.setResult(3, intent);
				TelePhoneActivity.this.finish();
			} else {
				if ("".equals(telephone.getText().toString())) {
					TelePhoneActivity.this.finish();
				}else{
					dialog();
				}
			}
			break;
		case R.id.save_telphone:
			if ("".equals(telephone.getText().toString())) {
				Toast.makeText(TelePhoneActivity.this, "您未改动号码", 0).show();
				TelePhoneActivity.this.finish();
			}else {
			save_number();
			Intent intent = new Intent();
			
			String tp = telephone.getText().toString().trim();
			intent.putExtra("telephone", tp);
			TelePhoneActivity.this.setResult(3, intent);
			TelePhoneActivity.this.finish();
			}
			break;
		default:
			break;
		}
	}
	
	private void save_number() {
		String phone = telephone.getText().toString().trim();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("field", "mobile");
		params.put("value", phone);
		params.put("uid", userid);
		client.post(this, ConstantsUrl.modify_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("__________newname______" + arg2);
						try {
							JSONObject json = new JSONObject(arg2.toString());
							String success = json.getString("success");
							if ("1".equals(success)) {
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							}else{
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						flag = 1;
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Toast.makeText(TelePhoneActivity.this, "保存失败",
								Toast.LENGTH_SHORT).show();

					}
				});
	}
	
	private void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("请先保存再返回");
		builder.setTitle("友情提示");
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				save_number();
				Intent intent = new Intent();
				
				String tp = telephone.getText().toString().trim();
				intent.putExtra("telephone", tp);
				TelePhoneActivity.this.setResult(3, intent);
				TelePhoneActivity.this.finish();
				
			}
			
		});

		builder.create();
		builder.show();
	}
}
