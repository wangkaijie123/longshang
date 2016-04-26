<<<<<<< HEAD
package com.lst.lstjx.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.ActionSheet;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.PictureUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.utils.ActionSheet.ActionSheetListener;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的头像
 * author describe parameter return 这个类中主要是从相册选择照片并保存到img中 和拍摄 另 用到了个菜单
 */
public class MinePhotoActivity extends FragmentActivity implements
		OnClickListener, ActionSheetListener {
	/** 用来请求照相功能的常量 */
	public static final int CAMERA_WITH_DATA = 168;
	/** 用来请求图片选择器的常量 */
	public static final int PHOTO_PICKED_WITH_DATA = CAMERA_WITH_DATA + 1;
	private TextView tv_more_photo_cancel, tv_more_photo_publish;
	private Button choose_photo_more_photo;
	private ImageView more_photo_image;
	private Dialog mDialog;
	/** 拍照的照片存储位置 */
	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
	public static Uri imageUri;
	private static File mCurrentPhotoFile;// 照相机拍照得到的图片
	private Bitmap myBitmap;
	private String uid;
	private File file;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {				
				Toast.makeText(MinePhotoActivity.this, "保存成功", 0).show();
			} else if (msg.what == 1) {
				Toast.makeText(MinePhotoActivity.this, "保存失败", 0).show();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_photo);
		initView();
		getData();
	}

	private void initView() {
		uid = SharePrefUtil.getString(MinePhotoActivity.this, "userId", "");
		tv_more_photo_cancel = (TextView) findViewById(R.id.tv_more_photo_cancel);
		tv_more_photo_publish = (TextView) findViewById(R.id.tv_more_photo_publish);
		choose_photo_more_photo = (Button) findViewById(R.id.choose_photo_more_photo);
		more_photo_image = (ImageView) findViewById(R.id.more_photo_image);

		tv_more_photo_cancel.setOnClickListener(this);
		tv_more_photo_publish.setOnClickListener(this);
		choose_photo_more_photo.setOnClickListener(this);
	}

	private void getData() {	
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", uid);
		client.post(MinePhotoActivity.this, ConstantsUrl.get_users_info,
				params, new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");

							String url = data.getString("face").toString();
							ImageLoader.getInstance().displayImage(url,
									more_photo_image);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
					

					}
				});

	}

	// 类似ios的底部菜单样式 工具類中的actionsheet
	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("从相册选择照片", "拍摄照片")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_more_photo_cancel:
			Bitmap bit = ((BitmapDrawable) more_photo_image.getDrawable())
					.getBitmap();
			Intent intent = new Intent();
			// intent.putExtra("bitmap", bit);
			MinePhotoActivity.this.setResult(4, intent);
			finish();
			break;

		case R.id.tv_more_photo_publish:
			try {
				upLoadImg(file, "face", uid);
				finish();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		// 选择照片的按鈕的的点击事件
		case R.id.choose_photo_more_photo:
			setTheme(R.style.ActionSheetStyleIOS7);
			showActionSheet();
			break;
		default:
			break;
		}
	}

	
	private void upLoadImg(File file, String field, String uid)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("field", field);
		params.put("value", file);
		params.put("uid", uid);
		System.out.println("___________file_______" + file);
		// 上传文件
		if (file.length() > 0) {
			mDialog = DialogUtil.createProgressDialog(MinePhotoActivity.this,
					"上传中请稍等");
			mDialog.show();
			client.post(ConstantsUrl.modify_users_face, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							// 上传成功后要做的工作
							Toast.makeText(MinePhotoActivity.this, "上传成功",
									Toast.LENGTH_LONG).show();
							mDialog.dismiss();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// 上传失败后要做到工作
							mDialog.dismiss();
							Toast.makeText(MinePhotoActivity.this, "上传失败",
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
			Toast.makeText(MinePhotoActivity.this, "文件不存在", Toast.LENGTH_LONG)
					.show();
		} // 压缩图片(第一次)
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "dismissed isCancle = " +
		// isCancle, 0).show();
	}

	/**
	 * 菜单的监听事件 一个是拍从相册中选择照片 一个是拍照
	 */
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub
		switch (index) {

		// 选择照片
		case 0:
			try {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				((Activity) MinePhotoActivity.this).startActivityForResult(
						intent, PHOTO_PICKED_WITH_DATA);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(MinePhotoActivity.this, "没有找到相册",
						Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getApplicationContext(), "选择照片 ", 0).show();
			break;
		// 拍照
		case 1:
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs(); // 创建照片的存储目录
			}
			try {
				imageUri = Uri.fromFile(getmCurrentPhotoFile());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				MinePhotoActivity.this.startActivityForResult(intent,
						CAMERA_WITH_DATA);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(MinePhotoActivity.this, "没有找到照相机",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 得到当前图片文件的路径
	 * 
	 * @return
	 */
	public static File getmCurrentPhotoFile() {
		if (mCurrentPhotoFile == null) {
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs(); // 创建照片的存储目录
			}
			mCurrentPhotoFile = new File(PHOTO_DIR, getCharacterAndNumber()
					+ ".png"/* 此处可更换文件后缀 */);
			if (!mCurrentPhotoFile.exists()) // 判断存储文件是否存在>不存在则创建
				try {
					mCurrentPhotoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return mCurrentPhotoFile;
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = new Intent("com.android.camera.action.CROP");
		Uri data2 = null;
		if (data == null) {
			data2 = PictureUtil.getImageUri();
		} else {
			data2 = data.getData();
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PictureUtil.PHOTO_PICKED_WITH_DATA:
				intent.setDataAndType(data2, "image/*");
				intent.putExtra("crop", true);
				// 设置裁剪尺寸
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 160);
				intent.putExtra("outputY", 130);
				intent.putExtra("return-data", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						PictureUtil.getImageCaiUri());
				startActivityForResult(intent, PictureUtil.PHOTO_CROP);
				break;
			case PictureUtil.CAMERA_WITH_DATA:
				intent.setDataAndType(data2, "image/*");
				intent.putExtra("crop", true);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 160);
				intent.putExtra("outputY", 130);
				intent.putExtra("return-data", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						PictureUtil.getImageCaiUri());
				startActivityForResult(intent, PictureUtil.PHOTO_CROP);
				break;
			case PictureUtil.PHOTO_CROP:
				Bundle bundle = data.getExtras();
				myBitmap = (Bitmap) bundle.get("data");
				Bitmap bitImage = comp(myBitmap);
				String fileName = getCharacterAndNumber();
				file = new File(PictureUtil.PHOTO_DIR, fileName + ".png");
				saveMyBitmap(bitImage, file);
				more_photo_image.setImageBitmap(bitImage);
				// final Map<String, File> fileParas = new HashMap<String,
				// File>();
				// fileParas.put("file", file);

				// /**
				// * 在这里选择完毕后,直接进行了上传,在logcat中查看结果吧..
				// */
				// new Thread(new Runnable() {
				// public void run() {
				// StringBuilder result = HttpUploadFile.sendFile(url, null,
				// fileParas);
				// System.out.println("###" + result.toString());
				// }
				// }).start();
				break;

			default:
				break;
			}
		}
	}

	// 压缩图片(第一次)
	private Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		if (baos.toByteArray().length / 1024 > 200) {// 判断如果图片大于200KB,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 160f;// 这里设置高度为800f
		float ww = 130f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 压缩图片(第二次)
	private Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public void saveMyBitmap(Bitmap mBitmap, File file) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
			byte[] bitmapData = baos.toByteArray();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bitmapData);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
=======
package com.lst.lstjx.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.ActionSheet;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.PictureUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.utils.ActionSheet.ActionSheetListener;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的头像
 * author describe parameter return 这个类中主要是从相册选择照片并保存到img中 和拍摄 另 用到了个菜单
 */
public class MinePhotoActivity extends FragmentActivity implements
		OnClickListener, ActionSheetListener {
	/** 用来请求照相功能的常量 */
	public static final int CAMERA_WITH_DATA = 168;
	/** 用来请求图片选择器的常量 */
	public static final int PHOTO_PICKED_WITH_DATA = CAMERA_WITH_DATA + 1;
	private TextView tv_more_photo_cancel, tv_more_photo_publish;
	private Button choose_photo_more_photo;
	private ImageView more_photo_image;
	private Dialog mDialog;
	/** 拍照的照片存储位置 */
	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
	public static Uri imageUri;
	private static File mCurrentPhotoFile;// 照相机拍照得到的图片
	private Bitmap myBitmap;
	private String uid;
	private File file;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {				
				Toast.makeText(MinePhotoActivity.this, "保存成功", 0).show();
			} else if (msg.what == 1) {
				Toast.makeText(MinePhotoActivity.this, "保存失败", 0).show();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_photo);
		initView();
		getData();
	}

	private void initView() {
		uid = SharePrefUtil.getString(MinePhotoActivity.this, "userId", "");
		tv_more_photo_cancel = (TextView) findViewById(R.id.tv_more_photo_cancel);
		tv_more_photo_publish = (TextView) findViewById(R.id.tv_more_photo_publish);
		choose_photo_more_photo = (Button) findViewById(R.id.choose_photo_more_photo);
		more_photo_image = (ImageView) findViewById(R.id.more_photo_image);

		tv_more_photo_cancel.setOnClickListener(this);
		tv_more_photo_publish.setOnClickListener(this);
		choose_photo_more_photo.setOnClickListener(this);
	}

	private void getData() {	
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", uid);
		client.post(MinePhotoActivity.this, ConstantsUrl.get_users_info,
				params, new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");

							String url = data.getString("face").toString();
							ImageLoader.getInstance().displayImage(url,
									more_photo_image);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
					

					}
				});

	}

	// 类似ios的底部菜单样式 工具類中的actionsheet
	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("从相册选择照片", "拍摄照片")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_more_photo_cancel:
			Bitmap bit = ((BitmapDrawable) more_photo_image.getDrawable())
					.getBitmap();
			Intent intent = new Intent();
			// intent.putExtra("bitmap", bit);
			MinePhotoActivity.this.setResult(4, intent);
			finish();
			break;

		case R.id.tv_more_photo_publish:
			try {
				upLoadImg(file, "face", uid);
				finish();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		// 选择照片的按鈕的的点击事件
		case R.id.choose_photo_more_photo:
			setTheme(R.style.ActionSheetStyleIOS7);
			showActionSheet();
			break;
		default:
			break;
		}
	}

	
	private void upLoadImg(File file, String field, String uid)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("field", field);
		params.put("value", file);
		params.put("uid", uid);
		System.out.println("___________file_______" + file);
		// 上传文件
		if (file.length() > 0) {
			mDialog = DialogUtil.createProgressDialog(MinePhotoActivity.this,
					"上传中请稍等");
			mDialog.show();
			client.post(ConstantsUrl.modify_users_face, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							// 上传成功后要做的工作
							Toast.makeText(MinePhotoActivity.this, "上传成功",
									Toast.LENGTH_LONG).show();
							mDialog.dismiss();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// 上传失败后要做到工作
							mDialog.dismiss();
							Toast.makeText(MinePhotoActivity.this, "上传失败",
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
			Toast.makeText(MinePhotoActivity.this, "文件不存在", Toast.LENGTH_LONG)
					.show();
		} // 压缩图片(第一次)
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "dismissed isCancle = " +
		// isCancle, 0).show();
	}

	/**
	 * 菜单的监听事件 一个是拍从相册中选择照片 一个是拍照
	 */
	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub
		switch (index) {

		// 选择照片
		case 0:
			try {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				((Activity) MinePhotoActivity.this).startActivityForResult(
						intent, PHOTO_PICKED_WITH_DATA);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(MinePhotoActivity.this, "没有找到相册",
						Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getApplicationContext(), "选择照片 ", 0).show();
			break;
		// 拍照
		case 1:
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs(); // 创建照片的存储目录
			}
			try {
				imageUri = Uri.fromFile(getmCurrentPhotoFile());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				MinePhotoActivity.this.startActivityForResult(intent,
						CAMERA_WITH_DATA);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(MinePhotoActivity.this, "没有找到照相机",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 得到当前图片文件的路径
	 * 
	 * @return
	 */
	public static File getmCurrentPhotoFile() {
		if (mCurrentPhotoFile == null) {
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs(); // 创建照片的存储目录
			}
			mCurrentPhotoFile = new File(PHOTO_DIR, getCharacterAndNumber()
					+ ".png"/* 此处可更换文件后缀 */);
			if (!mCurrentPhotoFile.exists()) // 判断存储文件是否存在>不存在则创建
				try {
					mCurrentPhotoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return mCurrentPhotoFile;
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = new Intent("com.android.camera.action.CROP");
		Uri data2 = null;
		if (data == null) {
			data2 = PictureUtil.getImageUri();
		} else {
			data2 = data.getData();
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PictureUtil.PHOTO_PICKED_WITH_DATA:
				intent.setDataAndType(data2, "image/*");
				intent.putExtra("crop", true);
				// 设置裁剪尺寸
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 160);
				intent.putExtra("outputY", 130);
				intent.putExtra("return-data", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						PictureUtil.getImageCaiUri());
				startActivityForResult(intent, PictureUtil.PHOTO_CROP);
				break;
			case PictureUtil.CAMERA_WITH_DATA:
				intent.setDataAndType(data2, "image/*");
				intent.putExtra("crop", true);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 160);
				intent.putExtra("outputY", 130);
				intent.putExtra("return-data", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						PictureUtil.getImageCaiUri());
				startActivityForResult(intent, PictureUtil.PHOTO_CROP);
				break;
			case PictureUtil.PHOTO_CROP:
				Bundle bundle = data.getExtras();
				myBitmap = (Bitmap) bundle.get("data");
				Bitmap bitImage = comp(myBitmap);
				String fileName = getCharacterAndNumber();
				file = new File(PictureUtil.PHOTO_DIR, fileName + ".png");
				saveMyBitmap(bitImage, file);
				more_photo_image.setImageBitmap(bitImage);
				// final Map<String, File> fileParas = new HashMap<String,
				// File>();
				// fileParas.put("file", file);

				// /**
				// * 在这里选择完毕后,直接进行了上传,在logcat中查看结果吧..
				// */
				// new Thread(new Runnable() {
				// public void run() {
				// StringBuilder result = HttpUploadFile.sendFile(url, null,
				// fileParas);
				// System.out.println("###" + result.toString());
				// }
				// }).start();
				break;

			default:
				break;
			}
		}
	}

	// 压缩图片(第一次)
	private Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		if (baos.toByteArray().length / 1024 > 200) {// 判断如果图片大于200KB,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 160f;// 这里设置高度为800f
		float ww = 130f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 压缩图片(第二次)
	private Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public void saveMyBitmap(Bitmap mBitmap, File file) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
			byte[] bitmapData = baos.toByteArray();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bitmapData);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
