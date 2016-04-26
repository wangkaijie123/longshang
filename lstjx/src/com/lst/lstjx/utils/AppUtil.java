package com.lst.lstjx.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;
	public class AppUtil {
		public static boolean Delfile(String file)
		{
			try {
				File f = new File(file);
				if (!f.exists()) {
					return false;
				}
				else {
				return	f.delete();
				}

			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		 
		}
		public static boolean fileIsExists(String fileurl) {
			try {
				File f = new File(fileurl);
				if (!f.exists()) {
					return false;
				}

			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
			return true;
		}

		/*测试是否上网
		 * */
	    public static byte[] read(InputStream in) throws IOException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len=0;
	        while ((len = in.read(buffer)) != -1)
	        {
	        	if (buffer==null) {
					break;
				}
	        	
	            baos.write(buffer, 0, len);
	            if (len!=buffer.length) {
					break;
				}
	        }
	        baos.close();
	        byte[] data = baos.toByteArray();
	        return data;
	    }
	  
		public static ProgressDialog showProgress(Activity activity, String hintText) {
			Activity mActivity = null;
			if (activity.getParent() != null) {
				mActivity = activity.getParent();
				if (mActivity.getParent() != null) {
					mActivity = mActivity.getParent();
				}
			} else {
				mActivity = activity;
			}
			final Activity finalActivity = mActivity;
			ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
			window.getWindow().setGravity(Gravity.CENTER);

			window.setCancelable(false);
			return window;
		}
		static public boolean isNetwork(Context context) {  
			      if (context != null) {  
			          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
			                  .getSystemService(Context.CONNECTIVITY_SERVICE);  
			          NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
			          if (mNetworkInfo != null) {  
			             return mNetworkInfo.isAvailable();  
			         }  
			     }  
			     return false;  
			 }  
//
//		/*得到json所有关键字是key的值
//		 * */
//		public static String[] getJsonObjectStringArray(JSONArray jsonArray, String key) {
//			String res[];
//			try {
//				res = new String[jsonArray.length()];
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject jsonObject2 = new JSONObject(jsonArray.getString(i));
//					res[i] = getJsonStringValue(jsonObject2, key);
//				}
//			} catch (Exception e) {
//				return new String[] {};
//			}
//			return res;
//		}
//		/*得到json所有关键字是key的值
//		 * */
//		public static int[] getJsonObjectIntegerArray(JSONArray jsonArray, String key) {
//			int res[];
//			try {
//				res = new int[jsonArray.length()];
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject jsonObject2 = new JSONObject(jsonArray.getString(i));
//					res[i] = getJsonIntegerValue(jsonObject2, key);
//				}
//			} catch (Exception e) {
//				return new int[] {};
//			}
//			return res;
//		}
         /* 得到json，第几inde的序列
          * */
		public static JSONObject getJsonObject(JSONArray jsonArray, int index) {
			try {
				if (jsonArray != null && index >= 0 && index < jsonArray.length()) {
					return jsonArray.getJSONObject(index);
				}
			} catch (JSONException e) {
				return null;
			}
			return null;
		}
//
//		public static String getArrayValue(String[] array, int index) {
//			if (array != null && index >= 0 && index < array.length) {
//				return array[index];
//			}
//			return "";
//		}
//
//		public static int getArrayValue(int[] array, int index) {
//			if (array != null && index >= 0 && index < array.length) {
//				return array[index];
//			}
//			return 0;
//		}
//
		public static String getJsonStringValue(JSONObject jsonObject, String key) {
			return getJsonStringValue(jsonObject, key, "");
		}

		public static String getJsonStringValue(JSONObject jsonObject, String key, String defaultValue) {
			try {
				if (jsonObject != null && jsonObject.has(key)) {
					String value = jsonObject.getString(key).trim();
					if (value.equals("null")) {
						value = "";
					}
					return value;
				}
			} catch (Exception e) {
				return defaultValue;
			}
			return defaultValue;
		}
//
		public static int getJsonIntegerValue(JSONObject json, String key) {
			return getJsonIntegerValue(json, key, 0);
		}
//
		public static int getJsonIntegerValue(JSONObject jsonObject, String key, int defaultValue) {
			try {
				if (jsonObject != null && jsonObject.has(key)) {
					return jsonObject.getInt(key);
				}
			} catch (Exception e) {
				return defaultValue;
			}
			return defaultValue;
		}
		//
		public static double getJsonDoubleValue(JSONObject json, String key) {
			return getJsonDoubleValue(json, key, 0);
		}
//
		public static double getJsonDoubleValue(JSONObject jsonObject, String key, int defaultValue) {
			try {
				if (jsonObject != null && jsonObject.has(key)) {
					return jsonObject.getDouble(key);
				}
			} catch (Exception e) {
				return defaultValue;
			}
			return defaultValue;
		}
//
//		public static Long getJsonLongValue(JSONObject json, String key) {
//			return getJsonLongValue(json, key, 0L);
//		}
//
//		public static Long getJsonLongValue(JSONObject jsonObject, String key, Long defaultValue) {
//			try {
//				if (jsonObject != null && jsonObject.has(key)) {
//					return jsonObject.getLong(key);
//				}
//			} catch (Exception e) {
//				return defaultValue;
//			}
//			return defaultValue;
//		}
//
//		public static float getJsonFloatValue(JSONObject jsonObject, String key, float defaultValue) {
//			try {
//				if (jsonObject != null && jsonObject.has(key)) {
//					return Float.valueOf(jsonObject.getString(key));
//				}
//			} catch (Exception e) {
//				return defaultValue;
//			}
//			return defaultValue;
//		}
//
		public static boolean getJsonBooleanValue(JSONObject jsonObject, String key, boolean defaultValue) {
			try {
				if (jsonObject != null && jsonObject.has(key)) {
					return jsonObject.getBoolean(key);
				}
			} catch (Exception e) {
				return defaultValue;
			}
			return defaultValue;
		}

		public static JSONObject getJsonObject(JSONObject jsonObject, String key) {
			try {
				if (jsonObject != null && jsonObject.has(key)) {
					return jsonObject.getJSONObject(key);
				}
			} catch (Exception e) {
				return new JSONObject();
			}
			return new JSONObject();
		}

		public static JSONArray getJsonArray(JSONObject jsonObject, String key) {
			try {
				if (jsonObject != null && jsonObject.has(key)) {
					return jsonObject.getJSONArray(key);
				}
			} catch (Exception e) {
			 
				return new JSONArray();
			}
			return new JSONArray();
		}
//
//		public static void removeDuplicate(ArrayList arrayList) {
//			HashSet h = new HashSet(arrayList);
//			arrayList.clear();
//			arrayList.addAll(h);
//		}
//
//		public static void removeDuplicateWithOrder(ArrayList arrayList) {
//			HashSet set = new HashSet();
//			List newList = new ArrayList();
//			for (Iterator iter = arrayList.iterator(); iter.hasNext();) {
//				Object element = iter.next();
//				if (!set.contains(element)) {
//					set.add(element);
//					newList.add(element);
//				}
//			}
//			arrayList.clear();
//			arrayList.addAll(newList);
//		}
//
//		public static JSONArray removeDuplicate(JSONArray jsonArray) {
//			HashSet set = new HashSet();
//			JSONArray newArray = new JSONArray();
//			for (int i = 0; i < jsonArray.length(); i++) {
//				try {
//					Object element = jsonArray.get(i);
//					if (!set.contains(element)) {
//						set.add(element);
//						newArray.put(element);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			return newArray;
//		}
//
//		public static boolean checkEmail(String email) {
//			try {
//				String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
//				Pattern regex = Pattern.compile(check);
//				Matcher matcher = regex.matcher(email);
//				boolean isMatched = matcher.matches();
//				if (isMatched) {
//					return true;
//				} else {
//					return false;
//				}
//			} catch (Exception e) {
//				return false;
//			}
//		}
//
//		public static boolean isMobilePhoneNumber(String number) {
//			String regx = "^(13[0-9]|15[0-9]|18[0-9]|14[5|7])\\d{8}$";
//			Pattern pattern = Pattern.compile(regx);
//			Matcher matcher = pattern.matcher(number);
//			return matcher.find();
//		}
//
		public static boolean isEmpty(String str) {
			return str == null || str.trim().length() == 0 || str.trim().equals("null");
		}

		public static boolean isNotEmpty(String str) {
			return !isEmpty(str);
		}
//
		public static void showLongMessage(Context mContext, CharSequence text) {
			if (text != null && text.length() > 0) {
				Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
			}
		}
//
		/*发布短提示消息*/
		public static void showShortMessage(Context mContext, CharSequence text) {
			if (text != null && text.length() > 0) {
				Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
			}
		}
//
//		/**
//		 * 
//		 * @param filePath
//		 * @return String
//		 */
//		public static String getFileContent(String filePath) throws IOException {
//			FileInputStream fis = new FileInputStream(filePath);
//			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
//			BufferedReader br = new BufferedReader(isr);
//			StringBuffer sbContent = new StringBuffer();
//			String sLine = "";
//
//			while ((sLine = br.readLine()) != null) {
//				String s = sLine.toString() + "\n";
//				sbContent = sbContent.append(s);
//			}
//
//			fis.close();
//			isr.close();
//			br.close();
//
//			return sbContent.toString();
//		}
//
//		public static CharSequence getTextFromHtml(String content) {
//			CharSequence text = "";
//			if (isNotEmpty(content)) {
//				text = Html.fromHtml(content);
//			}
//			return text;
//		}
//
//		public static Bitmap resizeBitmap(Bitmap bitmap, float desWidth, float desHeight) {
//			if (bitmap == null) {
//				return null;
//			}
//			int bmpWidth = bitmap.getWidth();
//			int bmpHeight = bitmap.getHeight();
//			if (bmpWidth > desWidth || bmpHeight > desHeight) {
//				Matrix matrix = new Matrix();
//
//				float scalFactor = Math.min(desWidth / bmpWidth, desHeight / bmpHeight);
//				matrix.postScale(scalFactor, scalFactor);
//
//				Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
//				bitmap.recycle();
//				bitmap = resizeBitmap;
//			}
//			return bitmap;
//		}
//
//		public static String TimeStampToString(Long timeStamp) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//			String date = sdf.format(new Date(timeStamp));
//			return date;
//		}
//
//		public static File bitmapToFile(Bitmap bitmap) {
//			try {
//				bitmap = resizeBitmap(bitmap, 500, 500);
//				File file = new File("/tmp.jpg");
//				FileOutputStream out = new FileOutputStream(file);
//				if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
//					out.flush();
//					out.close();
//				}
//				return file;
//			} catch (FileNotFoundException e) {
//				AppUtil.printLog("AppUtil", "Sorry, the file can not be created. " + e.getMessage());
//				return null;
//			} catch (IOException e) {
//				AppUtil.printLog("AppUtil", "IOException occurred when save upload file. " + e.getMessage());
//				return null;
//			}
//		}
//
//		/**
//		 * 获取圆形Bitmap
//		 * 
//		 * @param bitmap
//		 * @return
//		 */
//		public static Bitmap getCircleBitmap(Bitmap bitmap) {
//			if (bitmap == null) {
//				return null;
//			}
//			int height = bitmap.getHeight();
//			int width = bitmap.getWidth();
//			int diameter = width;
//			float center = diameter / 2f;
//			float dy = (height - diameter) / 2f;
//			float dx = 0;
//			if (height < width) {
//				diameter = height;
//				dy = 0;
//				dx = (width - diameter) / 2f;
//			}
//
//			Bitmap circleBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
//			Canvas c = new Canvas(circleBitmap);
//
//			int color = 0xffffffff;
//			Paint paint = new Paint();
//			paint.setColor(color);
//			paint.setAntiAlias(true);
//			c.drawARGB(0, 0, 0, 0);
//			c.drawCircle(center, center, center, paint);
//			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//			c.save();
//			c.translate(-dx, -dy);
//			c.drawBitmap(bitmap, 0, 0, paint);
//			c.restore();
//			return circleBitmap;
//		}
// 
// 
//	 
//	 
// 
//		public static String getStringEscapeHtml(String str) {
//			if (isNotEmpty(str)) {
//				str = str.replaceAll("<(img)[^>]+>", "[图片]");
//				str = str.replaceAll("\\<.*?>", "");
//			}
//			return str;
//		}
//
//		public static String subStringEscapeHtml(String str, int length) {
//			if (isNotEmpty(str)) {
//				str = getStringEscapeHtml(str);
//				if (str.length() > length)
//					str = str.substring(0, length) + " ...";
//			}
//			return str;
//		}
//
//		public static String escapeByTags(String s, String[] tags) {
//			for (String re : tags) {
//				Pattern p = Pattern
//						.compile("<[\\s]*?" + re + "[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?" + re + "[\\s]*?>", Pattern.CASE_INSENSITIVE);
//				Matcher m = p.matcher(s);
//				s = m.replaceAll("");
//			}
//			return s;
//		}
//
//		public static String fliterBR(String body) {
//			if (body != null) {
//				body = body.replaceAll("\n{2,}", "\n");
//				body = body.replace("<br />\n<br />", "<br />");
//				body = body.replace("<br /><br />", "<br />");
//				body = body.replace("<br />\n\r<br />", "<br />");
//				body = body.replace("<br />\r<br />", "<br />");
//				body = body.trim();
//				if (body.endsWith("<br />")) {
//					body = body.substring(0, body.lastIndexOf("<br />"));
//				}
//			} else {
//				body = "";
//			}
//			return body;
//		}
//
//		public static ProgressDialog showProgress(Activity activity, String hintText) {
//			Activity mActivity = null;
//			if (activity.getParent() != null) {
//				mActivity = activity.getParent();
//				if (mActivity.getParent() != null) {
//					mActivity = mActivity.getParent();
//				}
//			} else {
//				mActivity = activity;
//			}
//			final Activity finalActivity = mActivity;
//			ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
//			window.getWindow().setGravity(Gravity.CENTER);
//
//			window.setCancelable(false);
//			return window;
//		}
//
//		public static void openPhotoLibraryMenu(Activity activity) {
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			intent.setType("image/*");
//			activity.startActivityForResult(intent, 0);
//		}
//
//		public static void openImageCaptureMenu(Activity activity) {
//			File imageFile = new File("");
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//			intent.putExtra("outputFormat", "JPEG");
//			activity.startActivityForResult(intent, 1);
//		}
//
//		public static String convertStreamToString(InputStream is) throws IOException {
//			Writer writer = new StringWriter();
//
//			char[] buffer = new char[2048];
//			try {
//				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//				int n;
//				while ((n = reader.read(buffer)) != -1) {
//					writer.write(buffer, 0, n);
//				}
//			} finally {
//				is.close();
//			}
//			String text = writer.toString();
//			return text;
//		}
//
//		public static void printLog(String tag, String msg) {
//			Log.e(tag, msg);
//		}
//
//		public static String getHtmlTemplate(Context context, String assetsFileName) {
//			String html = "";
//			try {
//				html = convertStreamToString(context.getAssets().open(assetsFileName));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return html;
//		}
//
//		public static Long paraseLong(String str, Long defaultValue) {
//			try {
//				return Long.parseLong(str);
//			} catch (Exception e) {
//				Log.e("error", e.getMessage());
//			}
//			return defaultValue;
//		}
//
 
//		 
//	 
//		public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//			// Raw height and width of image
//			final int height = options.outHeight;
//			final int width = options.outWidth;
//			int inSampleSize = 1;
//
//			if (height > reqHeight || width > reqWidth) {
//
//				// Calculate ratios of height and width to requested height and
//				// width
//				final int heightRatio = Math.round((float) height / (float) reqHeight);
//				final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//				// Choose the smallest ratio as inSampleSize value, this will
//				// guarantee
//				// a final image with both dimensions larger than or equal to the
//				// requested height and width.
//				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//			}
//
//			return inSampleSize;
//		}
	}
