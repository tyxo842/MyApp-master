package tyxo.mobilesafe.net.volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONArrayResult extends BaseResult {
	private JSONArray value;

	public JSONArray getValue() {
		return value;
	}

	public void setValue(JSONArray value) {
		this.value = value;
	}

	public JSONArrayResult(JSONObject jsonObj){
		try {
			JSONObject jsonResult = jsonObj.getJSONObject("Result");
			this.setKey(jsonResult.getString("ResultKey"));
			this.setValue(jsonResult.getJSONArray("ResultSuccessValue"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
