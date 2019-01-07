package tyxo.mobilesafe.net.jpush;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import tyxo.mobilesafe.utils.NetUtil;
import tyxo.mobilesafe.utils.StringUtils;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * 极光推送管理类
 *
 * @author lwc
 */

public class JpushManager {

    private static final String TAG = "JpushManager";

    private static Context mContext;


    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    /**
     * 0 Set tag and alias success
     */
    public static final int SET_TAG_AND_ALIAS_SUCCESS = 0;
    /**
     * 6001 无效的设置，tag/alias 不应参数都为 null
     */
    public static final int INVALID_SETTINGS = 6001;
    /**
     * 6002 设置超时 建议重试
     */
    public static final int SETTING_TIMEOUT = 6002;
    /**
     * 6003 alias 字符串不合法 有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
     */
    public static final int ALIAS_ILLEGAL = 6003;
    /**
     * 6004 alias超长。最多 40个字节 中文 UTF-8 是 3 个字节
     */
    public static final int ALIAS_TO_LONG = 6004;
    /**
     * 6005 某一个 tag 字符串不合法 有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
     */
    public static final int ONE_TAG_ILLEGAL = 6005;
    /**
     * 6006 某一个 tag 超长。一个 tag 最多 40个字节 中文 UTF-8 是 3 个字节
     */
    public static final int ONE_TAG_TO_LONG = 6006;
    /**
     * 6007 tags 数量超出限制。最多 100个 这是一台设备的限制。一个应用全局的标签数量无限制。
     */
    public static final int TAGS_OUT_OF_LIMITS = 6007;
    /**
     * 6008 tag 超出总长度限制 总长度最多 1K 字节
     */
    public static final int TAGS_TO_LONG = 6008;
    /**
     * 6011 10s内设置tag或alias大于10次 短时间内操作过于频繁
     */
    public static final int FREQUENT_OPERATION = 6011;

    private static JpushManager mJpushManager = null;

    private JpushManager() {
    }

    public synchronized static JpushManager getInstance(Context context) {
        if (mJpushManager == null) {
            mJpushManager = new JpushManager();
        }
        mContext = context;
        return mJpushManager;
    }

    /**
     * 极光推送初始化
     */
    public void initJpush() {
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
        // 初始化 JPush
        JPushInterface.init(mContext);
        if (JPushInterface.isPushStopped(mContext.getApplicationContext())) {
            JPushInterface.resumePush(mContext.getApplicationContext());
            HLog.i(TAG, "推送服务恢复");
        } else {
            HLog.i(TAG, "推送服务初始化成功");
        }
    }

    /**
     * 恢复极光推送服务
     */
    public void resumeJpush() {
        if (JPushInterface.isPushStopped(mContext.getApplicationContext())) {
            JPushInterface.resumePush(mContext.getApplicationContext());
            HLog.i(TAG, "推送服务恢复");
        }
    }

    /**
     * 退出极光推送服务
     * <p/>
     * •JPush Service 不在后台运行
     * •收不到推送消息
     * •极光推送所有的其他 API 调用都无效,不能通过 JPushInterface.init 恢复，需要调用resumePush恢复。
     */
    public boolean stopJpush() {
        if (!JPushInterface.isPushStopped(mContext.getApplicationContext())) {
            JPushInterface.stopPush(mContext.getApplicationContext());
            HLog.i(TAG, "推送服务停止");
        }
        return JPushInterface.isPushStopped(mContext);
    }

    /**
     * 统计api
     *
     * @param activity 当前activity
     */
    public void onActivityResume(Activity activity) {
        JPushInterface.onResume(activity);
    }

    /**
     * 统计api
     *
     * @param activity 当前activity
     */
    public void onActivityPause(Activity activity) {
        JPushInterface.onPause(activity);
    }

    /**
     * 清除所有通知
     * 此 API 提供清除通知的功能，
     * 包括：清除所有 JPush 展现的通知（不包括非 JPush SDK 展现的）
     * 清除指定某个通知
     */
    public void clearAllNotifications() {
        JPushInterface.clearAllNotifications(mContext);
    }


    /**
     * 设置别名
     *
     * @param alias null 此次调用不设置此值。（注：不是指的字符串"null"）。
     *              "" （空字符串）表示取消之前的设置。每次调用设置有效的别名，覆盖之前的设置。
     *              有效的别名组成：字母（区分大小写）、数字、下划线、汉字。限制：alias 命名长度限制为 40 字节。
     *              （判断长度需采用UTF-8编码）
     */
    public void setAlias(String alias) {

        HLog.i(TAG, alias);
        if (TextUtils.isEmpty(alias)) {

            HLog.i(TAG, "别名为空");
            return;
        }
        if (!StringUtils.isValidTagAndAlias(alias)) {
            HLog.i(TAG, alias);
            return;
        }

        // 调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * 设置别名
     *
     * @param alias    String 字符串
     *                 null 此次调用不设置此值。（注：不是指的字符串"null"）。
     *                 "" （空字符串）表示取消之前的设置。每次调用设置有效的别名，覆盖之前的设置。
     *                 有效的别名组成：字母（区分大小写）、数字、下划线、汉字。限制：alias 命名长度限制为 40 字节。
     *                 （判断长度需采用UTF-8编码）
     * @param callBack 设置别名后的回调  ◦在 TagAliasCallback 的 gotResult 方法，
     *                 返回对应的参数 alias, tags。并返回对应的状态码：0为成功，其他返回码请参考错误码定义。
     */
    public void setAlias(String alias, final TagAliasCallback callBack) {

        HLog.i(TAG, alias);
        if (TextUtils.isEmpty(alias)) {

            HLog.i(TAG, "别名为空");
            return;
        }
        if (!StringUtils.isValidTagAndAlias(alias)) {
            HLog.i(TAG, alias);
            return;
        }
        HLog.d(TAG, "Set alias in handler.");
        JPushInterface.setAliasAndTags(mContext.getApplicationContext(),
                alias, null, callBack);

    }

    /**
     * 设置标签
     *
     * @param tag ◦null 此次调用不设置此值。（注：不是指的字符串"null"）
     *            ◦空数组或列表表示取消之前的设置。◦每次调用至少设置一个 tag，覆盖之前的设置，不是新增。
     *            ◦有效的标签组成：字母（区分大小写）、数字、下划线、汉字。
     *            ◦限制：每个 tag 命名长度限制为 40 字节，最多支持设置 100 个 tag，但总长度不得超过1K字节。
     *            （判断长度需采用UTF-8编码）
     */
    public void setTag(String tag) {
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            HLog.i(TAG, "标签为空");
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<>();
        for (String sTagItme : sArray) {
            if (!StringUtils.isValidTagAndAlias(sTagItme)) {
                HLog.i(TAG, "标签无效");
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    /**
     * 设置标签
     *
     * @param tag      ◦null 此次调用不设置此值。（注：不是指的字符串"null"）
     *                 ◦空数组或列表表示取消之前的设置。◦每次调用至少设置一个 tag，覆盖之前的设置，不是新增。
     *                 ◦有效的标签组成：字母（区分大小写）、数字、下划线、汉字。
     *                 ◦限制：每个 tag 命名长度限制为 40 字节，最多支持设置 100 个 tag，但总长度不得超过1K字节。
     *                 （判断长度需采用UTF-8编码）
     * @param callback ◦在 TagAliasCallback 的 gotResult 方法，返回对应的参数 alias, tags。
     *                 并返回对应的状态码：0为成功，其他返回码请参考错误码定义。
     */
    public void setTag(String tag, final TagAliasCallback callback) {
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            HLog.i(TAG, "标签为空");
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<>();
        for (String sTagItme : sArray) {
            if (!StringUtils.isValidTagAndAlias(sTagItme)) {
                HLog.i(TAG, "标签无效");
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        JPushInterface.setAliasAndTags(mContext.getApplicationContext(), null,
                tagSet, callback);

    }

    /**
     * 设置通知提示方式 - 基础属性
     *
     * @param context    activity context
     * @param drawableId 状态栏图标
     */

    public void setStyleBasic(Context context, int drawableId) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = drawableId;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    /**
     * @param context              activity context
     * @param layoutId             布局id
     * @param iconId               图标ID
     * @param titleId              题目ID
     * @param textId               textID
     * @param layoutIconDrawableId 下拉状态栏时显示的通知图
     * @param statusBarDrawableId  指定最顶层状态栏小图标
     */

    public void setStyleCustom(Context context, int layoutId, int iconId, int titleId, int textId, int layoutIconDrawableId, int statusBarDrawableId) {
        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context, layoutId, iconId, titleId, textId);
        // 指定下拉状态栏时显示的通知图
        builder.layoutIconDrawable = layoutIconDrawableId;
        // 指定最顶层状态栏小图标
        builder.statusBarDrawable = statusBarDrawableId;

        builder.developerArg0 = "developerArg2";
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    /**
     * 设置允许接收通知时间
     *
     * @param startTime 允许推送的开始时间 （24小时制：startHour的范围为0到23）
     * @param endTime   允许推送的结束时间 （24小时制：startHour的范围为0到23）
     * @param days      0表示星期天，1表示星期一，以此类推。
     *                  （7天制，Set集合里面的int范围为0到6）◦
     *                  Sdk1.2.9 – 新功能:set的值为null,则任何时间都可以收到消息和通知，set的size为0，
     *                  则表示任何时间都收不到消息和通知.
     */
    public void setPushTime(int startTime, int endTime, Set<Integer> days) {

        if (startTime > endTime) {

            return;
        }

        //调用JPush api设置Push时间
        JPushInterface.setPushTime(mContext, days, startTime, endTime);

    }

    /**
     * 默认情况下用户在收到推送通知时，客户端可能会有震动，响铃等提示。
     * 但用户在睡觉、开会等时间点希望为 "免打扰" 模式，也是静音时段的概念。
     *
     * @param startHour   静音时段的开始时间 - 小时 （24小时制，范围：0~23 ）
     * @param startMinute 静音时段的开始时间 - 分钟（范围：0~59 ）
     * @param endHour     静音时段的结束时间 - 小时 （24小时制，范围：0~23
     * @param endMinute   静音时段的结束时间 - 分钟（范围：0~59 ）
     */
    public void setSilenceTime(int startHour, int startMinute, int endHour, int endMinute) {
        JPushInterface.setSilenceTime(mContext, startHour, startMinute, endHour, endMinute);
    }


    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case SET_TAG_AND_ALIAS_SUCCESS:
                    logs = "Set tag and alias success";
                    HLog.i(TAG, logs);
                    break;

                case SETTING_TIMEOUT:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    HLog.i(TAG, logs);
                    if (NetUtil.CheckNetworkState()) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                                1000 * 60);
                    } else {
                        HLog.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    HLog.e(TAG, logs);
            }

            HLog.i(TAG, logs);
        }

    };

    /**
     * initJpush 之后任何地方调用
     *
     * @param maxNum 保留通知栏最多显示的条数
     */
    public void setLatestNotificationNumber(int maxNum) {
        JPushInterface.setLatestNotificationNumber(mContext, maxNum);
    }

    /**
     * @return 是否连接
     */
    public boolean getConnectionState() {
        return JPushInterface.getConnectionState(mContext);
    }


    private final static TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case SET_TAG_AND_ALIAS_SUCCESS:
                    logs = "Set tag and alias success";
                    HLog.i(TAG, logs);
                    break;

                case SETTING_TIMEOUT:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    HLog.i(TAG, logs);
                    if (NetUtil.CheckNetworkState()) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        HLog.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    HLog.e(TAG, logs);
            }

            HLog.i(TAG, logs);
        }

    };
    private final static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    HLog.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(mContext.getApplicationContext(),
                            (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    HLog.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(mContext.getApplicationContext(), null,
                            (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    HLog.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


}
