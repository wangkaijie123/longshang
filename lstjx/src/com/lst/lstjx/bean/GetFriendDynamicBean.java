package com.lst.lstjx.bean;

import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class GetFriendDynamicBean {
	public String code;
	public int success;
	// 用gson 要new 出来 date的名字是key的值不可以乱取
	public ArrayList<Date> data = new ArrayList<GetFriendDynamicBean.Date>();

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

	// Data的名字可以随便取 type 1文字 2 图片3 视频
	public static class Date {
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUname() {
			return uname;
		}

		public void setUname(String uname) {
			this.uname = uname;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		// public byte[] getVideo() {
		// return video;
		// }
		// public void setVideo(byte[] video) {
		// this.video = video;
		// }
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAddtime() {
			return addtime;
		}

		public void setAddtime(String addtime) {
			this.addtime = addtime;
		}

		private String id;
		private String uid;
		private String uname;
		private String content;
		private String title;
		private String img;
		 public ArrayList<String> video = new ArrayList<String>();
		
	
//		private String[] video;
//
//		public String[] getVideo() {
//			return video;
//		}
//
//		public void setVideo(String[] video) {
//			this.video = video;
//		}

		// private byte[] video;
		private String type;
		private String addtime;

	}

}
