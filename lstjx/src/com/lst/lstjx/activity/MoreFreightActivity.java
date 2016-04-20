package com.lst.lstjx.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lst.lstjx.address.AddressData;
import com.lst.lstjx.address.MyAlertDialog;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.datepicker.AbstractWheelTextAdapter;
import com.lst.lstjx.datepicker.ArrayWheelAdapter;
import com.lst.lstjx.datepicker.DatePickerPopWindow;
import com.lst.lstjx.datepicker.OnWheelChangedListener;
import com.lst.lstjx.datepicker.WheelView;
import com.lst.lstjx.utils.Bimp;
import com.lst.lstjx.utils.FileUtils;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.utils.SoftInputFromWindow;
import com.lst.yuewo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发布车源界面
 */
public class MoreFreightActivity extends Activity implements
		OnClickListener{
	
	private String cityTxt;
	
	private TextView tv_more_photo_cancel, tv_more_photo_publish,
			startingchoose, endingchoose,timechoose,tv_cx;

	private RelativeLayout rl_xz;
	protected static final int PUBLISHPHOTO = 0X54454;
	public static Uri imageUri;
	
	private GridView choose_photo_more_photo;
	private GridAdapter adapter;
	private File mfile;
	private LinearLayout lin_freight;
	private EditText 
			publish_dynamic_photo_et_content, starting, ending; // 编辑栏
	private String userid;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PUBLISHPHOTO:
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				SharePrefUtil.saveBoolean(
						MoreFreightActivity.this, "hasWlData", true);

				Toast.makeText(MoreFreightActivity.this, "发表成功",
						Toast.LENGTH_SHORT).show();
				finish();

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
		setContentView(R.layout.activity_more_photo_freight);
		initView();
		SoftInputFromWindow.setupUI(MoreFreightActivity.this, lin_freight); 
	}

	private void initView() {
		// TODO Auto-generated method stub
		userid = SharePrefUtil
				.getString(MoreFreightActivity.this, "userId", "");
		publish_dynamic_photo_et_content = (EditText) findViewById(R.id.publish_dynamic_photo_et_content);
		starting = (EditText) findViewById(R.id.starting);
		lin_freight = (LinearLayout) findViewById(R.id.lin_freight);
		ending = (EditText) findViewById(R.id.ending);
		rl_xz = (RelativeLayout) findViewById(R.id.rl_xz);
		tv_more_photo_cancel = (TextView) findViewById(R.id.tv_more_photo_cancel);
		tv_more_photo_publish = (TextView) findViewById(R.id.tv_more_photo_publish);
		choose_photo_more_photo = (GridView) findViewById(R.id.choose_photo_more_photo);
		startingchoose = (TextView) findViewById(R.id.startingchoose);
		endingchoose = (TextView) findViewById(R.id.endingchoose);
		timechoose = (TextView) findViewById(R.id.timechoose);
		timechoose = (TextView) findViewById(R.id.tv_cx);
		
		rl_xz.setOnClickListener(this);
		startingchoose.setOnClickListener(this);
		endingchoose.setOnClickListener(this);
		tv_more_photo_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				FileUtils.deleteDir();
				finish();
			}
		});
		tv_more_photo_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					String content = publish_dynamic_photo_et_content.getText()
							.toString().trim();
					List<File> mList = new ArrayList<File>();
					for (int i = 0; i < Bimp.drr.size(); i++) {
						String Str = Bimp.drr.get(i).substring(
								Bimp.drr.get(i).lastIndexOf("/") + 1,
								Bimp.drr.get(i).lastIndexOf("."));
						Bitmap bitmap = Bimp.bmp.get(i);
						mfile = new File(FileUtils.SDPATH, Str + ".JPEG");
						FileUtils.saveBitmap(bitmap, Str);
						mList.add(mfile);

					}
					try {
						upLoadImg(mList, content,"aa");

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		adapter = new GridAdapter(this);
		adapter.update();
		choose_photo_more_photo.setAdapter(adapter);
		choose_photo_more_photo
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (position == Bimp.bmp.size()) {
							new PopupWindows(MoreFreightActivity.this,
									choose_photo_more_photo);
						} else {
							Intent intent = new Intent(MoreFreightActivity.this,
									PhotoActivity.class);
							intent.putExtra("ID", position);
							startActivity(intent);
						}
					}

				});
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
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

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println("图片的路径" + path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(MoreFreightActivity.this,
							TestPicActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.timechoose:
			Date date=new Date();
			DateFormat df=new SimpleDateFormat("yyyyMMdd");
			DatePickerPopWindow popWindow=new DatePickerPopWindow(MoreFreightActivity.this,df.format(date));
			WindowManager.LayoutParams lp=getWindow().getAttributes();
			lp.alpha=0.5f;
			getWindow().setAttributes(lp);
			popWindow.showAtLocation(findViewById(R.id.root), Gravity.CENTER, 0, 0);
			popWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					WindowManager.LayoutParams lp=getWindow().getAttributes();
					lp.alpha=1f;
					getWindow().setAttributes(lp);
					
					String timeDate = SharePrefUtil.getString(getApplicationContext(), "TIMEDATE", "");
					if (timeDate!=null&&!"".equals(timeDate)) {
						timechoose.setText(timeDate);
					}
				
				}
			});
			break;
		case R.id.startingchoose:
			View view = dialogm();
			final MyAlertDialog dialog1 = new MyAlertDialog(MoreFreightActivity.this)
					.builder()
					.setTitle("请选择出发地")
					// .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
					// .setEditText("1111111111111")
					.setView(view)
					.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
							
						}
					});
			dialog1.setPositiveButton("保存", new OnClickListener() {
				@Override
				public void onClick(View v) {
					startingchoose.setText(cityTxt);
					Toast.makeText(getApplicationContext(), cityTxt, 1).show();
				}
			});
			dialog1.show();
			break;
		case R.id.endingchoose:
			View view1 = dialogm();
			final MyAlertDialog dialog2 = new MyAlertDialog(MoreFreightActivity.this)
					.builder()
					.setTitle("请选择出发地")
					// .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
					// .setEditText("1111111111111")
					.setView(view1)
					.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					});
			dialog2.setPositiveButton("保存", new OnClickListener() {
				@Override
				public void onClick(View v) {
					endingchoose.setText(cityTxt);
					Toast.makeText(getApplicationContext(), cityTxt, 1).show();
				}
			});
			dialog2.show();
			break;
		case R.id.rl_xz:
			Intent intent = new Intent(MoreFreightActivity.this,MyCarActivity.class);
			startActivityForResult(intent, 11);
			break;
		default:
			break;
		}
	}
	private View dialogm() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(0);

		// 地区选择
		final WheelView ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(0);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, cities, newValue);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						newValue);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		country.setCurrentItem(1);// 设置北京
		city.setCurrentItem(1);
		ccity.setCurrentItem(1);
		return contentView;
	}
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}
	
	/**
	 * Updates the ccity wheel
	 */
	private void updatecCities(WheelView city, String ccities[][][], int index,
			int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

	

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}


	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		case 11:
			if (data!=null) {
				String extra = data.getStringExtra("chexing");
				tv_cx.setText(extra);
			}
			break;
		}
	}


	// 得到系统当前时间并转化为字符串

	public static String getCharacterAndNumber() {
		String rel = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		rel = formatter.format(curDate);
		return rel;
	}

	@SuppressWarnings("unused")
	private void upLoadImg(List<File> file, String content,String title)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		for (int i = 0; i < file.size(); i++) {
			params.put("file" + i, file.get(i));
		}
		params.put("title", title);
		params.put("content", content);
		params.add("uid", userid);
	
		// 上传文件
		if (!file.isEmpty() && file.size() > 0) {
			// mDialog = DialogUtil.createProgressDialog(MoreBuyActivity.this,
			// "上传中请稍等");
			// mDialog.show();
			client.post(ConstantsUrl.publish_dynamic_pic, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							Message msg = mHandler.obtainMessage();
							msg.what = PUBLISHPHOTO;
							mHandler.sendMessage(msg);

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// 上传失败后要做到工作
							// mDialog.dismiss();
							Toast.makeText(MoreFreightActivity.this, "上传失败",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onProgress(int bytesWritten, int totalSize) {
							// TODO Auto-generated method stub
							super.onProgress(bytesWritten, totalSize);
							int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
							// 上传进度显示
							// Log.e("上传 Progress>>>>>", bytesWritten + " / " +
							// totalSize);
						}

						@Override
						public void onRetry(int retryNo) {
							// TODO Auto-generated method stub
							super.onRetry(retryNo);
							// 返回重试次数
						}

						
					});

		} else {
			Toast.makeText(MoreFreightActivity.this, "文件不存在", Toast.LENGTH_LONG)
					.show();
		} // 压缩图片(第一次)
	}



}
