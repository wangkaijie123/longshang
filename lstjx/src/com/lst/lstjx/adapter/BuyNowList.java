<<<<<<< HEAD
package com.lst.lstjx.adapter;

import java.util.ArrayList;
import java.util.List;


import com.lst.lstjx.bean.ShopCar;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author describe parameter 支付确认页面listview适配器 return
 */
public class BuyNowList extends BaseAdapter {
	private Context context;

	private HolderView holderView;

	private List<ShopCar> mList;
	private ArrayList<Integer> mposition;

	public BuyNowList(Context context, List<ShopCar> mList) {
		this.context = context;
		this.mList = mList;

	}

	public BuyNowList(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		if (currentView == null) {

			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(
					R.layout.buy_now_item, null);

			holderView.iv_adapter_list_pic = (ImageView) currentView
					.findViewById(R.id.iv_adapter_list_pic);
			holderView.tv_name = (TextView) currentView
					.findViewById(R.id.tv_name);
			holderView.tv_pro_price = (TextView) currentView
					.findViewById(R.id.tv_pro_price);
			holderView.pro_num = (TextView) currentView
					.findViewById(R.id.pro_num);
			
		
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}

		if (mList.size() != 0 ) {
			String pronum = SharePrefUtil.getString(context, "pronum", "");
			holderView.pro_num.setText("X "+mList.get(position).getNum());
			holderView.tv_name
					.setText(mList.get(position).getName().toString());
			holderView.tv_pro_price.setText("￥ "
					+ mList.get(position).getSell_price().toString());
			ImageLoader.getInstance().displayImage(
					mList.get(position).getImg(),
					holderView.iv_adapter_list_pic);

		}

		return currentView;
	}

	public class HolderView {
		private TextView tv_pro_price;
		private TextView tv_name,pro_num;
		private ImageView iv_adapter_list_pic, pro_list_car;
	}

}
=======
package com.lst.lstjx.adapter;

import java.util.ArrayList;
import java.util.List;


import com.lst.lstjx.bean.ShopCar;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author describe parameter 支付确认页面listview适配器 return
 */
public class BuyNowList extends BaseAdapter {
	private Context context;

	private HolderView holderView;

	private List<ShopCar> mList;
	private ArrayList<Integer> mposition;

	public BuyNowList(Context context, List<ShopCar> mList) {
		this.context = context;
		this.mList = mList;

	}

	public BuyNowList(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		if (currentView == null) {

			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(
					R.layout.buy_now_item, null);

			holderView.iv_adapter_list_pic = (ImageView) currentView
					.findViewById(R.id.iv_adapter_list_pic);
			holderView.tv_name = (TextView) currentView
					.findViewById(R.id.tv_name);
			holderView.tv_pro_price = (TextView) currentView
					.findViewById(R.id.tv_pro_price);
			holderView.pro_num = (TextView) currentView
					.findViewById(R.id.pro_num);
			
		
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}

		if (mList.size() != 0 ) {
			String pronum = SharePrefUtil.getString(context, "pronum", "");
			holderView.pro_num.setText("X "+mList.get(position).getNum());
			holderView.tv_name
					.setText(mList.get(position).getName().toString());
			holderView.tv_pro_price.setText("￥ "
					+ mList.get(position).getSell_price().toString());
			ImageLoader.getInstance().displayImage(
					mList.get(position).getImg(),
					holderView.iv_adapter_list_pic);

		}

		return currentView;
	}

	public class HolderView {
		private TextView tv_pro_price;
		private TextView tv_name,pro_num;
		private ImageView iv_adapter_list_pic, pro_list_car;
	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
