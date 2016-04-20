package com.lst.lstjx.activity;

import io.rong.imkit.widget.AsyncImageView;
import java.util.List;
import org.apache.http.Header;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.SearchFriendBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.model.ApiResult;
import com.lst.lstjx.utils.Constants;
import com.lst.yuewo.R;
/**
 * Created by Bob on 2015/3/26.
 */
public class SearchFriendActivity  extends Activity implements
		View.OnClickListener, AdapterView.OnItemClickListener {

	protected static final int SEARCHFRIEND = 0X5468;
	private EditText mEtSearch;
	private Button mBtSearch;
	private ListView mListSearch ;

	private String mEtStr;
	private SearchFriendBean mSearchFriendBean;
	private ImageView search_friend_goback;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SEARCHFRIEND:
				
				if (mSearchFriendBean==null||mSearchFriendBean.data.size() == 0||mSearchFriendBean.data==null) {
				}else{
					SearchFriendAdapter mAdapter = new SearchFriendAdapter();
					mListSearch.setAdapter(mAdapter);
					mListSearch.setOnItemClickListener(SearchFriendActivity.this);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.de_ac_search);
		initView();
		initData();

	}
	protected void initView() {
//		getSupportActionBar().setTitle(R.string.public_account_search);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar()
//				.setHomeAsUpIndicator(R.drawable.title_back);
		mEtSearch = (EditText) findViewById(R.id.de_ui_search);
		mBtSearch = (Button) findViewById(R.id.de_search);
		mListSearch = (ListView) findViewById(R.id.de_search_list);
		mEtSearch.addTextChangedListener(mTextWatcher);
		search_friend_goback = (ImageView) findViewById(R.id.search_friend_goback);
		search_friend_goback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			mEtStr = mEtSearch.getText().toString().trim();
			if (mEtStr!=null&&!TextUtils.isEmpty(mEtStr)) {
				goSearch(mEtStr);
			}
			
		}

	};

	protected void initData() {
		mBtSearch.setOnClickListener(this);
	
	}
	protected void goSearch(String mStr) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("username", mStr);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(SearchFriendActivity.this, ConstantsUrl.searchFriend, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {				
						Gson mGson = new Gson();
						mSearchFriendBean = mGson.fromJson(arg2,
								SearchFriendBean.class);
						if (mSearchFriendBean!=null&&mSearchFriendBean.data!=null&&mSearchFriendBean.data.size()!=0) {
							Message msg = mHandler.obtainMessage();
							msg.what = SEARCHFRIEND;
							msg.obj = mSearchFriendBean;
							mHandler.sendMessage(msg);
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mBtSearch)) {
			String userName = mEtSearch.getText().toString();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.PERSONAL_REQUESTCODE) {
			Intent intent = new Intent();
			this.setResult(Constants.SEARCH_REQUESTCODE, intent);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent in = new Intent(this, SearchPersonalDetailActivity.class);		
		in.putExtra("userid",mSearchFriendBean.data.get(position).getId());
		in.putExtra("username",mSearchFriendBean.data.get(position).getUsername());
		startActivity(in);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}

	class SearchFriendAdapter extends android.widget.BaseAdapter {
		private Context mContext;
		private LayoutInflater mLayoutInflater;
		private List<ApiResult> mResults;
		public SearchFriendAdapter() {
			mLayoutInflater = LayoutInflater.from(SearchFriendActivity.this);
		}

		@Override
		public int getCount() {
			return mSearchFriendBean.data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = mLayoutInflater.inflate(R.layout.de_item_search,
						parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mSearchName = (TextView) convertView
						.findViewById(R.id.search_item_name);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (viewHolder != null) {
				viewHolder.mSearchName.setText(mSearchFriendBean.data
						.get(position).getUsername());
				// viewHolder.mImageView.setImageDrawable(mResults.get(position).getPortrait());
			}

			return convertView;
		}

		class ViewHolder {
			TextView mSearchName;

			AsyncImageView mImageView;
		}
	}

}
