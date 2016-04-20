package com.lst.lstjx.common;

import android.content.Context;

import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.bean.PersonDetail;
import com.lst.lstjx.model.Friends;
import com.lst.lstjx.model.Groups;
import com.lst.lstjx.model.Status;
import com.lst.lstjx.model.User;
import com.lst.lstjx.parser.GsonParser;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;
import com.sea_monster.network.ApiReqeust;
import com.sea_monster.network.AuthType;
import com.sea_monster.network.BaseApi;
import com.sea_monster.network.NetworkManager;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * demo api 请求，需要设置cookie，否则会提示 “user not login”
 * 此处是 Demo 的接口，跟融云 SDK 没有关系，此处仅为示例代码，展示 App 的逻辑
 */
public class DemoApi extends BaseApi {
    //        private static String HOST = "http://119.254.110.241:80/";
    private static String   HOST = "http://webim.demo.rong.io/";
    private final static String DEMO_LOGIN_EMAIL = "email_login";
    private final static String DEMO_FRIENDS = "get_friend";
    private final static String DEMO_REQ = "reg";
    private final static String DEMO_UPDATE_PROFILE = "update_profile";
    private final static String DEMO_TOKEN = "token";
    private final static String DEMO_JOIN_GROUP = "join_group";
    private final static String DEMO_QUIT_GROUP = "quit_group";
    private final static String DEMO_GET_ALL_GROUP = "get_all_group";
    private final static String DEMO_GET_MY_GROUP = "get_my_group";
    private final static String DEMO_GET_GROUP = "get_group";
    private final static String DEMO_SEARCH_NAME = "seach_name";
    private final static String DEMO_GET_FRIEND = "get_friend";
    private final static String DEMO_REQUEST_FRIEND = "request_friend";
    private final static String DEMO_DELETE_FRIEND = "delete_friend";
    private final static String DEMO_PROCESS_REQUEST_FRIEND = "process_request_friend";
    private final static String DEMO_PROFILE = "profile";

    public DemoApi(Context context) {
        super(NetworkManager.getInstance(), context);
    }


    /**
     * 登录 demo server
     *
     * @param email
     * @param password
     * @param callback
     * 1 关羽  生产
     * 2，张飞 测试
     * @return
     */
    public AbstractHttpRequest<User> login(String email, String password, ApiCallback<User> callback) {


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
//        nameValuePairs.add(new BasicNameValuePair("env", "2"));

        ApiReqeust<User> apiReqeust = new DefaultApiReqeust<User>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_LOGIN_EMAIL), nameValuePairs, callback);
        AbstractHttpRequest<User> httpRequest = apiReqeust.obtainRequest(new GsonParser<User>(User.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);
        return httpRequest;
    }

    /**
     * demo server 注册新用户
     *
     * @param email
     * @param username
     * @param password
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Status> register(String email, String username, String mobile, String password, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_REQ),nameValuePairs,callback);
//        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_REQ), nameValuePairs, callback);

        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 注册新用户
     *
     * @param username
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Status> updateProfile(String username, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_UPDATE_PROFILE), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 登陆成功后获得token
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<User> getToken(ApiCallback<User> callback) {
        ApiReqeust<User> apiReqeust = new DefaultApiReqeust<User>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_TOKEN), callback);
        AbstractHttpRequest<User> httpRequest = apiReqeust.obtainRequest(new GsonParser<User>(User.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }


    /**
     * demo server 获取好友
     * 获取所有好友信息
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Friends> getFriends(ApiCallback<Friends> callback) {

        ApiReqeust<Friends> apiReqeust = new DefaultApiReqeust<Friends>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_FRIENDS), callback);
        AbstractHttpRequest<Friends> httpRequest = apiReqeust.obtainRequest(new GsonParser<Friends>(Friends.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 加入群组
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> joinGroup(String username, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", username + ""));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_JOIN_GROUP), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 退出群组
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> quitGroup(String username, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", username + ""));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_QUIT_GROUP), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }


    /**
     * demo server 获取所有群组列表
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Groups> getAllGroups(ApiCallback<Groups> callback) {

        ApiReqeust<Groups> apiReqeust = new DefaultApiReqeust<Groups>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_GET_ALL_GROUP), callback);
        AbstractHttpRequest<Groups> httpRequest = apiReqeust.obtainRequest(new GsonParser<Groups>(Groups.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 获取我的群组列表
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Groups> getMyGroups(ApiCallback<Groups> callback) {

        ApiReqeust<Groups> apiReqeust = new DefaultApiReqeust<Groups>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_GET_MY_GROUP), callback);
        AbstractHttpRequest<Groups> httpRequest = apiReqeust.obtainRequest(new GsonParser<Groups>(Groups.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 通过群组id 获取群组信息
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Groups> getMyGroupByGroupId(String groupid, ApiCallback<Groups> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", groupid ));

        ApiReqeust<Groups> apiReqeust = new DefaultApiReqeust<Groups>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_GET_GROUP), nameValuePairs, callback);
        AbstractHttpRequest<Groups> httpRequest = apiReqeust.obtainRequest(new GsonParser<Groups>(Groups.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 通过用户名搜索用户
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Friends> searchUserByUserName(String username, ApiCallback<Friends> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));

        ApiReqeust<Friends> apiReqeust = new DefaultApiReqeust<Friends>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_SEARCH_NAME), nameValuePairs, callback);
        AbstractHttpRequest<Friends> httpRequest = apiReqeust.obtainRequest(new GsonParser<Friends>(Friends.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 获取好友列表
     * 获取添加过的好友信息
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Friends> getNewFriendlist(ApiCallback<Friends> callback) {
        ApiReqeust<Friends> apiReqeust = new DefaultApiReqeust<Friends>(ApiReqeust.GET_METHOD, URI.create(HOST + DEMO_GET_FRIEND), callback);
        AbstractHttpRequest<Friends> httpRequest = apiReqeust.obtainRequest(new GsonParser<Friends>(Friends.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);
        return httpRequest;

    }


    /**
     * 发好友邀请
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<User> sendFriendInvite(String userid, String message, ApiCallback<User> callback) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", userid + ""));
        nameValuePairs.add(new BasicNameValuePair("message", message));

        ApiReqeust<User> apiReqeust = new DefaultApiReqeust<User>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_REQUEST_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<User> httpRequest = apiReqeust.obtainRequest(new GsonParser<User>(User.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }



    /**
     * demo server 删除好友
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> deletefriends(String id, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id + ""));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_DELETE_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 处理好友请求好友
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> processRequestFriend(String id, String isaccess, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id + ""));
        nameValuePairs.add(new BasicNameValuePair("is_access", isaccess));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HOST + DEMO_PROCESS_REQUEST_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class),  mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 根据userid 获得 userinfo
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<PersonDetail> getUserInfoByUserId(String userid,ApiCallback<PersonDetail> callback) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("uid", userid));

        ApiReqeust<PersonDetail> apiReqeust = new DefaultApiReqeust<PersonDetail>(ApiReqeust.GET_METHOD,URI.create(ConstantsUrl.get_users_info),nameValuePairs, callback);
        AbstractHttpRequest<PersonDetail> httpRequest = apiReqeust.obtainRequest(new GsonParser<PersonDetail>(PersonDetail.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    AuthType mAuthType = new AuthType() {

        @Override
        public void signRequest(HttpRequest httpRequest, List<NameValuePair> nameValuePairs) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
            if (DemoContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", null) != null) {
                httpRequest.addHeader("cookie", DemoContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", null));
            }

        }
    };
}
