package tyxo.mobilesafe.net.volley;
/**
 *  Volley Error Result： Volley 错误结果
 * @Classname VolleyNetworkManager
 */
public class VolleyErrorResult {

	/** 错误状态 */
	private int code;

	/** 结果说明 */
	private String value;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String result) {
		this.value = result;
	}

	public String toString(){
		return "code:"+code+" value = "+value;
	}
	
}
