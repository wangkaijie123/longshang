package com.lst.lstjx.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.lst.yuewo.R;

/**
 * author describe parameter return
 */
public class WebActivity extends Activity {
	
	protected static final int PRODEATAILS = 0X1;
	protected static final int RESULTADD = 0x2;
	private TextView pro_price, pro_name, pro_content, sale_count, stroe_num;
	private Button pro_cut, pro_add, add_shop_car;
	private EditText et_count;
	private ImageView pro_img;

	private int count;
	private String no;
	private String msg;
	private ProgressBar pb;
	private String result;
	private ListView lv_prodetails;
	private ScrollView pro_detail_mscro;
	private WebView mWebView;
	private ArrayList<String>	contentArr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.my_test); 
	        initView();
	        Intent intent = getIntent();
			result = intent.getStringExtra("url");
		
	        mWebView = (WebView) findViewById(R.id.webview);
	        pb = (ProgressBar) findViewById(R.id.pb);
	        //设置WebView属性，能够执行Javascript脚本  
	        mWebView.getSettings().setJavaScriptEnabled(true);  
	      //启动缓存
	        mWebView.getSettings().setSupportZoom(true);
	        mWebView.getSettings().setUseWideViewPort(true); 
	        mWebView.getSettings().setLoadWithOverviewMode(true);
	        mWebView.getSettings().setAppCacheEnabled(true);
	        //设置缓存模式
	        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	        mWebView.getSettings().setBuiltInZoomControls(true);
	        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
	        //加载需要显示的网页  
//	        mWebView.loadUrl(mUrl+"&id="+result);
	        mWebView.loadUrl(result);
	        //设置Web视图  
	        mWebView.setWebViewClient(new HelloWebViewClient ()); 
	        mWebView.setWebChromeClient(new WebClient() );		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		pro_add = (Button) findViewById(R.id.pro_add);
		pro_cut = (Button) findViewById(R.id.pro_cut);
		et_count = (EditText) findViewById(R.id.et_count);
		add_shop_car = (Button) findViewById(R.id.add_shop_car);
	}
	
	private class WebClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			pb.setProgress(newProgress);
			if(newProgress==100){
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
	}
	
	 private class HelloWebViewClient extends WebViewClient {  
	        @Override 
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
	            view.loadUrl(url);  
	            return true;  
	        }  	        
	    }  	
}
