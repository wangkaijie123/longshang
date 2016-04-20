package com.lst.lstjx.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lst.yuewo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearPeopleAdapter extends BaseAdapter{

	private List<String> data = new ArrayList<String>();
	private Context mContext;
	private LayoutInflater mInflater;
	public NearPeopleAdapter(Context context){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		for (int i = 0; i < 30; i++) {
			data.add(i+"");
		}
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_nearpeople, null);
			mHolder = new viewHolder();
			mHolder.head_picture = (ImageView) convertView.findViewById(R.id.head_picture);
			mHolder.sex = (ImageView) convertView.findViewById(R.id.sex);
			mHolder.name = (TextView) convertView.findViewById(R.id.name);
			mHolder.distance = (TextView) convertView.findViewById(R.id.distance);
			convertView.setTag(mHolder);
		}else{
			mHolder = (viewHolder) convertView.getTag();
		}
		mHolder.name.setText("textname");
		mHolder.distance.setText("110米以内");
		return convertView;
	}
class viewHolder{
	ImageView head_picture;
	ImageView sex;
	TextView name;
	TextView distance;
	
}
}
