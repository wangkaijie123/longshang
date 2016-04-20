package com.lst.lstjx.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

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
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import c.t.m.g.s;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.bean.CrGroup;
import com.lst.lstjx.bean.GetFriendBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.common.DemoApi;
import com.lst.lstjx.utils.IsHanyu;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.yuewo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 选择联系人创建群
 */
public class ChooseFriendToAddQun extends Activity implements OnClickListener {
	protected static final int CREATEFRIENDGETFRIENDLIST = 0X65456;
	private TextView choose_friend_create_group_cancel,
			choose_friend_create_group_confirm;
	private ListView personList;
	private GetFriendBean mGetFriendBean;
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();// 漢字轉成拼音的對象
	private String[] mPinyin;
	private String Fid; // 选择的单聊的userid
	private String username, q_name, q_show;
	private List<String> mFriendListName;// 解析数据后要转成arraylist 好排序
	private List<String> mFriendListNamePinyin;
	private String mCurrentPin, mPreviewBeforPin;
	private String userid;
	private Map<String, String> mNameHeadImg;
	private Map<String, String> mNewMap; //
	private Map<String, String> map;
	private ArrayList<String> idList;
	private String str; // 做判断 是否是字符串 是汉字就转为拼音 赋给str
	private ArrayList<String> nameList;
	private ArrayList<String> mNameHanZiList;
	private ArrayList<String> mIdHanZiList;// 装name是汉字的对应的id
	/** 标记CheckBox是否被选中 **/
	List<Boolean> mChecked;
	String uids;
	private ArrayList<Integer> HanyuPosition = new ArrayList<Integer>(); // 记录下汉语的位置

	private Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CREATEFRIENDGETFRIENDLIST:
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
						if (mGetFriendBean.data.get(i).getMark() != null
								&& !"".equals(mGetFriendBean.data.get(i)
										.getMark().toString().trim())) {
							mNameHeadImg.put(mGetFriendBean.data.get(i)
									.getMark(), mGetFriendBean.data.get(i)
									.getFace());
						} else {

							mNameHeadImg.put(mGetFriendBean.data.get(i)
									.getUsername(), mGetFriendBean.data.get(i)
									.getFace());
						}
					}
					// 拿到好友列表的数据后把名字装到list中进行排序
					for (int i = 0; i < mCount; i++) {
						if (mGetFriendBean.data.get(i).getMark() != null
								&& !"".equals(mGetFriendBean.data.get(i)
										.getMark().toString().trim())) {
							mFriendListName.add(mGetFriendBean.data.get(i)
									.getMark().toString());
							try {
								mFriendListNamePinyin.add(PinyinHelper
										.toHanyuPinyinString(
												mGetFriendBean.data.get(i)
														.getMark().toString(),
												spellFormat, ""));
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
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
						if (mGetFriendBean.data.get(i).getMark() == null
								|| "".equals(mGetFriendBean.data.get(i)
										.getMark().toString().trim())) {

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
						} else {
							if (IsHanyu.test(mGetFriendBean.data.get(i)
									.getMark().toString())) {
								System.out.println("=====好友备注=="
										+ mGetFriendBean.data.get(i).getMark()
												.toString());

								map.put(mGetFriendBean.data.get(i).getFid(),
										mGetFriendBean.data.get(i).getMark());
							} else {

								try {
									str = PinyinHelper.toHanyuPinyinString(
											mGetFriendBean.data.get(i)
													.getMark().toString(),
											spellFormat, "");
									// 记录下是汉语的对应的id
									mIdHanZiList.add(mGetFriendBean.data.get(i)
											.getFid().toString());
									mNameHanZiList.add(mGetFriendBean.data
											.get(i).getMark().toString());
									HanyuPosition.add(i);
								} catch (BadHanyuPinyinOutputFormatCombination e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								map.put(mGetFriendBean.data.get(i).getFid(),
										str);
							}
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
					if (idList.size() != 0 && nameList.size() != 0) {
						try {
							personList.setAdapter(new ListAdapter(
									ChooseFriendToAddQun.this));
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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
		setContentView(R.layout.ac_choose_friend_creat_qun);
		initView();
		Intent intent = getIntent();
		q_name = intent.getStringExtra("q_name");
		q_show = intent.getStringExtra("q_show");
		requstFriendList();
	}

	private void initView() {
		// TODO Auto-generated method stub
		userid = SharePrefUtil.getString(ChooseFriendToAddQun.this, "userId",
				"");
		choose_friend_create_group_cancel = (TextView) findViewById(R.id.choose_friend_create_group_cancel);
		choose_friend_create_group_confirm = (TextView) findViewById(R.id.choose_friend_create_group_confirm);
		choose_friend_create_group_cancel.setOnClickListener(this);
		choose_friend_create_group_confirm.setOnClickListener(this);
		personList = (ListView) findViewById(R.id.list_view);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.choose_friend_create_group_cancel:
			finish();
			break;
		case R.id.choose_friend_create_group_confirm:
			// 判断第几个是true 在做if判断，如果是true就装到useridlist中
			List<String> mFidList = new ArrayList<String>();
			for (int i = 0; i < mChecked.size(); i++) {
				if (mChecked.get(i)) {
					Fid = mGetFriendBean.data.get(i).getFid();
					mFidList.add(Fid);

				}
			}
			Object[] array = mFidList.toArray();
			createQun(q_name, q_show, array);
			ChooseFriendToAddQun.this.finish();

			break;
		default:
			break;
		}
	}

	private void createQun(String name, String show, Object[] fidList) {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("masterid", userid);
		params.add("introduce", show);
		params.add("groupname", name);
		params.put("crewid", fidList);
		http.post(ChooseFriendToAddQun.this, ConstantsUrl.crGroup, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out
								.println("==========CreatQunActivity__onSuccess======"
										+ arg2);
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONObject data = json.getJSONObject("data");
							String groupid = data.getString("groupid");
							String groupname = data.getString("groupname");
							System.out
									.println("====ChooseFriendToAddQun.groupname======="
											+ groupname);
							Group group = new Group(groupid, groupname, null);
							HashMap<String, Group> hash = new HashMap<String, Group>();
							hash.put(groupid, group);
							DemoContext.getInstance().setGroupMap(hash);
							RongIM.getInstance().startGroupChat(
									ChooseFriendToAddQun.this, groupid,
									groupname);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						System.out
								.println("==========CreatQunActivity__onFailure====="
										+ arg2);

					}
				});
	}

	// 获取好友列表
	private void requstFriendList() {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("uid", userid);
		http.post(ChooseFriendToAddQun.this, ConstantsUrl.getFriendList,
				params, new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						Gson mGson = new Gson();
						mGetFriendBean = mGson.fromJson(arg2,
								GetFriendBean.class);
						Message msg = mHandler.obtainMessage();
						msg.what = CREATEFRIENDGETFRIENDLIST;
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
			mChecked = new ArrayList<Boolean>();
			for (int i = 0; i < mGetFriendBean.data.size(); i++) {//
				// 遍历且设置CheckBox默认状态为未选中
				mChecked.add(false);
				System.out.println(mChecked.size());

			}
		}

		@Override
		public int getCount() {
			return mGetFriendBean.data.size();
		}

		@Override
		public Object getItem(int position) {
			return mGetFriendBean.data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getViewTypeCount() {// 这里需要返回需要集中布局类型，总大小为类型的种数的下标
			return VIEW_TYPE;
		}

		@SuppressWarnings("deprecation")
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				final int p = position;
				convertView = inflater.inflate(
						R.layout.contact_list_item_create_group, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView
						.findViewById(R.id.create_group_item_name);
				holder.img = (ImageView) convertView
						.findViewById(R.id.create_group_item_headimg);

				holder.create_group_item_cb = (CheckBox) convertView
						.findViewById(R.id.create_group_item_cb);
				holder.create_group_item_cb
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								CheckBox cb = (CheckBox) v;
								System.out.println("ppppppppppppppppppppp" + p);
								if (cb.isChecked()) {
									mChecked.set(p, cb.isChecked());// 设置CheckBox为选中状态

									String fid2 = mGetFriendBean.data.get(p)
											.getFid();
									System.out.println(fid2);
									System.out.println(cb.isChecked());
								} else {
									mChecked.set(p, cb.isChecked());// 设置CheckBox为未选中状态
									System.out.println(cb.isChecked());
								}
							}
						});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(
					mNameHeadImg.get(nameList.get(position).toString().trim()),
					holder.img);
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
			holder.create_group_item_cb.setChecked(false);

			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母
			TextView name; // 名字
			ImageView img; // 图片
			CheckBox create_group_item_cb;
		}
	}

	// 获得汉语拼音首字母
	@SuppressLint("DefaultLocale")
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

}
