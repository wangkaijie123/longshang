package com.lst.lstjx.bean;

import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class ProDetails {


	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public String getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(String goods_no) {
		this.goods_no = goods_no;
	}
	public String getMarket_price() {
		return market_price;
	}
	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public ArrayList<String> getContent() {
		return content;
	}
	public void setContent(ArrayList<String> content) {
		this.content = content;
	}
	public ArrayList<String> getImgs() {
		return imgs;
	}
	public void setImgs(ArrayList<String> imgs) {
		this.imgs = imgs;
	}
	private String sell_price;
	@Override
	public String toString() {
		return "ProDetails [name=" + name + ", sell_price=" + sell_price
				+ ", store=" + store + ", sale=" + sale + ", goods_no="
				+ goods_no + ", market_price=" + market_price + ", num=" + num
				+ ", content=" + content + ", imgs=" + imgs + "]";
	}
	private String store;
	private String sale;
	private String goods_no;
	private String market_price;
	private String num;
	private  ArrayList<String> content;
	private ArrayList<String>imgs;
}
