<<<<<<< HEAD
package com.lst.lstjx.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.FindActivity;
import com.lst.lstjx.activity.WebActivity;
import com.lst.lstjx.bean.AdInfo;
import com.lst.lstjx.bean.HomePic;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.AdBannerView;
import com.lst.lstjx.view.MyGridView;
import com.lst.yuewo.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by lst-004 on 15-3-5.
 */
public class HomeFragment extends Fragment implements OnClickListener{
	
	private View fragment_home;
	private String name;
	
	private String id;
	private String url;
	private String userId;
	private String img;
	private View olderSelectView = null;
	private Map<String, String> imgInfo = new HashMap<String, String>();
	private List<HomePic> mHomePics = new ArrayList<HomePic>();
	private AdBannerView mAdBannerView = null;
	private List<HomePic> mRecHomePics;
	
	private final int AD_ONCLICK = 0X44;
	private final int SPR_LIST = 0X55;
	protected final int REC_LIST = 0X66;
	protected final int NEWPRO = 0X8796;
	protected final int ADDSHOPCAR = 0X776541;
	private ArrayList<AdInfo> mAdInfoList = new ArrayList<AdInfo>();
	private ImageView iv_fresh;
	private ImageView iv_housekeep;
	private ImageView iv_new;
	private ImageView iv_family;

	
	private Handler mHandler = new Handler() {
	
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AD_ONCLICK:
				int position = (Integer) msg.obj;
				// 广告位点击时间
				AdInfo adInfo = mAdInfoList.get(Integer.parseInt(msg.obj
						.toString()));
				if (mRecHomePics != null && mRecHomePics.size() != 0) {
					Intent intent = new Intent(getActivity(), WebActivity.class);
					intent.putExtra("url", mRecHomePics.get(position).getUrl()
							.toString());
					startActivity(intent);
				}
				break;
			case 0x11:
				mRecHomePics = (List<HomePic>) msg.obj;
				for (int i = 0; i < mRecHomePics.size(); i++) {
					AdInfo mAdInfo = new AdInfo();
					mAdInfo.setAdvImg(mRecHomePics.get(i).getImg());
					mAdInfo.setAdvDesc(mRecHomePics.get(i).getName());
					mAdInfoList.add(mAdInfo);
				}
				mAdBannerView.setClickFlag(AD_ONCLICK);
				mAdBannerView.init(mHandler, mAdInfoList);

				break;
			}
			};
};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (fragment_home == null) {
			fragment_home = inflater.inflate(R.layout.fargment_homes,
					container, false);
		}
		initView();

		iv_fresh = (ImageView) fragment_home.findViewById(R.id.iv_fresh);
		iv_housekeep = (ImageView) fragment_home
				.findViewById(R.id.iv_housekeep);
		iv_family = (ImageView) fragment_home.findViewById(R.id.iv_family);
		iv_new = (ImageView) fragment_home.findViewById(R.id.iv_new);

		iv_fresh.setOnClickListener(this);
		iv_housekeep.setOnClickListener(this);
		iv_family.setOnClickListener(this);
		iv_new.setOnClickListener(this);
		getImageInfo();
		return fragment_home;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_fresh:
			//买货
			Intent intent0 = new Intent(getActivity(),
					FindActivity.class);
			intent0.putExtra("TYBEFLG", "0");
			startActivity(intent0);
			break;
		case R.id.iv_housekeep:
			//卖货
			Intent intent1 = new Intent(getActivity(),
					FindActivity.class);
			intent1.putExtra("TYBEFLG", "1");
			startActivity(intent1);
			break;
		case R.id.iv_family:
			//发货
			Intent intent2 = new Intent(getActivity(),
					FindActivity.class);
			intent2.putExtra("TYBEFLG", "2");
			startActivity(intent2);
			break;
		case R.id.iv_new:
			//运货
			Intent intent3 = new Intent(getActivity(),
					FindActivity.class);
			intent3.putExtra("TYBEFLG", "3");
			startActivity(intent3);
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 获取网络图片的方法
	 */
	private void getImageInfo() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(ConstantsUrl.image_me, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				JSONArray json;
				try {
					json = new JSONArray(arg2.toString());
					for (int i = 0; i < json.length(); i++) {
						JSONObject obj = json.getJSONObject(i);
						HomePic mHomePic = new HomePic();
						id = obj.getString("id");
						name = obj.getString("name");
						url = obj.getString("url");
						img = obj.getString("img");
						mHomePic.setId(id);
						mHomePic.setImg(img);
						mHomePic.setName(name);
						mHomePic.setUrl(url);
						mHomePics.add(mHomePic);
						System.out.println("imgInfo" + imgInfo.size());
					}
					Message msg = mHandler.obtainMessage();
					msg.obj = mHomePics;
					msg.what = 0x11;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
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

	/**
	 * 初始化轮播图的相关数据
	 */
	private void initView() {
		
		mAdBannerView = (AdBannerView) fragment_home
				.findViewById(R.id.page_banner_view);
		userId = SharePrefUtil.getString(getActivity(), "userid", "");
		
	}




	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("______________onStop____");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	
		super.onResume();
		System.out.println("______________onResume____");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("______________onStart____");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("______________nPause____");
	}

		
}





=======
package com.lst.lstjx.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.FindActivity;
import com.lst.lstjx.activity.WebActivity;
import com.lst.lstjx.bean.AdInfo;
import com.lst.lstjx.bean.HomePic;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.AdBannerView;
import com.lst.lstjx.view.MyGridView;
import com.lst.yuewo.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by lst-004 on 15-3-5.
 */
public class HomeFragment extends Fragment implements OnClickListener{
	
	private View fragment_home;
	private String name;
	
	private String id;
	private String url;
	private String userId;
	private String img;
	private View olderSelectView = null;
	private Map<String, String> imgInfo = new HashMap<String, String>();
	private List<HomePic> mHomePics = new ArrayList<HomePic>();
	private AdBannerView mAdBannerView = null;
	private List<HomePic> mRecHomePics;
	
	private final int AD_ONCLICK = 0X44;
	private final int SPR_LIST = 0X55;
	protected final int REC_LIST = 0X66;
	protected final int NEWPRO = 0X8796;
	protected final int ADDSHOPCAR = 0X776541;
	private ArrayList<AdInfo> mAdInfoList = new ArrayList<AdInfo>();
	private ImageView iv_fresh;
	private ImageView iv_housekeep;
	private ImageView iv_new;
	private ImageView iv_family;

	
	private Handler mHandler = new Handler() {
	
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AD_ONCLICK:
				int position = (Integer) msg.obj;
				// 广告位点击时间
				AdInfo adInfo = mAdInfoList.get(Integer.parseInt(msg.obj
						.toString()));
				if (mRecHomePics != null && mRecHomePics.size() != 0) {
					Intent intent = new Intent(getActivity(), WebActivity.class);
					intent.putExtra("url", mRecHomePics.get(position).getUrl()
							.toString());
					startActivity(intent);
				}
				break;
			case 0x11:
				mRecHomePics = (List<HomePic>) msg.obj;
				for (int i = 0; i < mRecHomePics.size(); i++) {
					AdInfo mAdInfo = new AdInfo();
					mAdInfo.setAdvImg(mRecHomePics.get(i).getImg());
					mAdInfo.setAdvDesc(mRecHomePics.get(i).getName());
					mAdInfoList.add(mAdInfo);
				}
				mAdBannerView.setClickFlag(AD_ONCLICK);
				mAdBannerView.init(mHandler, mAdInfoList);

				break;
			}
			};
};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (fragment_home == null) {
			fragment_home = inflater.inflate(R.layout.fargment_homes,
					container, false);
		}
		initView();

		iv_fresh = (ImageView) fragment_home.findViewById(R.id.iv_fresh);
		iv_housekeep = (ImageView) fragment_home
				.findViewById(R.id.iv_housekeep);
		iv_family = (ImageView) fragment_home.findViewById(R.id.iv_family);
		iv_new = (ImageView) fragment_home.findViewById(R.id.iv_new);

		iv_fresh.setOnClickListener(this);
		iv_housekeep.setOnClickListener(this);
		iv_family.setOnClickListener(this);
		iv_new.setOnClickListener(this);
		getImageInfo();
		return fragment_home;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_fresh:
			//买货
			Intent intent0 = new Intent(getActivity(),
					FindActivity.class);
			intent0.putExtra("TYBEFLG", "0");
			startActivity(intent0);
			break;
		case R.id.iv_housekeep:
			//卖货
			Intent intent1 = new Intent(getActivity(),
					FindActivity.class);
			intent1.putExtra("TYBEFLG", "1");
			startActivity(intent1);
			break;
		case R.id.iv_family:
			//发货
			Intent intent2 = new Intent(getActivity(),
					FindActivity.class);
			intent2.putExtra("TYBEFLG", "2");
			startActivity(intent2);
			break;
		case R.id.iv_new:
			//运货
			Intent intent3 = new Intent(getActivity(),
					FindActivity.class);
			intent3.putExtra("TYBEFLG", "3");
			startActivity(intent3);
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 获取网络图片的方法
	 */
	private void getImageInfo() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(ConstantsUrl.image_me, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				JSONArray json;
				try {
					json = new JSONArray(arg2.toString());
					for (int i = 0; i < json.length(); i++) {
						JSONObject obj = json.getJSONObject(i);
						HomePic mHomePic = new HomePic();
						id = obj.getString("id");
						name = obj.getString("name");
						url = obj.getString("url");
						img = obj.getString("img");
						mHomePic.setId(id);
						mHomePic.setImg(img);
						mHomePic.setName(name);
						mHomePic.setUrl(url);
						mHomePics.add(mHomePic);
						System.out.println("imgInfo" + imgInfo.size());
					}
					Message msg = mHandler.obtainMessage();
					msg.obj = mHomePics;
					msg.what = 0x11;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
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

	/**
	 * 初始化轮播图的相关数据
	 */
	private void initView() {
		
		mAdBannerView = (AdBannerView) fragment_home
				.findViewById(R.id.page_banner_view);
		userId = SharePrefUtil.getString(getActivity(), "userid", "");
		
	}




	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("______________onStop____");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	
		super.onResume();
		System.out.println("______________onResume____");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("______________onStart____");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("______________nPause____");
	}

		
}





>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
