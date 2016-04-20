package com.lst.lstjx.bean;

import java.util.ArrayList;
import java.util.List;

public class FriendDynamic {
	private String id;
	private String uid;
	private String uname;
	private String title;
	private String content;
	private List<String> img = new ArrayList<String>();
	private List<String> video = new ArrayList<String>();
	private String type;
	private String addtime;

	public FriendDynamic() {
		super();
	}

	public FriendDynamic(String id, String uid, String uname, String title,
			String content, List<String> img, List<String> video, String type,
			String addtime) {
		super();
		this.id = id;
		this.uid = uid;
		this.uname = uname;
		this.title = title;
		this.content = content;
		this.img = img;
		this.video = video;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<String> getVideo() {
		return video;
	}

	public void setVideo(List<String> video) {
		this.video = video;
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
		return "MyDynamic [id=" + id + ", uid=" + uid + ", uname=" + uname
				+ ", title=" + title + ", content=" + content + ", img=" + img
				+ ", video=" + video + ", type=" + type + ", addtime="
				+ addtime + "]";
	}

}
