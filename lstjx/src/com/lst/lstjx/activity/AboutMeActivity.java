package com.lst.lstjx.activity;

import com.lst.yuewo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
/**
 * 关于龙商
 * @author lst718-011
 *
 */
public class AboutMeActivity extends Activity {
	
	private LinearLayout goback_sz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutme);
		
		initview();
		
	}
	private void initview() {
		goback_sz = (LinearLayout) findViewById(R.id.goback_sz);
		
		goback_sz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AboutMeActivity.this.finish();
				
			}
		});
		
	}
}
