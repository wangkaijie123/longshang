<<<<<<< HEAD
package com.lst.lstjx.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author describe parameter return
 */
public class MyGroup implements Serializable {

	public String id;
	public String masterid;
	public String groupid;
	public String groupname;
	public String introduce;
	public String createtime;
	public String number;
	public String number_max;

	public MyGroup() {
		super();
	}
	public MyGroup(String id, String masterid, String groupid, String groupname,
			String introduce,  String number,String number_max,
			String createtime) {
		super();
		this.id = id;
		this.masterid = masterid;
		this.groupid = groupid;
		this.groupname = groupname;
		this.introduce = introduce;
		this.number = number;
		this.number_max = number_max;
		this.createtime = createtime;
	}
	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber_max() {
		return number_max;
	}

	public void setNumber_max(String number_max) {
		this.number_max = number_max;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMasterid() {
		return masterid;
	}
	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}
	@Override
	public String toString() {
		return "MyGroup [id=" + id + ", masterid=" + masterid + ", groupid="
				+ groupid + ", groupname=" + groupname + ", introduce="
				+ introduce + ", createtime=" + createtime + ", number="
				+ number + ", number_max=" + number_max + "]";
	}
	

}
=======
package com.lst.lstjx.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author describe parameter return
 */
public class MyGroup implements Serializable {

	public String id;
	public String masterid;
	public String groupid;
	public String groupname;
	public String introduce;
	public String createtime;
	public String number;
	public String number_max;

	public MyGroup() {
		super();
	}
	public MyGroup(String id, String masterid, String groupid, String groupname,
			String introduce,  String number,String number_max,
			String createtime) {
		super();
		this.id = id;
		this.masterid = masterid;
		this.groupid = groupid;
		this.groupname = groupname;
		this.introduce = introduce;
		this.number = number;
		this.number_max = number_max;
		this.createtime = createtime;
	}
	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber_max() {
		return number_max;
	}

	public void setNumber_max(String number_max) {
		this.number_max = number_max;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMasterid() {
		return masterid;
	}
	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}
	@Override
	public String toString() {
		return "MyGroup [id=" + id + ", masterid=" + masterid + ", groupid="
				+ groupid + ", groupname=" + groupname + ", introduce="
				+ introduce + ", createtime=" + createtime + ", number="
				+ number + ", number_max=" + number_max + "]";
	}
	

}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
