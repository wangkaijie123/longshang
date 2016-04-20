package com.lst.lstjx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.lst.lstjx.model.ApiResult;
import com.lst.yuewo.R;

import io.rong.imkit.widget.AsyncImageView ;

/**
 * Created by Bob on 2015/3/26.
 */
public class SearchFriendAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ApiResult> mResults;

    public SearchFriendAdapter(List<ApiResult> results, Context context){
        this.mResults = results;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null || convertView.getTag() == null){
            convertView = mLayoutInflater.inflate(R.layout.de_item_search,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.mSearchName = (TextView) convertView.findViewById(R.id.search_item_name);
       
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if(viewHolder != null) {
            viewHolder.mSearchName.setText(mResults.get(position).getUsername());
//            viewHolder.mImageView.setImageDrawable(mResults.get(position).getPortrait());
        }

        return convertView;
    }

    static class ViewHolder{
        TextView mSearchName;

        AsyncImageView mImageView;
    }
}
