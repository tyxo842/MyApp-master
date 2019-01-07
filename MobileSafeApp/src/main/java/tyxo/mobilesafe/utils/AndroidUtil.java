package tyxo.mobilesafe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import tyxo.mobilesafe.ConstantsMy;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.AppEnv;
import tyxo.mobilesafe.utils.log.HLog;

public class AndroidUtil {

    /** 获取手机型号 */
    public static String getMobileModel() {
        return Build.MODEL;
    }

    /** 获取制造商名称 //manufacturer */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "haihongs" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /** 根据ListView的子项重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView */
    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /*************************************************************************************/
    /**
     * 实例化AlertDialog的Builder
     *
     * @param context            上下文
     * @param message            消息提示框正文
     * @param title              消息提示框标题
     * @param negativeButtonText 确定按钮回调时用于验证的请求码,无回调设置为0
     * @param listener           确定按钮回调接口，无回调设置为null
     * @param isAddOhterButton   是否包含取消按钮
     * @return
     */
    private static Builder instanceDialogBuilder(Context context, String message, String title,
                                                 String positiveButtonText, String negativeButtonText,
                                                 final OnClickListener listener, boolean isAddOhterButton) {

        Builder builder = new Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        if (listener != null) {
            // 确定按钮
            builder.setPositiveButton(positiveButtonText, listener);

            // 是否包含取消按钮
            if (isAddOhterButton) {
                // 取消按钮
                builder.setNegativeButton(negativeButtonText, listener);
            }
        }

        return builder;
    }

    /**
     * 显示消息对话框(只包含确定按钮)
     *
     * @param context            上下文
     * @param message            提示框内容
     * @param title              提示框标题
     * @param positiveButtonText 按钮文本
     * @param listener           按钮点击事件
     * @throws Exception
     */
    public static void showMessageDialog(Context context, String message,
                       String title, String positiveButtonText, OnClickListener listener) throws Exception {
        if (context == null) {
            throw new Exception("argument context is null");
        } else {
            instanceDialogBuilder(context, message, title, positiveButtonText, "", listener, false).create().show();
        }
    }

    /**
     * 显示消息对话框(只包含确定按钮)
     *
     * @param context              上下文
     * @param messageId            提示框内容资源id
     * @param titleId              提示框标题资源id
     * @param positiveButtonTextId 按钮文本
     * @param listener             按钮点击事件
     */
    public static void showMessageDialog(Context context, int messageId,
                       int titleId, int positiveButtonTextId, OnClickListener listener) throws Exception {
        if (context == null) {
            throw new Exception("argument context is null");
        } else {
            String message = context.getResources().getString(messageId);
            String title = context.getResources().getString(titleId);
            String positiveButtonText = context.getResources().getString(positiveButtonTextId);
            showMessageDialog(context, message, title, positiveButtonText,listener);
        }
    }

    /**
     * 显示消息对话框(包含确定和取消两个按钮)
     *
     * @param context            上下文
     * @param message            消息内容
     * @param title              消息标题
     * @param negativeButtonText 回调函数请求代码，用于在回调函数在判断调用者来源
     * @param listener           回调接口
     */
    public static void showMessageDialogWithOtherButton(Context context,
                                                        String message, String title, String positiveButtonText,
                                                        String negativeButtonText, OnClickListener listener) {

        instanceDialogBuilder(context, message, title, positiveButtonText,negativeButtonText, listener, true).create().show();
    }

    /**
     * 显示消息对话框(包含确定和取消两个按钮)
     *
     * @param context          上下文
     * @param messageId        消息内容资源id
     * @param titleId          消息标题资源id
     * @param negativeButtonId 回调函数请求代码，用于在回调函数在判断调用者来源
     * @param listener         回调接口
     * @throws Exception
     */
    public static void showMessageDialogWithOtherButton(Context context,
                                                        int messageId, int titleId, int positiveButtonTextId,
                                                        int negativeButtonId, OnClickListener listener) throws Exception {
        if (context != null) {
            String message = context.getResources().getString(messageId);
            String title = context.getResources().getString(titleId);
            String positiveButtonText = context.getResources().getString(positiveButtonTextId);
            String negativeButtonText = context.getResources().getString( negativeButtonId);

            showMessageDialogWithOtherButton(context, message, title,positiveButtonText, negativeButtonText, listener);
        } else {
            throw new Exception("argument context is null");
        }
    }

	/*
     * private static AlertDialog.Builder instanceBuilder(Context context,String
	 * message, String title, String positiveButtonText, String
	 * negativeButtonText, final int requestCode, final IMessageDialogCallBack
	 * dialogCallBack, boolean isCancelable){
	 *
	 * AlertDialog.Builder builder = new Builder(context);
	 * builder.setMessage(message); builder.setTitle(title);
	 * builder.setPositiveButton(positiveButtonText, new
	 * DialogInterface.OnClickListener() {
	 *
	 * public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss();
	 *
	 * if (dialogCallBack != null) { // 点击关闭按钮时执行回调方法
	 * dialogCallBack.doCallBack(requestCode); } } }); // 是否包含取消按钮 if
	 * (isCancelable) { builder.setNegativeButton(negativeButtonText, new
	 * DialogInterface.OnClickListener() {
	 *
	 * public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss(); } }); }
	 *
	 * return builder; }
	 */
	/**
     * 显示消息对话框(只包含确定按钮)
     *
     * @param context   上下文
     * @param message   消息内容
     * @param title     消息标题
     * @param requestCode 回调函数请求代码，用于在回调函数在判断调用者来源
     * @param dialogCallBack 回调接口
     *
     */
	/*
	 * public static void showMessageDialog(Context context,String message,
	 * String title, String positiveButtonText, final int requestCode, final
	 * IMessageDialogCallBack dialogCallBack){
	 *
	 * instanceBuilder(context,message,title,positiveButtonText,"",requestCode,
	 * dialogCallBack,false) .create().show(); }
	 */

    /**
     * 显示消息对话框(只包含确定按钮)
     *
     * @param context   上下文
     * @param messageId 消息内容资源id
     * @param titleId   消息标题id
     * @param requestCode 回调函数请求代码，用于在回调函数在判断调用者来源
     * @param dialogCallBack 回调接口
     * @throws Exception
     */
	/*
	 * public static void showMessageDialog(Context context,int messageId, int
	 * titleId, int positiveButtonTextId, final int requestCode, final
	 * IMessageDialogCallBack dialogCallBack) throws Exception{ if (context !=
	 * null) { String message = context.getResources().getString(messageId);
	 * String title = context.getResources().getString(titleId); String
	 * positiveButtonText =
	 * context.getResources().getString(positiveButtonTextId);
	 *
	 * showMessageDialog(context, message, title,
	 * positiveButtonText,requestCode, dialogCallBack); }else { throw new
	 * Exception("argument context is null"); }
	 */

	/*
	 * AlertDialog.Builder builder = new Builder(context);
	 * builder.setMessage(messageId); builder.setTitle(titleId);
	 * builder.setPositiveButton(context.getResources().getString(R.string.ok),
	 * new DialogInterface.OnClickListener() {
	 *
	 * public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss();
	 *
	 * if (dialogCallBack != null) { // 点击关闭按钮时执行回调方法
	 * dialogCallBack.doCallBack(requestCode); } } }); builder.create().show();
	 * }
	 */

    /**
     * 显示消息对话框(包含确定和取消两个按钮)
     *
     * @param context   上下文
     * @param message   消息内容
     * @param title     消息标题
     * @param requestCode 回调函数请求代码，用于在回调函数在判断调用者来源
     * @param dialogCallBack 回调接口
     *
     */
	/*
	 * public static void showMessageDialogWithOtherButton(Context
	 * context,String message, String title, String positiveButtonText, String
	 * negativeButtonText, final int requestCode, final IMessageDialogCallBack
	 * dialogCallBack){
	 *
	 * instanceBuilder(context,message,title, positiveButtonText,
	 * negativeButtonText,requestCode,dialogCallBack,true) .create() .show();
	 *
	 *
	 * AlertDialog.Builder builder = new Builder(context);
	 * builder.setMessage(message); builder.setTitle(title);
	 * builder.setPositiveButton(context.getResources().getString(R.string.ok),
	 * new DialogInterface.OnClickListener() {
	 *
	 * public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss();
	 *
	 * if (dialogCallBack != null) { // 点击关闭按钮时执行回调方法
	 * dialogCallBack.doCallBack(requestCode); } } });
	 * builder.setNegativeButton(
	 * context.getResources().getString(R.string.cancel), new
	 * DialogInterface.OnClickListener() {
	 *
	 * public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss(); } }); builder.create().show(); }
	 */

    /*************************************************************************************/

    /** 获取手机SIM卡 */
    public static String getSimSerialNum(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNum = telephonyManager.getSimSerialNumber();

        if (simSerialNum == null) {
            simSerialNum = "";
        }

        return simSerialNum;
    }

    /** 获取手机Sim卡序列号 */
    public static String getSimSerialNumWithAppContext() {
        return getSimSerialNum(AppEnv.mAppContext);
    }

    /*************************************************************************************/

    /** 发送广播 */
    public static void sendBroadcst(Context ctx, String action) {
        Intent intent = new Intent(action);
        ctx.sendBroadcast(intent);
    }

    /** 发送带参数的广播 */
    public static void sendMsgBroadcst(Context ctx, String action, String key,
                                       Serializable msg) {
        Intent intent = new Intent(action);
        intent.putExtra(key, msg);
        ctx.sendBroadcast(intent);
    }

    /** 发送带参数的广播 */
    public static void sendMsgBroadcst(Context ctx, String action, String key,
                                       Parcelable msg) {
        Intent intent = new Intent(action);
        intent.putExtra(key, msg);
        ctx.sendBroadcast(intent);
    }

    public static Bundle getBundle(int what, int arg1, int arg2,
                                   Serializable obj) {
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantsMy.KEY_WHAT, what);
        bundle.putInt(ConstantsMy.KEY_ARG1, arg1);
        bundle.putInt(ConstantsMy.KEY_ARG2, arg1);
        bundle.putSerializable(ConstantsMy.KEY_OBJ, obj);
        return bundle;
    }

    /** 数据库 */
    public Bundle getBundle(int what, int arg1, int arg2, byte[] obj) {
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantsMy.KEY_WHAT, what);
        bundle.putInt(ConstantsMy.KEY_ARG1, arg1);
        bundle.putInt(ConstantsMy.KEY_ARG2, arg1);
        bundle.putByteArray(ConstantsMy.KEY_OBJ, obj);
        return bundle;
    }

    /**
     * 发送带参数的广播
     *
     * @param ctx
     * @param action 广播接收动作
     * @param what   广播类型
     * @param arg1   是否有数据 1，有数据，0无数据
     * @param arg2   有效数据长度
     * @param obj    数据对象
     */
    public static void sendMsgBroadcst(Context ctx, String action, int what,
                                       int arg1, int arg2, Serializable obj) {
        Intent intent = new Intent(action);
        intent.putExtra(ConstantsMy.KEY_WHAT, what);
        intent.putExtra(ConstantsMy.KEY_ARG1, arg1);
        intent.putExtra(ConstantsMy.KEY_ARG2, arg2);
        intent.putExtra(ConstantsMy.KEY_OBJ, obj);
        ctx.sendBroadcast(intent);
    }

    /**
     * 发送带参数的广播
     *
     * @param ctx
     * @param action 广播接收动作
     * @param what   广播类型
     * @param arg1   是否有数据 1，有数据，0无数据
     * @param arg2   有效数据长度
     * @param obj    数据对象
     */
    public static void sendMsgBroadcst(Context ctx, String action, int what,
                                       int arg1, int arg2, byte[] obj) {
        Intent intent = new Intent(action);
        intent.putExtra(ConstantsMy.KEY_WHAT, what);
        intent.putExtra(ConstantsMy.KEY_ARG1, arg1);
        intent.putExtra(ConstantsMy.KEY_ARG2, arg2);
        intent.putExtra(ConstantsMy.KEY_OBJ, obj);

        ctx.sendBroadcast(intent);
    }

    /*************************************************************************************/

    /** 提示框 */
    public static void showToastLong(Context context, int text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

    }

    public static void showToastLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context context, int text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(Context context, String text) {
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        ToastUtil.showToastS(context,text);
    }

    /** 定位显示Toast */
    public static void showLocationToast(Context context, String text, int duration, int gravity) {

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /*************************************************************************************/

    /** 清除指定目录下的文件 */
    public static void clearCache(String pPath) {
        File folder = new File(pPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] folders = folder.listFiles();
            for (File file : folders) {
                if (file == null)
                    continue;
                if (!file.exists())
                    continue;
                if (file.isDirectory()) {
                    clearCache(file.getPath());
                } else if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    /*************************************************************************************/

    /** 压缩指定路径的图片 */
    public static Bitmap safeDecodeBitmap(String localPath) {
        Options option = new Options();
        option.inJustDecodeBounds = true;
        Bitmap bitmapOri = BitmapFactory.decodeFile(localPath, option);

        try {
            if (option.outHeight > 1000 || option.outWidth > 1000) {
                option.inJustDecodeBounds = false;
                option.inSampleSize = 2;
                bitmapOri = BitmapFactory.decodeFile(localPath, option);
            } else {
                bitmapOri = BitmapFactory.decodeFile(localPath);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmapOri;
    }

    /*************************************************************************************/

    /** 防止快速重复点击 限制1秒只能点1次 */

    public static boolean isFastDoubleClick(View v) {
        long lastClickTime = 0;
        try {
            lastClickTime = (Long) v.getTag(R.id.tag_position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        v.setTag(R.id.tag_position, time);
        return false;
    }

    /*************************************************************************************/

	/** 获取当前程序的版本号 */
    public static int getVersionCode(Context context) throws NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionCode; // versionName=2.0; versionCode=2
    }
    /** 获取当前程序的版本名 */
    public static String getVersionName(Context context){
        try {
            // 获取 包管理者PackageManager 的实例
            PackageManager packageManager = context.getPackageManager();

            //返回包的基本信息.  第一个参数:表示包名;第二个参数:标记,0代表获取到所有的标记
            // getPackageName()是你当前类的包名
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionNameV = "v " + packInfo.versionName; //获取版本名
            HLog.i("lyjc", "versionName : " + packInfo.versionName);

            return versionNameV; // versionName=1.2.0; versionCode=2
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /** 当前代码是否运行在主线程 */
    public static boolean isRunningOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /** 退出程序 */
    public static void QuitHintDialog(final Context context) {

        new Builder(context).setMessage("确定退出掌上药店吗？")
                .setTitle("掌上药店")
                // .setIcon(R.drawable.logo)
                .setPositiveButton("退出", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ((Activity) context).finish();
                        } catch (Exception e) {
                            Log.e("close", "close error");
                        }
                    }
                })
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();

    }

    /** 获取yyyy-MM-dd HH:mm:ss格式的当前时间 */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                HLog.i("NotificationLaunch",String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        HLog.i("NotificationLaunch",String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /** 获取当前activity的name,并与传入的判断是否相同 */
    public static boolean isCurrentActivityTop(Context context, String activityName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runAClassN = activityManager.getRunningTasks(1).get(0).topActivity.getShortClassName();//ui.xx.类名
        String runAName = runAClassN.substring(runAClassN.lastIndexOf(".") + 1);//类名

        HLog.i("ly", runAName);
        if (activityName.equals(runAName)) {
            return true;
        }
        return false;
    }

    /** 根据Wifi信息获取本地Mac */
    public static String getLocalMacAddressFromWifiInfo(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /** 根据busybox获取本地Mac ; busybox: linux内的瑞士军刀 */
    public static String getLocalMacAddressFromBusybox(){
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig","HWaddr");

        //如果返回的result == null，则说明网络不可取
        if(result==null){
            return "网络出错，请检查网络";
        }

        //对该行数据进行解析
        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
        if(result.length()>0 && result.contains("HWaddr")==true){
            Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);
            HLog.v("test","Mac:"+Mac+" Mac.length: "+Mac.length());

             /*if(Mac.length()>1){
                 Mac = Mac.replaceAll(" ", "");
                 result = "";
                 String[] tmp = Mac.split(":");
                 for(int i = 0;i<tmp.length;++i){
                     result +=tmp[i];
                 }
             }*/
            result = Mac;
            HLog.v("test",result+" result.length: "+result.length());
        }
        return result;
    }
    private static String callCmd(String cmd,String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);

            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine ()) != null && line.contains(filter)== false) {
                //result += line;
                HLog.v("test","line: "+line);
            }

            result = line;
            HLog.v("test","result: "+result);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 根据IP获取本地Mac 2.3以上才有该 NetworkInterface 接口 */
    public static String getLocalMacAddressFromIp(Context context) {
        String mac_s= "";
        try {
            byte[] mac;
            NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }
    public static  String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    /** 获取本地IP */
    public static String getLocalIpAddress() {
        try {
            String ipv4;
            List nList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            HLog.e("tyxo WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /*public static String getLocalIpAddressModify() {
        try {
            String ipv4;
            List nList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nList) {
                List iaList = Collections.list(ni.getInetAddresses());
                for (InetAddress address : iaList) {
                    if (!address.isLoopbackAddress()&& InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress())) {
                        return ipv4;
                    }
                }
            }
        } catch (SocketException ex) {
            HLog.e("tyxo WifiPreference IpAddress", ex.toString());
        }
        return null;
    }*/

    /** 获取本地IP,解决android4.0获取IP错误的问题: */
    public static String getLocalIpAddressHim() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            HLog.e("tyxo WifiPreference IpAddress", ex.toString());
        }
        return null;
    }
}

































