package tyxo.mobilesafe.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import tyxo.mobilesafe.ConstValues;
import tyxo.mobilesafe.base.PlatUser;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.net.volley.VolleyManager;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.log.HLog;

/** 用户登录 */
public class NetLoginInfo {

    private static final String TAG = "NetLoginInfo";
    public static final String SP_USER_NAME_KEY = "userName";
    public static final String SP_USER_PWD_KEY = "pwd";

    /** 根据本地配置文件，完成自动登录 */
    public static void autoLogin(Context context, OnLoginResultHandler handler) {


        SharedPreferences mSpUser = context.getSharedPreferences(ConstValues.USER_DATA_FILE, Context.MODE_PRIVATE);
        String userName = mSpUser.getString(SP_USER_NAME_KEY, "");
        String pwd = mSpUser.getString(SP_USER_PWD_KEY, "");

        login(context, userName, pwd, handler);
    }


    public static void login(Context context, String userName, String pwd, OnLoginResultHandler handler) {
        //String url = StringUtils.combineURl(ConstValues.BASE_URL_USER, ConstValues.USER_LOGIN_URL);
        //login(context, url, userName, pwd, handler);
    }

    /** 用户登录 */
    public static void login(Context context, String url, String userName, String pwd, OnLoginResultHandler handler) {

        try {

            JSONObject jsonUser = new JSONObject();
            jsonUser.put("isMD5", "0");
            jsonUser.put("loginIP", "0.0.01");
            jsonUser.put("loginName", userName);
            jsonUser.put("loginPassword", pwd);
            jsonUser.put("systemId", "null");

            HLog.e(TAG, "login  jsonUser = " + jsonUser);
            loginWithJSONParams(context, url, jsonUser, handler);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**  用户登录 */
    public static void loginWithJSONParams(final Context context, String url, JSONObject jsonParams, final OnLoginResultHandler handler) {

        VolleyManager.getInstance(context).postJson(url, jsonParams, new VolleyCallBack<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    if (!TextUtils.isEmpty(response.toString())) {
                        HLog.e(TAG, "loginWithJSONParams  response = " + response);

                        Type type = new TypeToken<PlatUser>() {
                        }.getType();
                        PlatUser user = new Gson().fromJson(response.toString(), type);
                        if (user.resultCode == 200) {

                            /*MyApp myApp = MyApp.getInstance();
                            myApp.setCurrentUser(user);
                            myApp.setIsLogin(true);*/
                            handler.onLoginSuccess();
                        } else {
                            HLog.i(TAG, user.resultCode + ":" + user.msg);
                            handler.onLoginFailure(user.msg);
                        }
                    } else {
                        HLog.i(TAG, ConstValues.SERVER_RESPONSE_EMPTY);
                        handler.onLoginFailure(ConstValues.SERVER_RESPONSE_EMPTY);
                    }
                } catch (Exception e) {
                    HLog.i(TAG, "登录过程中发生异常" + e.getMessage());
                    handler.onLoginFailure("登录过程中发生异常");
                }
            }

            @Override
            public void onErrorResponse(VolleyErrorResult result) {
                HLog.e(TAG, result.toString());
                handler.onLoginFailure(result.getValue());
            }
        });
    }

    public static void AppUpdateUserInfo(Context context, PlatUser userInfo,
                                         VolleyCallBack<JSONObject> callBack) {
        Gson gson = new Gson();
//        String url = StringUtils.combineUrl(context,ConstValues.BASE_URL_USER, "WCFServices/WcfService.svc/AppUpdateUserInfo");
        String url = "此处是更新新版本的url";

        JSONObject jsonReq = new JSONObject();
        String json = gson.toJson(userInfo);

        try {
            jsonReq.put("userInfo", new JSONObject(json));
            VolleyManager.getInstance(context).postJson(url, jsonReq, callBack);

        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showToastS(context, "请求失败");
        }
    }

    /** 登录结果处理 */
    public interface OnLoginResultHandler {

        /** 后台自动登录成功 */
        void onLoginSuccess();

        /** 后台自动登录失败 */
        void onLoginFailure(String msg);
    }

    /** 登录成功后，保存用户信息到手机 */
    public static void saveInfoToLocalCache(Context context, String name, String Pwd) {
        // 存用户手机号到缓存
        SharedPreferences mSharedPreferences_UserName = context.getSharedPreferences(ConstValues.USER_DATA_FILE, Context.MODE_PRIVATE);
        mSharedPreferences_UserName.edit()
                .putString(SP_USER_NAME_KEY, name)
                .putString(SP_USER_PWD_KEY, Pwd)
                .commit();
    }
}
