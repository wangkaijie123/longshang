package com.lst.lstjx.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.SOSOLocationActivity;
import com.lst.lstjx.activity.SplashActivity;
import com.lst.lstjx.bean.GetGroupBean;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.common.DemoApi;
import com.lst.lstjx.database.DBManager;
import com.lst.lstjx.database.GroupInfos;
import com.lst.lstjx.database.GroupInfosDao;
import com.lst.lstjx.database.UserInfos;
import com.lst.lstjx.database.UserInfosDao;
import com.lst.lstjx.utils.SharePrefUtil;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Bob on 2015/1/30.
 */
public class DemoContext {

    private static DemoContext mDemoContext;
    public Context mContext;
    private DemoApi mDemoApi;
    private HashMap<String, Group> groupMap;
    private SharedPreferences mPreferences;
    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;
    private UserInfosDao mUserInfosDao;


    public static DemoContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new DemoContext();
        }
        return mDemoContext;
    }

    private DemoContext() {
    }

    private DemoContext(Context context) {
        mContext = context;
        mDemoContext = this;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        RongIM.setLocationProvider(new LocationProvider());

        mDemoApi = new DemoApi(context);
        getGroupList();
        mUserInfosDao = DBManager.getInstance(mContext).getDaoSession().getUserInfosDao();
    }
    private void getGroupList() {
		// TODO Auto-generated method stub

		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("masterid", SharePrefUtil.getString(
				mContext, "userId", ""));
		http.post(mContext, ConstantsUrl.srGroup, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println("0000Demo00000000" + arg2);
						try {
							JSONObject json = new JSONObject(arg2.toString());
							JSONArray data = json.getJSONArray("data");
							HashMap<String, Group> hashmap = new HashMap<String, Group>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject udata = (JSONObject) data.get(i);
								Group addGroup = new Group(udata
										.getString("groupid"), udata
										.getString("groupname"), null);
								hashmap.put(udata.getString("groupid"),
										addGroup);
								DemoContext.getInstance().setGroupMap(hashmap);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						System.out.println("请求群组信息失败————+——+");
					}
				});

	}

    public static void init(Context context) {
        mDemoContext = new DemoContext(context);
        
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }


    public void setGroupMap(HashMap<String, Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }


    public DemoApi getDemoApi() {
        return mDemoApi;
    }

    /**
     * 删除 userinfos 表
     */
    public void deleteUserInfos() {

        mUserInfosDao.deleteAll();
    }

    /**
     * 更新 好友信息
     *
     * @param targetid
     * @param status
     */
    public void updateUserInfos(String targetid, String status) {

        UserInfos userInfos = mUserInfosDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(targetid)).unique();
        userInfos.setStatus(status);
        userInfos.setUsername(userInfos.getUsername());
        userInfos.setPortrait(userInfos.getPortrait());
        userInfos.setUserid(userInfos.getUserid());

        mUserInfosDao.update(userInfos);

    }
   

    /**
     * 向数据库插入数据
     *
     * @param info   用户信息
     * @param status 状态
     */
    public void insertOrReplaceUserInfo(UserInfo info, String status) {

        UserInfos userInfos = new UserInfos();
        userInfos.setStatus(status);
        userInfos.setUsername(info.getName());
        userInfos.setPortrait(String.valueOf(info.getPortraitUri()));
        userInfos.setUserid(info.getUserId());
        mUserInfosDao.insertOrReplace(userInfos);
    }

    /**
     * 通过userid 查找 UserInfos,判断是否为好友，查找的是本地的数据库
     *
     * @param userId
     * @return
     */
    public boolean searcheUserInfosById(String userId) {
        if (userId != null) {

            UserInfos userInfos = mUserInfosDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();

            if (userInfos == null)
                return false;

            if (userInfos.getStatus().equals("1") || userInfos.getStatus().equals("3") || userInfos.getStatus().equals("5")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过userid 查找 UserInfo，查找的是本地的数据库
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfoById(String userId) {

        if (userId == null)
            return null;
        UserInfos userInfos = mUserInfosDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();
        if (userInfos == null && DemoContext.getInstance() != null) {
            return null;
        }


        return new UserInfo(userInfos.getUserid(), userInfos.getUsername(), Uri.parse(userInfos.getPortrait()));
    }
  

    public boolean hasUserId(String userId) {

        if (userId != null) {

            UserInfos userInfos = mUserInfosDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();

            if (userInfos == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据userids获得好友列表
     *
     * @return
     */
    public ArrayList<UserInfo> getUserInfoList(String[] userIds) {
    	
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        List<UserInfos> userInfosList = new ArrayList<UserInfos>();
        UserInfo userInfo;
        UserInfos userInfos;

        for (int i = 0; i < userIds.length; i++) {
            userInfos = mUserInfosDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userIds[i])).unique();
            userInfosList.add(userInfos);
            if (mUserInfosDao.getKey(userInfosList.get(i)) != null) {
                userInfo = new UserInfo(userInfosList.get(i).getUserid(), userInfosList.get(i).getUsername(), Uri.parse(userInfosList.get(i).getPortrait()));
                userInfoList.add(userInfo);
            }
        }
        if (userInfosList == null)
            return null;


        return (ArrayList) userInfoList;
    }

    /**
     * 通过groupid 获得groupname
     *
     * @param groupid
     * @return
     */
    public String getGroupNameById(String groupid) {
        Group groupReturn = null;
        if (!TextUtils.isEmpty(groupid) && groupMap != null) {

            if (groupMap.containsKey(groupid)) {
                groupReturn = groupMap.get(groupid);
            } else
                return null;

        }
        if (groupReturn != null)
            return groupReturn.getName();
        else
            return null;
    }


    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }


    class LocationProvider implements RongIM.LocationProvider {

        /**
         * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
         *
         * @param context  上下文
         * @param callback 回调
         */
        @Override
        public void onStartLocation(Context context, RongIM.LocationProvider.LocationCallback callback) {
            /**
             * demo 代码  开发者需替换成自己的代码。
             */
            DemoContext.getInstance().setLastLocationCallback(callback);
            Intent intent = new Intent(context, SOSOLocationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);//SOSO地图
        }
    }

}
