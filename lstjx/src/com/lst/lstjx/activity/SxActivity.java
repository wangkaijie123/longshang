<<<<<<< HEAD
package com.lst.lstjx.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.lst.lstjx.address.AddressData;
import com.lst.lstjx.address.MyAlertDialog;
import com.lst.lstjx.datepicker.AbstractWheelTextAdapter;
import com.lst.lstjx.datepicker.ArrayWheelAdapter;
import com.lst.lstjx.datepicker.DatePickerPopWindow;
import com.lst.lstjx.datepicker.OnWheelChangedListener;
import com.lst.lstjx.datepicker.WheelView;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

/**
 * author describe parameter return
 */
public class SxActivity extends Activity implements OnClickListener {
	private ImageView tv_more_photo_cancel;
	private TextView tv_more_photo_publish, startingchoose, endingchoose,
			timechoose;
	private String cityTxt;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shaixuan);
		initView();
	}

	private void initView() {
		tv_more_photo_cancel = (ImageView) findViewById(R.id.tv_more_photo_cancel);
		tv_more_photo_publish = (TextView) findViewById(R.id.tv_more_photo_publish);
		startingchoose = (TextView) findViewById(R.id.startingchoose);
		endingchoose = (TextView) findViewById(R.id.endingchoose);
		timechoose = (TextView) findViewById(R.id.timechoose);
		timechoose.setOnClickListener(this);
		startingchoose.setOnClickListener(this);
		endingchoose.setOnClickListener(this);
		tv_more_photo_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_more_photo_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String starting = startingchoose.getText().toString();
				String ending = endingchoose.getText().toString();
				String time = timechoose.getText().toString();
				Intent it = new Intent();
				if (!starting.equals("请选择")) {
					it.putExtra("starting", starting);
				}else {
					it.putExtra("starting", "");
				}
				if (!ending.equals("请选择")) {
					it.putExtra("ending", ending);
				}else {
					it.putExtra("ending", "");
				}
				if (!time.equals("请选择")) {
					it.putExtra("time", time);
				}else {
					it.putExtra("time", "");
				}
				setResult(1, it);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.timechoose:
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			DatePickerPopWindow popWindow = new DatePickerPopWindow(
					SxActivity.this, df.format(date));
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.5f;
			getWindow().setAttributes(lp);
			popWindow.showAtLocation(findViewById(R.id.root), Gravity.CENTER,
					0, 0);
			popWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);

					String timeDate = SharePrefUtil.getString(
							getApplicationContext(), "TIMEDATE", "");
					if (timeDate != null && !"".equals(timeDate)) {
						timechoose.setText(timeDate);
					}

				}
			});
			break;
		case R.id.startingchoose:
			View view = dialogm();
			final MyAlertDialog dialog1 = new MyAlertDialog(SxActivity.this)
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
			final MyAlertDialog dialog2 = new MyAlertDialog(SxActivity.this)
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
						+ "  "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ "  "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						newValue);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ "  "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ "  "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ "  "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ "  "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		country.setCurrentItem(1);// 设置北京
		city.setCurrentItem(1);
		ccity.setCurrentItem(1);
		return contentView;
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

	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
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
}
=======
package com.lst.lstjx.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.lst.lstjx.address.AddressData;
import com.lst.lstjx.address.MyAlertDialog;
import com.lst.lstjx.datepicker.AbstractWheelTextAdapter;
import com.lst.lstjx.datepicker.ArrayWheelAdapter;
import com.lst.lstjx.datepicker.DatePickerPopWindow;
import com.lst.lstjx.datepicker.OnWheelChangedListener;
import com.lst.lstjx.datepicker.WheelView;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

/**
 * author describe parameter return
 */
public class SxActivity extends Activity implements OnClickListener {
	private ImageView tv_more_photo_cancel;
	private TextView tv_more_photo_publish, startingchoose, endingchoose,
			timechoose;
	private String cityTxt;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shaixuan);
		initView();
	}

	private void initView() {
		tv_more_photo_cancel = (ImageView) findViewById(R.id.tv_more_photo_cancel);
		tv_more_photo_publish = (TextView) findViewById(R.id.tv_more_photo_publish);
		startingchoose = (TextView) findViewById(R.id.startingchoose);
		endingchoose = (TextView) findViewById(R.id.endingchoose);
		timechoose = (TextView) findViewById(R.id.timechoose);
		timechoose.setOnClickListener(this);
		startingchoose.setOnClickListener(this);
		endingchoose.setOnClickListener(this);
		tv_more_photo_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_more_photo_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String starting = startingchoose.getText().toString();
				String ending = endingchoose.getText().toString();
				String time = timechoose.getText().toString();
				Intent it = new Intent();
				if (!starting.equals("请选择")) {
					it.putExtra("starting", starting);
				}else {
					it.putExtra("starting", "");
				}
				if (!ending.equals("请选择")) {
					it.putExtra("ending", ending);
				}else {
					it.putExtra("ending", "");
				}
				if (!time.equals("请选择")) {
					it.putExtra("time", time);
				}else {
					it.putExtra("time", "");
				}
				setResult(1, it);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.timechoose:
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			DatePickerPopWindow popWindow = new DatePickerPopWindow(
					SxActivity.this, df.format(date));
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.5f;
			getWindow().setAttributes(lp);
			popWindow.showAtLocation(findViewById(R.id.root), Gravity.CENTER,
					0, 0);
			popWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);

					String timeDate = SharePrefUtil.getString(
							getApplicationContext(), "TIMEDATE", "");
					if (timeDate != null && !"".equals(timeDate)) {
						timechoose.setText(timeDate);
					}

				}
			});
			break;
		case R.id.startingchoose:
			View view = dialogm();
			final MyAlertDialog dialog1 = new MyAlertDialog(SxActivity.this)
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
			final MyAlertDialog dialog2 = new MyAlertDialog(SxActivity.this)
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
						+ "  "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ "  "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						newValue);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ "  "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ "  "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ "  "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ "  "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		country.setCurrentItem(1);// 设置北京
		city.setCurrentItem(1);
		ccity.setCurrentItem(1);
		return contentView;
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

	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
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
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
