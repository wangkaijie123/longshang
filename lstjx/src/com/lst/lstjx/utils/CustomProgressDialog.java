<<<<<<< HEAD
package com.lst.lstjx.utils;


import com.lst.yuewo.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 自定义圆形进度条对话框
 */
public class CustomProgressDialog extends ProgressDialog {

	private String content;
	private TextView progress_dialog_content;

	public CustomProgressDialog(Context context, String content) {
		super(context);
		this.content = content;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();


	}

	

	private void initData() {
		progress_dialog_content.setText(content);
	}

	public void setContent(String str) {
		progress_dialog_content.setText(str);
	}

	private void initView() {
		setContentView(R.layout.custom_progress_dialog);
		progress_dialog_content = (TextView) findViewById(R.id.progress_dialog_content);
	}

}
=======
package com.lst.lstjx.utils;


import com.lst.yuewo.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 自定义圆形进度条对话框
 */
public class CustomProgressDialog extends ProgressDialog {

	private String content;
	private TextView progress_dialog_content;

	public CustomProgressDialog(Context context, String content) {
		super(context);
		this.content = content;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();


	}

	

	private void initData() {
		progress_dialog_content.setText(content);
	}

	public void setContent(String str) {
		progress_dialog_content.setText(str);
	}

	private void initView() {
		setContentView(R.layout.custom_progress_dialog);
		progress_dialog_content = (TextView) findViewById(R.id.progress_dialog_content);
	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
