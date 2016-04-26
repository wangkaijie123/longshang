package com.lst.lstjx.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.ProPicActivity;
import com.lst.lstjx.bean.MyDeal;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FriendDealFragment extends Fragment implements OnClickListener {
	protected static final int MYDYNAMIC = 0X45684213;
	private MyDeal myDeal;
	private List<MyDeal> arrayList = new ArrayList<MyDeal>();
	private PullToRefreshListView mPullRefreshListView;
	private int delDynamicCode;
	private MyDealAdapter adapter;
	private GridViewAdapter mAdapter;
	private View Dynamic;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getActivity(), "数据加载失败", 0).show();
				break;
			case 1:
				//

				if (arrayList != null && arrayList.size() != 0) {
					adapter = new MyDealAdapter(getActivity(), arrayList);
					mPullRefreshListView.setAdapter(adapter);

				}

				break;
			case 2:
				//
				mPullRefreshListView.onRefreshComplete();

				if (arrayList != null && arrayList.size() != 0) {
					adapter = new MyDealAdapter(getActivity(), arrayList);
					mPullRefreshListView.setAdapter(adapter);

				}

				break;

			default:
				break;
			}
		};
	};

	private String hisuserid;

	public FriendDealFragment(String hisuserid) {
		this.hisuserid = hisuserid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Dynamic == null) {
			Dynamic = inflater.inflate(R.layout.activity_my_deal, container,
					false);
		}

		initView(Dynamic);
		postInfo(1);
		return Dynamic;
	}

	private void initView(View v) {

		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.dynamic);

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
						arrayList.clear();
						postInfo(2);
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

	private int flag = 0;
	private int pos;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback_wd:
			getActivity().finish();
			break;

		default:
			break;
		}

	}

	private void postInfo(final int i) {
		// mDialog = DialogUtil.createProgressDialog(MyDynamicActivity.this,
		// "正在加载数据");
		// mDialog.setCanceledOnTouchOutside(false);
		// mDialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", hisuserid);// 动态id
		client.post(getActivity(), ConstantsUrl.my_dynamic, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						System.out.println("获取成功_____________________" + arg2);

						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");

							flag = data.length();
							if (flag == 0) {
								SharePrefUtil.saveBoolean(getActivity(),
										"hHasData", false);
								getActivity().finish();

							} else {
								for (int i = 0; i < flag; i++) {
									JSONObject udata = (JSONObject) data.get(i);

									myDeal = new MyDeal();
									myDeal.setId(udata.getString("id")
											.toString());
									myDeal.setUid(udata.getString("uid")
											.toString());
									myDeal.setUname(udata.getString("uname")
											.toString());
									// myDeal.setUserface(udata.getString("userface")
									// .toString());
									myDeal.setContent(udata
											.getString("content").toString());

									myDeal.setAddtime(udata
											.getString("addtime").toString());
									JSONArray img = (JSONArray) udata
											.get("img");
									List<String> imgs = new ArrayList<String>();
									for (int j = 0; j < img.length(); j++) {

										imgs.add((String) img.get(j));
									}
									myDeal.setImg(imgs);

									arrayList.add(myDeal);
								}
								if (i == 1) {

									Message message = handler.obtainMessage();
									message.what = 1;
									message.obj = arrayList;
									handler.sendMessage(message);
								} else {
									Message message = handler.obtainMessage();
									message.what = 2;
									message.obj = arrayList;
									handler.sendMessage(message);

								}
							}
						} catch (Exception e) {
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

	class MyDealAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyDealAdapter(Context context, List<MyDeal> mDeal) {
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

		public MyDealAdapter(Context context) {
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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			viewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_my_deal, null);
				holder = new viewHolder();
				holder.more_photo = (GridView) convertView
						.findViewById(R.id.more_photo);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.userface = (ImageView) convertView
						.findViewById(R.id.userface);				
				holder.fenxiang = (ImageView) convertView
						.findViewById(R.id.fenxiang);
				holder.uname = (TextView) convertView.findViewById(R.id.uname);
				holder.addtime = (TextView) convertView
						.findViewById(R.id.addtime);
				convertView.setTag(holder);
			} else {
				holder = (viewHolder) convertView.getTag();
			}
		
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

			List<String> arrayList2 = new ArrayList<String>();
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
			ImageView shanchu;
			TextView uname;
			TextView addtime;
		}

	}

	public class GridViewAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;
		private List<String> arrayList2 = new ArrayList<String>();

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
