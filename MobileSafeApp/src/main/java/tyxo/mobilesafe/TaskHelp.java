package tyxo.mobilesafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import tyxo.mobilesafe.activity.SplashActivity;
import tyxo.mobilesafe.base.MyApp;
import tyxo.mobilesafe.base.PlatUser;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.net.volley.VolleyManager;
import tyxo.mobilesafe.utils.StringUtils;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * Created by LY on 2016/8/11 14: 07.
 * Mail      1577441454@qq.com
 * Describe : 用法:
 *              0.new TaskHelpNew --> task  (否则userName,orgId放不进去)
 *              1.new VolleyCallBack<JSONObject>()...;
 *              2.task.方法...;
 */
public class TaskHelp {
    private String userName;
    private String orgId;
    private PlatUser platUser;
    //private SharedPreferences mSpUser = MyApp.getAppContext().getSharedPreferences(ConstValues.USER_DATA_FILE, Context.MODE_PRIVATE);
    //private String userPWD = mSpUser.getString("pwd", "");
    //private String userLoginName = mSpUser.getString("userName", "");

    public TaskHelp() {
        //getPlatUserNameOrOrgid(MyApp.getInstance());
    }

    // platUser 为null时,跳转到splash重新登陆
    private void getPlatUserNameOrOrgid(Context context) {
        MyApp.getInstance().CheckNetworkState(context); // 检查网络

        platUser = (PlatUser) MyApp.getInstance().getCurrentUser();
        if (platUser != null) {
            this.userName = platUser.userName;
            this.orgId = platUser.orgId;
        } else {
            Intent intent = new Intent(context, SplashActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // 有白屏闪过,没有NEW_TASK会蹦
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 没有NEW_TASK会蹦
            context.startActivity(intent);
            return;
        }
    }


    /** 测试接口用,勿删! (从hh 挪移过来,get不到sp,会崩,记录一下 可变参数,接口思想) */
    public static void testNet(Context context,String... args) {

        PlatUser platUser = (PlatUser) MyApp.getInstance().getCurrentUser();
        String userName = platUser.userName;
        String orgId = platUser.orgId;
        SharedPreferences mSpUser = MyApp.getAppContext().getSharedPreferences(ConstValues.USER_DATA_FILE, Context.MODE_PRIVATE);
        String userPWD = mSpUser.getString("pwd", "");
        String userLoginName = mSpUser.getString("userName", "");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("userPwd", userPWD);
            jsonObject.put("orgId", orgId);
            jsonObject.put("orderContentId", args[1]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String url = StringUtils.combineURl(ConstValues.BASE_URL_CODE, ConstValues.ORDER_DETAIL_FRAGMENT_NORMAL);
        String url = args[0];
        VolleyManager.getInstance(context).postJson(url, jsonObject, new VolleyCallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HLog.i("tyxo",response.toString());
            }

            @Override
            public void onErrorResponse(VolleyErrorResult result) {
                HLog.i("tyxo",result.toString());
            }
        });
        HLog.i("tyxo:", " 请求url: "+url+"\n请求参数: "+jsonObject.toString());
    }


    /** 网络请求 基础方法 可变第一个参数要为url */
    public static void getDatasNet(Context context,VolleyCallBack<JSONObject> callback,String... args) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderContentId", args[1]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String url = StringUtils.combineURl(ConstValues.BASE_URL_CODE, ConstValues.ORDER_DETAIL_FRAGMENT_NORMAL);
        String url = args[0];

        VolleyManager.getInstance(context).postJson(url, jsonObject, callback);
        HLog.i("tyxo:", " 请求url: "+url+"\n请求参数: "+jsonObject.toString());
    }

    /** 网络请求 */
    public void orderModifyState(Context context,String orderId, String orderState,
                                 VolleyCallBack<JSONObject> callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", "tyxo");
            jsonObject.put("orgId", orgId);
            jsonObject.put("orderId", orderId);
            jsonObject.put("orderState", orderState);

                /*JSONArray jsonArray = new JSONArray();
                if (orderDetailIds!=null && orderDetailIds.size()>0){
                    for (int i=0;i<orderDetailIds.size();i++) {
                        JSONObject jsonInfo = new JSONObject();
                        jsonInfo.put("orderDetailId", orderDetailIds.get(i));
                        jsonArray.put(jsonInfo);
                    }
                }
                jsonObject.put("orderDetailIds", jsonArray.toString());*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "";
        VolleyManager.getInstance(context).postJson(url, jsonObject, callback);
        HLog.i("tyxo:", " 请求url: "+url+"\n请求参数: "+jsonObject.toString());
    }

    /** 天气查看 网络请求 */
    public static void requstWeatherDatas(Context context, VolleyCallBack<JSONObject> callback, Map<String,String> header){
        String url = "http://apis.baidu.com/heweather/weather/free?city=beijing";
        VolleyManager.getInstance(context).getJsonWithHeader(url,callback,header);
        /*
        // 原代码 设置, 需要封装
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                HLog.i("tyxo","TaskHelp jsonObject: "+ jsonObject);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HLog.e("tyxo","TaskHelp volleyError: "+ volleyError);
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://apis.baidu.com/heweather/weather/free?city=beijing";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers==null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                headers.put("apikey","2600907be4021f9979ecc9554a4065ac");
                //headers.put("Charset", "UTF-8");
                //headers.put("Content-Type", "application/x-javascript");
                //headers.put("Accept-Encoding", "gzip,deflate");
                return headers;
                //return super.getHeaders();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //request.setTag(TAG);
        //request.setShouldCache(true);

        queue.add(request);
        //queue.start();*/
    }

    /** girls 获取 网络请求 */
    public static void getGirls(Context context,int pageSize,int pageIndex,int rand,VolleyCallBack<JSONObject> callback){
        String urlBase = StringUtils.combineURl(ConstValues.MM_DABAI_BASE, ConstValues.MM_DABAI_BASE_NOZHI);
        String apiKey = "&showapi_appid=20676&showapi_sign=f730cd8c4cf8498895f83d43ddaba8c2";

        String url = urlBase+"?"+"num="+pageSize+"&page="+pageIndex+"&rand="+apiKey;
        VolleyManager.getInstance(context).getJson(url,callback);
        HLog.i("tyxo","getGirls url : "+url);
    }

    /**小罗童鞋 网络请求*/
    public static void getDataLuo(Context context,int pageNum,int pageSize,VolleyCallBack<JSONObject> callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNum", pageNum);
            jsonObject.put("pageSize", pageSize);
            //jsonObject.put("token", MD5Util.getMD5("lc0sd7mcx5vk1re3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://test.baike.art-d.com.cn:88/app/headLines/headLines.json?pageNum=1&pageSize=5";
        //String url = "http://test.baike.art-d.com.cn:88/app/headLines/headLines.json";
        VolleyManager.getInstance(context).postJson(url, jsonObject, callback);
        HLog.i("tyxo:", " 请求url: "+url+"\n请求参数: "+jsonObject.toString());
    }

}























