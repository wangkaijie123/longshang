package com.lst.lstjx.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.CrGroup;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.utils.SoftInputFromWindow;
import com.lst.yuewo.R;

/**
 * author describe parameter 建群的activity
 */
public class CreatQunActivity extends Activity implements OnClickListener {
	protected static final int CHANGEMARKCODE = 0X58646;
	private EditText q_show, q_name;
	private LinearLayout lin_jianqun;
	private TextView change_remake_cancel, change_remake_confirm;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_jianqun);
		initView();
		SoftInputFromWindow.setupUI(CreatQunActivity.this, lin_jianqun);

	}

	private void initView() {
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(CreatQunActivity.this, "userId", "");
		lin_jianqun = (LinearLayout) findViewById(R.id.lin_jianqun);
		change_remake_cancel = (TextView) findViewById(R.id.change_remake_cancel);
		change_remake_confirm = (TextView) findViewById(R.id.change_remake_confirm);
		change_remake_cancel.setOnClickListener(this);
		change_remake_confirm.setOnClickListener(this);
		q_name = (EditText) findViewById(R.id.q_name);
		q_show = (EditText) findViewById(R.id.q_show);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.change_remake_cancel:

			dialog();

			break;
		case R.id.change_remake_confirm:

			if ("".equals(q_name.getText().toString())
					|| "".equals(q_show.getText().toString())) {
				Toast.makeText(CreatQunActivity.this, "群名称/群介绍不能为空",
						Toast.LENGTH_SHORT).show();

			} else if (q_name.getText().toString().length() > 8) {
				Toast.makeText(CreatQunActivity.this, "群名称请小与8个字..",
						Toast.LENGTH_SHORT).show();
			} else if (q_show.getText().toString().length() > 200) {
				Toast.makeText(CreatQunActivity.this, "群介绍请小与200个字..",
						Toast.LENGTH_SHORT).show();
			} else {
				 Intent intent = new Intent(CreatQunActivity.this,
				 ChooseFriendToAddQun.class);
				 intent.putExtra("q_name",
				 q_name.getText().toString().trim());
				 intent.putExtra("q_show",
				 q_show.getText().toString().trim());
				 startActivity(intent);
			}
//			finish();
			break;
		default:
			break;
		}
	}


	private void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("是否放弃编辑");
		builder.setTitle("友情提示");
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				CreatQunActivity.this.finish();
			}

		});

		builder.create();
		builder.show();
	}
}
