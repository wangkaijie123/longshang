package com.lst.lstjx.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class SearchFriendBean  implements Serializable {
	public String code;
	public int success;
	// 用gson 要new 出来 date的名字是key的值不可以乱取
	public ArrayList<Date> data = new ArrayList<SearchFriendBean.Date>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	// Data的名字可以随便取
	public static class Date {
		public String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String username;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
}
