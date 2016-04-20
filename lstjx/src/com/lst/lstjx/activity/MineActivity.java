package com.lst.lstjx.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 我的个人详情
 * @author lst718-011
 *
 */
public class MineActivity extends Activity implements OnClickListener {
	private LinearLayout name, sex, my_lockion, telephone, user_photo;
	private TextView user_nick, user_sex, my_telephone;
	private ImageView face;
	private LinearLayout goback_wd;
	private String userid;
	private Dialog mDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.dismiss();
				break;
			case 1:
				mDialog.dismiss();
				Toast.makeText(MineActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);
		initView();
		getData();
	}

	private void initView() {

		userid = SharePrefUtil.getString(MineActivity.this, "userId", "");
		name = (LinearLayout) findViewById(R.id.name);
		sex = (LinearLayout) findViewById(R.id.sex);
		user_nick = (TextView) findViewById(R.id.user_name);
	
		user_sex = (TextView) findViewById(R.id.user_sex);
		goback_wd = (LinearLayout) findViewById(R.id.goback_wd);
		my_lockion = (LinearLayout) findViewById(R.id.my_lockion);
		telephone = (LinearLayout) findViewById(R.id.telephone);
		my_telephone = (TextView) findViewById(R.id.my_telephone);
		user_photo = (LinearLayout) findViewById(R.id.user_photo);
		face = (ImageView) findViewById(R.id.face);
		name.setOnClickListener(this);
		sex.setOnClickListener(this);
		goback_wd.setOnClickListener(this);
		my_lockion.setOnClickListener(this);
		telephone.setOnClickListener(this);
		user_photo.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
	
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.name:
//			intent.setClass(this, NameActivity.class);
//			startActivityForResult(intent, 1);
			break;
		case R.id.sex:
			intent.setClass(this, SexActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.goback_wd:
			Intent it = new Intent();
			
			String uname = user_nick.getText().toString().trim();
			it.putExtra("nickname", uname);
			MineActivity.this.setResult(1, it);
			MineActivity.this.finish();
			break;
		case R.id.my_lockion:
			intent.setClass(this, MyLocationActivity.class);
			startActivity(intent);
			break;
		case R.id.telephone:
			intent.setClass(this, TelePhoneActivity.class);
			startActivityForResult(intent, 3);
			break;
		case R.id.user_photo:
			intent.setClass(this, MinePhotoActivity.class);
			startActivityForResult(intent, 4);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1: // 子窗口ChildActivity的回传数据
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) { // 处理代码在此地
					String datas = bundle.getString("nickname");

					user_nick.setText(datas);

				}
			}
			break;
		case 2:
			if (data != null) {
				Bundle bundel = data.getExtras();
				if (bundel != null) {
					String datas = bundel.getString("sex");
					user_sex.setText(datas);
				}
			}
			break;
		case 3:
			if (data != null) {
				Bundle bundel = data.getExtras();
				if (bundel != null) {
					String datas = bundel.getString("telephone");
					my_telephone.setText(datas);
				}
			}
			break;
		case 4:
			if (data != null) {
				Bundle bundel = data.getExtras();
				if (bundel != null) {
					getData();
				}
			}
			break;
		default:
			break;
		}

	}

	private void getData() {
		mDialog = DialogUtil.createProgressDialog(MineActivity.this, "正在加载数据");
		mDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		client.post(MineActivity.this, ConstantsUrl.get_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {

						System.out.println("----arg2----" + arg2.toString());

						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");

							user_nick.setText(data.getString("username"));
							my_telephone.setText(data.getString("mobile"));

							String url = data.getString("face").toString();
							ImageLoader.getInstance().displayImage(url, face);
							if ("1".equals(data.getString("sex"))) {
								user_sex.setText("男");
							} else {
								user_sex.setText("女");
							}
							Message message = new Message();
							message.what = 0;
							handler.sendMessage(message);
						} catch (Exception e) {
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

}
