package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SoftInputFromWindow;
import com.lst.yuewo.R;

/**
 * 车牌设置
 * author describe parameter return
 */
public class ChePaiActivity extends Activity implements OnClickListener {
	protected static final int CHANGEMARKCODE = 0X58646;
	private EditText ac_change_remake_et;
	private TextView change_remake_cancel, change_remake_confirm;
	private String userid, mUserid;
	private String resultCode, mark; // 备注
	private int flag;
	private LinearLayout cp;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				Toast.makeText(ChePaiActivity.this, "保存成功", 0).show();
			} else if (msg.what == 1) {
				Toast.makeText(ChePaiActivity.this, "保存失败", 0).show();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chepai);
		initView();
		SoftInputFromWindow.setupUI(ChePaiActivity.this,cp);

	}

	// 修改备注
	private void changeRemake() {
		// TODO Auto-generated method stub
		mark = ac_change_remake_et.getText().toString().trim();
		if (mark == null || TextUtils.isEmpty(mark)) {
			Toast.makeText(ChePaiActivity.this, "请输入备注", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("fid", userid);
		params.put("uid", mUserid);
		params.put("mark", mark);
		http.post(ChePaiActivity.this, ConstantsUrl.changeRemake, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("======备注=====" + arg2);
						try {
							JSONObject obj = new JSONObject(arg2.toString());
							resultCode = obj.getString("success");
							if ("1".equals(resultCode)) {
								Message message = new Message();
								message.what = 0;
								mHandler.sendMessage(message);
							} else {
								Message message = new Message();
								message.what = 1;
								mHandler.sendMessage(message);
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
						// TODO Auto-generated method stub

					}
				});
	}

	private void initView() {
		// TODO Auto-generated method stub
		change_remake_cancel = (TextView) findViewById(R.id.change_remake_cancel);
		cp = (LinearLayout) findViewById(R.id.cp);
		change_remake_confirm = (TextView) findViewById(R.id.change_remake_confirm);
		change_remake_cancel.setOnClickListener(this);
		change_remake_confirm.setOnClickListener(this);
		ac_change_remake_et = (EditText) findViewById(R.id.et_chepai);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.change_remake_cancel:

//			if (flag == 1) {
//				Intent intent = new Intent();
//				intent.putExtra("chepai", ac_change_remake_et.getText()
//						.toString().trim());
//				ChePaiActivity.this.setResult(100, intent);
//				ChePaiActivity.this.finish();
//
//			} else {
				if ("".equals(ac_change_remake_et.getText().toString())) {
					ChePaiActivity.this.finish();
				} else {
					dialog();
				}
//			}
			break;
		case R.id.change_remake_confirm:
			if ("".equals(ac_change_remake_et.getText().toString())) {
				Toast.makeText(ChePaiActivity.this, "您未修改备注", 0).show();
				ChePaiActivity.this.finish();
			} else {
//				changeRemake();
				Intent intent = new Intent();

				String tp = ac_change_remake_et.getText().toString().trim();
				intent.putExtra("chepai", tp);
				ChePaiActivity.this.setResult(100, intent);
				ChePaiActivity.this.finish();
			}
			break;
		default:
			break;
		}
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
//				changeRemake();
				Intent intent = new Intent();

				String tp = ac_change_remake_et.getText().toString().trim();
				intent.putExtra("chepai", tp);
				ChePaiActivity.this.setResult(100, intent);
				ChePaiActivity.this.finish();

			}

		});

		builder.create();
		builder.show();
	}
}
