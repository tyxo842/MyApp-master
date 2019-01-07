package tyxo.mobilesafe.utils.md5;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



//import com.alibaba.fastjson.*;

/**
 * @author zhangxf
 * @date 创建时间：2016年8月11日 下午4:32:30
 * @version 1.0
 */
public class AESUtil {
	private static final String IV_STRING = "16-Bytes--String";
	private static final String UTF8="UTF-8";
	private static final String ISO88591="ISO8859-1";
	private static final String ConfigUtil = null;
	
	
	public static byte[] encryptAES(byte[] byteContent, String key)
			throws Exception {

		// 注意，为了能与 iOS 统一
		// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
		byte[] enCodeFormat = key.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

		byte[] initParam = IV_STRING.getBytes();
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

		// 指定加密的算法、工作模式和填充方式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

		byte[] encryptedBytes = cipher.doFinal(byteContent);
	    //BASE64Encoder
		return encryptedBytes;
	}

	public static byte[] decryptAES(byte[] encryptedBytes, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {


		byte[] enCodeFormat = key.getBytes();
		SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");

		byte[] initParam = IV_STRING.getBytes();
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

		byte[] result = cipher.doFinal(encryptedBytes);

		return result;
	}

	public static String enbase64(byte[] byteCode) throws UnsupportedEncodingException{
		
		return Base64.encodeToString(byteCode, 0);
		
	}
	
	public static byte[] debase64(byte[] byteCode) throws UnsupportedEncodingException{
		return Base64.decode(byteCode, 0);
	}
	
	public void test() throws Exception {
		String a = "test测试";
		
		byte[] aaa=AESUtil.encryptAES(a.getBytes(), a);
		//String str=AESUtil.enbase64(AESUtil.encryptAES(a.getBytes("GBK"), ConfigUtil.getEncryptKey()));
		//System.out.println(str);
	}
	
	public static String tes1t(String  stttString) throws Exception {
		String s1 = stttString;
		byte[] b1 = AESUtil.decryptAES(debase64(s1.getBytes()), "lc0sd7mcx5vk1re3");
		String s2=new String(b1);
		System.out.println("BBBB"+s2);
		return s2;
	}
	

	public static void main(String[] args) throws Exception {
//		tes1t();
//		System.out.println("7777777777"+tes1t());
	}

}
