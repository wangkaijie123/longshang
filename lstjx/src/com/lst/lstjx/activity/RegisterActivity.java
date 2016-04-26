package com.lst.lstjx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import com.lst.lstjx.app.DemoContext;
import com.lst.lstjx.bean.LoginBean;
import com.lst.lstjx.bean.UserInfor;
import com.lst.lstjx.common.ConstantsUrl;
import com.lst.lstjx.model.Status;
import com.lst.lstjx.utils.CommonUtils;
import com.lst.lstjx.utils.NetUtils;
import com.lst.lstjx.utils.SharePrefUtil;
import com.lst.lstjx.view.EditTextHolder;
import com.lst.lstjx.view.WinToast;
import com.lst.yuewo.R;
import com.sea_monster.exception.BaseException;
import com.sea_monster.exception.InternalException;
import com.sea_monster.network.AbstractHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Bob on 2015/2/6.
 */
public class RegisterActivity extends BaseApiActivity implements View.OnClickListener,EditTextHolder.OnEditTextFocusChangeListener,Handler.Callback {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int HANDLER_REGIST_HAS_NO_FOCUS = 1;
    private static final int HANDLER_REGIST_HAS_FOCUS = 2;
    //注册成功
	private static final int REGIST_SUCCESS = 0x00056;
	 //注册成功 
	private int registCode;
    /**
     * 注册邮箱
     */
    private EditText mRegistEmail;
    /**
     * 密码
     */
    private EditText mRegistPassword;
    /**
     * 昵称
     */
    private EditText mRegistNickName;
    /**
     * 注册button
     */
    private Button mRegisteButton;
    /**
     * 用户协议
     */
    private TextView mRegisteUserAgreement;
    /**
     * 输入邮箱删除按钮
     */
    private FrameLayout mEmailDeleteFramelayout;
    /**
     * 输入密码删除按钮
     */
    private FrameLayout mPasswordDeleteFramelayout;
    /**
     * 输入昵称删除按钮
     */
    private FrameLayout mNickNameDeleteFramelayout;
    /**
     * 提示消息
     */
    private TextView mRegistReminder;
    /**
     * logo
     */
    private ImageView mLogoImg;
    /**
     * 左侧title
     */
    private TextView mLeftTitle;
    /**
     * 右侧title
     */
    private TextView mRightTitle;
    /**
     * backgroud
     */
    private ImageView mImgBackgroud;
    EditTextHolder mEditUserNameEt;
    EditTextHolder mEditPassWordEt;
    EditTextHolder mEditNickNameEt;
    private Handler mHandler;
    /**
     * 软键盘的控制
     */
    private InputMethodManager mSoftManager;
    private AbstractHttpRequest<Status> httpRequest;
    /**
     * 是否展示title
     */
    private RelativeLayout mIsShowTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);
        initView();
        initData();
    }

    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏ActionBar
        mRegistEmail = (EditText) findViewById(R.id.et_register_mail);
        mRegistPassword = (EditText) findViewById(R.id.et_register_password);
        mRegistNickName = (EditText) findViewById(R.id.et_register_nickname);
        mRegisteUserAgreement = (TextView) findViewById(R.id.register_user_agreement);
        mRegisteButton = (Button) findViewById(R.id.register_agree_button);
      
//        mLogoImg = (ImageView) findViewById(R.id.de_logo);
        mLeftTitle = (TextView) findViewById(R.id.de_left);
        mRightTitle = (TextView) findViewById(R.id.de_right);
        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
        mIsShowTitle = (RelativeLayout) findViewById(R.id.de_merge_rel);
        mEmailDeleteFramelayout = (FrameLayout) findViewById(R.id.et_register_delete);
        mPasswordDeleteFramelayout = (FrameLayout) findViewById(R.id.et_password_delete);
        mNickNameDeleteFramelayout = (FrameLayout) findViewById(R.id.et_nickname_delete);
        mHandler = new Handler(this);
        mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        });
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        event.getKeyCode();
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                Message mess = Message.obtain();
                mess.what = HANDLER_REGIST_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
                break;
        }
        return super.dispatchKeyEvent(event);
    }
    protected void initData() {
        mRegisteButton.setOnClickListener(this);
        mRegisteUserAgreement.setOnClickListener(this);
        mLeftTitle.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mEditUserNameEt = new EditTextHolder(mRegistEmail, mEmailDeleteFramelayout, null);
        mEditNickNameEt = new EditTextHolder(mRegistNickName, mNickNameDeleteFramelayout, null);
        mEditPassWordEt = new EditTextHolder(mRegistPassword, mPasswordDeleteFramelayout, null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRegistEmail.setOnClickListener(RegisterActivity.this);
                mRegistNickName.setOnClickListener(RegisterActivity.this);
                mRegistPassword.setOnClickListener(RegisterActivity.this);
                mEditUserNameEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
                mEditNickNameEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
                mEditPassWordEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
            }
        }, 200);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mSoftManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Message mess = Message.obtain();
                mess.what = HANDLER_REGIST_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case HANDLER_REGIST_HAS_NO_FOCUS:
                mIsShowTitle.setVisibility(View.GONE);
//                mRegistReminder.setVisibility(View.VISIBLE);
//                mLogoImg.setVisibility(View.VISIBLE);
                break;
            case HANDLER_REGIST_HAS_FOCUS:
//                mLogoImg.setVisibility(View.GONE);
//                mRegistReminder.setVisibility(View.GONE);
                mIsShowTitle.setVisibility(View.VISIBLE);
                mLeftTitle.setText(R.string.app_sign_in);
                mRightTitle.setText(R.string.app_fogot_password);
                break;
                //注册成功
            case REGIST_SUCCESS:
            	if (registCode ==0000) {
					Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
					finish();
				}else if (registCode == 1002) {
					Toast.makeText(RegisterActivity.this, "邮箱已被注册", Toast.LENGTH_SHORT).show();
				}else if (registCode == 1003) {
					Toast.makeText(RegisterActivity.this, "用户名已被注册", Toast.LENGTH_SHORT).show();
				}
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_agree_button://注册button
                String email = mRegistEmail.getText().toString();
                String password = mRegistPassword.getText().toString();
                String phone = "123";
                String nickName = mRegistNickName.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nickName)) {
                    WinToast.toast(this, R.string.register_is_null);
                    return;
                } else if (!CommonUtils.isEmail(email)) {
                    WinToast.toast(this, R.string.register_email_error);
                    return;
                }else if (password.length()<6) {
					Toast.makeText(RegisterActivity.this, "密码不能少于六位", Toast.LENGTH_SHORT).show();
					 return;
                }
              
           
//                if (DemoContext.getInstance() != null)
//                   httpRequest = DemoContext.getInstance().getDemoApi().register(email, nickName, phone, password, this);
               //注册
                goRegist(email,password,nickName);

                break;
            case R.id.register_user_agreement://用户协议

                break;
            case R.id.et_register_mail:
            case R.id.et_register_password:
            case R.id.et_register_nickname:
                Message mess = Message.obtain();
                mess.what = HANDLER_REGIST_HAS_FOCUS;
                mHandler.sendMessage(mess);
                break;

            case R.id.de_left://登录
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.de_right://忘记密码
                WinToast.toast(this,"忘记密码");
                break;
        }
    }



    private void goRegist(String email, String password, String nickName) {
		// TODO Auto-generated method stub
    	RequestParams params = new RequestParams();
		params.add("email", email);
		params.add("userpwd", password);
		params.add("username", nickName);
		AsyncHttpClient http = new AsyncHttpClient();
		http.post(RegisterActivity.this,ConstantsUrl.registUrl, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
			System.out.println("_____________________regist"+arg2);
				JSONObject obj;
			
				try {
					obj = new JSONObject(arg2.toString());
			registCode  =obj.getInt("code"); 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage();
				msg.what = REGIST_SUCCESS;
				msg.obj = registCode;
				mHandler.sendMessage(msg);
			
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				System.out.println("_____________________regist_onFailure"+arg0+"111111"+arg2);
			}
		});
	}

	private void fillData() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Map<String, String> requestParameter = new HashMap<String, String>();
                requestParameter.put("", "");
                String result = NetUtils.sendPostRequest("", requestParameter);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                Log.e(TAG,"--------onPostExecute-----+"+result);
            }
        }.execute();
    }
    protected void onPause() {
        super.onPause();
        if (mSoftManager == null) {
            mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getCurrentFocus() != null) {
            mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);// 隐藏软键盘
        }
    }

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
        if (httpRequest == request) {

            if (obj instanceof Status) {
                Status status = (Status) obj;
                Gson gson = new Gson();
                if (status.getCode() == 200) {
                    WinToast.toast(this, R.string.register_success);

                    Intent intent = new Intent();
                    intent.putExtra(LoginActivity.INTENT_IMAIL, mRegistEmail.getText().toString());
                    intent.putExtra(LoginActivity.INTENT_PASSWORD, mRegistPassword.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else if (status.getCode() == 101) {
                    WinToast.toast(this, R.string.register_user_exits);
                } else {
                    WinToast.toast(this, R.string.register_failure);
                }
            }
        }
    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        Log.d(TAG + "--onCallApiFailure:", "onCallApiFailure");

        if (httpRequest == request) {

            if (e instanceof InternalException) {
                InternalException ie = (InternalException) e;

                Log.e(TAG, "------------------ RegisterActivity---------------:" + ie.getMessage());
                if (ie.getCode() == 403) {
                    WinToast.toast(this, R.string.register_user_exits);
                }
            }
        }
    }

    /**
     * 本地判断确认密码是否合法
     *
     * @return
     */
    private boolean isUserPasswordRepeateLegal() {
        String strRegisterPassWordRepeat = mRegistPassword.getText()
                .toString();
        if (TextUtils.isEmpty(strRegisterPassWordRepeat)) {
            return false;
        }
        int lenP = strRegisterPassWordRepeat.length();
        if (lenP < 6) {
            return false;
        }
        if (!TextUtils.isEmpty(strRegisterPassWordRepeat)
                && !mRegistPassword.equals(strRegisterPassWordRepeat)) {
            return false;
        }
        return true;
    }

    @Override
    public void onEditTextFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {

            case R.id.et_register_mail:
            case R.id.et_register_password:
            case R.id.et_register_nickname:
                Message mess = Message.obtain();
                if (hasFocus) {
                    mess.what = HANDLER_REGIST_HAS_FOCUS;
                }
                mHandler.sendMessage(mess);
                break;
        }
    }
}
