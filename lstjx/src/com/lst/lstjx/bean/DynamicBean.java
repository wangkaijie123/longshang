<<<<<<< HEAD
package com.lst.lstjx.bean;

import java.util.ArrayList;
import java.util.List;

public class DynamicBean {
	private int success;
	private String code;
	private Data mdata;
	private ArrayList<Data> data = new ArrayList<DynamicBean.Data>();

	public DynamicBean() {
		super();
	}

	@Override
	public String toString() {
		return "DynamicBean [success=" + success + ", code=" + code
				+ ", mdata=" + mdata + ", data=" + data + "]";
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Data getMdata() {
		return mdata;
	}

	public void setMdata(Data mdata) {
		this.mdata = mdata;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

	public class Data {
		private String id;
		public String uid;
		public String uname;

		public String content;
		public List<String> img = new ArrayList<String>();

		public String type;
		public String addtime;

		public Data() {
			super();
		}

		public Data(String id, String uid, String uname, String title,
				String content, List<String> img, List<String> video,
				String type, String addtime) {
			super();
			this.id = id;
			this.uid = uid;
			this.uname = uname;
			this.content = content;
			this.img = img;
			this.type = type;
			this.addtime = addtime;
		}

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

		public List<String> getImg() {
			return img;
		}

		public void setImg(List<String> img) {
			this.img = img;
		}

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

		@Override
		public String toString() {
			return "Data [id=" + id + ", uid=" + uid + ", uname=" + uname
					+ ", title=" + ", content=" + content + ", img=" + img
					+ ", video=" + ", type=" + type + ", addtime=" + addtime
					+ "]";
		}

	}
}
=======
package com.lst.lstjx.bean;

import java.util.ArrayList;
import java.util.List;

public class DynamicBean {
	private int success;
	private String code;
	private Data mdata;
	private ArrayList<Data> data = new ArrayList<DynamicBean.Data>();

	public DynamicBean() {
		super();
	}

	@Override
	public String toString() {
		return "DynamicBean [success=" + success + ", code=" + code
				+ ", mdata=" + mdata + ", data=" + data + "]";
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Data getMdata() {
		return mdata;
	}

	public void setMdata(Data mdata) {
		this.mdata = mdata;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

	public class Data {
		private String id;
		public String uid;
		public String uname;

		public String content;
		public List<String> img = new ArrayList<String>();

		public String type;
		public String addtime;

		public Data() {
			super();
		}

		public Data(String id, String uid, String uname, String title,
				String content, List<String> img, List<String> video,
				String type, String addtime) {
			super();
			this.id = id;
			this.uid = uid;
			this.uname = uname;
			this.content = content;
			this.img = img;
			this.type = type;
			this.addtime = addtime;
		}

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

		public List<String> getImg() {
			return img;
		}

		public void setImg(List<String> img) {
			this.img = img;
		}

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

		@Override
		public String toString() {
			return "Data [id=" + id + ", uid=" + uid + ", uname=" + uname
					+ ", title=" + ", content=" + content + ", img=" + img
					+ ", video=" + ", type=" + type + ", addtime=" + addtime
					+ "]";
		}

	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
