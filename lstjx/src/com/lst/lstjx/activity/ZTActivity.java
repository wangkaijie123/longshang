package com.lst.lstjx.activity;

import com.lst.yuewo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ZTActivity extends Activity implements OnClickListener {
	private ImageView sj, xj;
	private LinearLayout is_sj, is_xj, goback_fbsp;
	private int flag = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_sp_zt);
		initView();
	}

	private void initView() {
		goback_fbsp = (LinearLayout) findViewById(R.id.goback_fbsp);
		sj = (ImageView) findViewById(R.id.sj);
		xj = (ImageView) findViewById(R.id.xj);
		is_sj = (LinearLayout) findViewById(R.id.is_sj);
		is_xj = (LinearLayout) findViewById(R.id.is_xj);

		goback_fbsp.setOnClickListener(this);
		is_sj.setOnClickListener(this);
		is_xj.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_fbsp:
			Intent intent = new Intent();
			if (flag == 1) {
				intent.putExtra("zt", "上架");
			} else {
				intent.putExtra("zt", "下架");
			}
			ZTActivity.this.setResult(0, intent);
			this.finish();
			break;
		case R.id.is_sj:
			sj.setVisibility(View.VISIBLE);
			xj.setVisibility(View.GONE);
			flag = 1;
			break;
		case R.id.is_xj:
			xj.setVisibility(View.VISIBLE);
			sj.setVisibility(View.GONE);
			flag = 0;
			break;

		default:
			break;
		}

	}
}
