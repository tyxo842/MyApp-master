package tyxo.mobilesafe.net.volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求返回值的JSONObject类型
 *
 */
public class JSONResult extends BaseResult {

	private JSONObject value;

	public JSONObject getValue() {
		return value;
	}

	public void setValue(JSONObject value) {
		this.value = value;
	}

	public JSONResult(JSONObject jsonObj){
		try {
			JSONObject jsonResult = jsonObj.getJSONObject("Result");
			this.setKey(jsonResult.getString("ResultKey"));
			this.setValue(jsonResult.getJSONObject("ResultSuccessValue"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}


