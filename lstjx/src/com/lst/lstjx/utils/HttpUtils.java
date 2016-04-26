<<<<<<< HEAD
package com.lst.lstjx.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static String zhuanpost2(String url, List<String> keys,
			List<String> values) {
		String str = null;
		HttpClient hc = new DefaultHttpClient();
		HttpPost hp = new HttpPost(url);

		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		for (int i = 0; i < keys.size(); i++) {
			parameters.add(new BasicNameValuePair(keys.get(i), values.get(i)));
		}
		try {

			hp.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpResponse res = hc.execute(hp);
			str = EntityUtils.toString(res.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}

}
=======
package com.lst.lstjx.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static String zhuanpost2(String url, List<String> keys,
			List<String> values) {
		String str = null;
		HttpClient hc = new DefaultHttpClient();
		HttpPost hp = new HttpPost(url);

		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		for (int i = 0; i < keys.size(); i++) {
			parameters.add(new BasicNameValuePair(keys.get(i), values.get(i)));
		}
		try {

			hp.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpResponse res = hc.execute(hp);
			str = EntityUtils.toString(res.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
