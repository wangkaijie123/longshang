package com.lst.lstjx.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * author describe parameter return
 */
public class PersonDetail  implements Serializable {
	public int success;
	public String code;
    public Data data ;
	
	public class Data {
		public String username;
		public String face;
		public String email;
		public String nickname;
		public String mobile;
		public String sex;
		
	}
}
