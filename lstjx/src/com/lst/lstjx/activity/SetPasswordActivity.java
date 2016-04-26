package com.lst.lstjx.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

public class SetPasswordActivity extends Activity implements OnClickListener {

	private EditText oldpassword, newpassword, newpassword2;
	private int editStart;
	private int editEnd;
	private Button save;
	private String old, password, password2;
	private String userid;
	private LinearLayout goback_sz;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(SetPasswordActivity.this, "保存成功", 0).show();
				break;

			default:
				break;
			}
		};
	} ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setpassword);
		initView();
	}

	private void initView() {
		userid = SharePrefUtil.getString(SetPasswordActivity.this, "userId", "");
		oldpassword = (EditText) findViewById(R.id.oldpassword);
		newpassword = (EditText) findViewById(R.id.newpassword);
		newpassword2 = (EditText) findViewById(R.id.newpassword2);
		save = (Button) findViewById(R.id.save);
		goback_sz = (LinearLayout) findViewById(R.id.goback_sz);

		save.setOnClickListener(this);
		goback_sz.setOnClickListener(this);

		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			save();
			this.finish();
			break;
		case R.id.goback_sz:
//			if (!old.equals("") && !password.equals("") && !password2.equals("")) {
//				dialog();
//			}else{
				this.finish();
//			}
			break;
		default:
			break;
		}

	}

	private void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("友情提示");
		builder.setMessage("是否保存数据");
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				save();
				SetPasswordActivity.this.finish();
				

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SetPasswordActivity.this.finish();

			}
		});

	}

	// password
	// old
	private void save() {
		old = oldpassword.getText().toString().trim();
		password = newpassword.getText().toString().trim();
		password2 = newpassword2.getText().toString().trim();
		System.out.println("========"+userid+","+"old");
			if (password.equals(password2)) {
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				System.out.println("---old----"+old+","+userid+","+password);
				params.put("uid", userid);
				params.put("old", old);
				params.put("password", password);
				client.post(SetPasswordActivity.this, ConstantsUrl.modify_password,
						params, new TextHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1,
							String arg2) {
						System.out.println("----------"+arg2);
						Message message = new Message();
						message.what = 0;
						handler.sendMessage(message);
						
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1,
							String arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});
			} else {
				newpassword2.setText("");
				Toast.makeText(SetPasswordActivity.this, "请重新输入密码", 0).show();
			}

	}

}
