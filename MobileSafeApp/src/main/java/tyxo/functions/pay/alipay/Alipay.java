package tyxo.functions.pay.alipay;

// 支付宝应用支付
// 2012-09-20 14:41:47

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.MyApp;

/**
 *  描  述：支付宝支付工具类
 *  创建时间：16/3/17
 *  describe : 调这个方法(doPayByAlipay):

            private void doPayByAlipay() {
                 Alipay alipay = new Alipay(MyApp.getInstance(), this, HotLineServiceActivity.class,
                 HotLineServicePrepaidActivity.this, mOutTradeNo, mOpenUrl); // 第三个参数是具体业务页面使用到的，2014-1-26

                 //清除产品，否则价格异常
                 Alipay.sProducts.clear();
                 Alipay.OrderProduct product = new Alipay.OrderProduct();
                 product.subject = "专项电话咨询服务费用支付";
                 product.body = "专项电话咨询服务费用支付";
                 product.price = mPayPrice;
                 Alipay.sProducts.add(product);
                 alipay.android_pay(0);
            }
 */

@SuppressLint("HandlerLeak")
public class Alipay {

    public static final String TAG = "alipay-sdk";

    /*private static final int RQF_PAY = 1;
    private static final int RQF_LOGIN = 2;*/
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    public static ArrayList<OrderProduct> sProducts = new ArrayList<OrderProduct>();

    private MyApp mApp;
    private Activity currentActivity;
    Activity mActivity = null;
    Class<?> cls; // 被intent使用的类
    private String openUrl;
    private String outTradeNo;

    private Fragment currentFragment;

    // ===================================
    // JAVA 的接口
    // ===================================
    public Alipay(MyApp mApp, Activity activity, Class<?> cls,
                  Fragment mFragment, String outTradeNo, String openUrl) {

        this.mApp = mApp;
        this.currentFragment = mFragment;
        this.mActivity = activity;
        this.cls = cls;
        this.outTradeNo = outTradeNo;
        this.openUrl = openUrl;
    }

    // ===================================
    // JAVA 的接口
    // ===================================
    public Alipay(MyApp mApp, Activity activity, Class<?> cls,
                  Activity currentActivity, String outTradeNo, String openUrl) {

        this.mApp = mApp;
        this.currentActivity = currentActivity;
        this.mActivity = activity;
        this.cls = cls;
        this.outTradeNo = outTradeNo;
        this.openUrl = openUrl;
    }

    // 这里传过来的是想支付多少钱(最好定义成double的，方便调试，毕竟每次测试都支付几元大洋不是每个人都负担的起的)
    public void android_pay(int position) {
        Log.i("ExternalPartner", "start pay");

        String orderInfo = getOrderInfo(position);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    private String getOrderInfo(int position) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + sProducts.get(position).subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + sProducts.get(position).body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + sProducts.get(position).price.replace("一口价:", "") + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + openUrl
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"2m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        Log.d(TAG, "outTradeNo: " + key);
        return key;
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public static class OrderProduct {
        public String subject;
        public String body;
        public String price;
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.obj == null) {
                Toast.makeText(mActivity, "支付结果返回为空，请进行正确的支付操作", Toast.LENGTH_LONG).show();
                return;
            }

            //Result result = new Result((String) msg.obj);

            switch (msg.what) {

                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();

                        // 支付宝支付成功后，给服务器账户充值
//					doPrepaidTask();

                        payedCallback();


                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mActivity, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mActivity, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mActivity, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void payedCallback() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 跳转到快审查询页面
                Intent intent = new Intent(mActivity, cls);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (currentActivity != null) {

                    currentActivity.startActivityForResult(intent,101);
                    // 设置切换动画，从左边进入，左边退出
                    currentActivity.overridePendingTransition(
                            R.anim.slide_right_in, R.anim.slide_right_out);
                } else {
                    currentFragment.startActivityForResult(intent,
                            101);
                }
            }
        }, 1000);
    }

    private static final int SUCCESSED = 1;
    private static final int FAILED = 0;

    private void doPrepaidTask() {

    }


    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mActivity);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, Keys.PRIVATE);
    }
}
