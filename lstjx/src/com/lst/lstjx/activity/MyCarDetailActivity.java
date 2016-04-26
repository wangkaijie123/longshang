<<<<<<< HEAD
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

import com.lst.lstjx.bean.MyCar;
import com.lst.lstjx.bean.MyCarDetails;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.Bimp;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * author describe parameter return
 */
public class MyCarDetailActivity extends Activity {
	protected static final int GETSHOPCAT = 0X48165;
	protected static final int REFUSHSHOPCAR = 0X8797465;
	protected static final int DELSHAOPCAR = 0x561432;
	protected static final int CHANGECOUNT = 0x9741;
	protected static final int ADDCAR = 0x54158;
	private TextView tv_more_photo_cancel, chechang, chexing, chepai, zaizhong;
	private ImageView xingche, jiashi;
	private GridView choose_photo_more_photo;
	private String userid;
	private Dialog mDialog;
	private MyCarDetails mCarDetails;
	private MyCarDetails reCarBean;
	private GridViewAdapter adapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				reCarBean = (MyCarDetails) msg.obj;
				chexing.setText(reCarBean.getChexing().toString());
				chechang.setText(reCarBean.getChechang().toString());
				zaizhong.setText(reCarBean.getZaizhong().toString());
				ImageLoader.getInstance().displayImage(
						reCarBean.getJiashi().toString(), jiashi);
				ImageLoader.getInstance().displayImage(
						reCarBean.getXingche().toString(), xingche);
				if (reCarBean.getImgs() != null
						&& reCarBean.getImgs().size() > 0) {
					
					adapter = new GridViewAdapter(MyCarDetailActivity.this);
					choose_photo_more_photo.setAdapter(adapter);
					choose_photo_more_photo
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									Intent intent = new Intent(
											MyCarDetailActivity.this,
											ProPicActivity.class);
									intent.putExtra("propicurl", reCarBean
											.getImgs().get(position - 1));
									startActivity(intent);
								}
							});
				}
				mDialog.dismiss();
				break;
			case 1:
				mDialog.dismiss();
				Toast.makeText(MyCarDetailActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_car_detail);
		intView();
		reqMyCarDetail();
	}

	private void intView() {
		userid = SharePrefUtil.getString(getApplicationContext(), "Uid", "");
		tv_more_photo_cancel = (TextView) findViewById(R.id.tv_more_photo_cancel);
		chechang = (TextView) findViewById(R.id.chechang);
		chexing = (TextView) findViewById(R.id.chexing);
		chepai = (TextView) findViewById(R.id.chepai);
		zaizhong = (TextView) findViewById(R.id.zaizhong);
		jiashi = (ImageView) findViewById(R.id.jiashi);
		xingche = (ImageView) findViewById(R.id.xingche);
		choose_photo_more_photo = (GridView) findViewById(R.id.choose_photo_more_photo);

		tv_more_photo_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public class GridViewAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridViewAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
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
			final int coord = position;
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

			if (reCarBean.getImgs().size() != 0 && reCarBean.getImgs().size() < 9) {

				ImageLoader.getInstance().displayImage(
						reCarBean.getImgs().get(coord).toString(), holder.image);
			} else {
				holder.image.setVisibility(View.GONE);
			}
			
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	private void reqMyCarDetail() {
		mDialog = DialogUtil.createProgressDialog(MyCarDetailActivity.this,
				"正在加载数据");
		mDialog.show();
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(MyCarDetailActivity.this, ConstantsUrl.getShopCar, params,
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject json;
						try {
							json = new JSONObject(arg2.toString());

							JSONObject data = json.getJSONObject("data");

							JSONArray imgs = json.getJSONArray("imgs");
							ArrayList<String> imgsArr = new ArrayList<String>();
							for (int i = 0; i < imgs.length(); i++) {
								imgsArr.add(imgs.getString(i));
							}
							mCarDetails = new MyCarDetails();
							mCarDetails.setChechang(data.getString("chechang"));
							mCarDetails.setChexing(data.getString("chexing"));
							mCarDetails.setZaizhong(data.getString("zaizhong"));
							mCarDetails.setXingche(data.getString("xingche")
									.toString());
							mCarDetails.setZaizhong(data.getString("jiashi")
									.toString());
							mCarDetails.setImgs(imgsArr);
							Message message = new Message();
							message.obj = mCarDetails;
							message.what = 0;
							handler.sendMessage(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}
}
=======
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

import com.lst.lstjx.bean.MyCar;
import com.lst.lstjx.bean.MyCarDetails;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.Bimp;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * author describe parameter return
 */
public class MyCarDetailActivity extends Activity {
	protected static final int GETSHOPCAT = 0X48165;
	protected static final int REFUSHSHOPCAR = 0X8797465;
	protected static final int DELSHAOPCAR = 0x561432;
	protected static final int CHANGECOUNT = 0x9741;
	protected static final int ADDCAR = 0x54158;
	private TextView tv_more_photo_cancel, chechang, chexing, chepai, zaizhong;
	private ImageView xingche, jiashi;
	private GridView choose_photo_more_photo;
	private String userid;
	private Dialog mDialog;
	private MyCarDetails mCarDetails;
	private MyCarDetails reCarBean;
	private GridViewAdapter adapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				reCarBean = (MyCarDetails) msg.obj;
				chexing.setText(reCarBean.getChexing().toString());
				chechang.setText(reCarBean.getChechang().toString());
				zaizhong.setText(reCarBean.getZaizhong().toString());
				ImageLoader.getInstance().displayImage(
						reCarBean.getJiashi().toString(), jiashi);
				ImageLoader.getInstance().displayImage(
						reCarBean.getXingche().toString(), xingche);
				if (reCarBean.getImgs() != null
						&& reCarBean.getImgs().size() > 0) {
					
					adapter = new GridViewAdapter(MyCarDetailActivity.this);
					choose_photo_more_photo.setAdapter(adapter);
					choose_photo_more_photo
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									Intent intent = new Intent(
											MyCarDetailActivity.this,
											ProPicActivity.class);
									intent.putExtra("propicurl", reCarBean
											.getImgs().get(position - 1));
									startActivity(intent);
								}
							});
				}
				mDialog.dismiss();
				break;
			case 1:
				mDialog.dismiss();
				Toast.makeText(MyCarDetailActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_car_detail);
		intView();
		reqMyCarDetail();
	}

	private void intView() {
		userid = SharePrefUtil.getString(getApplicationContext(), "Uid", "");
		tv_more_photo_cancel = (TextView) findViewById(R.id.tv_more_photo_cancel);
		chechang = (TextView) findViewById(R.id.chechang);
		chexing = (TextView) findViewById(R.id.chexing);
		chepai = (TextView) findViewById(R.id.chepai);
		zaizhong = (TextView) findViewById(R.id.zaizhong);
		jiashi = (ImageView) findViewById(R.id.jiashi);
		xingche = (ImageView) findViewById(R.id.xingche);
		choose_photo_more_photo = (GridView) findViewById(R.id.choose_photo_more_photo);

		tv_more_photo_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public class GridViewAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridViewAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
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
			final int coord = position;
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

			if (reCarBean.getImgs().size() != 0 && reCarBean.getImgs().size() < 9) {

				ImageLoader.getInstance().displayImage(
						reCarBean.getImgs().get(coord).toString(), holder.image);
			} else {
				holder.image.setVisibility(View.GONE);
			}
			
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	private void reqMyCarDetail() {
		mDialog = DialogUtil.createProgressDialog(MyCarDetailActivity.this,
				"正在加载数据");
		mDialog.show();
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(MyCarDetailActivity.this, ConstantsUrl.getShopCar, params,
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						JSONObject json;
						try {
							json = new JSONObject(arg2.toString());

							JSONObject data = json.getJSONObject("data");

							JSONArray imgs = json.getJSONArray("imgs");
							ArrayList<String> imgsArr = new ArrayList<String>();
							for (int i = 0; i < imgs.length(); i++) {
								imgsArr.add(imgs.getString(i));
							}
							mCarDetails = new MyCarDetails();
							mCarDetails.setChechang(data.getString("chechang"));
							mCarDetails.setChexing(data.getString("chexing"));
							mCarDetails.setZaizhong(data.getString("zaizhong"));
							mCarDetails.setXingche(data.getString("xingche")
									.toString());
							mCarDetails.setZaizhong(data.getString("jiashi")
									.toString());
							mCarDetails.setImgs(imgsArr);
							Message message = new Message();
							message.obj = mCarDetails;
							message.what = 0;
							handler.sendMessage(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
