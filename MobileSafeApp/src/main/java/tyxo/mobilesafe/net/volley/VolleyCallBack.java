package tyxo.mobilesafe.net.volley;

/** Volley network manager： Volley网络回调 */
public interface VolleyCallBack<T> {
	
	/**
	 * 请求应答
	 * @param response  请求响应内容
	 */
	void onResponse(T response);
    /**
     * 请求错误
     * @param result 请求响应错误内容
     */
	void onErrorResponse(VolleyErrorResult result);
}
