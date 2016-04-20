package com.lst.lstjx.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lst.lstjx.activity.AddFriendPersonalDetail;
import com.lst.lstjx.activity.ConversationListActivity;
import com.lst.lstjx.activity.FrameActivity;
import com.lst.lstjx.activity.GroupDetailActivity;
import com.lst.lstjx.activity.LoginActivity;
import com.lst.lstjx.activity.PersonalDetailActivity;
import com.lst.lstjx.activity.PhotoActivity;
import com.lst.lstjx.activity.SOSOLocationActivity;
import com.lst.lstjx.app.photo.PhotoCollectionsProvider;
import com.lst.lstjx.bean.PersonDetail;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.database.DBManager;
import com.lst.lstjx.database.UserInfos;
import com.lst.lstjx.database.UserInfosDao;
import com.lst.lstjx.message.DeAgreedFriendRequestMessage;
import com.lst.lstjx.model.Groups;
import com.lst.lstjx.model.User;
import com.lst.lstjx.provider.ContactsProvider;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;
import io.rong.imkit.PushNotificationManager;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有： 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements
		RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
		RongIM.UserInfoProvider, RongIM.GroupInfoProvider,
		RongIM.ConversationBehaviorListener,
		RongIMClient.ConnectionStatusListener, RongIM.LocationProvider,
		RongIMClient.OnReceivePushMessageListener,
		RongIM.ConversationListBehaviorListener, ApiCallback {

	private static final String TAG = RongCloudEvent.class.getSimpleName();
	public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
	private static RongCloudEvent mRongCloudInstance;
	private PersonDetail mPersonDetail;
	private Context mContext;
	private Group addGroup;
	private List<Group> mGroups = new ArrayList<Group>();
	private UserInfosDao mUserInfosDao;
	private AbstractHttpRequest<PersonDetail> getUserInfoByUserIdHttpRequest;
	private AbstractHttpRequest<PersonDetail> getFriendByUserIdHttpRequest;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123:

				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化 RongCloud.
	 * 
	 * @param context
	 *            上下文。
	 */
	public static void init(Context context) {

		if (mRongCloudInstance == null) {

			synchronized (RongCloudEvent.class) {

				if (mRongCloudInstance == null) {
					mRongCloudInstance = new RongCloudEvent(context);
				}
			}
		}
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            上下文。
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();

	}

	/**
	 * RongIM.init(this) 后直接可注册的Listener。
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// 设置用户信息提供者。
		RongIM.setGroupInfoProvider(this, false);// 设置群组信息提供者。
		RongIM.setConversationBehaviorListener(this);// 设置会话界面操作的监听器。
		RongIM.setLocationProvider(this);// 设置地理位置提供者,不用位置的同学可以注掉此行代码
		RongIM.setConversationListBehaviorListener(this);
		RongIM.getInstance().setMessageAttachedUserInfo(true);

		// RongIM.setPushMessageBehaviorListener(this);//自定义 push 通知。
	}

	/*
	 * 连接成功注册。 <p/> 在RongIM-connect-onSuccess后调用。
	 */
	@SuppressWarnings("static-access")
	public void setOtherListener() {
		RongIM.getInstance().getRongIMClient()
				.setOnReceiveMessageListener(this);// 设置消息接收监听器。
		RongIM.getInstance().setSendMessageListener(this);// 设置发出消息接收监听器.
		RongIM.getInstance().getRongIMClient()
				.setConnectionStatusListener(this);// 设置连接状态监听器。

		// //扩展功能自定义
		InputProvider.ExtendProvider[] provider = {
				new PhotoCollectionsProvider(RongContext.getInstance()),// 图片
				new CameraInputProvider(RongContext.getInstance()),// 相机
				new LocationInputProvider(RongContext.getInstance()),// 地理位置
				new VoIPInputProvider(RongContext.getInstance()),// 语音通话
				new ContactsProvider(RongContext.getInstance()) // 通讯录
		};
		InputProvider.ExtendProvider[] provider1 = {
				new PhotoCollectionsProvider(RongContext.getInstance()),// 图片
				new CameraInputProvider(RongContext.getInstance()),// 相机
				new LocationInputProvider(RongContext.getInstance()),// 地理位置
		};
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.PRIVATE, provider);
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.DISCUSSION, provider1);
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.GROUP, provider1);
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.CUSTOMER_SERVICE, provider1);
		// RongIM.getInstance().setPrimaryInputProvider(new
		// InputTestProvider((RongContext) mContext));

	}

	/**
	 * 自定义 push 通知。
	 * 
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public boolean onReceivePushMessage(PushNotificationMessage msg) {
		Log.d(TAG, "onReceived-onPushMessageArrive:" + msg.getContent());

		PushNotificationManager.getInstance().onReceivePush(msg);

		Intent intent = new Intent();
		Uri uri;

		intent.setAction(Intent.ACTION_VIEW);

		Conversation.ConversationType conversationType = msg
				.getConversationType();

		uri = Uri.parse("rong://" + RongContext.getInstance().getPackageName())
				.buildUpon().appendPath("conversationlist").build();
		intent.setData(uri);
		Log.d(TAG, "onPushMessageArrive-url:" + uri.toString());

		Notification notification = null;

		PendingIntent pendingIntent = PendingIntent.getActivity(
				RongContext.getInstance(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		if (android.os.Build.VERSION.SDK_INT < 11) {
			notification = new Notification(RongContext.getInstance()
					.getApplicationInfo().icon, "自定义 notification",
					System.currentTimeMillis());

			notification.setLatestEventInfo(RongContext.getInstance(),
					"自定义 title", "这是 Content:" + msg.getObjectName(),
					pendingIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
		} else {

			notification = new Notification.Builder(RongContext.getInstance())
					.setLargeIcon(getAppIcon())
					.setSmallIcon(R.drawable.ic_launcher)
					.setTicker("自定义 notification").setContentTitle("自定义 title")
					.setContentText("这是 Content:" + msg.getObjectName())
					.setContentIntent(pendingIntent).setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_ALL).build();

		}

		NotificationManager nm = (NotificationManager) RongContext
				.getInstance().getSystemService(
						RongContext.getInstance().NOTIFICATION_SERVICE);

		nm.notify(0, notification);

		return true;
	}

	private Bitmap getAppIcon() {
		BitmapDrawable bitmapDrawable;
		Bitmap appIcon;
		bitmapDrawable = (BitmapDrawable) RongContext.getInstance()
				.getApplicationInfo()
				.loadIcon(RongContext.getInstance().getPackageManager());
		appIcon = bitmapDrawable.getBitmap();
		return appIcon;
	}

	/**
	 * 获取RongCloud 实例。
	 * 
	 * @return RongCloud。
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
	 * 
	 * @param message
	 *            接收到的消息的实体信息。
	 * @param left
	 *            剩余未拉取消息数目。
	 */
	@Override
	public boolean onReceived(Message message, int left) {

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
			if (textMessage.getContent().equals("11")) {
				Log.e(TAG, "---onReceived--111111--");
				return false;
			}
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onReceived-RichContentMessage:"
							+ richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// 小灰条消息
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-informationNotificationMessage:"
					+ informationNotificationMessage.getMessage());
			if (DemoContext.getInstance() != null)
				getFriendByUserIdHttpRequest = DemoContext
						.getInstance()
						.getDemoApi()
						.getUserInfoByUserId(message.getSenderUserId(),
								(ApiCallback<PersonDetail>) this);
		} else if (messageContent instanceof DeAgreedFriendRequestMessage) {// 好友添加成功消息
			DeAgreedFriendRequestMessage deAgreedFriendRequestMessage = (DeAgreedFriendRequestMessage) messageContent;
			Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:"
					+ deAgreedFriendRequestMessage.getMessage());
			receiveAgreeSuccess(deAgreedFriendRequestMessage);
		} else if (messageContent instanceof ContactNotificationMessage) {// 好友添加消息
			ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;"
					+ contactContentMessage.getExtra());
			Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:"
					+ contactContentMessage.getMessage().toString());
			Intent in = new Intent();
			in.setAction(FrameActivity.ACTION_DMEO_RECEIVE_MESSAGE);
			in.putExtra("rongCloud", contactContentMessage);
			in.putExtra("has_message", true);
			mContext.sendBroadcast(in);
		} else {
			Log.d(TAG, "onReceived-其他消息，自己来判断处理");
		}

		return false;

	}

	/**
	 * @param deAgreedFriendRequestMessage
	 */
	private void receiveAgreeSuccess(
			DeAgreedFriendRequestMessage deAgreedFriendRequestMessage) {
		if (DemoContext.getInstance() != null) {
			if (deAgreedFriendRequestMessage.getUserInfo() != null) {
				if (DemoContext.getInstance().hasUserId(
						deAgreedFriendRequestMessage.getUserInfo().getUserId())) {
					DemoContext.getInstance().updateUserInfos(
							deAgreedFriendRequestMessage.getUserInfo()
									.getUserId(), "1");
				} else {
					DemoContext.getInstance().insertOrReplaceUserInfo(
							deAgreedFriendRequestMessage.getUserInfo(), "1");
				}

			}
		}
		Intent in = new Intent();
		in.setAction(FrameActivity.ACTION_DMEO_AGREE_REQUEST);
		in.putExtra("AGREE_REQUEST", true);
		mContext.sendBroadcast(in);

	}

	@Override
	public Message onSend(Message message) {
		return message;
	}

	/**
	 * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
	 * 
	 * @param message
	 *            消息。
	 */
	@Override
	public boolean onSent(Message message,
			RongIM.SentMessageErrorCode sentMessageErrorCode) {

		if (message.getSentStatus() == Message.SentStatus.FAILED) {

			if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {// 不在聊天室

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {// 不在讨论组

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {// 不在群组

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {// 你在他的黑名单中
				WinToast.toast(mContext, "你在对方的黑名单中");
			}
		}

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onSent-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onSent-RichContentMessage:"
							+ richContentMessage.getContent());
		} else {
			Log.d(TAG, "onSent-其他消息，自己来判断处理");
		}
		return false;
	}

	/**
	 * 用户信息的提供者 融云会话界面 和 会话列表的 头像 昵称展示
	 */

	@Override
	public UserInfo getUserInfo(final String userId) {

		// // 先从获取数据库操作的实例
		mUserInfosDao = DBManager.getInstance(mContext)

		.getDaoSession().getUserInfosDao();

		if (userId == null) {
			return null;
		}
		UserInfos userInfo = mUserInfosDao.queryBuilder()
				.where(UserInfosDao.Properties.Userid.eq(userId)).unique();

		if (userInfo == null && DemoContext.getInstance() != null) {
			requstUserInfoById(userId);
		}

		return DemoContext.getInstance().getUserInfoById(userId);

	}

	// /**
	// * 通过userid 查找 UserInfo，查找的是本地的数据库
	// *
	// * @param userId
	// * @return
	// */

	private void requstUserInfoById(final String userId) {
		// TODO Auto-generated method stub
		AsyncHttpClient http = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		System.out
				.println("++++++requstUserInfoById+++++++++++++++++++++++++++++++++"
						+ userId.toString());
		params.add("uid", userId);
		http.post(mContext, ConstantsUrl.get_users_info, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out
								.println("++++++requstUserInfoById+++++++++++++++++++arg2+++++++++++++++"
										+ arg2.toString());
						// mPersonDetail = new PersonDetail();
						Gson mGson = new Gson();
						mPersonDetail = mGson.fromJson(arg2.toString(),
								PersonDetail.class);
						UserInfos addFriend = new UserInfos();
						addFriend.setUsername(mPersonDetail.data.username);
						addFriend.setUserid(userId);
						addFriend.setPortrait(mPersonDetail.data.face);
						addFriend.setStatus("0");
						mUserInfosDao.insertOrReplace(addFriend);
						android.os.Message mes = mHandler.obtainMessage();
						mHandler.sendEmptyMessage(0x123);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
	 * 
	 * @param groupId
	 *            群组 Id.
	 * @return 群组信息，（注：由开发者提供群组信息）。
	 */
	@Override
	public Group getGroupInfo(String groupId) {
		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		if (DemoContext.getInstance().getGroupMap() == null) {

			 return null;
		}
		System.out.println("RongCloudEvent.getGroupMap().get(groupId)"
				+ DemoContext.getInstance().getGroupMap().get(groupId));
		return DemoContext.getInstance().getGroupMap().get(groupId);

	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param conversationType
	 *            会话类型。
	 * @param user
	 *            被点击的用户的信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onUserPortraitClick(Context context,
			Conversation.ConversationType conversationType, UserInfo user) {
		Log.e(TAG, "----onUserPortraitClick");

		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		if (user != null) {

			if (conversationType
					.equals(Conversation.ConversationType.PUBLIC_SERVICE)
					|| conversationType
							.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
				RongIM.getInstance().startPublicServiceProfile(mContext,
						conversationType, user.getUserId());
			} else if (conversationType
					.equals(Conversation.ConversationType.GROUP)) {
				Intent in = new Intent(context, GroupDetailActivity.class);
				in.putExtra("USER", user);
				in.putExtra("targetId", user.getUserId());
				in.putExtra("SEARCH_CONVERSATIONTYPE", conversationType);
				context.startActivity(in);
			} else {
				Intent in = new Intent(context, PersonalDetailActivity.class);
				in.putExtra("USER", user);
				in.putExtra("SEARCH_USERID", user.getUserId());
				in.putExtra("SEARCH_CONVERSATIONTYPE", conversationType);
				context.startActivity(in);

			}
		}

		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context context,
			Conversation.ConversationType conversationType, UserInfo userInfo) {
		Log.e(TAG, "----onUserPortraitLongClick");

		return true;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param message
	 *            被点击的消息的实体信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onMessageClick(Context context, View view, Message message) {
		Log.e(TAG, "----onMessageClick");

		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		if (message.getContent() instanceof LocationMessage) {
			Intent intent = new Intent(context, SOSOLocationActivity.class);
			intent.putExtra("location", message.getContent());
			context.startActivity(intent);
		} else if (message.getContent() instanceof RichContentMessage) {
			RichContentMessage mRichContentMessage = (RichContentMessage) message
					.getContent();
			Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());
		} else if (message.getContent() instanceof ImageMessage) {
			ImageMessage imageMessage = (ImageMessage) message.getContent();
			Intent intent = new Intent(context, PhotoActivity.class);

			intent.putExtra(
					"photo",
					imageMessage.getLocalUri() == null ? imageMessage
							.getRemoteUri() : imageMessage.getLocalUri());
			if (imageMessage.getThumUri() != null)
				intent.putExtra("thumbnail", imageMessage.getThumUri());

			context.startActivity(intent);
		}

		Log.d("Begavior",
				message.getObjectName() + ":" + message.getMessageId());

		return false;
	}

	@Override
	public boolean onMessageLongClick(Context context, View view,
			Message message) {

		Log.e(TAG, "----___________________________________onMessageLongClick");
		return false;
	}

	/**
	 * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
	 * 
	 * @param status
	 *            网络状态。
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		Log.d(TAG, "onChanged:" + status);
		if (status.getMessage().equals(
				ConnectionStatus.DISCONNECTED.getMessage())) {
		}
	}

	/**
	 * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
	 * 
	 * @param context
	 *            上下文
	 * @param callback
	 *            回调
	 */
	@Override
	public void onStartLocation(Context context, LocationCallback callback) {
		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		DemoContext.getInstance().setLastLocationCallback(callback);
		context.startActivity(new Intent(context, SOSOLocationActivity.class));// SOSO地图
	}

	/**
	 * 点击会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conversation) {
		MessageContent messageContent = conversation.getMessageContent();

		if (messageContent instanceof TextMessage) {// 文本消息

			TextMessage textMessage = (TextMessage) messageContent;
		} else if (messageContent instanceof ContactNotificationMessage) {
			Log.e(TAG, "---onConversationClick--ContactNotificationMessage-");
			Intent intent = new Intent(context, AddFriendPersonalDetail.class);
			System.out
					.println("____________________conversation.getConversationSenderId()______"
							+ conversation.getConversationSenderId().toString());
			intent.putExtra("userid", conversation.getConversationSenderId());
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	/**
	 * 长按会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            长按会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationLongClick(Context context, View view,
			UIConversation conversation) {
		return false;
	}

	@Override
	public void onComplete(AbstractHttpRequest abstractHttpRequest, Object obj) {
		if (getUserInfoByUserIdHttpRequest != null
				&& getUserInfoByUserIdHttpRequest.equals(abstractHttpRequest)) {
			if (obj instanceof User) {
				final User user = (User) obj;
				if (user.getCode() == 200) {
					UserInfos addFriend = new UserInfos();
					addFriend.setUsername(user.getResult().getUsername());
					addFriend.setUserid(user.getResult().getId());
					addFriend.setPortrait(user.getResult().getPortrait());
					addFriend.setStatus("0");
					mUserInfosDao.insertOrReplace(addFriend);
				}
			}
		} else if (getFriendByUserIdHttpRequest != null
				&& getFriendByUserIdHttpRequest.equals(abstractHttpRequest)) {
			Log.e(TAG, "-------hasUserId----000000-------");
			if (obj instanceof User) {
				final User user = (User) obj;
				Log.e(TAG, "-------hasUserId------11111111-----");
				if (user.getCode() == 200) {
					Log.e(TAG, "-------hasUserId------2222222-----");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (DemoContext.getInstance() != null) {

								Log.e(TAG,
										"-------hasUserId--------is what---"
												+ DemoContext
														.getInstance()
														.hasUserId(
																user.getResult()
																		.getId()));
								if (DemoContext.getInstance().hasUserId(
										user.getResult().getId())) {
									Log.e(TAG, "-------hasUserId-----------");
									DemoContext.getInstance().updateUserInfos(
											user.getResult().getId(), "1");
								} else {
									Log.e(TAG, "-------hasUserId---no--------");
									UserInfo info = new UserInfo(user
											.getResult().getId(), user
											.getResult().getUsername(), Uri
											.parse(user.getResult()
													.getPortrait()));
									DemoContext.getInstance()
											.insertOrReplaceUserInfo(info, "1");
								}
							}
						}
					});

				}
			}
		}
	}

	@Override
	public void onFailure(AbstractHttpRequest abstractHttpRequest,
			BaseException e) {

	}

	@Override
	public boolean onMessageLinkClick(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
