<<<<<<< HEAD
package com.lst.lstjx.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import c.t.m.g.a;
import c.t.m.g.c;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.adapter.MyDynamicPLAdapter;
import com.lst.lstjx.bean.DynamicPLBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 动态评论
 * @author lst718-011
 *
 */
public class DynamicDetailCanPublishActivity extends Activity implements
		OnClickListener {
	private ImageView goback_wddt;
	private ImageView face, sex;
	private TextView title, content, addtime;
	private String id, uid, man, username;
	private Intent intent;
	private Dialog mDialog;
	private ListView pl;
	private int result,code;
	private EditText et_publish_conment;
	private Button btn_publish_conment;
	private MyDynamicPLAdapter adapter;
	private DynamicPLBean plBean;
	private List<DynamicPLBean> beans = new ArrayList<DynamicPLBean>();
	// 计时器
	private Timer timer;
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 3;
			handler.sendMessage(message);
		}
	};
	private int timer_flag = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.dismiss();
				break;
			case 1:
				Toast.makeText(DynamicDetailCanPublishActivity.this, "数据加载失败",
						0).show();
				break;
			case 2:
				System.out.println("=========bean==="
						+ beans.get(0).getUsername());

				adapter = new MyDynamicPLAdapter(
						DynamicDetailCanPublishActivity.this, beans);
				pl.setAdapter(adapter);

				break;
			case 3:
				if (timer_flag == 5) {
					Toast.makeText(DynamicDetailCanPublishActivity.this,
							"数据获取失败", 0).show();
					timer.cancel(); // 退出计时器
					mDialog.dismiss();

				}
				timer_flag++;
				break;
			case 4:
					if (code == 200) {
						
						
						Toast.makeText(DynamicDetailCanPublishActivity.this,
								"评论成功", 0).show();
						
						
					}
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.fragment_dynamic_detail);
		intent = getIntent();
		username = intent.getStringExtra("username");
		id = intent.getStringExtra("id");
		uid = intent.getStringExtra("uid");
		man = intent.getStringExtra("sex");
		initview();
	}

	private void initview() {
		goback_wddt = (ImageView) findViewById(R.id.goback_wddt);
		face = (ImageView) findViewById(R.id.face);
		sex = (ImageView) findViewById(R.id.sex);
		title = (TextView) findViewById(R.id.title);
		addtime = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content_xq);
		et_publish_conment = (EditText) findViewById(R.id.et_publish_conment);
		btn_publish_conment = (Button) findViewById(R.id.btn_publish_conment);

		btn_publish_conment.setOnClickListener(this);
		pl = (ListView) findViewById(R.id.pl);
		if ("1".equals(man)) {
			sex.setBackgroundResource(R.drawable.ic_sex_male);
		} else {
			sex.setBackgroundResource(R.drawable.ic_sex_female);
		}

		goback_wddt.setOnClickListener(this);
		getinfo();

		getPL();
	}

	private void getinfo() {
		mDialog = DialogUtil.createProgressDialog(
				DynamicDetailCanPublishActivity.this, "正在加载数据...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("dynamic_id", id);
		client.post(DynamicDetailCanPublishActivity.this,
				ConstantsUrl.my_dynamic_details, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");
							System.out.println("========data=======" + data);
							if (data == null) {
								timer = new Timer(true);
								timer.schedule(task, 1000, 1000);// 延时1000ms后执行，1000ms执行一次
							} else {
								title.setText(data.getString("uname"));
								addtime.setText(data.getString("addtime"));
								content.setText(data.getString("content"));

								String url = data.getString("face");
								ImageLoader.getInstance().displayImage(url,
										face);
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				});

	}

	private void getPL() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("dynamic_id", id);
		client.post(DynamicDetailCanPublishActivity.this,
				ConstantsUrl.my_dynamic_comment, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							System.out.println("------hsib----" + arg2);
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");
							System.out.println("=======data222======" + data);
							if (data != null) {
								for (int i = 0; i < data.length(); i++) {
									JSONObject info = data.getJSONObject(i);
									plBean = new DynamicPLBean();
									plBean.setFace(info.getString("face"));
									plBean.setUsername(info
											.getString("username"));
									plBean.setSex(info.getString("sex"));
									plBean.setContent(info.getString("content"));
									plBean.setAddtime(info.getString("addtime"));

									beans.add(plBean);
								}
								System.out.println("========================="
										+ beans.get(0).getUsername());
								Message message = new Message();
								message.what = 2;
								message.obj = beans;
								handler.sendMessage(message);
							} else {
								mDialog.dismiss();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_wddt:
			this.finish();
			break;
		case R.id.btn_publish_conment:
			// 发表评论
			
			publishComment();
			et_publish_conment.setText("");
			//点击后隐藏软键盘
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(DynamicDetailCanPublishActivity.this
                            .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
			
			adapter = new MyDynamicPLAdapter(
					DynamicDetailCanPublishActivity.this, beans);
			adapter.clear();
			
			getPL();
			break;
		default:
			break;
		}

	}
//发表评论
	private void publishComment() {
		
		String userid = SharePrefUtil.getString(DynamicDetailCanPublishActivity.this, "userId", "");
		String conment = et_publish_conment.getText().toString();
		RequestParams params = new RequestParams();
		params.add("content", conment);
		params.add("uid", userid);
		params.add("dynamic_id", id);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(DynamicDetailCanPublishActivity.this, ConstantsUrl.publish_comment,
				params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						try {
							JSONObject json = new JSONObject(arg2.toString());
							code = json.getInt("success");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 4;
						msg.obj = code;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message msg = handler.obtainMessage();
						msg.what = 1;
						
						handler.sendMessage(msg);
					}
				});
	}
}
=======
package com.lst.lstjx.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import c.t.m.g.a;
import c.t.m.g.c;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.adapter.MyDynamicPLAdapter;
import com.lst.lstjx.bean.DynamicPLBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 动态评论
 * @author lst718-011
 *
 */
public class DynamicDetailCanPublishActivity extends Activity implements
		OnClickListener {
	private ImageView goback_wddt;
	private ImageView face, sex;
	private TextView title, content, addtime;
	private String id, uid, man, username;
	private Intent intent;
	private Dialog mDialog;
	private ListView pl;
	private int result,code;
	private EditText et_publish_conment;
	private Button btn_publish_conment;
	private MyDynamicPLAdapter adapter;
	private DynamicPLBean plBean;
	private List<DynamicPLBean> beans = new ArrayList<DynamicPLBean>();
	// 计时器
	private Timer timer;
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 3;
			handler.sendMessage(message);
		}
	};
	private int timer_flag = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.dismiss();
				break;
			case 1:
				Toast.makeText(DynamicDetailCanPublishActivity.this, "数据加载失败",
						0).show();
				break;
			case 2:
				System.out.println("=========bean==="
						+ beans.get(0).getUsername());

				adapter = new MyDynamicPLAdapter(
						DynamicDetailCanPublishActivity.this, beans);
				pl.setAdapter(adapter);

				break;
			case 3:
				if (timer_flag == 5) {
					Toast.makeText(DynamicDetailCanPublishActivity.this,
							"数据获取失败", 0).show();
					timer.cancel(); // 退出计时器
					mDialog.dismiss();

				}
				timer_flag++;
				break;
			case 4:
					if (code == 200) {
						
						
						Toast.makeText(DynamicDetailCanPublishActivity.this,
								"评论成功", 0).show();
						
						
					}
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.fragment_dynamic_detail);
		intent = getIntent();
		username = intent.getStringExtra("username");
		id = intent.getStringExtra("id");
		uid = intent.getStringExtra("uid");
		man = intent.getStringExtra("sex");
		initview();
	}

	private void initview() {
		goback_wddt = (ImageView) findViewById(R.id.goback_wddt);
		face = (ImageView) findViewById(R.id.face);
		sex = (ImageView) findViewById(R.id.sex);
		title = (TextView) findViewById(R.id.title);
		addtime = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content_xq);
		et_publish_conment = (EditText) findViewById(R.id.et_publish_conment);
		btn_publish_conment = (Button) findViewById(R.id.btn_publish_conment);

		btn_publish_conment.setOnClickListener(this);
		pl = (ListView) findViewById(R.id.pl);
		if ("1".equals(man)) {
			sex.setBackgroundResource(R.drawable.ic_sex_male);
		} else {
			sex.setBackgroundResource(R.drawable.ic_sex_female);
		}

		goback_wddt.setOnClickListener(this);
		getinfo();

		getPL();
	}

	private void getinfo() {
		mDialog = DialogUtil.createProgressDialog(
				DynamicDetailCanPublishActivity.this, "正在加载数据...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("dynamic_id", id);
		client.post(DynamicDetailCanPublishActivity.this,
				ConstantsUrl.my_dynamic_details, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");
							System.out.println("========data=======" + data);
							if (data == null) {
								timer = new Timer(true);
								timer.schedule(task, 1000, 1000);// 延时1000ms后执行，1000ms执行一次
							} else {
								title.setText(data.getString("uname"));
								addtime.setText(data.getString("addtime"));
								content.setText(data.getString("content"));

								String url = data.getString("face");
								ImageLoader.getInstance().displayImage(url,
										face);
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				});

	}

	private void getPL() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("dynamic_id", id);
		client.post(DynamicDetailCanPublishActivity.this,
				ConstantsUrl.my_dynamic_comment, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							System.out.println("------hsib----" + arg2);
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");
							System.out.println("=======data222======" + data);
							if (data != null) {
								for (int i = 0; i < data.length(); i++) {
									JSONObject info = data.getJSONObject(i);
									plBean = new DynamicPLBean();
									plBean.setFace(info.getString("face"));
									plBean.setUsername(info
											.getString("username"));
									plBean.setSex(info.getString("sex"));
									plBean.setContent(info.getString("content"));
									plBean.setAddtime(info.getString("addtime"));

									beans.add(plBean);
								}
								System.out.println("========================="
										+ beans.get(0).getUsername());
								Message message = new Message();
								message.what = 2;
								message.obj = beans;
								handler.sendMessage(message);
							} else {
								mDialog.dismiss();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_wddt:
			this.finish();
			break;
		case R.id.btn_publish_conment:
			// 发表评论
			
			publishComment();
			et_publish_conment.setText("");
			//点击后隐藏软键盘
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(DynamicDetailCanPublishActivity.this
                            .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
			
			adapter = new MyDynamicPLAdapter(
					DynamicDetailCanPublishActivity.this, beans);
			adapter.clear();
			
			getPL();
			break;
		default:
			break;
		}

	}
//发表评论
	private void publishComment() {
		
		String userid = SharePrefUtil.getString(DynamicDetailCanPublishActivity.this, "userId", "");
		String conment = et_publish_conment.getText().toString();
		RequestParams params = new RequestParams();
		params.add("content", conment);
		params.add("uid", userid);
		params.add("dynamic_id", id);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(DynamicDetailCanPublishActivity.this, ConstantsUrl.publish_comment,
				params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						try {
							JSONObject json = new JSONObject(arg2.toString());
							code = json.getInt("success");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 4;
						msg.obj = code;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Message msg = handler.obtainMessage();
						msg.what = 1;
						
						handler.sendMessage(msg);
					}
				});
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
