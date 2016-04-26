<<<<<<< HEAD
package com.lst.lstjx.utils;

import java.util.regex.Pattern;

/**
 * author describe parameter return 判断一个字符串是否是汉字或者字母或者数字
 */
public class IsHanyu {
	// // 1 判断是否为数字
	// public static boolean isNumeric(String str) {
	// for (int i = str.length(); --i >= 0;) {
	// if (!Character.isDigit(str.charAt(i))) {
	// return false;
	// }
	// }
	// return true;
	// }

	// 2>用正则表达式
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// // 3>用ascii码
	// public static boolean isNumeric(String str) {
	// for (int i = str.length(); --i >= 0;) {
	// int chr = str.charAt(i);
	// if (chr < 48 || chr > 57)
	// return false;
	// }
	// return true;
	// }

	// 2.判断一个字符串的首字符是否为字母
	public static boolean test(String s) {
		char c = s.charAt(0);
		int i = (int) c;
		if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean check(String fstrData) {
		char c = fstrData.charAt(0);
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	// 3 .判断是否为汉字
	public static boolean vd(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;

				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}
}
=======
package com.lst.lstjx.utils;

import java.util.regex.Pattern;

/**
 * author describe parameter return 判断一个字符串是否是汉字或者字母或者数字
 */
public class IsHanyu {
	// // 1 判断是否为数字
	// public static boolean isNumeric(String str) {
	// for (int i = str.length(); --i >= 0;) {
	// if (!Character.isDigit(str.charAt(i))) {
	// return false;
	// }
	// }
	// return true;
	// }

	// 2>用正则表达式
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// // 3>用ascii码
	// public static boolean isNumeric(String str) {
	// for (int i = str.length(); --i >= 0;) {
	// int chr = str.charAt(i);
	// if (chr < 48 || chr > 57)
	// return false;
	// }
	// return true;
	// }

	// 2.判断一个字符串的首字符是否为字母
	public static boolean test(String s) {
		char c = s.charAt(0);
		int i = (int) c;
		if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean check(String fstrData) {
		char c = fstrData.charAt(0);
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	// 3 .判断是否为汉字
	public static boolean vd(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;

				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}
}
>>>>>>> e91e926442dc022c63334bc3f3ca8926acdda605
