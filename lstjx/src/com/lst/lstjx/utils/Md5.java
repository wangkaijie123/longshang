package com.lst.lstjx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lst-004 on 15-5-6.
 */
public class Md5 {
    public static String Md5(String password,int length){
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for(int offest = 0 ; offest < b.length ; offest ++){
                i = b[offest];
                if (i < 0){
                    i += 256;
                }
                if(i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            if (length == 16){
                result = buf.toString().substring(8,24);
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return result;
    }
}
