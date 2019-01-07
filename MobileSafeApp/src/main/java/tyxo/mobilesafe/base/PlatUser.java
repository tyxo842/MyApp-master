package tyxo.mobilesafe.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import tyxo.mobilesafe.ConstValues;
import tyxo.mobilesafe.base.abstracthh.EntityBase;
import tyxo.mobilesafe.base.interfacehh.Table;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.net.volley.VolleyManager;
import tyxo.mobilesafe.utils.AndroidUtil;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * 平台企业用户
 *
 * @author yuexl
 */
@Table(name = "PlatUser")//表名
public class PlatUser extends EntityBase implements Serializable {


    protected static final String TAG = "PlatEntUser";
    public static final String SP_USER_NAME_KEY = "userName";
    public static final String SP_USER_PWD_KEY = "pwd";
    /**
     * msg : 登陆成功
     * msgModels : [{"code":"SYS_MSG","msgIcon":"","name":"系统消息"},{"code":"ORDER_RETURN","msgIcon":"ison2","name":"退货单"},{"code":"ORDER","msgIcon":"ison1","name":"订单"}]
     * msgOrgType : ent
     * orgId : CFYC00000000000000707102
     * orgLevel : 0
     * orgLevelName : 0
     * orgName : 北京威高亚华人工关节开发有限公司
     * projectModels : [{"bidCode":"001","projectId":"PROJ00000000000000081044","projectName":"温附二检验试剂"}]
     * resultCode : 200
     * serviceModels : [{"serviceCode":"20#10#10#10","serviceIcon":"image","serviceName":"注册证查询"},{"serviceCode":"20#10#10#60","serviceIcon":"image","serviceName":"产品查询"},{"serviceCode":"20#10#10#20","serviceIcon":"image","serviceName":"快速审核"},{"serviceCode":"20#10#10#40","serviceIcon":"image","serviceName":"电话服务"},{"serviceCode":"20#10#10#50","serviceIcon":"image","serviceName":"在线答疑"}]
     * userId : DEMT00000000000000000227
     * userName : 北京威高亚华人工关节开发有限公司
     */

    public String msg;
    public String msgOrgType;
    public String orgId;
    public String orgLevel;
    public String orgLevelName;
    public String orgName;
    public int resultCode;
    public String userId;
    public String userName;
    /**
     * code : SYS_MSG
     * msgIcon :
     * name : 系统消息
     */

    public List<MsgModelsEntity> msgModels;
    /**
     * bidCode : 001
     * projectId : PROJ00000000000000081044
     * projectName : 温附二检验试剂
     */

    public List<ProjectModelsEntity> projectModels;
    /**
     * serviceCode : 20#10#10#10
     * serviceIcon : image
     * serviceName : 注册证查询
     */

    public List<ServiceModelsEntity> serviceModels;


    /**
     * 根据本地配置文件，完成自动登录 */
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

    /**
     * 用户登录
     */
    public static void login(Context context, String url, String userName, String pwd, OnLoginResultHandler handler) {

        try {

            JSONObject jsonUser = new JSONObject();
            jsonUser.put("isMD5", "0");
            jsonUser.put("loginIP", "0.0.01");
            jsonUser.put("loginName", userName);
            jsonUser.put("loginPassword", pwd);
            jsonUser.put("systemId", "null");

            HLog.i("zyw 登录接口url", url);
            loginWithJSONParams(context, url, jsonUser, handler);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录
     */
    public static void loginWithJSONParams(final Context context, String url, JSONObject jsonParams, final OnLoginResultHandler handler) {

        VolleyManager.getInstance(context).postJson(url, jsonParams, new VolleyCallBack<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                HLog.i("zyw 登录接口返回的数据",response.toString());
                try {
                    if (!TextUtils.isEmpty(response.toString())) {


                        Type type = new TypeToken<PlatUser>() {
                        }.getType();
                        PlatUser user = new Gson().fromJson(response.toString(), type);
                        if (user.resultCode == 200) {

                            MyApp myApp = MyApp.getInstance();
                            myApp.setCurrentUser(user);
                            myApp.setIsLogin(true);
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
        String url = "此处填写更新url";

        JSONObject jsonReq = new JSONObject();
        String json = gson.toJson(userInfo);

        try {
            jsonReq.put("userInfo", new JSONObject(json));
            VolleyManager.getInstance(context).postJson(url, jsonReq, callBack);

        } catch (JSONException e) {
            e.printStackTrace();
            AndroidUtil.showToastShort(context, "请求失败");
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMsgOrgType(String msgOrgType) {
        this.msgOrgType = msgOrgType;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public void setOrgLevelName(String orgLevelName) {
        this.orgLevelName = orgLevelName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMsgModels(List<MsgModelsEntity> msgModels) {
        this.msgModels = msgModels;
    }

    public void setProjectModels(List<ProjectModelsEntity> projectModels) {
        this.projectModels = projectModels;
    }

    public void setServiceModels(List<ServiceModelsEntity> serviceModels) {
        this.serviceModels = serviceModels;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgOrgType() {
        return msgOrgType;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public String getOrgLevelName() {
        return orgLevelName;
    }

    public String getOrgName() {
        return orgName;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<MsgModelsEntity> getMsgModels() {
        return msgModels;
    }

    public List<ProjectModelsEntity> getProjectModels() {
        return projectModels;
    }

    public List<ServiceModelsEntity> getServiceModels() {
        return serviceModels;
    }

    /**
     * 登录结果处理的接�?     *
     * @author yuexl
     */
    public interface OnLoginResultHandler {

        /**
         * 后台自动登录成功
         */
        public void onLoginSuccess();

        /**
         * 后台自动登录失败
         */
        public void onLoginFailure(String msg);
    }

    /**
     * 登录成功后，保存用户信息到手机
     * @param context
     */
    public void saveInfoToLocalCache(Context context, String name,String Pwd){
        // 存用户手机号到缓存
        SharedPreferences mSharedPreferences_UserName = context.getSharedPreferences(ConstValues.USER_DATA_FILE, Context.MODE_PRIVATE);
        mSharedPreferences_UserName.edit()
                .putString(SP_USER_NAME_KEY,name)
                .putString(SP_USER_PWD_KEY, Pwd)
                .commit();
    }

    public static class MsgModelsEntity {
        private String code;
        private String msgIcon;
        private String name;

        public void setCode(String code) {
            this.code = code;
        }

        public void setMsgIcon(String msgIcon) {
            this.msgIcon = msgIcon;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getMsgIcon() {
            return msgIcon;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProjectModelsEntity {
        private String bidCode;
        private String projectId;
        private String projectName;

        public void setBidCode(String bidCode) {
            this.bidCode = bidCode;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getBidCode() {
            return bidCode;
        }

        public String getProjectId() {
            return projectId;
        }

        public String getProjectName() {
            return projectName;
        }
    }

    public static class ServiceModelsEntity {
        private String serviceCode;
        private String serviceIcon;
        private String serviceName;

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public void setServiceIcon(String serviceIcon) {
            this.serviceIcon = serviceIcon;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public String getServiceIcon() {
            return serviceIcon;
        }

        public String getServiceName() {
            return serviceName;
        }
    }
}
