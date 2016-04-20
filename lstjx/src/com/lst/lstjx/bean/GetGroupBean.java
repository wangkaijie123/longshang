package com.lst.lstjx.bean;

import io.rong.imlib.model.Group;

import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class GetGroupBean {
	public String code;
	public int success;
	// 用gson 要new 出来 date的名字是key的值不可以乱取
	public ArrayList<Group> data = new ArrayList<Group>();

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

	public ArrayList<Group> getData() {
		return data;
	}

	public void setData(ArrayList<Group> data) {
		this.data = data;
	}

	

	
}
