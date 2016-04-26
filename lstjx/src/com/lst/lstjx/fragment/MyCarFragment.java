<<<<<<< HEAD
package com.lst.lstjx.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.MyCarDetailActivity;
import com.lst.lstjx.bean.MyCar;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyCarFragment.MyCarAdapter.HolderView;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * author describe parameter return
 */
public class MyCarFragment extends Fragment implements OnClickListener {
	protected static final int GETSHOPCAT = 0X48165;
	protected static final int REFUSHSHOPCAR = 0X8797465;
	protected static final int DELSHAOPCAR = 0x561432;
	protected static final int CHANGECOUNT = 0x9741;
	protected static final int ADDCAR = 0x54158;
	private MyCarAdapter adapter;
	private Context mContext = getActivity();
	private String userid;
	private PullToRefreshListView mPullRefreshListView;

	// 以下是adapter中的声明
	public HolderView holderView;
	private List<MyCar> mCar;
	private List<MyCar> reCar;
	private View Dynamic;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETSHOPCAT:
				reCar = (List<MyCar>) msg.obj;
				//
				// 设置adapter
				if (reCar != null && reCar.size() != 0) {
					adapter = new MyCarAdapter(mContext);
					mPullRefreshListView.setVisibility(View.VISIBLE);
					mPullRefreshListView.setAdapter(adapter);
					mPullRefreshListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									Intent intent = new Intent(getActivity(),
											MyCarDetailActivity.class);
									// intent.putExtra("chexing", "罐装车");
									// setResult(11);
									startActivity(intent);

								}
							});
				} 
				break;
			case REFUSHSHOPCAR:
				reCar = (List<MyCar>) msg.obj;

				mPullRefreshListView.onRefreshComplete();
				if (reCar != null && reCar.size() != 0) {
					adapter = new MyCarAdapter(mContext);
					mPullRefreshListView.setAdapter(adapter);
					Toast.makeText(mContext, "刷新完成", Toast.LENGTH_SHORT).show();
				}
				break;
			// case DELSHAOPCAR:
			// int recResult = (Integer) msg.obj;
			// if (recResult == 1) {
			// recShopCar.remove(PositionDel);
			// adapter.notifyDataSetChanged();
			// Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
			// if (recShopCar != null && recShopCar.size() == 0) {
			// adapter.notifyDataSetChanged();
			// }
			// }
			// break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_my_car, container,
					false);
		}
		initView(Dynamic);
		reqCar(1);
		initRefresh();
		return Dynamic;
	}

	private void initView(View v) {
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(mContext, "userId", "");
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.pull_refresh_list);

	}

	// 初始化 下拉刷新组件
	private void initRefresh() {
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(mContext,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						reCar.clear();
						reqCar(2);
						// Do work to refresh the list here.
					}
				});
		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						Toast.makeText(mContext, "没有更多信息了", Toast.LENGTH_SHORT)
								.show();
					}
				});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		/**
		 * Add Sound Event Listener
		 */
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				mContext);
		mPullRefreshListView.setOnPullEventListener(soundListener);
	}

	// 请求车的数据
	private void reqCar(final int i) {

		// TODO Auto-generated method stub
		mCar = new ArrayList<MyCar>();
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(mContext, ConstantsUrl.getShopCar, params,
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONArray arr = new JSONArray(arg2.toString());
//							if (arr.length() == 0) {
//								Message msg = mHandler.obtainMessage();
//								msg.what = ADDCAR;
//								mHandler.sendMessage(msg);
//							} else {
								for (int i = 0; i < arr.length(); i++) {
									MyCar mshCar = new MyCar();
									JSONObject josn = arr.getJSONObject(i);
									String chexing = josn.getString("chexing");
									String chechang = josn
											.getString("chechang");
									String zaizhong = josn
											.getString("zaizhong");
									String img = josn.getString("img");
									String id = josn.getString("id");
									mshCar.setId(id);
									mshCar.setImg(img);
									mshCar.setChechang("车长" + chechang);
									mshCar.setChexing("车型" + chexing);
									mshCar.setZaizhong("载重" + zaizhong);
									mCar.add(mshCar);
								}
								if (i == 1) {
									Message msg = mHandler.obtainMessage();
									msg.obj = mCar;
									msg.what = GETSHOPCAT;
									mHandler.sendMessage(msg);
								} else {
									Message msg = mHandler.obtainMessage();
									msg.obj = mCar;
									msg.what = REFUSHSHOPCAR;
									mHandler.sendMessage(msg);
								}
//							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

	

	class MyCarAdapter extends BaseAdapter {
		private Context context;

		public MyCarAdapter(Context context) {
			this.context = context;

		}

		public int getCount() {
			return reCar.size();
		}

		public Object getItem(int arg0) {
			return reCar.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("InflateParams")
		public View getView(final int position, View currentView, ViewGroup arg2) {
			if (currentView == null) {
				holderView = new HolderView();
				currentView = LayoutInflater.from(context).inflate(
						R.layout.my_car_item, null);
				holderView.iv_adapter_list_car = (ImageView) currentView
						.findViewById(R.id.iv_adapter_list_car);
				holderView.tv_chechang = (TextView) currentView
						.findViewById(R.id.tv_chechang);
				holderView.tv_chexing = (TextView) currentView
						.findViewById(R.id.tv_chexing);
				holderView.tv_zaizhong = (TextView) currentView
						.findViewById(R.id.tv_zaizhong);

				currentView.setTag(holderView);
			} else {
				holderView = (HolderView) currentView.getTag();

			}
			ImageLoader.getInstance().displayImage(
					reCar.get(position).getImg(),
					holderView.iv_adapter_list_car);
			holderView.tv_chechang.setText("车长:"
					+ reCar.get(position).getChechang().toString());
			holderView.tv_chexing.setText("车型:"
					+ reCar.get(position).getChexing().toString());
			holderView.tv_zaizhong.setText("载重:"
					+ reCar.get(position).getZaizhong().toString());
			return currentView;
		}

		public class HolderView {
			public TextView tv_chexing;
			public TextView tv_chechang;
			public TextView tv_zaizhong;
			public ImageView iv_adapter_list_car;

		}

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
=======
package com.lst.lstjx.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.MyCarDetailActivity;
import com.lst.lstjx.bean.MyCar;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.fragment.MyCarFragment.MyCarAdapter.HolderView;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * author describe parameter return
 */
public class MyCarFragment extends Fragment implements OnClickListener {
	protected static final int GETSHOPCAT = 0X48165;
	protected static final int REFUSHSHOPCAR = 0X8797465;
	protected static final int DELSHAOPCAR = 0x561432;
	protected static final int CHANGECOUNT = 0x9741;
	protected static final int ADDCAR = 0x54158;
	private MyCarAdapter adapter;
	private Context mContext = getActivity();
	private String userid;
	private PullToRefreshListView mPullRefreshListView;

	// 以下是adapter中的声明
	public HolderView holderView;
	private List<MyCar> mCar;
	private List<MyCar> reCar;
	private View Dynamic;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETSHOPCAT:
				reCar = (List<MyCar>) msg.obj;
				//
				// 设置adapter
				if (reCar != null && reCar.size() != 0) {
					adapter = new MyCarAdapter(mContext);
					mPullRefreshListView.setVisibility(View.VISIBLE);
					mPullRefreshListView.setAdapter(adapter);
					mPullRefreshListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									Intent intent = new Intent(getActivity(),
											MyCarDetailActivity.class);
									// intent.putExtra("chexing", "罐装车");
									// setResult(11);
									startActivity(intent);

								}
							});
				} 
				break;
			case REFUSHSHOPCAR:
				reCar = (List<MyCar>) msg.obj;

				mPullRefreshListView.onRefreshComplete();
				if (reCar != null && reCar.size() != 0) {
					adapter = new MyCarAdapter(mContext);
					mPullRefreshListView.setAdapter(adapter);
					Toast.makeText(mContext, "刷新完成", Toast.LENGTH_SHORT).show();
				}
				break;
			// case DELSHAOPCAR:
			// int recResult = (Integer) msg.obj;
			// if (recResult == 1) {
			// recShopCar.remove(PositionDel);
			// adapter.notifyDataSetChanged();
			// Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
			// if (recShopCar != null && recShopCar.size() == 0) {
			// adapter.notifyDataSetChanged();
			// }
			// }
			// break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_my_car, container,
					false);
		}
		initView(Dynamic);
		reqCar(1);
		initRefresh();
		return Dynamic;
	}

	private void initView(View v) {
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(mContext, "userId", "");
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.pull_refresh_list);

	}

	// 初始化 下拉刷新组件
	private void initRefresh() {
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(mContext,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						reCar.clear();
						reqCar(2);
						// Do work to refresh the list here.
					}
				});
		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						Toast.makeText(mContext, "没有更多信息了", Toast.LENGTH_SHORT)
								.show();
					}
				});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		/**
		 * Add Sound Event Listener
		 */
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				mContext);
		mPullRefreshListView.setOnPullEventListener(soundListener);
	}

	// 请求车的数据
	private void reqCar(final int i) {

		// TODO Auto-generated method stub
		mCar = new ArrayList<MyCar>();
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(mContext, ConstantsUrl.getShopCar, params,
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONArray arr = new JSONArray(arg2.toString());
//							if (arr.length() == 0) {
//								Message msg = mHandler.obtainMessage();
//								msg.what = ADDCAR;
//								mHandler.sendMessage(msg);
//							} else {
								for (int i = 0; i < arr.length(); i++) {
									MyCar mshCar = new MyCar();
									JSONObject josn = arr.getJSONObject(i);
									String chexing = josn.getString("chexing");
									String chechang = josn
											.getString("chechang");
									String zaizhong = josn
											.getString("zaizhong");
									String img = josn.getString("img");
									String id = josn.getString("id");
									mshCar.setId(id);
									mshCar.setImg(img);
									mshCar.setChechang("车长" + chechang);
									mshCar.setChexing("车型" + chexing);
									mshCar.setZaizhong("载重" + zaizhong);
									mCar.add(mshCar);
								}
								if (i == 1) {
									Message msg = mHandler.obtainMessage();
									msg.obj = mCar;
									msg.what = GETSHOPCAT;
									mHandler.sendMessage(msg);
								} else {
									Message msg = mHandler.obtainMessage();
									msg.obj = mCar;
									msg.what = REFUSHSHOPCAR;
									mHandler.sendMessage(msg);
								}
//							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

	

	class MyCarAdapter extends BaseAdapter {
		private Context context;

		public MyCarAdapter(Context context) {
			this.context = context;

		}

		public int getCount() {
			return reCar.size();
		}

		public Object getItem(int arg0) {
			return reCar.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("InflateParams")
		public View getView(final int position, View currentView, ViewGroup arg2) {
			if (currentView == null) {
				holderView = new HolderView();
				currentView = LayoutInflater.from(context).inflate(
						R.layout.my_car_item, null);
				holderView.iv_adapter_list_car = (ImageView) currentView
						.findViewById(R.id.iv_adapter_list_car);
				holderView.tv_chechang = (TextView) currentView
						.findViewById(R.id.tv_chechang);
				holderView.tv_chexing = (TextView) currentView
						.findViewById(R.id.tv_chexing);
				holderView.tv_zaizhong = (TextView) currentView
						.findViewById(R.id.tv_zaizhong);

				currentView.setTag(holderView);
			} else {
				holderView = (HolderView) currentView.getTag();

			}
			ImageLoader.getInstance().displayImage(
					reCar.get(position).getImg(),
					holderView.iv_adapter_list_car);
			holderView.tv_chechang.setText("车长:"
					+ reCar.get(position).getChechang().toString());
			holderView.tv_chexing.setText("车型:"
					+ reCar.get(position).getChexing().toString());
			holderView.tv_zaizhong.setText("载重:"
					+ reCar.get(position).getZaizhong().toString());
			return currentView;
		}

		public class HolderView {
			public TextView tv_chexing;
			public TextView tv_chechang;
			public TextView tv_zaizhong;
			public ImageView iv_adapter_list_car;

		}

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
