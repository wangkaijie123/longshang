package com.lst.lstjx.adapter;

import java.util.List;
import java.util.Map;

import com.lst.lstjx.bean.AdressDetail;
import com.lst.yuewo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * author describe parameter return
 */
public class AdressAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private List<String> kindList;
	private LayoutInflater inflater;
	private List<AdressDetail> mList;
	private AddressCallback mCallback;
   private 	ViewHolder holder;
	public interface AddressCallback {
		public void click(View v);
	}

	public AdressAdapter(Context context, List<AdressDetail> mList,
			AddressCallback mCallback) {
		super();
		this.context = context;
		this.mList = mList;
		this.inflater = LayoutInflater.from(context);
		this.mCallback = mCallback;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {

		return mList.size();

	}

	@Override
	public Object getItem(int arg0) {

		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
	

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adress_lv_item, null);

			holder.adress_ueser_name = (TextView) convertView
					.findViewById(R.id.adress_ueser_name);
			holder.pro_city = (TextView) convertView
					.findViewById(R.id.pro_city);
			holder.x_adress = (TextView) convertView
					.findViewById(R.id.x_adress);
			holder.adress_phone = (TextView) convertView
					.findViewById(R.id.adress_phone);
			holder.del_address = (TextView) convertView
					.findViewById(R.id.del_address);
//			holder.adress_isCommon = (TextView) convertView
//					.findViewById(R.id.adress_isCommon);
			holder.pencil = (TextView) convertView.findViewById(R.id.pencil);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.adress_ueser_name.setText(mList.get(position).getAccept_name()
				.toString());
		holder.pro_city.setText(mList.get(position).getProvince().toString()
				+ "  " + mList.get(position).getCity().toString() + ""
				+ mList.get(position).getArea().toString());
		holder.x_adress.setText(mList.get(position).getAdress().toString());
		holder.adress_phone.setText(mList.get(position).getMobile().toString());

		holder.del_address.setOnClickListener(this);
		holder.pencil.setOnClickListener(this);
		holder.pencil.setTag(position);
		holder.del_address.setTag(position);
		return convertView;
	}

	class ViewHolder {
		
		TextView adress_ueser_name, pro_city, x_adress, adress_phone,
				adress_isCommon,pencil,del_address;

	}

	@Override
	public void onClick(View v) {
		
		mCallback.click(v);
	}

}