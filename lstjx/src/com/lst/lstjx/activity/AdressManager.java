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
import com.lst.lstjx.adapter.AdressAdapter;
import com.lst.lstjx.adapter.AdressAdapter.AddressCallback;
import com.lst.lstjx.bean.AdressDetail;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.DialogUtil;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 收货地址
 * author describe parameter return
 */
public class AdressManager extends Activity implements AddressCallback {
	protected static final int ADRESSDETAIL = 0X1324;
	protected static final int SETCOMADD = 0x76;
	protected static final int DELADRESS = 0X676456;
	private ListView mListView;
	private TextView mAddAdress;
	private List<AdressDetail> mRecListBean;
	private List<AdressDetail> mListBean;
	private int postition;
	private AdressAdapter adapter;
	private Dialog dialog;
	private ImageView bt_buy_back;
	private int mPosition ;
	private int delId ;
	private LinearLayout noaddress,haveaddress;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADRESSDETAIL:
				mRecListBean = (List<AdressDetail>) msg.obj;
				if (mRecListBean.size()!=0&&mRecListBean!=null) {
					noaddress.setVisibility(View.GONE);
					haveaddress.setVisibility(View.VISIBLE);
					adapter = new AdressAdapter(AdressManager.this, mRecListBean,
							AdressManager.this);
					adapter.notifyDataSetChanged();
					mListView.setAdapter(adapter);
				}else{
					noaddress.setVisibility(View.VISIBLE);
					haveaddress.setVisibility(View.GONE);
				}
				
				break;
			case SETCOMADD:
				int recResult = (Integer) msg.obj;
				if (recResult == 0) {
					Toast.makeText(AdressManager.this, "设置成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AdressManager.this, "设置失败，请重试",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case DELADRESS:
				int delResult = (Integer) msg.obj;
				if (delResult == 0) {
					Toast.makeText(AdressManager.this, "删除失败，请重试",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AdressManager.this, "删除成功",
							Toast.LENGTH_SHORT).show();
				String addressid = 	SharePrefUtil.getString(AdressManager.this, "addressid", "");
					if ((Integer.toString(delId)).equals(addressid)) {
						SharePrefUtil.saveString(AdressManager.this, "addressid", "");
					}
					if (mRecListBean.size()!=0&&mRecListBean!=null) {
						mRecListBean.remove(mPosition);
						adapter.notifyDataSetChanged();
						if (mRecListBean.size()==0) {
							noaddress.setVisibility(View.VISIBLE);
							haveaddress.setVisibility(View.GONE);
						}
					}else{
						noaddress.setVisibility(View.VISIBLE);
						haveaddress.setVisibility(View.GONE);
					}
					
					
				}
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_adress_manager);
		initView();
		goBack();
		getAdress();
		/**
		 * 添加收货地址的按钮事件
		 * 
		 * @param V
		 */
		mAddAdress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AdressManager.this,
						AddAdressActivity.class);
				startActivityForResult(intent, 0);
				
			}
		});
	}
	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent data) {
		
		if (resultCode ==-1) {
			getAdress();
			
		}
	};
	private void initView() {
		// TODO Auto-generated method stub
		noaddress = (LinearLayout) findViewById(R.id.noaddress);
		haveaddress = (LinearLayout) findViewById(R.id.haveaddress);
		mAddAdress = (TextView) findViewById(R.id.addAdress);
		mListView = (ListView) findViewById(R.id.my_adress_lv);
		bt_buy_back = (ImageView)findViewById(R.id.go_back);
	}
	
	/**
	 * 回退按钮
	 */
	private void goBack() {
		// TODO Auto-generated method stub
		bt_buy_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 获取收货地址的数据
	 */

	private void getAdress() {
		 dialog = DialogUtil.createProgressDialog(AdressManager.this, "获得收货地址中");
		 dialog.show();
		mListBean = new ArrayList<AdressDetail>();
		String userid = SharePrefUtil.getString(AdressManager.this, "userId",
				"");
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		http.post(AdressManager.this, ConstantsUrl.getAdress, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						try {

							JSONArray arr = new JSONArray(result.toString());
							for (int i = 0; i < arr.length(); i++) {
								AdressDetail mAdressDetail = new AdressDetail();
								JSONObject json = arr.getJSONObject(i);
								String id = json.getString("id");
								String zip = json.getString("zip");
								String accept_name = json
										.getString("accept_name");
								String mobile = json.getString("mobile");
								String province = json.getString("province");
								String city = json.getString("city");
								String area = json.getString("area");
								String adress = json.getString("address");
								mAdressDetail.setAccept_name(accept_name);
								mAdressDetail.setAdress(adress);
								mAdressDetail.setArea(area);
								mAdressDetail.setCity(city);
								mAdressDetail.setId(id);
								mAdressDetail.setMobile(mobile);
								mAdressDetail.setProvince(province);
								mAdressDetail.setZip(zip);
								mListBean.add(mAdressDetail);
							}

							Message msg = mHandler.obtainMessage();
							msg.what = ADRESSDETAIL;
							msg.obj = mListBean;
							mHandler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						dialog.dismiss();
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void click(View v) {
		switch (v.getId()) {
		/**
		 * 删除收货地址
		 */
		case R.id.del_address:
			 mPosition = (Integer) v.getTag();
			dialog = DialogUtil.createProgressDialog(AdressManager.this, "别动，删除中");
			 dialog.show();
			int delPostition = (Integer) v.getTag();
			delId = Integer
					.parseInt(mRecListBean.get(delPostition).getId());
			RequestParams param = new RequestParams();
			param.put("id", delId);
			AsyncHttpClient httpdel = new AsyncHttpClient();
			httpdel.post(AdressManager.this, ConstantsUrl.delComAdress, param,
					new TextHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								String arg2) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							try {
								JSONArray arr = new JSONArray(arg2.toString());
								JSONObject json = arr.getJSONObject(0);
								int result = json.getInt("no");

								Message msg = mHandler.obtainMessage();
								msg.what = DELADRESS;
								msg.obj = result;
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
						}
					});
			break;

		/**
		 * 设置默认地址的监听事件
		 */
		case R.id.pencil:
			dialog = DialogUtil.createProgressDialog(AdressManager.this, "设置中，请稍等");
			 dialog.show();
			postition = (Integer) v.getTag();
			String addressid = mRecListBean.get(postition).getId();
			RequestParams params = new RequestParams();
			params.add("id", addressid);
			SharePrefUtil
					.saveString(AdressManager.this, "addressid", addressid);
			AsyncHttpClient http = new AsyncHttpClient();
			http.post(AdressManager.this, ConstantsUrl.setComAdress, params,
					new TextHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								String arg2) {
							dialog.dismiss();
							// TODO Auto-generated method stub
							JSONObject josn;
							try {
								josn = new JSONObject(arg2.toString());
								int result = josn.optInt("result");

								Message msg = mHandler.obtainMessage();
								msg.what = SETCOMADD;
								msg.obj = result;
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
						}
					});
			break;
		default:
			break;
		}
	}
}
