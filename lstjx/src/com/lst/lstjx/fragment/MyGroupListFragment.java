package com.lst.lstjx.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.FrameActivity;
import com.lst.lstjx.activity.GroupDetailActivity;
import com.lst.lstjx.bean.MyGroup;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyGroupListFragment.MyListAdapter.HolderView;
import com.lst.lstjx.model.ApiResult;
import com.lst.lstjx.utils.Constants;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.LoadingDialog;
import com.lst.yuewo.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.BaseFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 2015/1/25.
 */
public class MyGroupListFragment extends BaseFragment implements
		AdapterView.OnItemClickListener {

	private static final String TAG = MyGroupListFragment.class.getSimpleName();
	private static final int RESULTCODE = 100;
	private ListView mGroupListView;
	private List<ApiResult> mResultList;
	private LoadingDialog mDialog;
	private String userid;
	private MyListAdapter adapter;
	private MyGroup myGroup;
	private UpdateGroupBroadcastReciver mBroadcastReciver;
	private List<MyGroup> mGroups = new ArrayList<MyGroup>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (mGroups != null && mGroups.size() > 0) {
					adapter = new MyListAdapter(getActivity(), mGroups);
					mGroupListView.setAdapter(adapter);
					mDialog.dismiss();
				} else {
					mDialog.dismiss();
				}
				break;
			}
		};
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.de_fr_group_list, container,
				false);
		mGroupListView = (ListView) view.findViewById(R.id.de_group_list);
		mGroupListView.setItemsCanFocus(false);
		mDialog = new LoadingDialog(getActivity());
		userid = SharePrefUtil.getString(getActivity(), "userId", "");
		initData();
		return view;
	}

	private class UpdateGroupBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(
					FrameActivity.ACTION_DMEO_GROUP_MESSAGE)) {
				initData();
			}
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mGroupListView.setOnItemClickListener(this);

		super.onViewCreated(view, savedInstanceState);
	}

	private void initData() {
		mDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("masterid", userid);
		client.post(getActivity(), ConstantsUrl.srGroup, params,
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

	public HolderView holderView;

	public class MyListAdapter extends BaseAdapter {
		private Context context;
		private List<MyGroup> mGroups = new ArrayList<MyGroup>();

		public MyListAdapter(Context context) {
			this.context = context;

		}

		public MyListAdapter(Context context, List<MyGroup> mGroups) {
			this.context = context;
			this.mGroups = mGroups;
		}

		public int getCount() {
			return mGroups.size();
		}

		public Object getItem(int arg0) {
			return mGroups.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(final int position, View currentView, ViewGroup arg2) {
			if (currentView == null) {
				holderView = new HolderView();
				currentView = LayoutInflater.from(context).inflate(
						R.layout.my_group_item, null);
				holderView.iv_adapter_list_group = (ImageView) currentView
						.findViewById(R.id.iv_adapter_list_group);
				holderView.tv_qun_id = (TextView) currentView
						.findViewById(R.id.tv_qun_id);
				holderView.tv_qun_name = (TextView) currentView
						.findViewById(R.id.tv_qun_name);

				currentView.setTag(holderView);
			} else {
				holderView = (HolderView) currentView.getTag();

			}
			holderView.tv_qun_name.setText("群名称:"
					+ mGroups.get(position).getGroupname());
			holderView.tv_qun_id.setText("群介绍:"
					+ mGroups.get(position).getIntroduce());
			holderView.iv_adapter_list_group
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
//							Uri uri = Uri
//									.parse("rong://"
//											+ getActivity()
//													.getApplicationInfo().packageName)
//									.buildUpon()
//									.appendPath("conversationSetting")
//									.appendPath(
//											String.valueOf(Conversation.ConversationType.GROUP))
//									.appendQueryParameter("groupid",
//											mGroups.get(position).getGroupid())
//									.build();

							Intent intent = new Intent(getActivity(),
									GroupDetailActivity.class);
							intent.putExtra("INTENT_GROUP",
									mGroups.get(position).getGroupid());
//
//							intent.setData(uri);
							startActivity(intent);

						}
					});
			return currentView;
		}

		public class HolderView {
			public TextView tv_qun_id;
			public TextView tv_qun_name;
			public ImageView iv_adapter_list_group;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		RongIM.getInstance().startGroupChat(getActivity(),
				mGroups.get(position).getGroupid(),
				mGroups.get(position).getGroupname());
		// Uri uri = Uri
		// .parse("rong://"
		// + getActivity().getApplicationInfo().packageName)
		// .buildUpon()
		// .appendPath("conversationSetting")
		// .appendPath(String.valueOf(Conversation.ConversationType.GROUP))
		// .appendQueryParameter("targetId",
		// mGroups.get(position).getGroupid()).build();
		//
		// Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
		// intent.putExtra("INTENT_GROUP", mGroups.get(position));
		//
		// intent.setData(uri);
		// startActivityForResult(intent, RESULTCODE);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Constants.GROUP_JOIN_REQUESTCODE:
		case Constants.GROUP_QUIT_REQUESTCODE:

			initData();
			break;
		}

	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onRestoreUI() {
		// TODO Auto-generated method stub

	}

}