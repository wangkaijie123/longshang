<<<<<<< HEAD
package com.lst.lstjx.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lst.lstjx.activity.MyDynamicImgActivity;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.bean.MyDynamic;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyDynamicAdapter extends BaseAdapter {
	private List<MyDynamic> mDynamics = new ArrayList<MyDynamic>();
	private LayoutInflater inflater;
	private Context context;
	// private DynamicBean mDynamicBean;
	private MyDynamic myDynamic;
	public MyDynamicAdapter(Context context, List<MyDynamic> myDynamic) {
		this.mDynamics = myDynamic;
		this.context = context;
		inflater = LayoutInflater.from(context);	
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDynamics.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDynamics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final viewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_dynamic, null);
			holder = new viewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.img = (ImageView) convertView.findViewById(R.id.msg);
			holder.uname = (TextView) convertView.findViewById(R.id.uname);
			holder.addtime = (TextView) convertView.findViewById(R.id.addtime);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		System.out.println("-----position---" + position);
		System.out.println("----type----" + mDynamics.get(position));
		holder.title.setText(mDynamics.get(position).getTitle());
		holder.uname.setText(mDynamics.get(position).getUname());
		holder.addtime.setText(mDynamics.get(position).getAddtime());
		holder.content.setText(mDynamics.get(position).getContent());
		final Intent intent = new Intent();
		if ("1".equals(mDynamics.get(position).getType())) {
			holder.img.setVisibility(View.GONE);
		} else if ("2".equals(mDynamics.get(position).getType())) {
			holder.img.setVisibility(View.VISIBLE);
			if (mDynamics.get(position).getImg().size()!=0) {
				final String url = mDynamics.get(position).getImg().get(0).toString();
				ImageLoader.getInstance().displayImage(url, holder.img);
				holder.img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {						
						intent.setClass(context, MyDynamicImgActivity.class);
//						Bitmap bit = BitmapFactory.decodeFile(url);
						intent.putExtra("bitmap", url);
						context.startActivity(intent);					
					}
				});
			}		
		}
		return convertView;
	}
	class viewHolder {
		TextView title;
		TextView content;
		ImageView img;
		TextView uname;
		TextView addtime;
	}
}
=======
package com.lst.lstjx.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lst.lstjx.activity.MyDynamicImgActivity;
import com.lst.lstjx.bean.DynamicBean;
import com.lst.lstjx.bean.MyDynamic;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyDynamicAdapter extends BaseAdapter {
	private List<MyDynamic> mDynamics = new ArrayList<MyDynamic>();
	private LayoutInflater inflater;
	private Context context;
	// private DynamicBean mDynamicBean;
	private MyDynamic myDynamic;
	public MyDynamicAdapter(Context context, List<MyDynamic> myDynamic) {
		this.mDynamics = myDynamic;
		this.context = context;
		inflater = LayoutInflater.from(context);	
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDynamics.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDynamics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final viewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_dynamic, null);
			holder = new viewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.img = (ImageView) convertView.findViewById(R.id.msg);
			holder.uname = (TextView) convertView.findViewById(R.id.uname);
			holder.addtime = (TextView) convertView.findViewById(R.id.addtime);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		System.out.println("-----position---" + position);
		System.out.println("----type----" + mDynamics.get(position));
		holder.title.setText(mDynamics.get(position).getTitle());
		holder.uname.setText(mDynamics.get(position).getUname());
		holder.addtime.setText(mDynamics.get(position).getAddtime());
		holder.content.setText(mDynamics.get(position).getContent());
		final Intent intent = new Intent();
		if ("1".equals(mDynamics.get(position).getType())) {
			holder.img.setVisibility(View.GONE);
		} else if ("2".equals(mDynamics.get(position).getType())) {
			holder.img.setVisibility(View.VISIBLE);
			if (mDynamics.get(position).getImg().size()!=0) {
				final String url = mDynamics.get(position).getImg().get(0).toString();
				ImageLoader.getInstance().displayImage(url, holder.img);
				holder.img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {						
						intent.setClass(context, MyDynamicImgActivity.class);
//						Bitmap bit = BitmapFactory.decodeFile(url);
						intent.putExtra("bitmap", url);
						context.startActivity(intent);					
					}
				});
			}		
		}
		return convertView;
	}
	class viewHolder {
		TextView title;
		TextView content;
		ImageView img;
		TextView uname;
		TextView addtime;
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
