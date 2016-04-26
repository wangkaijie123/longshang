<<<<<<< HEAD
package com.lst.lstjx.bean;

import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class GetFriendBean {
	public String code;
	public int success;
	// 用gson 要new 出来 date的名字是key的值不可以乱取
	public ArrayList<Date> data = new ArrayList<GetFriendBean.Date>();

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
		public String getFid() {
			return fid;
		}

		public void setFid(String fid) {
			this.fid = fid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getFace() {
			return face;
		}

		public void setFace(String face) {
			this.face = face;
		}

		public String getMark() {
			return mark;
		}

		public void setMark(String mark) {
			this.mark = mark;
		}
		private String fid;
		private String username;
		private String face;
		private String mark;
		

	}
}
=======
package com.lst.lstjx.bean;

import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class GetFriendBean {
	public String code;
	public int success;
	// 用gson 要new 出来 date的名字是key的值不可以乱取
	public ArrayList<Date> data = new ArrayList<GetFriendBean.Date>();

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
		public String getFid() {
			return fid;
		}

		public void setFid(String fid) {
			this.fid = fid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getFace() {
			return face;
		}

		public void setFace(String face) {
			this.face = face;
		}

		public String getMark() {
			return mark;
		}

		public void setMark(String mark) {
			this.mark = mark;
		}
		private String fid;
		private String username;
		private String face;
		private String mark;
		

	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
