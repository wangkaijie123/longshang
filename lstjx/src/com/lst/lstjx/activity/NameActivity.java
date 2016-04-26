package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.MyHandler;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

public class NameActivity extends Activity implements OnClickListener {
	private MyHandler myHandler;
	private LinearLayout goback_grxx;
	private EditText write_name;
	private TextView save;
	private String userid;
	private int flag=0;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(NameActivity.this, "保存成功", 0).show();
				break;
			case 1:
				Toast.makeText(NameActivity.this, "数据加载失败", 0).show();
				break;
			default:
				break;
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_name);
		initView();
	}

	private void initView() {
		userid = SharePrefUtil.getString(NameActivity.this, "userId", "");
		System.out.println("===========我的id=="+userid);
		goback_grxx = (LinearLayout) findViewById(R.id.goback_grxx);
		write_name = (EditText) findViewById(R.id.set_user_name);
		save = (TextView) findViewById(R.id.save_name);

		goback_grxx.setOnClickListener(this);
		save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_grxx:
			if (flag == 1) {
				Intent intent = new Intent();
				intent.putExtra("nickname", write_name.getText().toString().trim());
				NameActivity.this.setResult(1, intent);
				NameActivity.this.finish();
			} else {
				if ("".equals(write_name.getText().toString())) {
					NameActivity.this.finish();
				}else{
					dialog();
				}
			}
			break;
		case R.id.save_name:
			if ("".equals(write_name.getText().toString())) {
				Toast.makeText(NameActivity.this, "您未改动昵称", 0).show();
				NameActivity.this.finish();
			}else {
				saveInfo();
				Intent intent = new Intent();
				String uname = write_name.getText().toString().trim();
				intent.putExtra("nickname", uname);
				NameActivity.this.setResult(1, intent);
				NameActivity.this.finish();
			
			}
				
			break;
		default:
			break;
		}

	}

	/**
	 *   
	 */
	private void dialog() {
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("请先保存再返回");
		builder.setTitle("友情提示");

		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveInfo();
				Intent intent = new Intent();

				String uname = write_name.getText().toString().trim();
				intent.putExtra("nickname", uname);
				NameActivity.this.setResult(1, intent);
				NameActivity.this.finish();

			}

		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				NameActivity.this.finish();

			}
		});
		builder.create();
		builder.show();
	}

	private void saveInfo() {
		
		String nickname = write_name.getText().toString().trim();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("field", "nickname");
		params.put("value", nickname);
		params.put("uid", userid);
		client.post(this, ConstantsUrl.modify_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						
						try {
							JSONObject json = new JSONObject(arg2.toString());
							String success =  json.getString("success");
							if ("1".equals(success)) {
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
								
							}else {
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						flag=1;
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Toast.makeText(NameActivity.this, "保存失败",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

}
