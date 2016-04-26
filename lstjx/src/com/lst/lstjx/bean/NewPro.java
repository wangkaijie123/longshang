package com.lst.lstjx.bean;

/**
 * author describe parameter return
 */
public class NewPro {
	@Override
	public String toString() {
		return "NewPro [id=" + id + ", name=" + name + ", sell_price="
				+ sell_price + ", img=" + img + "]";
	}

	private String id;
	private String name;
	private String sell_price;
	private String img;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
