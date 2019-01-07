package tyxo.functions.prettygirls.app;

import android.app.Application;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import tyxo.functions.prettygirls.app.exception.LocalFileHandler;
import tyxo.functions.prettygirls.util.LogUtil;
import tyxo.functions.prettygirls.util.ToastUtil;


/**
 * Created by oracleen on 2016/6/28.
 */
public class MyApplication extends Application {

    private static MyApplication mApplication;
    public static String currentGirl = "http://ww2.sinaimg.cn/large/610dc034jw1f5k1k4azguj20u00u0421.jpg";
    private static Handler mainHandler;//主线程的handler

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        //配置是否显示log
        LogUtil.isDebug = true;

        //配置时候显示toast
        ToastUtil.isShow = true;

        //配置程序异常退出处理
        Thread.setDefaultUncaughtExceptionHandler(new LocalFileHandler(this));
        mainHandler = new Handler();//初始化handler
    }

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(6, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        return client;
    }

    public static MyApplication getIntstance() {
        return mApplication;
    }

    public static Handler getMainHandler(){
        return mainHandler;
    }
}
