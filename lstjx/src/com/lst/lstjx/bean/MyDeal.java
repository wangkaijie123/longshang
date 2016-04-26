package com.lst.lstjx.bean;

import java.util.ArrayList;
import java.util.List;

public class MyDeal {
	private String id;
	private String uid;
	private String uname;
	private String userface;
	private String content;
	private List<String> img = new ArrayList<String>();
	private String addtime;

	public MyDeal() {
		super();
	}

	public MyDeal(String id, String uid, String uname, String userface,
			String content, List<String> img, String addtime) {
		super();
		this.id = id;
		this.uid = uid;
		this.uname = uname;
		this.userface = userface;
		this.content = content;
		this.img = img;
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

	public String getUserface() {
		return userface;
	}

	public void setUserface(String userface) {
		this.userface = userface;
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

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "MyDeal [id=" + id + ", uid=" + uid + ", uname=" + uname
				+ ", userface=" + userface + ", content=" + content + ", img="
				+ img + ", addtime=" + addtime + "]";
	}

}
