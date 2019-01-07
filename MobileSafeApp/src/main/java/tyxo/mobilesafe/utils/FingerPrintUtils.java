package tyxo.mobilesafe.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import tyxo.functions.prettygirls.home.HomeActivity;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * Created by LY on 2016/9/12 11: 51.
 * Mail      tyxo842@163.com
 * Describe : 指纹解锁,很简单的使用,直接 new FingerPrintUtils(context);
 */
public class FingerPrintUtils {
    private Context context;
    private boolean isCheckOk;
    private ProgressDialog pd ;
    private Handler handler;
    private Handler fHandler;
    private FingerprintManagerCompat fManager;
    public FingerPrintUtils(Context context) {
        this.context = context;
        handler= new MyHandlerF();
        initAttribute();
        fingerCheck();
    }

    private void initAttribute() {
        fHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                fManager.authenticate(null,0,null,new MyCallBackFinger(),null);
                HLog.v("tyxo","MainActivity fHandler : "+"重启指纹模块");
            }
        };
        fManager = FingerprintManagerCompat.from(context);// 获取一个FingerPrintManagerCompat的实例
    }

    /**指纹 解锁*/
    private boolean fingerCheck(){
        pd = new ProgressDialog(context);
        pd.setTitle("请录入指纹");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//风格:圆形,旋转
        //pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//风格:水平
        pd.setMessage("");
        pd.setIndeterminate(false);//进度条是否不明确
        pd.setCancelable(true);//按返回取消
        pd.show();
        /**
         * 开始验证，什么时候停止由系统来确定，如果验证成功，那么系统会关系sensor，如果失败，则允许
         * 多次尝试，如果依旧失败，则会拒绝一段时间，然后关闭sensor，过一段时候之后再重新允许尝试
         *
         * 第四个参数为重点，需要传入一个FingerprintManagerCompat.AuthenticationCallback的子类
         * 并重写一些方法，不同的情况回调不同的函数
         */
        fManager.authenticate(null,0,null,new MyCallBackFinger(),null);

        return isCheckOk;
    }
    private class MyCallBackFinger extends FingerprintManagerCompat.AuthenticationCallback{

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            isCheckOk = false;
            fHandler.sendMessageDelayed(new Message(), 1000 * 30);
            HLog.e("tyxo","onAuthenticationError ："+errString);
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            isCheckOk = false;
            fHandler.sendMessageDelayed(new Message(), 1000 * 30);
            HLog.e("tyxo","onAuthenticationFailed ："+"验证失败");
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            isCheckOk = true;
            Message msg = new Message();
            msg.what = 5;
            handler.sendMessage(msg);
            HLog.v("tyxo","onAuthenticationSucceeded ："+"验证成功");
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
    }

    private class MyHandlerF extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    pd.hide();
                    break;
            }
        }
    }
}
