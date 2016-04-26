<<<<<<< HEAD
package com.lst.lstjx.activity;

import java.io.File;
import java.io.FileInputStream;
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
import com.lst.lstjx.adapter.MyDynamicPLAdapter;
import com.lst.lstjx.bean.DynamicPLBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.PictureUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 动态详情
 * @author lst718-011
 *
 */
public class AboutDTActivity extends Activity implements OnClickListener {
	private LinearLayout goback_wddt;
	private ImageView face, sex, content_img;
	private TextView title, content, addtime;
	private String id, uid, man;
	private Intent intent;
	private Dialog mDialog;
	private ListView pl;

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
				Toast.makeText(AboutDTActivity.this, "数据加载失败", 0).show();
				mDialog.dismiss();
				break;
			case 2:
				// List<DynamicPLBean> add = new
				System.out.println("=========bean==="
						+ beans.get(0).getUsername());
				if (beans.size()>0) {
					
					adapter = new MyDynamicPLAdapter(AboutDTActivity.this, beans);
					pl.setAdapter(adapter);
				}
				mDialog.dismiss();
				break;
			case 3:
				if (timer_flag == 5) {
					Toast.makeText(AboutDTActivity.this, "数据获取失败", 0).show();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dt_xq);
		intent = getIntent();
		id = intent.getStringExtra("id");
		uid = intent.getStringExtra("uid");
		man = intent.getStringExtra("sex");
		initview();
	}

	private void initview() {
		goback_wddt = (LinearLayout) findViewById(R.id.goback_wddt);
		face = (ImageView) findViewById(R.id.face);
		sex = (ImageView) findViewById(R.id.sex);
//		content_img = (ImageView) findViewById(R.id.content_img);
		title = (TextView) findViewById(R.id.title);
		addtime = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content_xq);
		pl = (ListView) findViewById(R.id.pl);
		pl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				inputTitleDialog();
			}
		});

		if ("1".equals(man)) {
			sex.setBackgroundResource(R.drawable.ic_sex_male);
		} else {
			sex.setBackgroundResource(R.drawable.ic_sex_female);
		}

		goback_wddt.setOnClickListener(this);
		getinfo();

		getPL();
	}
	 private void inputTitleDialog() {

	        final EditText inputServer = new EditText(this);
	        inputServer.setFocusable(true);

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle(getString(R.string.dialog_title)).setView(inputServer).setNegativeButton(
	                getString(R.string.record_save_dialog_cancel), null);
	        builder.setPositiveButton(getString(R.string.record_save_dialog_ok),
	                new DialogInterface.OnClickListener() {

	                    public void onClick(DialogInterface dialog, int which) {
	                        String inputName = inputServer.getText().toString();
	                        //回复好友评论
	                        
	                    }
	                });
	        builder.show();
	    }

	private void getinfo() {
		mDialog = DialogUtil.createProgressDialog(AboutDTActivity.this,
				"正在加载数据...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("dynamic_id", id);
		client.post(AboutDTActivity.this, ConstantsUrl.my_dynamic_details,
				params, new TextHttpResponseHandler() {

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
								//服务器数据为图片名
//								String object =(String)data.get("img");
								String url = data.getString("face");
								ImageLoader.getInstance().displayImage(url,
										face);
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							}
						} catch (Exception e) {

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
		// 应为用户id “1”为测试数据
		params.add("dynamic_id", id);
		client.post(AboutDTActivity.this, ConstantsUrl.my_dynamic_comment,
				params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							
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

		default:
			break;
		}

	}

}
=======
package com.lst.lstjx.activity;

import java.io.File;
import java.io.FileInputStream;
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
import com.lst.lstjx.adapter.MyDynamicPLAdapter;
import com.lst.lstjx.bean.DynamicPLBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.PictureUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 动态详情
 * @author lst718-011
 *
 */
public class AboutDTActivity extends Activity implements OnClickListener {
	private LinearLayout goback_wddt;
	private ImageView face, sex, content_img;
	private TextView title, content, addtime;
	private String id, uid, man;
	private Intent intent;
	private Dialog mDialog;
	private ListView pl;

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
				Toast.makeText(AboutDTActivity.this, "数据加载失败", 0).show();
				mDialog.dismiss();
				break;
			case 2:
				// List<DynamicPLBean> add = new
				System.out.println("=========bean==="
						+ beans.get(0).getUsername());
				if (beans.size()>0) {
					
					adapter = new MyDynamicPLAdapter(AboutDTActivity.this, beans);
					pl.setAdapter(adapter);
				}
				mDialog.dismiss();
				break;
			case 3:
				if (timer_flag == 5) {
					Toast.makeText(AboutDTActivity.this, "数据获取失败", 0).show();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dt_xq);
		intent = getIntent();
		id = intent.getStringExtra("id");
		uid = intent.getStringExtra("uid");
		man = intent.getStringExtra("sex");
		initview();
	}

	private void initview() {
		goback_wddt = (LinearLayout) findViewById(R.id.goback_wddt);
		face = (ImageView) findViewById(R.id.face);
		sex = (ImageView) findViewById(R.id.sex);
//		content_img = (ImageView) findViewById(R.id.content_img);
		title = (TextView) findViewById(R.id.title);
		addtime = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content_xq);
		pl = (ListView) findViewById(R.id.pl);
		pl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				inputTitleDialog();
			}
		});

		if ("1".equals(man)) {
			sex.setBackgroundResource(R.drawable.ic_sex_male);
		} else {
			sex.setBackgroundResource(R.drawable.ic_sex_female);
		}

		goback_wddt.setOnClickListener(this);
		getinfo();

		getPL();
	}
	 private void inputTitleDialog() {

	        final EditText inputServer = new EditText(this);
	        inputServer.setFocusable(true);

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle(getString(R.string.dialog_title)).setView(inputServer).setNegativeButton(
	                getString(R.string.record_save_dialog_cancel), null);
	        builder.setPositiveButton(getString(R.string.record_save_dialog_ok),
	                new DialogInterface.OnClickListener() {

	                    public void onClick(DialogInterface dialog, int which) {
	                        String inputName = inputServer.getText().toString();
	                        //回复好友评论
	                        
	                    }
	                });
	        builder.show();
	    }

	private void getinfo() {
		mDialog = DialogUtil.createProgressDialog(AboutDTActivity.this,
				"正在加载数据...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("dynamic_id", id);
		client.post(AboutDTActivity.this, ConstantsUrl.my_dynamic_details,
				params, new TextHttpResponseHandler() {

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
								//服务器数据为图片名
//								String object =(String)data.get("img");
								String url = data.getString("face");
								ImageLoader.getInstance().displayImage(url,
										face);
								Message message = new Message();
								message.what = 0;
								handler.sendMessage(message);
							}
						} catch (Exception e) {

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
		// 应为用户id “1”为测试数据
		params.add("dynamic_id", id);
		client.post(AboutDTActivity.this, ConstantsUrl.my_dynamic_comment,
				params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							
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

		default:
			break;
		}

	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
