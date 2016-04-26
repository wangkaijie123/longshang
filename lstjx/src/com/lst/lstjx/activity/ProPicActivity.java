<<<<<<< HEAD
package com.lst.lstjx.activity;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author describe parameter return
 */
public class ProPicActivity extends Activity {
	private ImageView propir_img;
	private String propicUrl;
	private Button goBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.propir_activity);
		initView();
		ImageLoader.getInstance().displayImage(propicUrl, propir_img);	
//		Bitmap mBit = ImageLoader.getInstance().loadImageSync(propicUrl);
//		 BitmapDrawable bd= new BitmapDrawable(mBit); 
//		 
//			
//		
//		final InputStream is = getResources().openRawResource(R.raw.android1);
////		final Drawable placeHolder = getResources().getDrawable(R.drawable.android_placeholder);
//		TileBitmapDrawable.attachTileBitmapDrawable(propir_img,is ,bd, null);
	}

	private void initView() {
		// TODO Auto-generated method stub
		propir_img = (ImageView) findViewById(R.id.propir_img);
		goBack = (Button) findViewById(R.id.goBack);
		Intent intent = getIntent();
		propicUrl = intent.getStringExtra("propicurl");
		goBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
=======
package com.lst.lstjx.activity;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author describe parameter return
 */
public class ProPicActivity extends Activity {
	private ImageView propir_img;
	private String propicUrl;
	private Button goBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.propir_activity);
		initView();
		ImageLoader.getInstance().displayImage(propicUrl, propir_img);	
//		Bitmap mBit = ImageLoader.getInstance().loadImageSync(propicUrl);
//		 BitmapDrawable bd= new BitmapDrawable(mBit); 
//		 
//			
//		
//		final InputStream is = getResources().openRawResource(R.raw.android1);
////		final Drawable placeHolder = getResources().getDrawable(R.drawable.android_placeholder);
//		TileBitmapDrawable.attachTileBitmapDrawable(propir_img,is ,bd, null);
	}

	private void initView() {
		// TODO Auto-generated method stub
		propir_img = (ImageView) findViewById(R.id.propir_img);
		goBack = (Button) findViewById(R.id.goBack);
		Intent intent = getIntent();
		propicUrl = intent.getStringExtra("propicurl");
		goBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
