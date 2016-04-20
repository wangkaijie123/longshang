package com.lst.lstjx.fragment;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.ContactListActivity;
import com.lst.lstjx.activity.GroupActivity;
import com.lst.lstjx.activity.GroupDetailActivity;
import com.lst.lstjx.activity.ProPicActivity;
import com.lst.lstjx.bean.MyDeal;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * author 广场页面 商品fragment页面 describe parameter return
 */
public class QunLiaoFragment extends Fragment implements OnClickListener {
	protected static final int NEWPRO = 0x546;
	protected static final int REFUSHNEWGOODS = 0X7641;
	private Context mContext = getActivity();
	private MyDeal myDeal;
	private List<MyDeal> arrayList = new ArrayList<MyDeal>();
	private View mQun;
	private RelativeLayout new_friend, qun_friend,qun_my;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (mQun == null) {
			mQun = inflater.inflate(R.layout.fragment_qun, container, false);
		}
		initView(mQun);

		return mQun;
	}

	private String userid;

	private void initView(View mQun) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(getActivity(), "userId", "");
		new_friend = (RelativeLayout) mQun.findViewById(R.id.new_friend);
		qun_friend = (RelativeLayout) mQun.findViewById(R.id.qun_friend);
		qun_my = (RelativeLayout) mQun.findViewById(R.id.qun_my);
		new_friend.setOnClickListener(this);
		qun_friend.setOnClickListener(this);
		qun_my.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_friend:
			// 3.24 targetId为固定ID
			Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
			intent.putExtra("targetId", 00);
			startActivity(intent);
			break;
		case R.id.qun_friend:
			Intent intent1 = new Intent(getActivity(),
					GroupDetailActivity.class);
			intent1.putExtra("targetId", 01);
			startActivity(intent1);

			break;
		case R.id.qun_my:
			if (RongIM.getInstance() != null) {
                RongIM.getInstance().startSubConversationList(getActivity(), Conversation.ConversationType.GROUP);
            }
			
			break;

		default:
			break;
		}

	}

}