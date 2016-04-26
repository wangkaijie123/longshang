package com.lst.lstjx.bean;
/**
"id":"1",
"dynamic_id":"1",
"uid":"2",
"content":"\u8bc4\u8bba\u5185\u5bb9",
"addtime":"2015-11-12 00:00:00",
"face":"\/ChatApi\/Upload\/face\/s_56415d01a144a.png",
"sex":"1",
"username":"admin"
*/
public class DynamicPLBean {

	private String id;
	private String dynamic_id;
	private String uid;
	private String content;
	private String addtime;
	private String face;
	private String sex;
	private String username;
	public DynamicPLBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DynamicPLBean(String id, String dynamic_id, String uid,
			String content, String addtime, String face, String sex,
			String username) {
		super();
		this.id = id;
		this.dynamic_id = dynamic_id;
		this.uid = uid;
		this.content = content;
		this.addtime = addtime;
		this.face = face;
		this.sex = sex;
		this.username = username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDynamic_id() {
		return dynamic_id;
	}
	public void setDynamic_id(String dynamic_id) {
		this.dynamic_id = dynamic_id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "DynamicPLBean [id=" + id + ", dynamic_id=" + dynamic_id
				+ ", uid=" + uid + ", content=" + content + ", addtime="
				+ addtime + ", face=" + face + ", sex=" + sex + ", username="
				+ username + "]";
	}
	
}
