package com.lst.lstjx.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * "id": "2", "username": "admin", "face": "/Upload/face/1.png",
 * "email":"827400817@qq.com", "nickname": "哈哈", "mobile": "15869547521", "sex":
 * "1
 * 
 * @author gpc
 * 
 */
public class UserInfor {

	private String id;
	private String username;
	private String face;
	private String email;
	private String nickname;
	private String mobile;
	private String sex;

	public UserInfor() {
		super();
	}

	public UserInfor(String id, String username, String face, String email,
			String nickname, String mobile, String sex) {
		super();
		this.id = id;
		this.username = username;
		this.face = face;
		this.email = email;
		this.nickname = nickname;
		this.mobile = mobile;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "UserInfor [id=" + id + ", username=" + username + ", face="
				+ face + ", email=" + email + ", nickname=" + nickname
				+ ", mobile=" + mobile + ", sex=" + sex + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}