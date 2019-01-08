package tyxo.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import tyxo.mobilesafe.base.AppEnv;

/**
 * Description:监测用户状态
 */
public class MonitorUserService extends Service {

    // 用户线程
    private UserThread mUserThread = null;
    Handler mHandler = new Handler();
    private long heartbeatDelayMillis = 1 * 60 * 1000; //频率 1分钟

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 开启线程
        mUserThread = new UserThread();
        mUserThread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    class UserThread extends Thread {
        // 运行状态，下一步骤有大用
        public boolean isRunning = true;

        public void run() {
            while (true) {
                try {
//                    if (null == MyApp.getInstance().getCurrentUser()) {
                    if (null == AppEnv.mAppContext) {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                //后台登录
                                backgroundLogin();
                            }
                        });
                    }
                    Thread.sleep(heartbeatDelayMillis);
                } catch (Exception e) {
                    try {
                        Thread.sleep(heartbeatDelayMillis);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    private void backgroundLogin() {
        NetLoginInfo.OnLoginResultHandler handler = new NetLoginInfo.OnLoginResultHandler() {

            @Override
            public void onLoginSuccess() {
            }

            @Override
            public void onLoginFailure(String msg) {
            }
        };

        NetLoginInfo.autoLogin(AppEnv.mAppContext, handler);
    }

    @Override
    public void onDestroy() {
        mUserThread.isRunning = false;
    }

}
