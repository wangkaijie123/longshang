package com.lst.lstjx.bean;

import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class MyCarDetails {

	private String id;
	private String chexing;
	private String chechang;
	private String zaizhong;
	private String jiashi;
	private String xingche;
	private ArrayList<String> imgs;
	@Override
	public String toString() {
		return "MyCarDetails [id=" + id + ", chexing=" + chexing
				+ ", chechang=" + chechang + ", zaizhong=" + zaizhong
				+ ", jiashi=" + jiashi + ", xingche=" + xingche + ", imgs="
				+ imgs + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getJiashi() {
		return jiashi;
	}
	public void setJiashi(String jiashi) {
		this.jiashi = jiashi;
	}
	public String getXingche() {
		return xingche;
	}
	public void setXingche(String xingche) {
		this.xingche = xingche;
	}
	public ArrayList<String> getImgs() {
		return imgs;
	}
	public void setImgs(ArrayList<String> imgs) {
		this.imgs = imgs;
	}
	
	
}
