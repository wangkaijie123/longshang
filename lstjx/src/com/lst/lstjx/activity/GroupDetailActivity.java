package com.lst.lstjx.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.bean.MyGroup;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.model.ApiResult;
import com.lst.lstjx.model.Status;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.LoadingDialog;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.sea_monster.network.AbstractHttpRequest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 群组详情 Created by Bob on 2015/3/28.
 */
public class GroupDetailActivity extends BaseActivity implements
		OnClickListener{
	private AsyncImageView mGroupImg;
	private TextView mGroupName;
	private TextView mGroupId;
	private LinearLayout mRelGroupIntro;
	private TextView mGroupIntro;
	private RelativeLayout mRelGroupNum;
	private TextView mGroupNum;
	private RelativeLayout mRelGroupMyName;
	private TextView mGroupMyName;
	private RelativeLayout mRelGroupCleanMess;
	private Button mGroupJoin;
	private Button mGroupQuit;
	private Button mGroupChat;
	private String userid;
	private LoadingDialog mDialog;
	private String mGroupid,number,mGroupname;
	private MyGroup myGroup;
	private int delGroupCode,joGroupCode;
	private List<MyGroup> mGroups = new ArrayList<MyGroup>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (mGroups != null && mGroups.size() > 0) {
					for (int i = 0; i < mGroups.size(); i++) {
						
						if (mGroupid.equals(mGroups.get(i).getGroupid())) {
							mGroupname = mGroups.get(i).getGroupname();
							mGroupName.setText(mGroupname);
							mGroupId.setText(mGroups.get(i).getGroupid());
							number = mGroups.get(i).getNumber();
							mGroupIntro.setText(mGroups.get(i).getIntroduce());
							mGroupNum.setText(mGroups.get(i).getNumber());
							mGroupJoin.setVisibility(View.GONE);
							mGroupChat.setVisibility(View.VISIBLE);
							mGroupQuit.setVisibility(View.VISIBLE);
						}
						mDialog.dismiss();
					}
				} else {
					mDialog.dismiss();
				}
				break;
			case 1:
				if (delGroupCode == 1) {
					WinToast.makeText(GroupDetailActivity.this, "删除成功")
							.show();
				} else {
					WinToast.makeText(GroupDetailActivity.this, "删除失败,请重试")
							.show();
				}
				break;
			case 2:
				if (joGroupCode == 1) {
					WinToast.makeText(GroupDetailActivity.this, "添加成功")
					.show();
				} else {
					WinToast.makeText(GroupDetailActivity.this, "添加失败,请重试")
					.show();
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.de_fr_group_intro);
		getSupportActionBar().setTitle(R.string.personal_title);
		userid = SharePrefUtil
				.getString(GroupDetailActivity.this, "userId", "");
		mDialog = new LoadingDialog(GroupDetailActivity.this);
		mDialog.show();
		initView();
		groupDetail();

	}

	private void groupDetail() {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("masterid", userid);
		client.post(GroupDetailActivity.this, ConstantsUrl.srGroup, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("=======GroupListFragment====="
								+ arg2);
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");

							for (int i = 0; i < data.length(); i++) {
								JSONObject udata = (JSONObject) data.get(i);
								myGroup = new MyGroup();
								myGroup.setId(udata.getString("id").toString());
								myGroup.setGroupid(udata.getString("groupid")
										.toString());
								myGroup.setMasterid(udata.getString("masterid")
										.toString());
								myGroup.setGroupname(udata.getString(
										"groupname").toString());
								myGroup.setIntroduce(udata
										.getString("introduce".toString()));
								myGroup.setCreatetime(udata.getString(
										"createtime").toString());
								myGroup.setNumber(udata.getString("number")
										.toString());
								myGroup.setNumber_max(udata.getString(
										"number_max").toString());

								mGroups.add(myGroup);
							}
							Message message = new Message();
							message.obj = mGroups;
							message.what = 0;
							handler.sendMessage(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	protected void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar()
				.setHomeAsUpIndicator(R.drawable.de_actionbar_back);
		Intent intent = getIntent();
		mGroupid = intent.getStringExtra("INTENT_GROUP");
		mGroupImg = (AsyncImageView) findViewById(R.id.group_portrait);
		mGroupName = (TextView) findViewById(R.id.group_name);
		mGroupId = (TextView) findViewById(R.id.group_id);
		mRelGroupIntro = (LinearLayout) findViewById(R.id.rel_group_intro);
		mGroupIntro = (TextView) findViewById(R.id.group_intro);
		mRelGroupNum = (RelativeLayout) findViewById(R.id.rel_group_number);
		mGroupNum = (TextView) findViewById(R.id.group_number);
		mRelGroupMyName = (RelativeLayout) findViewById(R.id.rel_group_myname);
		mGroupMyName = (TextView) findViewById(R.id.group_myname);
		mRelGroupCleanMess = (RelativeLayout) findViewById(R.id.rel_group_clear_message);
		mGroupJoin = (Button) findViewById(R.id.join_group);
		mGroupQuit = (Button) findViewById(R.id.quit_group);
		mGroupChat = (Button) findViewById(R.id.chat_group);
		mGroupJoin.setOnClickListener(this);
		mGroupChat.setOnClickListener(this);
		mGroupQuit.setOnClickListener(this);

	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.join_group:
			if (DemoContext.getInstance() != null) {

				if (number.equals("500")) {
					WinToast.toast(GroupDetailActivity.this, "群组人数已满");
					return;
				}

				if (mDialog != null && !mDialog.isShowing())
					mDialog.show();

				joGroup();

			}
			break;
		case R.id.quit_group:

			final AlertDialog.Builder alterDialog = new AlertDialog.Builder(
					this);
			alterDialog.setMessage("确定删除群组？");
			alterDialog.setCancelable(true);
			alterDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							quGroup();
						}
					});
			alterDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alterDialog.show();
			break;

		case R.id.chat_group:
			if (DemoContext.getInstance() != null)
				RongIM.getInstance().startGroupChat(GroupDetailActivity.this,
						mGroupid, mGroupName.getText().toString());
			break;
		}
	}

	private void quGroup() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("masterid", userid);
		params.add("groupid", mGroupid);
		client.post(GroupDetailActivity.this, ConstantsUrl.quGroup, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;
						System.out.println("_______________arg2.tosring"
								+ arg2.toString());
						try {
							obj = new JSONObject(arg2.toString());
							delGroupCode = obj.getInt("success");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.obj = delGroupCode;
						handler.sendMessage(msg);
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}
	

	private void joGroup() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("masterid", userid);
		params.add("groupid", mGroupid);
		params.add("groupname", mGroupname);
		client.post(GroupDetailActivity.this, ConstantsUrl.joGroup, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject obj;
						System.out.println("_______________arg2.tosring"
								+ arg2.toString());
						try {
							obj = new JSONObject(arg2.toString());
							joGroupCode = obj.getInt("success");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = joGroupCode;
						handler.sendMessage(msg);
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}
}
