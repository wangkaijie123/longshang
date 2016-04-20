package com.lst.lstjx.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * @author lst718-011
 *
 */
/**
 * @author lst718-011
 *
 */
public class HttpConnect
{

	/**
	 * 默认图片读取超时
	 */
	private final static int DEFAULT_BITMAP_TIMEOUT = 30 * 1000;

	/**
	 * 默认图片读取超时
	 */
	private final static int DEFAULT_TIMEOUT = 30 * 1000;

	/**
	 * http状态正常值
	 */
	private final static int HTTP_STATE_OK = 200;

	/**
	 * 默认编码
	 */
	public final static String ENCODING = "UTF-8";

	/**
	 * 缓冲大小
	 */
	private final static int BUFFER_SIZE = 1024 * 4;

	/**
	 * 从网络获取图片
	 * 
	 * @param 图片路径bitmapPath
	 * @param 超时时间timeout
	 * @return 返回bitmap
	 * @throws 网络错误或者解析图片为null的时候抛出Exception
	 */
	public static Bitmap getHttpBitmap(String bitmapPath, int timeout) throws Exception
	{
		// long time = System.currentTimeMillis();
		// 获取缓存
		Bitmap bitmap = ImageCacheUtils.getBitmapFormCache(bitmapPath);
		if (bitmap != null) return bitmap;

		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;// 使得内存可以被回收
		bfOptions.inTempStorage = new byte[1 * 1024]; // 临时存储

		InputStream is = null;
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL url = new URL(bitmapPath);
		conn = (HttpURLConnection) url.openConnection();
		if (timeout > 0)
		{
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
		}
		conn.setRequestProperty("Connection", "close");
		conn.connect();
		is = conn.getInputStream();
		url = null;
		if (conn.getResponseCode() == HTTP_STATE_OK)
		{
			bis = new BufferedInputStream(is, BUFFER_SIZE);
			int i = -1;
			byte buf[] = new byte[4 * 1024];
			while ((i = bis.read(buf)) != -1)
			{
				out.write(buf, 0, i);
			}
			byte imgData[] = out.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length, bfOptions);
		}
		try
		{
			if (is != null) is.close();
			if (bis != null) bis.close();
			if (out != null) out.close();
			if (conn != null) conn.disconnect();
			is = null;
			bis = null;
			out = null;
			conn = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (bitmap == null)
		{
			throw new Exception();
		}
		else
		{
			// 图片本地存储
			ImageCacheUtils.saveBitmapToCache(bitmapPath, bitmap);
			return bitmap;
		}
	}
	/**
	 * 默认超时时间读取图片的方法
	 * 
	 * @param bitmapPath
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getHttpBitmap(String bitmapPath) throws Exception
	{
		return getHttpBitmap(bitmapPath, DEFAULT_BITMAP_TIMEOUT);
	}

	public static BitmapDrawable getHttpDrawable(String bitmapPath) throws Exception
	{
		return new BitmapDrawable(getHttpBitmap(bitmapPath));
	}

	/**
	 * http post方法基于httpclient
	 * 
	 * @param url
	 *            请求的url
	 * @param list
	 *            参数列表
	 * @param headers
	 *            http头列表
	 * @param timeout
	 *            超时时间
	 * @return 返回字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postHttpString(String url, List<NameValuePair> list, List<Header> headers, int timeout)
			throws Exception
	{

		// // 打印访问的url
		//
		// // 打印参数键值对
		// if (list != null) {
		// for (NameValuePair pair : list) {
		// }
		// }
		//
		// // 打印头参数
		// if (headers != null) {
		// for (Header head : headers) {
		// }
		// }

		HttpClient httpclient = new DefaultHttpClient();

		// 设置超时时间,连接超时和读取超时
		if (timeout > 0)
		{
			httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
		}

		// 设置请求头参数,为null的时候不设置
		if (headers != null)
		{
			httpclient.getParams().setParameter("http.default-headers", headers);
		}
		HttpPost httppost = new HttpPost(url);

		// 设置编码格式
		if (list != null)
		{
			httppost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
		}

		// 请求服务器
		HttpResponse response = httpclient.execute(httppost);

		// 获取http状态码
		int statuscode = response.getStatusLine().getStatusCode();
		if (statuscode == HttpStatus.SC_OK)
		{
			return EntityUtils.toString(response.getEntity());
		}
		else
		{
			// http状态不正确,主动抛出异常
			throw new Exception(statuscode + "");
		}
	}

	/**
	 * http post方法基于httpclient,使用默认的http请求头
	 * 
	 * @param url
	 *            请求的url
	 * @param list
	 *            参数列表
	 * @param timeout
	 *            超时事件
	 * @return
	 * @throws Exception
	 * @author Administrator
	 * @time 2011-12-26
	 */
	public static String postHttpString(String url, List<NameValuePair> list, int timeout) throws Exception
	{
		return postHttpString(url, list, null, timeout);
	}

	/**
	 * http post方法基于httpclient,使用默认的http请求头,默认的超时事件
	 * 
	 * @param url
	 *            请求的url
	 * @param list
	 *            参数列表
	 * @return
	 * @throws Exception
	 * @author Administrator
	 * @time 2011-12-26
	 */
	public static String postHttpString(String url, List<NameValuePair> list) throws Exception
	{
		return postHttpString(url, list, null, DEFAULT_TIMEOUT);
	}

	/**
	 * http get方法基于httpclient返回String
	 * 
	 * @param url
	 *            请求url
	 * @param http头列表
	 * @param timeout
	 *            超时时间
	 * @return 返回字符串
	 * @throws Exception
	 */
	public static String getHttpString(String url, List<Header> headers, int timeout) throws Exception
	{
		String s = null;
		Log.d("test", "url:" + url);
		// 打印参数键值对
		HttpClient httpclient = new DefaultHttpClient();
		// 设置超时时间,连接超时和读取超时
		if (timeout > 0)
		{
			httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
		}
		// 设置请求头参数,为null的时候不设置
		if (headers != null)
		{
			httpclient.getParams().setParameter("http.default-headers", headers);
		}
		HttpGet httpget = new HttpGet(url);
		// 请求服务器
		HttpResponse response = httpclient.execute(httpget);
		// 获取http状态码
		int statuscode = response.getStatusLine().getStatusCode();
		if (statuscode == HttpStatus.SC_OK)
		{
			s = EntityUtils.toString(response.getEntity());
		}
		else
		{
			// http状态不正确,主动抛出异常
			throw new Exception(statuscode + "");
		}
		return s;
	}

	/**
	 * http get方法基于httpclient返回String，使用默认的请求头
	 * 
	 * @param url
	 *            请求url
	 * @param timeout
	 *            超时时间
	 * @return 返回字符串
	 * @throws Exception
	 */
	public static String getHttpString(String url, int timeout) throws Exception
	{
		return getHttpString(url, null, timeout);
	}

	/**
	 * http get方法基于httpclient返回String，使用默认的请求头，
	 * 
	 * @param url
	 *            请求url
	 * @param timeout
	 *            超时时间
	 * @return 返回字符串
	 * @throws Exception
	 */
	public static String getHttpString(String url) throws Exception
	{
		return getHttpString(url, null, DEFAULT_TIMEOUT);
	}

	public static byte[] readStream(InputStream inputStream) throws Exception
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1)
		{
			bout.write(buffer, 0, len);
		}
		bout.close();
		inputStream.close();

		return bout.toByteArray();
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param parmas
	 * @return
	 */
	public static String postHttpString(String url, Map<String, String> parmas)
	{
		String s = null;
		DefaultHttpClient client = new DefaultHttpClient();// http客户端
		// 设置超时时间,连接超时和读取超时
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		HttpPost httpPost = new HttpPost(url);
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

		if (parmas != null)
		{

			Set<String> keys = parmas.keySet();

			for (Iterator<String> i = keys.iterator(); i.hasNext();)
			{

				String key = (String) i.next();

				pairs.add(new BasicNameValuePair(key, parmas.get(key)));

			}

		}
		try
		{

			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");

			/*
			 * 
			 * 将POST数据放入HTTP请求
			 */

			httpPost.setEntity(p_entity);

			/*
			 * 
			 * 发出实际的HTTP POST请求
			 */

			HttpResponse response = client.execute(httpPost);

			// HttpEntity entity = response.getEntity();
			// 获取http状态码
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK)
			{
				s = EntityUtils.toString(response.getEntity());

			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		Log.d("test", s);
		return s;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param parmas
	 * @return
	 */
	public static void postLogHttpString(String url, Map<String, String> parmas)
	{
		String s = null;
		DefaultHttpClient client = new DefaultHttpClient();// http客户端
		// 设置超时时间,连接超时和读取超时
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		HttpPost httpPost = new HttpPost(url);
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();

		if (parmas != null)
		{

			Set<String> keys = parmas.keySet();

			for (Iterator<String> i = keys.iterator(); i.hasNext();)
			{

				String key = (String) i.next();

				pairs.add(new BasicNameValuePair(key, parmas.get(key)));

			}

		}
		try
		{

			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");

			/*
			 * 
			 * 将POST数据放入HTTP请求
			 */

			httpPost.setEntity(p_entity);

			/*
			 * 
			 * 发出实际的HTTP POST请求
			 */

			HttpResponse response = client.execute(httpPost);

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

}
