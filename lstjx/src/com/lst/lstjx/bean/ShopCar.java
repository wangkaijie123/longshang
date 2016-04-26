package com.lst.lstjx.bean;

import java.io.Serializable;

/**
 * author describe parameter return
 */
public class ShopCar implements Serializable{
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGood_id() {
		return good_id;
	}

	public void setGood_id(String good_id) {
		this.good_id = good_id;
	}

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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "ShopCar [id=" + id + ", good_id=" + good_id + ", name=" + name
				+ ", sell_price=" + sell_price + ", img=" + img + "]";
	}

	private String good_id;
	private String name;
	private String sell_price;
	private String img;
	public String  getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	private String num;

}
