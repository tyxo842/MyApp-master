package tyxo.mobilesafe.net.volley;

/**
 * 
 * @ClassName: BaseResult 
 * @Description: Volley请求类的返回结果
 *
 */
public class BaseResult{
	/*// 返回结果码
	private int code;*/
	
	private String key; // 后期改为使用code
	/*public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}*/
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
