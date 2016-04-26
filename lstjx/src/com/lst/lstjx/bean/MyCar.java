package com.lst.lstjx.bean;

import java.io.Serializable;

/**
 * author describe parameter return
 */
public class MyCar implements Serializable{
	private String id;
	private String chechang;
	private String chexing;
	private String zaizhong;
	private String img;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getChechang() {
		return chechang;
	}
	public void setChechang(String chechang) {
		this.chechang = chechang;
	}
	public String getChexing() {
		return chexing;
	}
	public void setChexing(String chexing) {
		this.chexing = chexing;
	}
	
	
	public String getZaizhong() {
		return zaizhong;
	}
	public void setZaizhong(String zaizhong) {
		this.zaizhong = zaizhong;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "MyCar [id=" + id + ", chechang=" + chechang + ", chexing="
				+ chexing + ", zaizhong=" + zaizhong + ", img=" + img + "]";
	}
	
	

}
