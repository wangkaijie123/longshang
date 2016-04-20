package com.lst.lstjx.bean;

/**
 * author describe 流水号的数据 parameter return
 */
public class GetTn {
	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReqReserved() {
		return reqReserved;
	}

	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public String getTxnSubType() {
		return txnSubType;
	}

	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}



	@Override
	public String toString() {
		return "GetTn [accessType=" + accessType + ", bizType=" + bizType
				+ ", certId=" + certId + ", encoding=" + encoding + ", merId="
				+ merId + ", orderId=" + orderId + ", reqReserved="
				+ reqReserved + ", respCode=" + respCode + ", respMsg="
				+ respMsg + ", signMethod=" + signMethod + ", tn=" + tn
				+ ", txnSubType=" + txnSubType + ", txnTime=" + txnTime
				+ ", txnType=" + txnType + ", version=" + version
				+ ", signature=" + signature + "]";
	}
	private String accessType;
	private String bizType;
	private String certId;
	private String encoding;
	private String merId;
	private String orderId;
	private String reqReserved;
	private String respCode;
	private String respMsg;
	private String signMethod;
	private String tn;
	private String txnSubType;
	private String txnTime;
	private String txnType;
	private String version;
	private String signature;
}
