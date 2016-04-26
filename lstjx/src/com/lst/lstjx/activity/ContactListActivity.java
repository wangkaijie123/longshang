<<<<<<< HEAD
package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.http.Header;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.GetFriendBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.IsHanyu;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.MyLetterListView;
import com.lst.lstjx.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import io.rong.imlib.model.Conversation;
/**
 * 通讯录
 * author describe parameter return
 */
public class ContactListActivity extends Activity implements OnClickListener {
	private ArrayList<String> mNameHanZiList;
	private ArrayList<String> mIdHanZiList;// 装name是汉字的对应的id
	private ArrayList<String> idList;
	private ArrayList<String> nameList;
	private String str; // 做判断 是否是字符串 是汉字就转为拼音 赋给str
	private Map<String, String> mNameHeadImg;
	private Map<String, String> mNewMap; //
	private Map<String, String> map;
	private ArrayList<Integer> HanyuPosition = new ArrayList<Integer>(); // 记录下汉语的位置
	private HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();// 漢字轉成拼音的對象
	private List<String> mFriendListName;// 解析数据后要转成arraylist 好排序
	private List<String> mFriendListNamePinyin;
	private GetFriendBean mGetFriendBean;
	private ListAdapter adapter;
	private TextView overlay; // 对话框首字母textview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private String userid;
	private OverlayThread overlayThread; // 显示首字母对话框
	private String[] mPinyin;
	private PullToRefreshListView mPullRefreshListView;
	protected static final int GETFRIENDLIST = 0X66666;
	protected static final int REFRESH = 0X654132;
	private String mCurrentPin, mPreviewBeforPin;
	private ImageView contact_search_frend;
	// zzf
	public static final int PERSONDETAIL = 10;
	private String username;
	private int pos;
	private RelativeLayout  qun_friend;
	private String newname;
	private Handler mHandler = new Handler() {
		@SuppressWarnings({ "deprecation" })
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETFRIENDLIST:
				// key是name value是img的链接地址；
				// 装到一个map中在getview中通过getvalues来拿到img的地址，然后再设置头像
				mNameHeadImg = new HashMap<String, String>();
				mNameHanZiList = new ArrayList<String>();
				mIdHanZiList = new ArrayList<String>();
				mFriendListName = new ArrayList<String>();
				mFriendListNamePinyin = new ArrayList<String>();// 名字的拼音的集合
				if (mGetFriendBean.getSuccess() == 1 && mGetFriendBean != null) {
					int mCount = mGetFriendBean.data.size();

					for (int i = 0; i < mCount; i++) {
						System.out
								.println("_______________mGetFriendBean______"
										+ mGetFriendBean.data.get(i)
												.getUsername());
						

							mNameHeadImg.put(mGetFriendBean.data.get(i)
									.getUsername(), mGetFriendBean.data.get(i)
									.getFace());
						
					}
					// 拿到好友列表的数据后把名字装到list中进行排序
					for (int i = 0; i < mCount; i++) {
						
							mFriendListName.add(mGetFriendBean.data.get(i)
									.getUsername().toString());

							try {
								mFriendListNamePinyin.add(PinyinHelper
										.toHanyuPinyinString(
												mGetFriendBean.data.get(i)
														.getUsername()
														.toString(),
												spellFormat, ""));
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}
					mPinyin = new String[mFriendListNamePinyin.size()];
					for (int j = 0; j < mPinyin.length; j++) {
						mPinyin[j] = mFriendListNamePinyin.get(j);
					}
					// 拼音排序
					Arrays.sort(mPinyin, String.CASE_INSENSITIVE_ORDER);
					// Collections.sort(mFriendListNamePinyin, comparator);

					// 将id和name对应起来加入map中进行排序
					map = new HashMap<String, String>();
					idList = new ArrayList<String>();
					nameList = new ArrayList<String>();
					mCount = mGetFriendBean.data.size();
					// 判断是不是汉字 是的话转为拼音在装进list 不是就直接装进map
					for (int i = 0; i < mCount; i++) {
						
							if (IsHanyu.test(mGetFriendBean.data.get(i)
									.getUsername().toString())) {
								map.put(mGetFriendBean.data.get(i).getFid(),
										mGetFriendBean.data.get(i)
												.getUsername());
							} else {

								try {
									str = PinyinHelper.toHanyuPinyinString(
											mGetFriendBean.data.get(i)
													.getUsername().toString(),
											spellFormat, "");
									// 记录下是汉语的对应的id
									mIdHanZiList.add(mGetFriendBean.data.get(i)
											.getFid().toString());
									mNameHanZiList.add(mGetFriendBean.data
											.get(i).getUsername().toString());
									HanyuPosition.add(i);
								} catch (BadHanyuPinyinOutputFormatCombination e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								map.put(mGetFriendBean.data.get(i).getFid(),
										str);
							
						
						}
					}

					List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
							map.entrySet());
					Collections.sort(list,
							new Comparator<Map.Entry<String, String>>() {
								// 升序排序
								public int compare(Entry<String, String> o1,
										Entry<String, String> o2) {
									return o1.getValue().compareTo(
											o2.getValue());
								}
							});
					mNewMap = new HashMap<String, String>();
					// 将排序过后的map 的key 和value 分别装到list中 这样方便使用， id和name 已经对应 不过
					// 全部是英文 汉语已经转变成拼音
					for (Map.Entry<String, String> mapping : list) {

						idList.add(mapping.getKey());
						nameList.add(mapping.getValue());
					}
					for (int i = 0; i < idList.size(); i++) {
						idList.get(i).toString();
						for (int j = 0; j < mIdHanZiList.size(); j++) {
							if ((idList.get(i).toString()).equals(mIdHanZiList
									.get(j).toString())) {
								nameList.set(i, mNameHanZiList.get(j));
							}
						}
					}
					// zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
					if (idList.size() != 0 && nameList.size() != 0) {
						try {
							adapter = new ListAdapter(ContactListActivity.this);
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mPullRefreshListView.setAdapter(adapter);
					}
					//
				}

				break;
			case REFRESH:

				mPullRefreshListView.onRefreshComplete();
				if (mGetFriendBean.getSuccess() == 1 && mGetFriendBean != null
						&& mGetFriendBean.data.size() == 0) {
					return;
				}
				mFriendListName = new ArrayList<String>();
				mFriendListNamePinyin = new ArrayList<String>();// 名字的拼音的集合
				if (mGetFriendBean.getSuccess() == 1 && mGetFriendBean != null) {
					int mCount = mGetFriendBean.data.size();
					// 拿到好友列表的数据后把名字装到list中进行排序
					for (int i = 0; i < mCount; i++) {
						System.out
								.println("+++++++++++++++++++++mGetFriendBean++++++++++++"
										+ mGetFriendBean.data.get(i)
												.getUsername().toString()
										+ mGetFriendBean.data.size());
						

							mNameHeadImg.put(mGetFriendBean.data.get(i)
									.getUsername(), mGetFriendBean.data.get(i)
									.getFace());

							try {
								mFriendListNamePinyin.add(PinyinHelper
										.toHanyuPinyinString(
												mGetFriendBean.data.get(i)
														.getUsername()
														.toString(),
												spellFormat, ""));
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							
						}
					}
					mPinyin = new String[mFriendListNamePinyin.size()];
					for (int j = 0; j < mPinyin.length; j++) {
						mPinyin[j] = mFriendListNamePinyin.get(j);
					}
					// 拼音排序
					Arrays.sort(mPinyin, String.CASE_INSENSITIVE_ORDER);
					// Collections.sort(mFriendListNamePinyin, comparator);

					map = new HashMap<String, String>();
					idList = new ArrayList<String>();
					nameList = new ArrayList<String>();
					mCount = mGetFriendBean.data.size();
					for (int i = 0; i < mCount; i++) {
						
							if (IsHanyu.test(mGetFriendBean.data.get(i)
									.getUsername().toString())) {
								map.put(mGetFriendBean.data.get(i).getFid(),
										mGetFriendBean.data.get(i)
												.getUsername());
							} else {

								try {
									str = PinyinHelper.toHanyuPinyinString(
											mGetFriendBean.data.get(i)
													.getUsername().toString(),
											spellFormat, "");
									// 记录下是汉语的对应的id
									mIdHanZiList.add(mGetFriendBean.data.get(i)
											.getFid().toString());
									mNameHanZiList.add(mGetFriendBean.data
											.get(i).getUsername().toString());
									HanyuPosition.add(i);
								} catch (BadHanyuPinyinOutputFormatCombination e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								map.put(mGetFriendBean.data.get(i).getFid(),
										str);
							
						}
					}

					List<Map.Entry<String, String>> list2 = new ArrayList<Map.Entry<String, String>>(
							map.entrySet());
					Collections.sort(list2,
							new Comparator<Map.Entry<String, String>>() {
								// 升序排序
								public int compare(Entry<String, String> o1,
										Entry<String, String> o2) {
									return o1.getValue().compareTo(
											o2.getValue());
								}
							});
					mNewMap = new HashMap<String, String>();
					// 将排序过后的map 的key 和value 分别装到list中 这样方便使用， id和name已经对应不过
					// 全部是英文 汉语已经转变成拼音
					for (Map.Entry<String, String> mapping : list2) {
						idList.add(mapping.getKey());
						nameList.add(mapping.getValue());
					}
					for (int i = 0; i < idList.size(); i++) {
						idList.get(i).toString();
						for (int j = 0; j < mIdHanZiList.size(); j++) {
							if ((idList.get(i).toString()).equals(mIdHanZiList
									.get(j).toString())) {
								nameList.set(i, mNameHanZiList.get(j));
							}
						}
					}
					if (idList.size() != 0 && nameList.size() != 0) {
						try {
							adapter = new ListAdapter(ContactListActivity.this);
							mPullRefreshListView.onRefreshComplete();
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mPullRefreshListView.setAdapter(adapter);
						Toast.makeText(ContactListActivity.this, "已经刷新",
								Toast.LENGTH_SHORT).show();
					}

				}
				break;
			default:
				break;
			}
		};
	};

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		FrameActivity.getInstance().addActivity(this);
		initView();

		requstFriendList();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	// 获取好友的列表
	private void requstFriendList() {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		http.post(ContactListActivity.this, ConstantsUrl.getFriendList, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("我的好友列表数据" + arg2.toString());
						Gson mGson = new Gson();
						mGetFriendBean = mGson.fromJson(arg2,
								GetFriendBean.class);
						Message msg = mHandler.obtainMessage();
						msg.what = GETFRIENDLIST;
						msg.obj = mGetFriendBean;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	// 刷新好友的列表
	private void refreshFriendList() {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		System.out.println("userid______________________" + userid);
		http.post(ContactListActivity.this, ConstantsUrl.getFriendList, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub

						Gson mGson = new Gson();
						mGetFriendBean = mGson.fromJson(arg2,
								GetFriendBean.class);
						// zzf
						// mGetFriendBean.data.get(pos).setUsername(username);
						Message msg = mHandler.obtainMessage();
						msg.what = REFRESH;
						msg.obj = mGetFriendBean;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initView() {
		userid = SharePrefUtil
				.getString(ContactListActivity.this, "userId", "");
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.list_view);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// Do work to refresh the list here.
						refreshFriendList();
					}
				});
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						Toast.makeText(ContactListActivity.this, "已经到底部了",
								Toast.LENGTH_SHORT).show();
					}
				});
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT);
		View header = getLayoutInflater().inflate(R.layout.item_contact_header,
				mPullRefreshListView, false);
		header.setLayoutParams(layoutParams);
		ListView lv = mPullRefreshListView.getRefreshableView();
		lv.addHeaderView(header);
		
		// 漢字換拼音的初始化
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		// 組件初始化
		qun_friend = (RelativeLayout) findViewById(R.id.qun_friend);
		qun_friend.setOnClickListener(this);
		contact_search_frend = (ImageView) findViewById(R.id.contact_search_frend);
		contact_search_frend.setOnClickListener(this);
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
	}

	public class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		final int VIEW_TYPE = 3;

		@SuppressWarnings("deprecation")
		public ListAdapter(Context context)
				throws BadHanyuPinyinOutputFormatCombination {
			this.inflater = LayoutInflater.from(context);
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[mGetFriendBean.data.size()];
			for (int i = 0; i < mGetFriendBean.data.size(); i++) {
				// 当前的name的拼音
				String mPin = PinyinHelper.toHanyuPinyinString(mPinyin[i],
						spellFormat, "");
				// 上一个的name的拼音
				String mBeforPin = (i - 1) > 0 ? PinyinHelper
						.toHanyuPinyinString(mPinyin[i - 1], spellFormat, "")
						: " ";
				// 当前汉语拼音首字母
				String currentStr = getAlpha(mPin);
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(mBeforPin) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(mPin);
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getCount() {

			return idList.size();
		}

		@Override
		public Object getItem(int position) {
			return mGetFriendBean.data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.contact_list_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);

				holder.item_contactlist_img = (ImageView) convertView
						.findViewById(R.id.item_contactlist_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 通过把名字和头像放入map进行对应，然后通过name拿到img
			// 通过把id和头像放入map进行对应，然后通过id拿到img

			ImageLoader.getInstance().displayImage(
					mNameHeadImg.get(nameList.get(position).toString().trim()),
					holder.item_contactlist_img);
			holder.name.setText(nameList.get(position));
			// 当前的name的拼音
			try {
				mCurrentPin = PinyinHelper.toHanyuPinyinString(
						mPinyin[position], spellFormat, "");
				// 上一个的name的拼音
				mPreviewBeforPin = (position - 1) > 0 ? PinyinHelper
						.toHanyuPinyinString(mPinyin[position - 1],
								spellFormat, "") : "";
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String currentStr = getAlpha(mCurrentPin);// 本次拼音
			String previewStr = (position - 1) >= 0 ? getAlpha(mPreviewBeforPin)
					: " ";// 上一个拼音
			if (!previewStr.equals(currentStr)) {// 不一样则显示
				holder.alpha.setVisibility(View.VISIBLE);
				if (currentStr.equals("#")) {
					currentStr = "#";
				}
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			// }
			System.out.println("____________nameList.get(posi tion)___________"
					+ nameList.get(position) + currentStr.toString()
					+ "___previewStr__" + previewStr.toString());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ContactListActivity.this,
							PersonalDetailActivity.class);
					intent.putExtra("userid", idList.get(position));
					intent.putExtra("username", nameList.get(position));
					startActivity(intent);
				}
			});
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母
			TextView name; // 名字
			ImageView item_contactlist_img;
		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mPullRefreshListView.getRefreshableView()
						.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	// 获得汉语拼音首字母
	@SuppressWarnings("unused")
	private String getAlpha(String str) {
		if (str.equals("-")) {
			return "&";
		}
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.contact_search_frend:
			startActivity(new Intent(ContactListActivity.this,
					SearchFriendActivity.class));
			break;
		case R.id.qun_friend:
//			if (RongIM.getInstance() != null) {
//                RongIM.getInstance().startSubConversationList(ContactListActivity.this, Conversation.ConversationType.GROUP);
//            }
			startActivity(new Intent(ContactListActivity.this,
					GroupActivity.class));
			break;

		default:
			break;
		}
	}
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      
	        if(requestCode == 20){
	           refreshFriendList();
	        }

	        super.onActivityResult(requestCode, resultCode, data);
	    }

}
=======
package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.http.Header;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.bean.GetFriendBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.utils.IsHanyu;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.MyLetterListView;
import com.lst.lstjx.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import io.rong.imlib.model.Conversation;
/**
 * 通讯录
 * author describe parameter return
 */
public class ContactListActivity extends Activity implements OnClickListener {
	private ArrayList<String> mNameHanZiList;
	private ArrayList<String> mIdHanZiList;// 装name是汉字的对应的id
	private ArrayList<String> idList;
	private ArrayList<String> nameList;
	private String str; // 做判断 是否是字符串 是汉字就转为拼音 赋给str
	private Map<String, String> mNameHeadImg;
	private Map<String, String> mNewMap; //
	private Map<String, String> map;
	private ArrayList<Integer> HanyuPosition = new ArrayList<Integer>(); // 记录下汉语的位置
	private HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();// 漢字轉成拼音的對象
	private List<String> mFriendListName;// 解析数据后要转成arraylist 好排序
	private List<String> mFriendListNamePinyin;
	private GetFriendBean mGetFriendBean;
	private ListAdapter adapter;
	private TextView overlay; // 对话框首字母textview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private String userid;
	private OverlayThread overlayThread; // 显示首字母对话框
	private String[] mPinyin;
	private PullToRefreshListView mPullRefreshListView;
	protected static final int GETFRIENDLIST = 0X66666;
	protected static final int REFRESH = 0X654132;
	private String mCurrentPin, mPreviewBeforPin;
	private ImageView contact_search_frend;
	// zzf
	public static final int PERSONDETAIL = 10;
	private String username;
	private int pos;
	private RelativeLayout  qun_friend;
	private String newname;
	private Handler mHandler = new Handler() {
		@SuppressWarnings({ "deprecation" })
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETFRIENDLIST:
				// key是name value是img的链接地址；
				// 装到一个map中在getview中通过getvalues来拿到img的地址，然后再设置头像
				mNameHeadImg = new HashMap<String, String>();
				mNameHanZiList = new ArrayList<String>();
				mIdHanZiList = new ArrayList<String>();
				mFriendListName = new ArrayList<String>();
				mFriendListNamePinyin = new ArrayList<String>();// 名字的拼音的集合
				if (mGetFriendBean.getSuccess() == 1 && mGetFriendBean != null) {
					int mCount = mGetFriendBean.data.size();

					for (int i = 0; i < mCount; i++) {
						System.out
								.println("_______________mGetFriendBean______"
										+ mGetFriendBean.data.get(i)
												.getUsername());
						

							mNameHeadImg.put(mGetFriendBean.data.get(i)
									.getUsername(), mGetFriendBean.data.get(i)
									.getFace());
						
					}
					// 拿到好友列表的数据后把名字装到list中进行排序
					for (int i = 0; i < mCount; i++) {
						
							mFriendListName.add(mGetFriendBean.data.get(i)
									.getUsername().toString());

							try {
								mFriendListNamePinyin.add(PinyinHelper
										.toHanyuPinyinString(
												mGetFriendBean.data.get(i)
														.getUsername()
														.toString(),
												spellFormat, ""));
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}
					mPinyin = new String[mFriendListNamePinyin.size()];
					for (int j = 0; j < mPinyin.length; j++) {
						mPinyin[j] = mFriendListNamePinyin.get(j);
					}
					// 拼音排序
					Arrays.sort(mPinyin, String.CASE_INSENSITIVE_ORDER);
					// Collections.sort(mFriendListNamePinyin, comparator);

					// 将id和name对应起来加入map中进行排序
					map = new HashMap<String, String>();
					idList = new ArrayList<String>();
					nameList = new ArrayList<String>();
					mCount = mGetFriendBean.data.size();
					// 判断是不是汉字 是的话转为拼音在装进list 不是就直接装进map
					for (int i = 0; i < mCount; i++) {
						
							if (IsHanyu.test(mGetFriendBean.data.get(i)
									.getUsername().toString())) {
								map.put(mGetFriendBean.data.get(i).getFid(),
										mGetFriendBean.data.get(i)
												.getUsername());
							} else {

								try {
									str = PinyinHelper.toHanyuPinyinString(
											mGetFriendBean.data.get(i)
													.getUsername().toString(),
											spellFormat, "");
									// 记录下是汉语的对应的id
									mIdHanZiList.add(mGetFriendBean.data.get(i)
											.getFid().toString());
									mNameHanZiList.add(mGetFriendBean.data
											.get(i).getUsername().toString());
									HanyuPosition.add(i);
								} catch (BadHanyuPinyinOutputFormatCombination e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								map.put(mGetFriendBean.data.get(i).getFid(),
										str);
							
						
						}
					}

					List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
							map.entrySet());
					Collections.sort(list,
							new Comparator<Map.Entry<String, String>>() {
								// 升序排序
								public int compare(Entry<String, String> o1,
										Entry<String, String> o2) {
									return o1.getValue().compareTo(
											o2.getValue());
								}
							});
					mNewMap = new HashMap<String, String>();
					// 将排序过后的map 的key 和value 分别装到list中 这样方便使用， id和name 已经对应 不过
					// 全部是英文 汉语已经转变成拼音
					for (Map.Entry<String, String> mapping : list) {

						idList.add(mapping.getKey());
						nameList.add(mapping.getValue());
					}
					for (int i = 0; i < idList.size(); i++) {
						idList.get(i).toString();
						for (int j = 0; j < mIdHanZiList.size(); j++) {
							if ((idList.get(i).toString()).equals(mIdHanZiList
									.get(j).toString())) {
								nameList.set(i, mNameHanZiList.get(j));
							}
						}
					}
					// zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
					if (idList.size() != 0 && nameList.size() != 0) {
						try {
							adapter = new ListAdapter(ContactListActivity.this);
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mPullRefreshListView.setAdapter(adapter);
					}
					//
				}

				break;
			case REFRESH:

				mPullRefreshListView.onRefreshComplete();
				if (mGetFriendBean.getSuccess() == 1 && mGetFriendBean != null
						&& mGetFriendBean.data.size() == 0) {
					return;
				}
				mFriendListName = new ArrayList<String>();
				mFriendListNamePinyin = new ArrayList<String>();// 名字的拼音的集合
				if (mGetFriendBean.getSuccess() == 1 && mGetFriendBean != null) {
					int mCount = mGetFriendBean.data.size();
					// 拿到好友列表的数据后把名字装到list中进行排序
					for (int i = 0; i < mCount; i++) {
						System.out
								.println("+++++++++++++++++++++mGetFriendBean++++++++++++"
										+ mGetFriendBean.data.get(i)
												.getUsername().toString()
										+ mGetFriendBean.data.size());
						

							mNameHeadImg.put(mGetFriendBean.data.get(i)
									.getUsername(), mGetFriendBean.data.get(i)
									.getFace());

							try {
								mFriendListNamePinyin.add(PinyinHelper
										.toHanyuPinyinString(
												mGetFriendBean.data.get(i)
														.getUsername()
														.toString(),
												spellFormat, ""));
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							
						}
					}
					mPinyin = new String[mFriendListNamePinyin.size()];
					for (int j = 0; j < mPinyin.length; j++) {
						mPinyin[j] = mFriendListNamePinyin.get(j);
					}
					// 拼音排序
					Arrays.sort(mPinyin, String.CASE_INSENSITIVE_ORDER);
					// Collections.sort(mFriendListNamePinyin, comparator);

					map = new HashMap<String, String>();
					idList = new ArrayList<String>();
					nameList = new ArrayList<String>();
					mCount = mGetFriendBean.data.size();
					for (int i = 0; i < mCount; i++) {
						
							if (IsHanyu.test(mGetFriendBean.data.get(i)
									.getUsername().toString())) {
								map.put(mGetFriendBean.data.get(i).getFid(),
										mGetFriendBean.data.get(i)
												.getUsername());
							} else {

								try {
									str = PinyinHelper.toHanyuPinyinString(
											mGetFriendBean.data.get(i)
													.getUsername().toString(),
											spellFormat, "");
									// 记录下是汉语的对应的id
									mIdHanZiList.add(mGetFriendBean.data.get(i)
											.getFid().toString());
									mNameHanZiList.add(mGetFriendBean.data
											.get(i).getUsername().toString());
									HanyuPosition.add(i);
								} catch (BadHanyuPinyinOutputFormatCombination e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								map.put(mGetFriendBean.data.get(i).getFid(),
										str);
							
						}
					}

					List<Map.Entry<String, String>> list2 = new ArrayList<Map.Entry<String, String>>(
							map.entrySet());
					Collections.sort(list2,
							new Comparator<Map.Entry<String, String>>() {
								// 升序排序
								public int compare(Entry<String, String> o1,
										Entry<String, String> o2) {
									return o1.getValue().compareTo(
											o2.getValue());
								}
							});
					mNewMap = new HashMap<String, String>();
					// 将排序过后的map 的key 和value 分别装到list中 这样方便使用， id和name已经对应不过
					// 全部是英文 汉语已经转变成拼音
					for (Map.Entry<String, String> mapping : list2) {
						idList.add(mapping.getKey());
						nameList.add(mapping.getValue());
					}
					for (int i = 0; i < idList.size(); i++) {
						idList.get(i).toString();
						for (int j = 0; j < mIdHanZiList.size(); j++) {
							if ((idList.get(i).toString()).equals(mIdHanZiList
									.get(j).toString())) {
								nameList.set(i, mNameHanZiList.get(j));
							}
						}
					}
					if (idList.size() != 0 && nameList.size() != 0) {
						try {
							adapter = new ListAdapter(ContactListActivity.this);
							mPullRefreshListView.onRefreshComplete();
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mPullRefreshListView.setAdapter(adapter);
						Toast.makeText(ContactListActivity.this, "已经刷新",
								Toast.LENGTH_SHORT).show();
					}

				}
				break;
			default:
				break;
			}
		};
	};

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		FrameActivity.getInstance().addActivity(this);
		initView();

		requstFriendList();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	// 获取好友的列表
	private void requstFriendList() {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		http.post(ContactListActivity.this, ConstantsUrl.getFriendList, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("我的好友列表数据" + arg2.toString());
						Gson mGson = new Gson();
						mGetFriendBean = mGson.fromJson(arg2,
								GetFriendBean.class);
						Message msg = mHandler.obtainMessage();
						msg.what = GETFRIENDLIST;
						msg.obj = mGetFriendBean;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	// 刷新好友的列表
	private void refreshFriendList() {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		System.out.println("userid______________________" + userid);
		http.post(ContactListActivity.this, ConstantsUrl.getFriendList, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub

						Gson mGson = new Gson();
						mGetFriendBean = mGson.fromJson(arg2,
								GetFriendBean.class);
						// zzf
						// mGetFriendBean.data.get(pos).setUsername(username);
						Message msg = mHandler.obtainMessage();
						msg.what = REFRESH;
						msg.obj = mGetFriendBean;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initView() {
		userid = SharePrefUtil
				.getString(ContactListActivity.this, "userId", "");
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.list_view);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// Do work to refresh the list here.
						refreshFriendList();
					}
				});
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						Toast.makeText(ContactListActivity.this, "已经到底部了",
								Toast.LENGTH_SHORT).show();
					}
				});
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT);
		View header = getLayoutInflater().inflate(R.layout.item_contact_header,
				mPullRefreshListView, false);
		header.setLayoutParams(layoutParams);
		ListView lv = mPullRefreshListView.getRefreshableView();
		lv.addHeaderView(header);
		
		// 漢字換拼音的初始化
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		// 組件初始化
		qun_friend = (RelativeLayout) findViewById(R.id.qun_friend);
		qun_friend.setOnClickListener(this);
		contact_search_frend = (ImageView) findViewById(R.id.contact_search_frend);
		contact_search_frend.setOnClickListener(this);
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
	}

	public class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		final int VIEW_TYPE = 3;

		@SuppressWarnings("deprecation")
		public ListAdapter(Context context)
				throws BadHanyuPinyinOutputFormatCombination {
			this.inflater = LayoutInflater.from(context);
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[mGetFriendBean.data.size()];
			for (int i = 0; i < mGetFriendBean.data.size(); i++) {
				// 当前的name的拼音
				String mPin = PinyinHelper.toHanyuPinyinString(mPinyin[i],
						spellFormat, "");
				// 上一个的name的拼音
				String mBeforPin = (i - 1) > 0 ? PinyinHelper
						.toHanyuPinyinString(mPinyin[i - 1], spellFormat, "")
						: " ";
				// 当前汉语拼音首字母
				String currentStr = getAlpha(mPin);
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(mBeforPin) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(mPin);
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getCount() {

			return idList.size();
		}

		@Override
		public Object getItem(int position) {
			return mGetFriendBean.data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.contact_list_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);

				holder.item_contactlist_img = (ImageView) convertView
						.findViewById(R.id.item_contactlist_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 通过把名字和头像放入map进行对应，然后通过name拿到img
			// 通过把id和头像放入map进行对应，然后通过id拿到img

			ImageLoader.getInstance().displayImage(
					mNameHeadImg.get(nameList.get(position).toString().trim()),
					holder.item_contactlist_img);
			holder.name.setText(nameList.get(position));
			// 当前的name的拼音
			try {
				mCurrentPin = PinyinHelper.toHanyuPinyinString(
						mPinyin[position], spellFormat, "");
				// 上一个的name的拼音
				mPreviewBeforPin = (position - 1) > 0 ? PinyinHelper
						.toHanyuPinyinString(mPinyin[position - 1],
								spellFormat, "") : "";
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String currentStr = getAlpha(mCurrentPin);// 本次拼音
			String previewStr = (position - 1) >= 0 ? getAlpha(mPreviewBeforPin)
					: " ";// 上一个拼音
			if (!previewStr.equals(currentStr)) {// 不一样则显示
				holder.alpha.setVisibility(View.VISIBLE);
				if (currentStr.equals("#")) {
					currentStr = "#";
				}
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			// }
			System.out.println("____________nameList.get(posi tion)___________"
					+ nameList.get(position) + currentStr.toString()
					+ "___previewStr__" + previewStr.toString());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ContactListActivity.this,
							PersonalDetailActivity.class);
					intent.putExtra("userid", idList.get(position));
					intent.putExtra("username", nameList.get(position));
					startActivity(intent);
				}
			});
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母
			TextView name; // 名字
			ImageView item_contactlist_img;
		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mPullRefreshListView.getRefreshableView()
						.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	// 获得汉语拼音首字母
	@SuppressWarnings("unused")
	private String getAlpha(String str) {
		if (str.equals("-")) {
			return "&";
		}
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.contact_search_frend:
			startActivity(new Intent(ContactListActivity.this,
					SearchFriendActivity.class));
			break;
		case R.id.qun_friend:
//			if (RongIM.getInstance() != null) {
//                RongIM.getInstance().startSubConversationList(ContactListActivity.this, Conversation.ConversationType.GROUP);
//            }
			startActivity(new Intent(ContactListActivity.this,
					GroupActivity.class));
			break;

		default:
			break;
		}
	}
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      
	        if(requestCode == 20){
	           refreshFriendList();
	        }

	        super.onActivityResult(requestCode, resultCode, data);
	    }

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
