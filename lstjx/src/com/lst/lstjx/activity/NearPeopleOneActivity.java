package com.lst.lstjx.activity;

import com.lst.yuewo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NearPeopleOneActivity extends Activity implements OnClickListener{
	private ImageView goback_fjdr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearpeople_jt);
		initView();
	}
	private void initView() {
		goback_fjdr = (ImageView) findViewById(R.id.goback_fjdr);
		goback_fjdr.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_fjdr:
			NearPeopleOneActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
