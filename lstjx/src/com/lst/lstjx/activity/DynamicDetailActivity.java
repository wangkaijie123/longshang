package com.lst.lstjx.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lst.yuewo.R;

/**
 * author describe parameter return 动态详情页面
 */
public class DynamicDetailActivity extends Activity {
	private ListView lv_dynamic_detail;
	private DynamicDetailListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamicdetail);
		inintView();
	}

	private void inintView() {
		// TODO Auto-generated method stub
		lv_dynamic_detail = (ListView) findViewById(R.id.lv_dynamic_detail);
		mAdapter = new DynamicDetailListAdapter(DynamicDetailActivity.this);
		lv_dynamic_detail.setAdapter(mAdapter);
	}

	private class DynamicDetailListAdapter extends BaseAdapter {

		private Context context;
//		private int position = 0;
	Holder hold;
//		private String[] strTitle = new String[] { "原罪", "日本帝国主义", "生命",
//				"让我们为了明天珍惜自己的健康", "王维达" };
//		private String[] strContent = new String[] {
//				"报道称，根据命令，张龙植晋升为陆军中将，薛泰星等6人晋升为陆军大校，郑文成外62人晋升为陆军上校，崔光南等43人晋升为陆军中校，赵光哲等34名晋升为陆军少校。",
//				"薛泰星等6人晋升为陆军大校，郑文成外62人晋升为陆军上校，崔光南等43人晋升为陆军中校，赵光哲等34名晋升为陆军少校。",
//				"崔光南等43人晋升为陆军中校，赵光哲等34名晋升为陆军少校",
//				"崔光南等43人晋升为陆军中校，赵光哲等34名晋升为陆军少校", "43人晋升为陆军中校，赵光哲等34名晋升为陆军少校" };
//		private String[] strName = new String[] { "wangweida", "王维达", "wang",
//				"vinder", "VinDer.Wang" };
//		private int[] imgContent = new int[] { R.drawable.lingshi1,
//				R.drawable.lingshi1, R.drawable.lingshi1, R.drawable.lingshi1,
//				R.drawable.lingshi1 };

		public DynamicDetailListAdapter(Context context) {
			this.context = context;
		}

		public int getCount() {
			return 5;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int arg0, View view, ViewGroup viewGroup) {
			if (view == null) {
				view = View.inflate(context,
						R.layout.item_dynamic_detail_comment, null);
				hold = new Holder(view);
				view.setTag(hold);
			} else {
				hold = (Holder) view.getTag();
			}
			// hold.dynamic_title_tv.setText(strTitle[arg0]);
			// hold.dynamic_content_tv.setText(strContent[arg0]);
			// hold.dynamic_content_name.setText(strName[arg0]);
			// hold.dynamic_content_time.setText(newStoptime);
			// hold.dynamic_content_img.setImageResource(imgContent[arg0]);
			return view;
		}

		private class Holder {
			// LinearLayout layout;
			// ImageView dynamic_content_img;
			// TextView dynamic_title_tv, dynamic_content_tv,
			// dynamic_content_name, dynamic_content_time;

			public Holder(View view) {
				// dynamic_title_tv = (TextView) view
				// .findViewById(R.id.dynamic_title_tv);
				// dynamic_content_tv = (TextView) view
				// .findViewById(R.id.dynamic_content_tv);
				// dynamic_content_name = (TextView) view
				// .findViewById(R.id.dynamic_content_name);
				// dynamic_content_time = (TextView) view
				// .findViewById(R.id.dynamic_content_time);
				// dynamic_content_img = (ImageView) view
				// .findViewById(R.id.dynamic_content_img);

			}
		}
	}
}
