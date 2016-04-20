package com.lst.lstjx.bean;

/**
 * author describe parameter return
 */
public class CreateOrder {
	private String price;
	private String orderid;
	private String num;
	@Override
	public String toString() {
		return "CreateOrder [price=" + price + ", orderid=" + orderid
				+ ", num=" + num + "]";
	}
	
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

}
