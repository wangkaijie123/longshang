package com.lst.lstjx.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.adapter.MyDynamicAdapter;
import com.lst.lstjx.bean.MyDynamic;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * author        
 * describe		
 * parameter
 * 动态列表
 * return
 */
public class Hisdynamic extends Activity   implements OnClickListener, OnItemClickListener{
	protected static final int MYDYNAMIC = 0X45684213;
	private MyDynamic myDynamic;

	private List<MyDynamic> arrayList = new ArrayList<MyDynamic>();
	private MyDynamicAdapter adapter;
	private ListView dynamic_listview;
	private LinearLayout goback_wd;
	private Dialog mDialog;
	private String hisuserid;
	// 计时器
	private Timer timer;
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}
	};
	private int timer_flag = 0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Hisdynamic.this, "数据加载失败", 0).show();
				break;
			case 1:
				//
				mDialog.dismiss();
				if (arrayList != null && arrayList.size() != 0) {
					adapter = new MyDynamicAdapter(Hisdynamic.this,
							arrayList);
					dynamic_listview.setAdapter(adapter);
					
				}
				break;
			case 2:
				if (timer_flag == 5) {
					Toast.makeText(Hisdynamic.this, "数据获取失败", 0).show();
					timer.cancel(); // 退出计时器
					mDialog.dismiss();

				}
				timer_flag++;
				break;
			default:
				break;
			}
		};
	};
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.activity_my_dynamic);
		Intent intent = getIntent();
		hisuserid = intent.getStringExtra("hisuserid");
		initView();
		postInfo();
	}

	private void initView() {
		userid = SharePrefUtil
				.getString(Hisdynamic.this, "userId", null);
		goback_wd = (LinearLayout) findViewById(R.id.goback_wd);
		dynamic_listview = (ListView) findViewById(R.id.dynamic);
		goback_wd.setOnClickListener(this);
		dynamic_listview.setOnItemClickListener(this);
	}
	private int flag = 0;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_wd:
			finish();
			break;

		default:
			break;
		}

	}

	private void postInfo() {
		mDialog = DialogUtil.createProgressDialog(Hisdynamic.this,
				"正在加载数据");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", hisuserid);// 动态id
		client.post(Hisdynamic.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");

							flag = data.length();
							if (data == null) {
								timer = new Timer(true);
								timer.schedule(task, 1000, 1000);// 延时1000ms后执行，1000ms执行一次
							} else {
								for (int i = 0; i < flag; i++) {
									JSONObject udata = (JSONObject) data.get(i);
									myDynamic = new MyDynamic();
									myDynamic.setId(udata.getString("id")
											.toString());
									myDynamic.setUid(udata.getString("uid")
											.toString());
									myDynamic.setUname(udata.getString("uname")
											.toString());
									myDynamic.setTitle(udata.getString("title"
											.toString()));
									myDynamic.setContent(udata.getString(
											"content").toString());
									myDynamic.setType(udata.getString("type")
											.toString());
									myDynamic.setAddtime(udata.getString(
											"addtime").toString());
									JSONArray img = (JSONArray) udata
											.get("img");
									List<String> imgs = new ArrayList<String>();
									for (int j = 0; j < img.length(); j++) {
										imgs.add((String) img.get(j));
									}
									myDynamic.setImg(imgs);
									JSONArray video = (JSONArray) udata
											.get("video");
									List<String> videos = new ArrayList<String>();
									for (int j = 0; j < video.length(); j++) {
										videos.add((String) video.get(j));
									}
									myDynamic.setVideo(videos);

									arrayList.add(myDynamic);
								}
								Message message = Hisdynamic.this.handler
										.obtainMessage();
								message.what = 1;
								message.obj = arrayList;
								Hisdynamic.this.handler
										.sendMessage(message);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						/**
						 * Gson mGos = new Gson(); mDynamicBean =
						 * mGos.fromJson(arg2.toString(), DynamicBean.class);
						 * Message msg = handler.obtainMessage(); msg.what =
						 * MYDYNAMIC; msg.obj = mDynamicBean;
						 * handler.sendMessage(msg);
						 */
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message msg = handler.obtainMessage();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(Hisdynamic.this, AboutDTActivity.class);
		intent.putExtra("id", arrayList.get(position).getId());
		intent.putExtra("uid", arrayList.get(position).getUid());
//		intent.putExtra("sex", sex);
		
		startActivity(intent);

	}
}
