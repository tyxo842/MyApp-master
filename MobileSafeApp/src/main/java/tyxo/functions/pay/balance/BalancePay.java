package tyxo.functions.pay.balance;

import android.content.Context;

import org.json.JSONObject;

import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyManager;
import tyxo.mobilesafe.utils.ToastUtil;

/**
 *  描  述：余额支付网络请求
 *  创建时间：16/3/17
 */
public class BalancePay {

    public static void payByBalance(Context context, String url, String outTradeNo, VolleyCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("outTradeNo", outTradeNo);
            VolleyManager.getInstance(context).postJson(url, jsonObject, callBack);
        } catch (Exception e) {
            ToastUtil.showToastS(context,e.getMessage());
        }
    }
}
