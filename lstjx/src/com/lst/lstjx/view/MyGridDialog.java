<<<<<<< HEAD
package com.lst.lstjx.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.lst.yuewo.R;
import android.app.Dialog;
import android.content.Context;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;


public class MyGridDialog extends Dialog{

	private Context context;
	private String[] str;
	private GridView dlg_grid = null;
	private int position; 
	private TextView my_dialog_title;
	private String title; 
	public MyGridDialog(Context context) {
		super(context);
		this.context = context;
	}
	public MyGridDialog(Context context, int theme, String[] str,String title) {
		super(context, theme);
		this.context = context;
		this.str = str;
		this.title = title;
	}
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置对话框使用的布局文件
		this.setContentView(R.layout.toast_view_griddialog);

		dlg_grid = (GridView) findViewById(R.id.chexingimg);
		my_dialog_title = (TextView) findViewById(R.id.my_dialog_title);
		my_dialog_title.setText(title);
		// 设置GridView的数据源
		SimpleAdapter adapter = new SimpleAdapter(context,//当前View
				getPriorityList(str), //数据源
				R.layout.grid_item,//item的布局文件
				new String[] { "list_value" },
				new int[] { R.id.itemText1 });//布局文件里面对应的view的id
		dlg_grid.setAdapter(adapter);

		// 为GridView设置监听器
		dlg_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
		
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "" + arg2, Toast.LENGTH_SHORT).show();// 显示信息;
				position = arg2;
				//点击item后Dialog消失
//				str.valueOf(arg2);
				MyGridDialog.this.dismiss();
			}
		});
	}
	//返回点击的位置
	public int getPosition() {
		return position;
	}
	//得到数据，这是要显示在GridView上的数据
	private List<HashMap<String, Object>> getPriorityList(String[] str2) {
		List<HashMap<String, Object>> priorityList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < str2.length; i++) {
			HashMap<String, Object> a = new HashMap<String, Object>();
			a.put("list_value", str2[i]);
			priorityList.add(a);
		}
		return priorityList;
	}

}
=======
package com.lst.lstjx.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.lst.yuewo.R;
import android.app.Dialog;
import android.content.Context;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;


public class MyGridDialog extends Dialog{

	private Context context;
	private String[] str;
	private GridView dlg_grid = null;
	private int position; 
	private TextView my_dialog_title;
	private String title; 
	public MyGridDialog(Context context) {
		super(context);
		this.context = context;
	}
	public MyGridDialog(Context context, int theme, String[] str,String title) {
		super(context, theme);
		this.context = context;
		this.str = str;
		this.title = title;
	}
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置对话框使用的布局文件
		this.setContentView(R.layout.toast_view_griddialog);

		dlg_grid = (GridView) findViewById(R.id.chexingimg);
		my_dialog_title = (TextView) findViewById(R.id.my_dialog_title);
		my_dialog_title.setText(title);
		// 设置GridView的数据源
		SimpleAdapter adapter = new SimpleAdapter(context,//当前View
				getPriorityList(str), //数据源
				R.layout.grid_item,//item的布局文件
				new String[] { "list_value" },
				new int[] { R.id.itemText1 });//布局文件里面对应的view的id
		dlg_grid.setAdapter(adapter);

		// 为GridView设置监听器
		dlg_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
		
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "" + arg2, Toast.LENGTH_SHORT).show();// 显示信息;
				position = arg2;
				//点击item后Dialog消失
//				str.valueOf(arg2);
				MyGridDialog.this.dismiss();
			}
		});
	}
	//返回点击的位置
	public int getPosition() {
		return position;
	}
	//得到数据，这是要显示在GridView上的数据
	private List<HashMap<String, Object>> getPriorityList(String[] str2) {
		List<HashMap<String, Object>> priorityList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < str2.length; i++) {
			HashMap<String, Object> a = new HashMap<String, Object>();
			a.put("list_value", str2[i]);
			priorityList.add(a);
		}
		return priorityList;
	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
