package com.lst.lstjx.bean;

/**
 * author describe parameter return
 */
public class HomePic {

	private String img;
	private String name;
	private String url;
	private String id;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
	
		return "HomePic [img=" + img + ", name=" + name + ", url=" + url
				+ ", id=" + id + "]";
	}

}
