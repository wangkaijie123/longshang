package com.lst.lstjx.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.lst.lstjx.bean.DynamicPLBean;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.unionpay.mobile.android.widgets.ba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDynamicPLAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<DynamicPLBean> beans  = new ArrayList<DynamicPLBean>();

	public MyDynamicPLAdapter(Context context, List<DynamicPLBean> beans) {
		this.context = context;
		this.beans = beans;
		inflater = LayoutInflater.from(context);
	}

	
	public void clear() {
		// TODO Auto-generated method stub
		
		beans.clear();
		
	
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder ;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.item_wddt_plxq, null);
			
			holder.face = (ImageView) convertView.findViewById(R.id.face);
			holder.username = (TextView) convertView.findViewById(R.id.username);
			holder.sex = (ImageView) convertView.findViewById(R.id.sex);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.addtime = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}else{
			holder = (viewHolder) convertView.getTag();
		}
		holder.username.setText(beans.get(position).getUsername().toString());
		holder.content.setText(beans.get(position).getContent().toString());
		holder.addtime.setText(beans.get(position).getAddtime().toString());
		if ("1".equals(beans.get(position).getSex().toString())) {
			holder.sex.setBackgroundResource(R.drawable.ic_sex_male);
		}else if("0".equals(beans.get(position).getSex().toString())){
			holder.sex.setBackgroundResource(R.drawable.ic_sex_female);
		}
		String url = beans.get(position).getFace().toString();
		ImageLoader.getInstance().displayImage(url, holder.face);
		
		return convertView;
	}

	class viewHolder {
		private ImageView face;
		private TextView username;
		private ImageView sex;
		private TextView content;
		private TextView addtime;

	}

}
