package com.lst.lstjx.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.Header;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import c.t.m.g.d;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.ActionSheet;
import com.lst.lstjx.utils.ActionSheet.ActionSheetListener;
import com.lst.lstjx.utils.Bimp;
import com.lst.lstjx.utils.FileUtils;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.MyGridDialog;
import com.lst.yuewo.R;

/**
 * 添加我的车辆
 * author describe parameter return 这个类中主要是从相册选择照片并保存到img中 和拍摄 另 用到了个菜单
 */
public class AddCarActivity extends FragmentActivity implements
		OnClickListener, ActionSheetListener {

	private TextView tv_more_photo_cancel, tv_more_photo_publish, chepai,
			chexing, chechang;
	protected static final int PUBLISHPHOTO = 0X54454;
	public static Uri imageUri;
	private GridView choose_photo_more_photo;
	private GridAdapter adapter;
	private RelativeLayout root1, root2, root3, root4, root5, root6;
	private EditText zaizhong; // 编辑栏
	private String userid;
	private File mfile;

	private String[] str1 = { "4.2", "5.2", "5.8", "6.2", "6.5", "6.8", "7.2",
			"8.0", "9.6", "12.0", "13.0", "13.5", "15.0", "16.5", "17.5",
			"18.5", "20.0", "22.0", "24.0" };
	private String[] str2 = { "电动车", "轿车", "面包车", "金杯车", "厢式车", "低栏车", "中拦车",
			"高栏车", "集装箱", "平板车", "高低板车", "罐式车", "冷藏车", "保温车", "危险品车", "铁笼车",
			"叉车", "吊车", "铲车" };
	private ImageView jiashi, xingche;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PUBLISHPHOTO:

				Toast.makeText(AddCarActivity.this, "发表成功", Toast.LENGTH_SHORT)
						.show();
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				SharePrefUtil.saveBoolean(AddCarActivity.this, "hasClData", true);
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
		setContentView(R.layout.activity_more_photo_car);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(AddCarActivity.this, "userId", "");
		tv_more_photo_cancel = (TextView) findViewById(R.id.tv_more_photo_cancel);
		tv_more_photo_publish = (TextView) findViewById(R.id.tv_more_photo_publish);
		root1 = (RelativeLayout) findViewById(R.id.root1);
		jiashi = (ImageView) findViewById(R.id.jiashi);
		root2 = (RelativeLayout) findViewById(R.id.root2);
		xingche = (ImageView) findViewById(R.id.xingche);
		root3 = (RelativeLayout) findViewById(R.id.root3);
		chepai = (TextView) findViewById(R.id.chepai);
		root4 = (RelativeLayout) findViewById(R.id.root4);
		chexing = (TextView) findViewById(R.id.chexing);
		root5 = (RelativeLayout) findViewById(R.id.root5);
		zaizhong = (EditText) findViewById(R.id.zaizhong);
		root6 = (RelativeLayout) findViewById(R.id.root6);
		chechang = (TextView) findViewById(R.id.chechang);
		choose_photo_more_photo = (GridView) findViewById(R.id.choose_photo_more_photo);

		root1.setOnClickListener(this);
		root2.setOnClickListener(this);
		root3.setOnClickListener(this);
		root4.setOnClickListener(this);
		root5.setOnClickListener(this);
		root6.setOnClickListener(this);
		jiashi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(AddCarActivity.this,MyDynamicImgActivity.class);
//				intent.putExtra("bitmap", FileUtils.SDPATH+str+".JPEG");
//				startActivity(intent);
			}
		});
		xingche.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		tv_more_photo_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;

				finish();
			}
		});
		tv_more_photo_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String content =
				// publish_dynamic_photo_et_content.getText()
				// .toString().trim();
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
					upLoadImg(mList, "00", "00h");

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
							new PopupWindows(AddCarActivity.this,
									choose_photo_more_photo);
						} else {
							Intent intent = new Intent(AddCarActivity.this,
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

		@SuppressWarnings("deprecation")
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

					Intent intent = new Intent(AddCarActivity.this,
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

	int flog = 0;

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.root1:

			flog = 1;
			setTheme(R.style.ActionSheetStyleIOS7);
			showActionSheet();

			break;
		case R.id.root2:
			flog = 2;
			setTheme(R.style.ActionSheetStyleIOS7);
			showActionSheet();
			break;
		case R.id.root3:
			Intent intent = new Intent(AddCarActivity.this,
					ChePaiActivity.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.root4:
			final MyGridDialog dlg = new MyGridDialog(AddCarActivity.this,
					R.style.dlg_priority, str2, "请选择车型");

			dlg.show();
			// 监听当Dialog消失后要触发的时间
			dlg.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					int ps = dlg.getPosition();
					chexing.setText(str2[ps]);
				}
			});

			break;
		case R.id.root5:

			break;
		case R.id.root6:
			final MyGridDialog dlg2 = new MyGridDialog(AddCarActivity.this,
					R.style.dlg_priority, str1, "请选择车长");
			dlg2.show();
			// 监听当Dialog消失后要触发的时间
			dlg2.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					int ps = dlg2.getPosition();
					chechang.setText(str1[ps]+"M");
				}
			});
			break;
		default:
			break;
		}
	}

	// 类似ios的底部菜单样式 工具類中的actionsheet
	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("从相册选择照片", "拍摄照片")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	/**
	 * 菜单的监听事件 一个是拍从相册中选择照片 一个是拍照
	 */
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub
		switch (index) {
		// 选择照片
		case 0:
			selectPicture();
			break;
		// 拍照
		case 1:
			photo();
			break;
		default:
			break;
		}
	}

	private void selectPicture() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK); // Pick an item from the data
		intent.setType("image/*"); // 从所有图片中进行选择
		startActivityForResult(intent, 1);
	}
private String str ;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 100) {
			String extra = data.getStringExtra("chepai");
			chepai.setText(extra);

		}
		if (resultCode == RESULT_OK) {// 从相册选择照片不裁切
			try {
				Uri selectedImage = data.getData(); // 获取系统返回的照片的Uri
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);// 从系统表中查询指定Uri对应的照片
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex); // 获取照片路径
				cursor.close();
				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				str = picturePath.substring(
						picturePath.lastIndexOf("/") + 1,
						picturePath.lastIndexOf("."));
				FileUtils.saveBitmap(bitmap, str);
				if (flog == 1) {
					jiashi.setImageBitmap(bitmap);
				} else if (flog == 2) {
					xingche.setImageBitmap(bitmap);
				}
			} catch (Exception e) {
				// TODO Auto-generatedcatch block
				e.printStackTrace();
			}
		}
		if (requestCode == TAKE_PICTURE) {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			if (flog == 1) {

				jiashi.setImageBitmap(bitmap);
			} else if (flog == 2) {
				xingche.setImageBitmap(bitmap);
			} else {
				if (Bimp.drr.size() < 9 && resultCode == -1) {
					Bimp.drr.add(path);
				}
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
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

	// 得到系统当前时间并转化为字符串
	@SuppressLint("SimpleDateFormat")
	public static String getCharacterAndNumber() {
		String rel = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		rel = formatter.format(curDate);
		return rel;
	}

	@SuppressWarnings("unused")
	private void upLoadImg(List<File> file, String content, String title)
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
		System.out.println("___________file_______" + file.toString());
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
							Toast.makeText(AddCarActivity.this, "上传失败",
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
			Toast.makeText(AddCarActivity.this, "文件不存在", Toast.LENGTH_LONG)
					.show();
		} // 压缩图片(第一次)
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub

	}

}
