<<<<<<< HEAD
package com.lst.lstjx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.MoreDeliverActivity.CountryAdapter;
import com.lst.lstjx.address.AddressData;
import com.lst.lstjx.address.MyAlertDialog;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.datepicker.AbstractWheelTextAdapter;
import com.lst.lstjx.datepicker.ArrayWheelAdapter;
import com.lst.lstjx.datepicker.OnWheelChangedListener;
import com.lst.lstjx.datepicker.WheelView;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

/**
 * author describe parameter return
 */
public class AddAdressActivity extends Activity {
	protected static final int FirstAdd = 0x11;
	protected static final int CHILD = 0x22;
	protected static final int THREE = 0x3454;
	protected static final int SAVE = 0X464;
	private String area_id;
	private String area_name;
	private String child_id;
	private String child_name;
	private Map<String, String> mThreeRec;
	private Map<String, String> mThree;
	private Map<String, String> mChild;
	private Map<String, String> mFirst;
	private Map<String, String> mFirstRec;
	private Map<String, String> mChildRec;
	private WheelView province_spinner, city_spinner, area_spinner;
	private EditText ed_name, ed_phone, ed_xAdress, ed_adress_name, ed_zip;
	private String[] strProvince;
	private String[] strCity;
	private String FirstStr;
	private String FirstKey;
	private String ChildStr;
	private String ChildKey;
	private String ThreeKey;
	private String ThreeStr;
	private int recode;
	private Dialog dialog;
	private ImageView bn_back;
	private String cityTxt;
	private Button saveAddressBtn,cancelBtn;
	// 省市两级的适配器
	ArrayAdapter<String> provinceAdapter = null;
	ArrayAdapter<String> cityAdapter = null;
	ArrayAdapter<String> area_adapter = null;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FirstAdd:
				mFirstRec = (Map<String, String>) msg.obj;
				List<String> FirstKey = new ArrayList<String>();
				List<String> FirstValue = new ArrayList<String>();
				for (Entry<String, String> entry : mFirstRec.entrySet()) {
					FirstKey.add(entry.getKey());
					FirstValue.add(entry.getValue());
				}
				provinceAdapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.add_spin_check_text,
						FirstValue);
//				province_spinner.setAdapter(provinceAdapter);
//				onLinsten(FirstValue, FirstKey);
				break;
			case CHILD:
				List<String> childKey = new ArrayList<String>();
				List<String> childValue = new ArrayList<String>();
				mChild = (Map<String, String>) msg.obj;
				for (Entry<String, String> entry : mChild.entrySet()) {
					childKey.add(entry.getKey());
					childValue.add(entry.getValue());
				}
				cityAdapter = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.add_spin_check_text, childValue);
//				city_spinner.setAdapter(cityAdapter);
//				onChildLinsten(childValue, childKey);
				break;
			case THREE:
				List<String> threeKey = new ArrayList<String>();
				List<String> threeValue = new ArrayList<String>();
				mThreeRec = (Map<String, String>) msg.obj;
				for (Entry<String, String> entry : mThreeRec.entrySet()) {
					threeKey.add(entry.getKey());
					threeValue.add(entry.getValue());
				}

				area_adapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.add_spin_check_text,
						threeValue);
//				area_spinner.setAdapter(area_adapter);
//				onThreeLinsten(threeValue, threeKey);
				break;

			case SAVE:
				int reccodr = (Integer) msg.obj;
				if (reccodr == 0) {
					Intent intent = new Intent();
					AddAdressActivity.this.setResult(RESULT_OK, intent);
					
					Toast.makeText(AddAdressActivity.this, "添加成功",
							Toast.LENGTH_SHORT).show();
					AddAdressActivity.this.finish();
				} else {
					Toast.makeText(AddAdressActivity.this, "用户太多，请稍后重试",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_adress);
		// 初始化界面中的组件
		initView();
		goBack();// 返回按钮
		// 获得一级城市
		getFirst();
		
	}

	/**
	 * 保存按钮的事件
	 * 
	 * @param v
	 */
	public void saveAdress(View v) {
		String userid = SharePrefUtil.getString(AddAdressActivity.this,
				"userId", "");
		String strName = ed_name.getText().toString().trim();
		String strPhone = ed_phone.getText().toString().trim();
		String strXAdress = ed_xAdress.getText().toString().trim();
//		String strZip = ed_zip.getText().toString().trim();
		if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(strName)
				|| TextUtils.isEmpty(strPhone) || TextUtils.isEmpty(strXAdress)
//				|| TextUtils.isEmpty(strZip)
				) {
			Toast.makeText(AddAdressActivity.this, "请您将信息填写完整",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			dialog = DialogUtil.createProgressDialog(AddAdressActivity.this,
					"保存中");
			dialog.show();
			AsyncHttpClient http = new AsyncHttpClient();
			RequestParams params = new RequestParams();			
			params.add("userid", userid);
			params.add("acceptname", strName);
//			params.add("zip", strZip);
			params.add("telephone", strPhone);
			params.add("province", FirstKey);
			params.add("city", ChildKey);
			params.add("area", ThreeKey);
			params.add("mobile", strPhone);
			params.add("address", strXAdress);
			http.post(AddAdressActivity.this, ConstantsUrl.addAdress, params,
					new TextHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								String result) {
							dialog.dismiss();
							// TODO Auto-generated method stub
							JSONObject json;
							try {
								json = new JSONObject(result.toString());
								recode = json.getInt("result");
								Message msg = mHandler.obtainMessage();
								msg.obj = recode;
								msg.what = SAVE;
								mHandler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Toast.makeText(AddAdressActivity.this,
									"用户爆棚，请重新保存", Toast.LENGTH_LONG).show();
						}
					});
		}
	}

//	/**
//	 * 一级城市的监听事件
//	 * 
//	 * @param mList
//	 * @param mKey
//	 */
//	private void onLinsten(final List<String> mList, final List<String> mKey) {
//		province_spinner
//				.setOnItemSelectedListener(new OnItemSelectedListener() {
//					@Override
//					public void onItemSelected(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						FirstStr = mList.get(arg2);
//						FirstKey = mKey.get(arg2);
//						// 二级
//						getChild(FirstKey);
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> arg0) {
//						// TODO Auto-generated method stub
//					}
//				});
//	}

	/**
	 * 三级城市的监听事件
	 * 
	 * @param mList
	 * @param mKey
	 */
//	private void onThreeLinsten(final List<String> mList,
//			final List<String> mKey) {
//		area_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				ThreeStr = mList.get(arg2);
//				ThreeKey = mKey.get(arg2);
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//			}
//		});
//	}

	/**
	 * 二级城市的监听事件
	 * 
	 * @param mList
	 * @param mKey
	 */
//	private void onChildLinsten(final List<String> mList,
//			final List<String> mKey) {
//		city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				ChildStr = mList.get(arg2);
//				ChildKey = mKey.get(arg2);
//				getThree(ChildKey);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//			}
//		});
//	}

	/**
	 * 获取三级地区的数据
	 */
	protected void getThree(String key) {
		// TODO Auto-generated method stub
		mThree = new HashMap<String, String>();
		RequestParams params = new RequestParams();
		params.add("pid", key);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(AddAdressActivity.this, ConstantsUrl.ChildAdress, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						JSONArray arr;
						try {
							arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								JSONObject json = arr.getJSONObject(i);
								area_id = json.getString("area_id");
								area_name = json.getString("area_name");
								mThree.put(area_id, area_name);
								System.out.println("id" + area_id + "name"
										+ area_name);
							}
							Message msg = mHandler.obtainMessage();
							msg.what = THREE;
							msg.obj = mThree;
							mHandler.sendMessage(msg);
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

	/**
	 * 获取二级城市
	 */
	private void getChild(String key) {
		mChild = new HashMap<String, String>();
		RequestParams params = new RequestParams();
		params.add("pid", key);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(AddAdressActivity.this, ConstantsUrl.ChildAdress, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						JSONArray arr;
						try {
							arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								JSONObject json = arr.getJSONObject(i);
								child_id = json.getString("area_id");
								child_name = json.getString("area_name");
								mChild.put(child_id, child_name);
							}
							Message msg = mHandler.obtainMessage();
							msg.what = CHILD;
							msg.obj = mChild;
							mHandler.sendMessage(msg);
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
	/**
	 * 获取一级城市
	 */
	private void getFirst() {
		mFirst = new HashMap<String, String>();
		AsyncHttpClient http = new AsyncHttpClient();
		http.get(AddAdressActivity.this, ConstantsUrl.FirstAdrsess,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						// TODO Auto-generated method stub
						JSONArray arr;
						try {
							arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								JSONObject json = arr.getJSONObject(i);
								area_id = json.getString("area_id");
								area_name = json.getString("area_name");
								mFirst.put(area_id, area_name);
							}
							Message msg = mHandler.obtainMessage();
							msg.what = FirstAdd;
							msg.obj = mFirst;
							mHandler.sendMessage(msg);

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
	/**
	 * 初始化数据
	 */
	private void initView() {
		
		ed_adress_name = (EditText) findViewById(R.id.add_address_choseEdId);
		ed_adress_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View view = dialogm();
				final MyAlertDialog dialog1 = new MyAlertDialog(
						AddAdressActivity.this).builder()
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
						ed_adress_name.setText(cityTxt);
						Toast.makeText(getApplicationContext(), cityTxt, 1).show();
					}
				});
				dialog1.show();
			}
		});
		Intent intent=getIntent();
		String id=intent.getStringExtra("Id");
		String accept_name=intent.getStringExtra("AcceptName");
		String mobile=intent.getStringExtra("Mobile");
		String adress=intent.getStringExtra("Adress");
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_name.setText(accept_name);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_phone.setText(mobile);
		ed_xAdress = (EditText) findViewById(R.id.ed_xAdress);
		bn_back= (ImageView)findViewById(R.id.bn_back);
		saveAddressBtn = (Button) findViewById(R.id.bn_save_adress);
		cancelBtn = (Button) findViewById(R.id.add_address_cancel_Btn);
		saveAddressBtn.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveAdress(v);
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
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
	/**
	 * 回退按钮
	 */
	private void goBack() {
		// TODO Auto-generated method stub
		bn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
=======
package com.lst.lstjx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;

/**
 * author describe parameter return
 */
public class AddAdressActivity extends Activity {
	protected static final int FirstAdd = 0x11;
	protected static final int CHILD = 0x22;
	protected static final int THREE = 0x3454;
	protected static final int SAVE = 0X464;
	private String area_id;
	private String area_name;
	private String child_id;
	private String child_name;
	private Map<String, String> mThreeRec;
	private Map<String, String> mThree;
	private Map<String, String> mChild;
	private Map<String, String> mFirst;
	private Map<String, String> mFirstRec;
	private Map<String, String> mChildRec;
	private Spinner province_spinner, city_spinner, area_spinner;
	private EditText ed_name, ed_phone, ed_xAdress, ed_adress_name, ed_zip;
	private String[] strProvince;
	private String[] strCity;
	private String FirstStr;
	private String FirstKey;
	private String ChildStr;
	private String ChildKey;
	private String ThreeKey;
	private String ThreeStr;
	private int recode;
	private Dialog dialog;
	private ImageView bn_back;
	// 省市两级的适配器
	ArrayAdapter<String> provinceAdapter = null;
	ArrayAdapter<String> cityAdapter = null;
	ArrayAdapter<String> area_adapter = null;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FirstAdd:
				mFirstRec = (Map<String, String>) msg.obj;
				List<String> FirstKey = new ArrayList<String>();
				List<String> FirstValue = new ArrayList<String>();
				for (Entry<String, String> entry : mFirstRec.entrySet()) {
					FirstKey.add(entry.getKey());
					FirstValue.add(entry.getValue());
				}
				provinceAdapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.add_spin_check_text,
						FirstValue);
				province_spinner.setAdapter(provinceAdapter);
				onLinsten(FirstValue, FirstKey);
				break;
			case CHILD:
				List<String> childKey = new ArrayList<String>();
				List<String> childValue = new ArrayList<String>();
				mChild = (Map<String, String>) msg.obj;
				for (Entry<String, String> entry : mChild.entrySet()) {
					childKey.add(entry.getKey());
					childValue.add(entry.getValue());
				}
				cityAdapter = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.add_spin_check_text, childValue);
				city_spinner.setAdapter(cityAdapter);
				onChildLinsten(childValue, childKey);
				break;
			case THREE:
				List<String> threeKey = new ArrayList<String>();
				List<String> threeValue = new ArrayList<String>();
				mThreeRec = (Map<String, String>) msg.obj;
				for (Entry<String, String> entry : mThreeRec.entrySet()) {
					threeKey.add(entry.getKey());
					threeValue.add(entry.getValue());
				}

				area_adapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.add_spin_check_text,
						threeValue);
				area_spinner.setAdapter(area_adapter);
				onThreeLinsten(threeValue, threeKey);
				break;

			case SAVE:
				int reccodr = (Integer) msg.obj;
				if (reccodr == 0) {
					Intent intent = new Intent();
					AddAdressActivity.this.setResult(RESULT_OK, intent);
					AddAdressActivity.this.finish();
					Toast.makeText(AddAdressActivity.this, "添加成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AddAdressActivity.this, "用户太多，请稍后重试",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_adress);
		// 初始化界面中的组件
		initView();
		goBack();// 返回按钮
		// 获得一级城市
		getFirst();
	}

	/**
	 * 保存按钮的事件
	 * 
	 * @param v
	 */
	public void saveAdress(View v) {
		String userid = SharePrefUtil.getString(AddAdressActivity.this,
				"userId", "");
		String strName = ed_name.getText().toString().trim();
		String strPhone = ed_phone.getText().toString().trim();
		String strXAdress = ed_xAdress.getText().toString().trim();
		String strZip = ed_zip.getText().toString().trim();
		if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(strName)
				|| TextUtils.isEmpty(strPhone) || TextUtils.isEmpty(strXAdress)
				|| TextUtils.isEmpty(strZip)) {
			Toast.makeText(AddAdressActivity.this, "请您将信息填写完整",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			dialog = DialogUtil.createProgressDialog(AddAdressActivity.this,
					"保存中");
			dialog.show();
			AsyncHttpClient http = new AsyncHttpClient();
			RequestParams params = new RequestParams();			
			params.add("userid", userid);
			params.add("acceptname", strName);
			params.add("zip", strZip);
			params.add("telephone", strPhone);
			params.add("province", FirstKey);
			params.add("city", ChildKey);
			params.add("area", ThreeKey);
			params.add("mobile", strPhone);
			params.add("address", strXAdress);
			http.post(AddAdressActivity.this, ConstantsUrl.addAdress, params,
					new TextHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								String result) {
							dialog.dismiss();
							// TODO Auto-generated method stub
							JSONObject json;
							try {
								json = new JSONObject(result.toString());
								recode = json.getInt("result");
								Message msg = mHandler.obtainMessage();
								msg.obj = recode;
								msg.what = SAVE;
								mHandler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Toast.makeText(AddAdressActivity.this,
									"用户爆棚，请重新保存", Toast.LENGTH_LONG).show();
						}
					});
		}
	}

	/**
	 * 一级城市的监听事件
	 * 
	 * @param mList
	 * @param mKey
	 */
	private void onLinsten(final List<String> mList, final List<String> mKey) {
		province_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						FirstStr = mList.get(arg2);
						FirstKey = mKey.get(arg2);
						// 二级
						getChild(FirstKey);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	/**
	 * 三级城市的监听事件
	 * 
	 * @param mList
	 * @param mKey
	 */
	private void onThreeLinsten(final List<String> mList,
			final List<String> mKey) {
		area_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ThreeStr = mList.get(arg2);
				ThreeKey = mKey.get(arg2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 二级城市的监听事件
	 * 
	 * @param mList
	 * @param mKey
	 */
	private void onChildLinsten(final List<String> mList,
			final List<String> mKey) {
		city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ChildStr = mList.get(arg2);
				ChildKey = mKey.get(arg2);
				getThree(ChildKey);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 获取三级地区的数据
	 */
	protected void getThree(String key) {
		// TODO Auto-generated method stub
		mThree = new HashMap<String, String>();
		RequestParams params = new RequestParams();
		params.add("pid", key);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(AddAdressActivity.this, ConstantsUrl.ChildAdress, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						JSONArray arr;
						try {
							arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								JSONObject json = arr.getJSONObject(i);
								area_id = json.getString("area_id");
								area_name = json.getString("area_name");
								mThree.put(area_id, area_name);
								System.out.println("id" + area_id + "name"
										+ area_name);
							}
							Message msg = mHandler.obtainMessage();
							msg.what = THREE;
							msg.obj = mThree;
							mHandler.sendMessage(msg);
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

	/**
	 * 获取二级城市
	 */
	private void getChild(String key) {
		mChild = new HashMap<String, String>();
		RequestParams params = new RequestParams();
		params.add("pid", key);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(AddAdressActivity.this, ConstantsUrl.ChildAdress, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						JSONArray arr;
						try {
							arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								JSONObject json = arr.getJSONObject(i);
								child_id = json.getString("area_id");
								child_name = json.getString("area_name");
								mChild.put(child_id, child_name);
							}
							Message msg = mHandler.obtainMessage();
							msg.what = CHILD;
							msg.obj = mChild;
							mHandler.sendMessage(msg);
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
	/**
	 * 获取一级城市
	 */
	private void getFirst() {
		mFirst = new HashMap<String, String>();
		AsyncHttpClient http = new AsyncHttpClient();
		http.get(AddAdressActivity.this, ConstantsUrl.FirstAdrsess,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						// TODO Auto-generated method stub
						JSONArray arr;
						try {
							arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								JSONObject json = arr.getJSONObject(i);
								area_id = json.getString("area_id");
								area_name = json.getString("area_name");
								mFirst.put(area_id, area_name);
							}
							Message msg = mHandler.obtainMessage();
							msg.what = FirstAdd;
							msg.obj = mFirst;
							mHandler.sendMessage(msg);

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
	/**
	 * 初始化数据
	 */
	private void initView() {
		ed_zip = (EditText) findViewById(R.id.ed_zip);
		province_spinner = (Spinner) findViewById(R.id.province_spinner);
		city_spinner = (Spinner) findViewById(R.id.city_spinner);
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_xAdress = (EditText) findViewById(R.id.ed_xAdress);
		area_spinner = (Spinner) findViewById(R.id.area_spinner);
		bn_back = (ImageView) findViewById(R.id.bn_back);
	}

	/**
	 * 回退按钮
	 */
	private void goBack() {
		// TODO Auto-generated method stub
		bn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
