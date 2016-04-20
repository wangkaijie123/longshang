package com.lst.lstjx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bob on 2015/3/15.
 */
public class CrGroup implements Serializable {
    /**
     * 返回码
     */
    private int code;
    /**
     * 错误码 message
     */
    private String message;
    /**
     * 群组信息
     */
    private String tyId;

    public CrGroup() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTyId() {
		return tyId;
	}

	public void setTyId(String tyId) {
		this.tyId = tyId;
	}

}
