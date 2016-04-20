package com.lst.lstjx.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.adapter.MyDynamicAdapter;
import com.lst.lstjx.bean.MyDynamic;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;

public class MyDynamicActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	protected static final int MYDYNAMIC = 0X45684213;
	private MyDynamic myDynamic;

	private List<MyDynamic> arrayList = new ArrayList<MyDynamic>();
	private MyDynamicAdapter adapter;
	private ListView dynamic_listview;
	private LinearLayout goback_wd;
//	private Dialog mDialog;
	private String sex;
	private int delDynamicCode;
	// 计时器
//	private Timer timer;
//	TimerTask task = new TimerTask() {
//		public void run() {
//			Message message = new Message();
//			message.what = 2;
//			handler.sendMessage(message);
//		}
//	};
//	private int timer_flag = 0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(MyDynamicActivity.this, "数据加载失败", 0).show();
				break;
			case 1:
				//

				if (arrayList != null && arrayList.size() != 0) {
					adapter = new MyDynamicAdapter(MyDynamicActivity.this,
							arrayList);
					dynamic_listview.setAdapter(adapter);
//					mDialog.dismiss();
				}
				break;
//			case 2:
//				if (timer_flag == 5) {
//					Toast.makeText(MyDynamicActivity.this, "数据获取失败", 0).show();
//					timer.cancel(); // 退出计时器
//					mDialog.dismiss();
//
//				}
//				timer_flag++;
//				break;
			case 3:
				if (delDynamicCode == 1) {
					WinToast.makeText(MyDynamicActivity.this, "删除成功").show();
					arrayList.remove(pos);
					adapter = new MyDynamicAdapter(MyDynamicActivity.this,
							arrayList);
					dynamic_listview.setAdapter(adapter);
				} else {
					WinToast.makeText(MyDynamicActivity.this, "删除失败,请重试")
							.show();
				}
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
		setContentView(R.layout.activity_my_dynamic);
		Intent intent = getIntent();
		sex = intent.getStringExtra("sex");
		initView();
		postInfo();
	}

	private void initView() {
		userid = SharePrefUtil
				.getString(MyDynamicActivity.this, "userId", null);
		goback_wd = (LinearLayout) findViewById(R.id.goback_wd);

		dynamic_listview = (ListView) findViewById(R.id.dynamic);

		goback_wd.setOnClickListener(this);
		dynamic_listview.setOnItemClickListener(this);
		dynamic_listview
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
							dialog(position);
						return true;
					}
				}) ;
	}
private void dialog(int position) {
	AlertDialog.Builder builder = new Builder(this);
	builder.setMessage("是否删除该动态");
	builder.setTitle("友情提示");
	pos = position;
	builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
		
			myDynamicDel(pos);

		}

	});

	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			MyDynamicActivity.this.finish();

		}
	});
	builder.create();
	builder.show();
	}
	private void myDynamicDel(int position) {
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		params.add("id", arrayList.get(pos).getId());		
		// params.add("fid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(MyDynamicActivity.this, ConstantsUrl.my_dynamic_del, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;
						System.out.println("_______________ 删除动态arg2.tosring"
								+ arg2.toString());

						try {
							obj = new JSONObject(arg2.toString());
							delDynamicCode = obj.getInt("success");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 3;
						msg.obj = delDynamicCode;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});
	}

	private int flag = 0;
	private int pos;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_wd:
			MyDynamicActivity.this.finish();
			break;

		default:
			break;
		}

	}

	private void postInfo() {
//		mDialog = DialogUtil.createProgressDialog(MyDynamicActivity.this,
//				"正在加载数据");
//		mDialog.setCanceledOnTouchOutside(false);
//		mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", userid);// 动态id
		client.post(MyDynamicActivity.this, ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");

							flag = data.length();
//							if (data == null) {
//								timer = new Timer(true);
//								timer.schedule(task, 1000, 1000);// 延时1000ms后执行，1000ms执行一次
//							} else {
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
								Message message = MyDynamicActivity.this.handler
										.obtainMessage();
								message.what = 1;
								message.obj = arrayList;
								MyDynamicActivity.this.handler
										.sendMessage(message);
//							}
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
		intent.setClass(MyDynamicActivity.this, AboutDTActivity.class);
		intent.putExtra("id", arrayList.get(position).getId());
		intent.putExtra("uid", arrayList.get(position).getUid());
		intent.putExtra("sex", sex);

		startActivity(intent);

	}

}
