<<<<<<< HEAD
package com.lst.lstjx.bean;

import org.json.JSONObject;

import org.json.JSONObject;
public class AdInfo
{
	private String advImg;
	private String advLink;
	private String advType;
	private String advDesc;
	private String modelId;
	private String channelName;
	private Integer adRes;
	

	public AdInfo(JSONObject obj)
	{
		this.advImg = obj.optString("adv_img");
		this.advLink = obj.optString("adv_link");
		this.advType = obj.optString("adv_type");
		this.advDesc = obj.optString("adv_desc");
		this.modelId = obj.optString("model_id");
		this.channelName = obj.optString("channel_name");
	}
	
	public AdInfo(int res)
	{
		this.adRes = res;
	}
	public AdInfo()
	{
	}

	public String getAdvImg()
	{
		return advImg;
	}

	public void setAdvImg(String advImg)
	{
		this.advImg = advImg;
	}

	public String getAdvLink()
	{
		return advLink;
	}

	public void setAdvLink(String advLink)
	{
		this.advLink = advLink;
	}

	public String getAdvType()
	{
		return advType;
	}

	public void setAdvType(String advType)
	{
		this.advType = advType;
	}

	public String getAdvDesc()
	{
		return advDesc;
	}

	public void setAdvDesc(String advDesc)
	{
		this.advDesc = advDesc;
	}

	public String getModelId()
	{
		return modelId;
	}

	public void setModelId(String modelId)
	{
		this.modelId = modelId;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public Integer getAdRes()
	{
		return adRes;
	}

	public void setAdRes(Integer adRes)
	{
		this.adRes = adRes;
	}


	
}


=======
package com.lst.lstjx.bean;

import org.json.JSONObject;

import org.json.JSONObject;
public class AdInfo
{
	private String advImg;
	private String advLink;
	private String advType;
	private String advDesc;
	private String modelId;
	private String channelName;
	private Integer adRes;
	

	public AdInfo(JSONObject obj)
	{
		this.advImg = obj.optString("adv_img");
		this.advLink = obj.optString("adv_link");
		this.advType = obj.optString("adv_type");
		this.advDesc = obj.optString("adv_desc");
		this.modelId = obj.optString("model_id");
		this.channelName = obj.optString("channel_name");
	}
	
	public AdInfo(int res)
	{
		this.adRes = res;
	}
	public AdInfo()
	{
	}

	public String getAdvImg()
	{
		return advImg;
	}

	public void setAdvImg(String advImg)
	{
		this.advImg = advImg;
	}

	public String getAdvLink()
	{
		return advLink;
	}

	public void setAdvLink(String advLink)
	{
		this.advLink = advLink;
	}

	public String getAdvType()
	{
		return advType;
	}

	public void setAdvType(String advType)
	{
		this.advType = advType;
	}

	public String getAdvDesc()
	{
		return advDesc;
	}

	public void setAdvDesc(String advDesc)
	{
		this.advDesc = advDesc;
	}

	public String getModelId()
	{
		return modelId;
	}

	public void setModelId(String modelId)
	{
		this.modelId = modelId;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public Integer getAdRes()
	{
		return adRes;
	}

	public void setAdRes(Integer adRes)
	{
		this.adRes = adRes;
	}


	
}


>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
