package tyxo.mobilesafe.utils.md5;

import java.security.MessageDigest;

/**
 * 
 * Copyright (c)2013,银海艺能  All rights reserved.
 * 
 * <core>字符串的工具类</core>
 * 
 * @author Administrator  来自 小罗童鞋
 * 
 * @version
 *
 */
public class MD5Util {

//	public static void main(String[] args) {
//		System.out.println(MD5Util.getMD5("lc0sd7mcx5vk1re3"));
//	}

	public final static String getMD5(String str) {
		if (str != null) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			try {
				byte[] strBytes = str.getBytes();
				// 获得MD5摘要算法的 MessageDigest 对象
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				// 使用指定的字节更新摘要
				messageDigest.update(strBytes);
				// 获得密文
				byte[] md = messageDigest.digest();
				// 把密文转换成十六进制的字符串形式
				int j = md.length;
				char arr[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					arr[k++] = hexDigits[byte0 >>> 4 & 0xf];
					arr[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(arr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
