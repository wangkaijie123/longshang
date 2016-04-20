package com.lst.lstjx.bean;

import java.util.ArrayList;
import java.util.List;

public class MyWl {
	private String id;
	private String uid;
	private String uname;
	private String userface;
	private String content;
	private String Starttime;
	private String Starting;
	private String ending;
	private String chexing;
	private String chechang;
	private String zaizhong;
	private List<String> img = new ArrayList<String>();
	private String addtime;

	public MyWl() {
		super();
	}

	public MyWl(String id, String uid, String uname, String userface,
			String Starting, String ending, String Starttime, String content,
			String chexing, String chechang, String zaizhong, List<String> img,
			String addtime) {
		super();
		this.id = id;
		this.uid = uid;
		this.uname = uname;
		this.userface = userface;
		this.content = content;
		this.img = img;
		this.addtime = addtime;
		this.Starttime = Starttime;
		this.Starting = Starting;
		this.ending = ending;
		this.chexing = chexing;
		this.chechang = chechang;
		this.zaizhong = zaizhong;

	}

	public String getChexing() {
		return chexing;
	}

	public void setChexing(String chexing) {
		this.chexing = chexing;
	}

	public String getChechang() {
		return chechang;
	}

	public void setChechang(String chechang) {
		this.chechang = chechang;
	}

	public String getZaizhong() {
		return zaizhong;
	}

	public void setZaizhong(String zaizhong) {
		this.zaizhong = zaizhong;
	}

	public String getStarting() {
		return Starting;
	}

	public void setStarting(String starting) {
		Starting = starting;
	}

	public String getEnding() {
		return ending;
	}

	public void setEnding(String ending) {
		this.ending = ending;
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

	public String getStarttime() {
		return Starttime;
	}

	public void setStarttime(String starttime) {
		Starttime = starttime;
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
		return "MyWl [id=" + id + ", uid=" + uid + ", uname=" + uname
				+ ", userface=" + userface + ", content=" + content
				+ ", Starttime=" + Starttime + ", Starting=" + Starting
				+ ", ending=" + ending + ", chexing=" + chexing + ", chechang="
				+ chechang + ", zaizhong=" + zaizhong + ", img=" + img
				+ ", addtime=" + addtime + "]";
	}

}
