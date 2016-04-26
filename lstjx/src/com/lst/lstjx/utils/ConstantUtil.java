package com.lst.lstjx.utils;

import android.os.Environment;

public class ConstantUtil
{
	/** 成功返回:1 */
	public static final String RESULT_SUCCESS = "1";
	/** 返回失败:-1 */
	public static final String RESULT_FAIL = "-1";

	// app 图片存储路径
	public static final String APP_PIC_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/wxy/pic/";
	// app 文件存储路径
	
	/**
	 * 
	 */
	public static final String APP_FILE_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/wxy/file/";

	
	
}
