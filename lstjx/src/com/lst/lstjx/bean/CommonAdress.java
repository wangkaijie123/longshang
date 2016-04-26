package com.lst.lstjx.bean;

/**
 * author describe parameter return
 */
public class CommonAdress {
	private String accept_name;
	private String mobile;
	private String adress;
	private String zip;

	public String getAccept_name() {
		return accept_name;
	}

	public void setAccept_name(String accept_name) {
		this.accept_name = accept_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
	}

	@Override
	public String toString() {
		return "getCommonAdress [accept_name=" + accept_name + ", mobile="
				+ mobile + ", adress=" + adress + ", zip=" + zip + "]";
	}

}
