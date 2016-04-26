package com.lst.lstjx.activity;

import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MyDynamicImgActivity extends Activity {
	private ImageView img;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wddt_img);
		img = (ImageView) findViewById(R.id.dtimg);
		intent = getIntent();
		String bit = intent.getStringExtra("bitmap");
		ImageLoader.getInstance().displayImage(bit, img);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyDynamicImgActivity.this.finish();
				
			}
		});
	}
}
