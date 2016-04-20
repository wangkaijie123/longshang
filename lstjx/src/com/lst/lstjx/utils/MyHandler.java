package com.lst.lstjx.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class MyHandler {

	private volatile static MyHandler gethandler ;
	
	private MyHandler(final Context context, final String str){
		Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				Toast.makeText(context, str, 0).show();
			};
		};
	}
	
	public static MyHandler getHandler(Context context,String str ){
		if (gethandler == null) {
			synchronized (MyHandler.class) {
				if (gethandler == null) {
					gethandler = new MyHandler(context,str){
						
					};
				}
			}
		}
		return gethandler;
	}
	
	
	
	
	// public void handleMessage(android.os.Message msg) {
	/*
	 * public class Singleton {
	 *  private volatile static Singleton uniqueInstance;
	 * 
	 * private Singleton() {}
	 * 
	 * public static Singleton getInstance() { 
	 * if(uniqueInstance == null) {
	 *    synchronized (Singleton.class) {
	 *     if(uniqueInstance == null) {
	 *        uniqueInstance = new Singleton(); 
	 *      } 
	 *    } 
	 * } 
	 * return uniqueInstance; }
	 */

}
