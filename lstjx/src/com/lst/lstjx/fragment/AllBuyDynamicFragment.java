package com.lst.lstjx.fragment;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.DynamicDetailCanPublishActivity;
import com.lst.lstjx.activity.ProPicActivity;
import com.lst.lstjx.bean.MyDeal;
import com.lst.lstjx.bean.MyDynamic;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 最新买家家
 */

public class AllBuyDynamicFragment extends Fragment {
	protected static final int MYDINAMIC = 0X9599;
	protected static final int REFUSHMYDINAMIC = 0X8456;
	private PullToRefreshListView mPullRefreshListView;
	private List<MyDeal> arrayList;
	private View Dynamic;
	private MyDeal myDeal;
//	private MyDeal myDeal2;
	private List<String> arrayList2;
	private GridViewAdapter mAdapter;
	private String userid;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MYDINAMIC:
				if (arrayList != null && arrayList.size() != 0) {
					MyAllBuyDynamicAdapter mAllBuyDynamicAdapter = new MyAllBuyDynamicAdapter(
							getActivity());
					mPullRefreshListView.setAdapter(mAllBuyDynamicAdapter);
				}
				break;
			case REFUSHMYDINAMIC:
				mPullRefreshListView.onRefreshComplete();
				Toast.makeText(getActivity(), "已经刷新", Toast.LENGTH_SHORT)
						.show();
				// System.out.println("__________arrayList_______"+arrayList.get(0).getVideo().toString());
				if (arrayList != null && arrayList.size() != 0) {
					MyAllBuyDynamicAdapter mAllBuyDynamicAdapter = new MyAllBuyDynamicAdapter(
							getActivity());
					mPullRefreshListView.setAdapter(mAllBuyDynamicAdapter);
				}
				break;
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
			Dynamic = inflater.inflate(R.layout.fragment_dynamic, container,
					false);
		}
		initView(Dynamic);
		getAllBuyDinamic(1);
		return Dynamic;
	}

	// 请求所有动态的网络请求
	private void getAllBuyDinamic(final int i) {
		// TODO Auto-generated method stub
		arrayList = new ArrayList<MyDeal>();
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		http.post(getActivity(), ConstantsUrl.my_fid_dynamic, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {

						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");
							System.out.println("我的好友动态" + data);
							for (int i = 0; i < data.length(); i++) {
								JSONObject udata = (JSONObject) data.get(i);

								myDeal = new MyDeal();
								myDeal.setId(udata.getString("id").toString());
								myDeal.setUid(udata.getString("uid").toString());
								myDeal.setUname(udata.getString("uname")
										.toString());
								// myDeal.setUserface(udata.getString("userface")
								// .toString());
								myDeal.setContent(udata.getString("content")
										.toString());

								myDeal.setAddtime(udata.getString("addtime")
										.toString());
								JSONArray img = (JSONArray) udata.get("img");
								List<String> imgs = new ArrayList<String>();
								for (int j = 0; j < img.length(); j++) {

									imgs.add((String) img.get(j));
								}
								myDeal.setImg(imgs);

								arrayList.add(myDeal);
							}
							if (i == 1) {
								Message msg = mHandler.obtainMessage();
								msg.what = MYDINAMIC;
								msg.obj = arrayList;
								mHandler.sendMessage(msg);
							} else {
								Message msg = mHandler.obtainMessage();
								msg.what = REFUSHMYDINAMIC;
								msg.obj = arrayList;
								mHandler.sendMessage(msg);
							}
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

	// 初始化数据
	private void initView(View v) {
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(getActivity(), "userId", "");
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(getActivity(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// Do work to refresh the list here.
						getAllBuyDinamic(2);
					}
				});
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						Toast.makeText(getActivity(), "已经到底部了",
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	class MyAllBuyDynamicAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAllBuyDynamicAdapter(Context context, List<MyDynamic> myDynamic) {
			inflater = LayoutInflater.from(context);
			initopt();
		}

		private void initopt() {
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					// 加载图片时的图片
					.showImageForEmptyUri(R.drawable.ic_launcher)
					// 没有图片资源时的默认图片
					.showImageOnFail(R.drawable.ic_launcher)
					// 加载失败时的图片
					.cacheInMemory(true)
					// 启用内存缓存
					.cacheOnDisc(true)
					// 启用外存缓存
					.considerExifParams(true)
					// 启用EXIF和JPEG图像格式
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(20)) // 设置显示风格这里是圆角矩形
					.build();

		}

		public MyAllBuyDynamicAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("InflateParams")
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final viewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_my_buy_dynamic,
						null);
				holder = new viewHolder();
				holder.more_photo = (GridView) convertView
						.findViewById(R.id.more_photo);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.userface = (ImageView) convertView
						.findViewById(R.id.userface);
				holder.chat = (TextView) convertView.findViewById(R.id.chat);
				holder.fenxiang = (ImageView) convertView
						.findViewById(R.id.fenxiang);
				holder.uname = (TextView) convertView.findViewById(R.id.uname);
				holder.addtime = (TextView) convertView
						.findViewById(R.id.addtime);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
			}
			holder.chat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RongIM.getInstance().startPrivateChat(getActivity(),
							arrayList.get(position).getUid(),
							arrayList.get(position).getUname());
				}
			});
			holder.fenxiang.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent1 = new Intent(Intent.ACTION_SEND);
					intent1.setType("image/*");
					intent1.setType("text/plain");
					intent1.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
					intent1.putExtra(Intent.EXTRA_TEXT, "邀请您使用'龙商家信'。下载地址：");
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(Intent.createChooser(intent1, getActivity()
							.getTitle()));

				}
			});
			holder.uname.setText(arrayList.get(position).getUname());
			holder.addtime.setText(arrayList.get(position).getAddtime());
			holder.content.setText(arrayList.get(position).getContent());
			// ImageLoader.getInstance().displayImage(
			// arrayList.get(position).getUserface(), holder.userface);

//			myDeal2 = arrayList.get(position);
			arrayList2 = new ArrayList<String>();
			for (int i = 0; i < arrayList.get(position).getImg().size(); i++) {
				String imgUrl = arrayList.get(position).getImg().get(i);
				arrayList2.add(imgUrl);
			}
			mAdapter = new GridViewAdapter(getActivity(), arrayList2);
			holder.more_photo.setAdapter(mAdapter);
			holder.more_photo.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position1, long id) {
					Intent intent = new Intent(getActivity(),
							ProPicActivity.class);
					intent.putExtra("propicurl", arrayList.get(position)
							.getImg().get(position1));
					startActivity(intent);
				}
			});

			return convertView;

		}

		public class viewHolder {
			GridView more_photo;
			TextView content;
			ImageView userface;
			ImageView fenxiang;
			TextView chat;
			TextView uname;
			TextView addtime;
		}

	}

	public class GridViewAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;
		private List<String> arrayList2 = new ArrayList<String>() ;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridViewAdapter(Context context, List<String> arrayList2) {
			inflater = LayoutInflater.from(context);
			this.arrayList2 = arrayList2;
		}

		public int getCount() {
			return arrayList2.size();
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
//			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (arrayList2.size() != 0 && arrayList2.size() < 9) {

				ImageLoader.getInstance().displayImage(
						arrayList2.get(position).toString(), holder.image);
			} else {
				holder.image.setVisibility(View.GONE);
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

	}
}
