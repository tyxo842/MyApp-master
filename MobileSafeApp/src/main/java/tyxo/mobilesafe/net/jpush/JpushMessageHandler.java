package tyxo.mobilesafe.net.jpush;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.MyApp;
import tyxo.mobilesafe.base.PlatUser;
import tyxo.mobilesafe.utils.AndroidUtil;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * notification:alert
 * message:自定义消息
 * <p/>
 * Created on 2015/11/20.
 */
public class JpushMessageHandler {

    private static String userId;
    private MyApp myApp = new MyApp().getInstance();

    /**
     * 接收自定义消息
     */
    public static void messageReceived(Context context, Bundle bundle) {
        try {
            // 解析消息
            /*PlatAppMessage appMessage = new Gson().fromJson(bundle.getString(JPushInterface.EXTRA_MESSAGE), new TypeToken<PlatAppMessage>() {
            }.getType());
            HLog.i("ctmd", "接收自定义消息1");
            //2015-12-25 11:50:32 修改为 Android5.0原生的通知特性
            NotificationCompat.Builder builder = getBuilder(context, appMessage);
            //推送消息的跳转
            MessageOpened(context, appMessage, builder);
            //通知栏显示消息
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify((int) System.currentTimeMillis(), builder.build());
            //推送消息的处理
            MessageHandler(context, appMessage);*/
        } catch (Exception ex) {
            ex.printStackTrace();
            HLog.i("zyw", "messageReceived 方法异常");
        }
    }

    /**
     * 设置通知栏样式
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private static NotificationCompat.Builder getBuilder(Context context, Object appMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        BitmapDrawable draw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.icon_logol);
        Bitmap m = draw.getBitmap();
        builder.setContentTitle(context.getResources().getString(R.string.app_name))
                .setLargeIcon(m)
                //NotificationCompat.Builder实例化我们的通知后，最终需要将各种图标，参数配置，应用到通知视图上面。可以看到如果我们只设置smallIcon而不设置largeIcon也是可以的，此时直接将small作为大图标设置给左侧的id为R.id.icon的ImageView。要注意的事一般情况下都不可以不设置smallIcon，否则通知无法正常显示出来。
                // processSmallIconAsLarge方法里面负责将我们设置的smallIcon二次处理，也就是这里会改变我们最终看到的通知图标，包括顶部状态栏和下拉显示的小图标。
                //操作会导致通知的图标统统变白色。
//                .setContentText(appMessage.getAlert())
                //.setNumber(number)//显示数量
//                .setTicker(appMessage.getAlert())//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_MAX)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.icon_logo_white)
                .setVisibility(Notification.VISIBILITY_PRIVATE)//设置通知在锁屏界面显示的内容5.0
                //.setContentInfo("内容信息")
                .setPriority(Notification.PRIORITY_MAX)
                //.setSubText("ddddd");
        ;
        return builder;
    }

    /**
     * 通知栏消息的跳转
     */
    private static void MessageOpened(Context context, Object appMessage, NotificationCompat.Builder builder) {
        PlatUser user = (PlatUser) MyApp.getInstance().getCurrentUser();

        //判断用户是否登陆
        if (null == user) {
            /*Intent resultIntent = new Intent(context, UserLoginActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);*/
        } else if (AndroidUtil.isAppAlive(context, "com.searainbow.enterpriseplat")) {
            //判断app进程是否存活
            //如果存活的话，就直接启动MessageListInfoActivity，但要考虑一种情况，就是app的进程虽然仍然在
            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动MessageListInfoActivity
            //再按Back键就不会返回MainActivity了。所以在启动MessageListInfoActivity前，要先启动MainActivity。
            /*Intent resultIntent = new Intent(context, MessageListInfoActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.putExtra("messageCode", appMessage.getMsgBusCode());

//            Intent mainDrawerActivity = new Intent(context, MainDrawerActivity.class);
            Intent mainDrawerActivity = new Intent(context, NewMainActivity.class);
            mainDrawerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent[] intents = {mainDrawerActivity, resultIntent};
            PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);*/
        }
    }

    /**
     * 推送消息的处理
     */
    private static void MessageHandler(final Context context, Object appMessage) {

        /**获取到推送消息 */
        /*final String MsgId = appMessage.getMsgId();
        final String MsgBusCode = appMessage.getMsgBusCode();
        final String MsgCreateDate = appMessage.getMsgCreateDate();
        if (MyApp.getInstance() != null) {
            userId = MyApp.getInstance().getCurrentUser().getUserId();
        }

        //根据消息类型查询本地数据库
        PlatMessage.MessageDetailEntity model = HDbManager.create(context)
                .findFirst(Selector.from(PlatMessage.MessageDetailEntity.class)
                        .where("busCodes", "=", MsgBusCode).orderBy("createDate", true));
        if (null != model) {
            //本地有该类型的消息，取该类型消息的MaxDate，网络请求
            saveLocalData(model, MsgCreateDate, context, MsgBusCode);
        } else {
            //本地无该类型的消息，根据消息ID获取消息
            NetGetMessageListInfoByMsgId.getMsgListInfoByMsgId(context, MsgId, userId,
                    new NetGetMessageListInfoByMsgId.OnGetMessageListInfoByMsgIdResultHandler() {
                        @Override
                        public void onGetMessageListInfoBymsgIdSuccess(PlatMessage platMessage) {
                            HLog.i("zyw", "通知请求结果 platMessage：" + platMessage.getMessageDetail().get(0).getMsgId());
                            *插入数据库 刷新列表与适配器
                            if (null != platMessage.getMessageDetail() && platMessage.getMessageDetail().size() > 0) {
                                //增加用户字段
                                platMessage.getMessageDetail().get(0).setUserId(userId);
                                //判断数据重复
                                PlatMessage.MessageDetailEntity mOnlyModel = HDbManager.create(context)
                                        .findFirst(Selector.from(PlatMessage.MessageDetailEntity.class)
                                                .where("msgId", "=", platMessage.getMessageDetail().get(0)
                                                        .getMsgId()).orderBy("createDate", true));
                                //存入数据库
                                if (null == mOnlyModel) {
                                    HDbManager.create(MyApp.getInstance()).save(platMessage.getMessageDetail().get(0));
                                    HLog.i("zyw", "已保存消息到数据库");
                                    sendBoradcast(context);
                                }
                            }
                        }

                        @Override
                        public void onGetMessageListInfoByMsgIdFailure(String msg) {
                            HLog.i("zyw 推送", "处理失败" + msg);
                        }
                    });
        }*/
    }

    /*private static void saveLocalData(PlatMessage.MessageDetailEntity model, String MsgCreateDate,
                                      final Context context, String MsgBusCode) {
        //取本地消息最大时间
        String maxTime = model.getCreateDate();
        //根据消息类型，与时间网络请求
        NetGetMessageListInfoByMsgId.getMsgListInfoByMsgIdWithMsgBusCode(context, maxTime, userId, MsgBusCode,
                new NetGetMessageListInfoByMsgId.OnGetMessageListInfoByMsgIdResultHandler() {
                    @Override
                    public void onGetMessageListInfoBymsgIdSuccess(PlatMessage platMessage) {

                        *//**插入数据库 刷新列表与适配器 *//*
                        if (null != platMessage.getMessageDetail() && platMessage.getMessageDetail().size() > 0) {
                            HLog.i("zyw", "platMessage.getMessageDetail().size()=" + platMessage.getMessageDetail().size() + "");
                            //增加用户字段
                            for (PlatMessage.MessageDetailEntity entry : platMessage.getMessageDetail()) {
                                entry.setUserId(MyApp.getInstance().getCurrentUser().userId);

                                PlatMessage.MessageDetailEntity mOnlyModel = HDbManager.create(context)
                                        .findFirst(Selector.from(PlatMessage.MessageDetailEntity.class)
                                                .where("msgId", "=", entry.getMsgId()).orderBy("createDate", true));
                                if (null == mOnlyModel) {
                                    //存入数据库,判断数据重复
                                    HDbManager.create(MyApp.getInstance()).save(entry);
                                    HLog.i("zyw", "已保存消息到数据库");
                                    //发送广播
                                    sendBoradcast(context);
                                }
                            }
                        }
                    }

                    @Override
                    public void onGetMessageListInfoByMsgIdFailure(String msg) {
                        HLog.i("zyw 推送", "处理失败" + msg);
                    }
                });
    }*/

    private static void sendBoradcast(Context context) {
        Intent intent = new Intent("PushMessageAction");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
