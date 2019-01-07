package tyxo.mobilesafe.net.volley;


import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.AppEnv;
import tyxo.mobilesafe.utils.log.HLog;

/** Volley的管理器 */
public class VolleyManager {

    // ---------added by chenhn start---------
    private String VOLLEY_TAG = "volley";
    /** 请求超时错误 VOLLEY_TIMEOUT_ERROR = 408; */
    public static int VOLLEY_TIMEOUT_ERROR = 408;

    /** 解析错误 VOLLEY_PARSE_ERROR = 0x01; */
    public static int VOLLEY_PARSE_ERROR = 0x01;

    /** 无法访问错误 VOLLEY_NETWORK_ERROR = 0x02; */
    public static int VOLLEY_NETWORK_ERROR = 0x02;

    /** 认证失败错误 VOLLEY_AUTHFAILURE_ERROR = 0x03; */
    public static int VOLLEY_AUTHFAILURE_ERROR = 0x03;

    /** 客户端错误 VOLLEY_CLIENT_ERROR = 0x04; */
    public static int VOLLEY_CLIENT_ERROR = 0x04;

    /** 远程服务器错误 VOLLEY_SERVER_ERROR = 0x05; */
    public static int VOLLEY_SERVER_ERROR = 0x05;

    /** 网络无连接错误 VOLLEY_NO_CONNECT_ERROR = 0x06; */
    public static int VOLLEY_NO_CONNECT_ERROR = 0x06;

    // ---------added by chenhn end---------

    public final String TAG = this.getClass().getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleyManager(Context context) {
        mCtx = context;
        // 获取请求队列
        mRequestQueue = getRequestQueue();
        // 初始化图像加载器
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(mCtx));
    }

    private static VolleyManager mInstance;
    private static Object lock = new Object();

    /** 返回VolleyManager的实例 */
    public static VolleyManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null)
                    mInstance = new VolleyManager(context);
            }
        }
        return mInstance;
    }

    // 懒加载请求队列
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /** 将请求添加到请求队列 */
    public <T> void addToRequestQueue(Request<T> request) { getRequestQueue().add(request); }

    /**
     * 添加请求队列
     *
     * @param req 网络请求对象
     * @param tag 标识网络请求的tag，可用该tag取消对应的请求
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * 获取图片加载器 使用方法： mImageLoader =
     * VolleyManager.getInstance(this).getImageLoader();
     * mImageLoader.get(IMAGE_URL, ImageLoader.getImageListener(mImageView,
     * R.drawable.def_image, R.drawable.err_image));
     * 或者使用Volley的NetworkImageView，
     * 详见https://developer.android.com/training/volley/request.html
     *
     * @return
     */
    public ImageLoader getImageLoader() { return mImageLoader; }

    // ---------added by chenhn start---------

    /** 开始所有任务 */
    public void start() { mRequestQueue.start(); }

    /** 停止所有任务 */
    public void stop() { mRequestQueue.stop(); }

    /** 取消所有访问任务 */
    public void cancelAll() { mRequestQueue.cancelAll(VOLLEY_TAG); }

    /** 取消指定URL相关任务 */
    public void cancel(final String url) {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {
                if (request.getCacheKey().equals(url)) {
                    HLog.e(TAG, "apply  true");
                    return true;
                }
                HLog.e(TAG, "apply  false");
                return false;
            }
        });
    }

    /**
     * 检查请求是否已经结束
     *
     * @param url 网络请求地址
     * @return 返回false，说明请求任务进行中，返回true，说明请求已完成或者不存在
     */
//     public boolean requestDelivered(Object url) { return  mRequestQueue.requestDelivered(url); }

    /** 清除网络数据缓存 */
    public void clear() { mRequestQueue.getCache().clear(); }

    /**
     * 发送Get请求
     *
     * @param url      访问url
     * @param callBack 回调接口
     */
    public void getJson(String url, final VolleyCallBack<JSONObject> callBack) {
        MyJsonRequest jsonObjectRequest = new MyJsonRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                 HLog.i(TAG, "onResponse  response = " + response.toString());
                if (callBack != null)
                    callBack.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HLog.i(TAG, "onErrorResponse error = " + error);
                if (callBack != null)
                    callBack.onErrorResponse(getErrorResult(error));
            }
        });
        jsonObjectRequest.setTag(VOLLEY_TAG);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * 发送Get请求 带Header
     *
     * @param url      访问url
     * @param callBack 回调接口
     */
    public void getJsonWithHeader(String url, final VolleyCallBack<JSONObject> callBack, final Map <String,String> header) {
        MyJsonRequest jsonObjectRequest = new MyJsonRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                 HLog.i(TAG, "onResponse  response = " + response.toString());
                if (callBack != null)
                    callBack.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HLog.i(TAG, "onErrorResponse error = " + error);
                if (callBack != null)
                    callBack.onErrorResponse(getErrorResult(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers==null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }
                if (header != null || !headers.equals(Collections.emptyMap())) {
                    Iterator it = header.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        Object key = entry.getKey();
                        Object value = entry.getValue();
                        headers.put((String)key, (String) value);
                    }
                } else {
                    //headers.put("apikey","2600907be4021f9979ecc9554a4065ac");
                }
                //headers.put("Charset", "UTF-8");
                //headers.put("Content-Type", "application/x-javascript");
                //headers.put("Accept-Encoding", "gzip,deflate");
                return headers;
                //return super.getHeaders();
            }
        };
        jsonObjectRequest.setTag(VOLLEY_TAG);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * 发送Post请求
     *
     * @param url      访问url
     * @param json     请求参数，JSONObject对象
     * @param callBack 回调接口
     */
    public void postJson(String url, JSONObject json,
                         final VolleyCallBack<JSONObject> callBack) {
        HLog.i(TAG, "url = " + url);
        HLog.i(TAG, "json.toString()=" + json.toString());
        MyJsonRequest jsonObjectRequest = new MyJsonRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                HLog.i(TAG, "response.toString() = " + response.toString());
                if (callBack != null)
                    callBack.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HLog.i(TAG, "onErrorResponse error = " + error);
                if (callBack != null)
                    callBack.onErrorResponse(getErrorResult(error));
            }
        });
        jsonObjectRequest.setTag(VOLLEY_TAG);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjectRequest);

    }

    /**
     * 发送Post请求
     *
     * @param url      访问url
     * @param map      map集合存储的请求参数
     * @param callBack 回调接口
     */
    public void postMap(String url, final Map<String, String> map,
                        final VolleyCallBack<String> callBack) {

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        HLog.i(TAG,
                                "onResponse  response = " + response.toString());
                        if (callBack != null)
                            callBack.onResponse(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HLog.i(TAG, "onResponse  error = " + error);
                if (callBack != null)
                    callBack.onErrorResponse(getErrorResult(error));
            }

        }) {
            // 重写Request的getParams方法，将Map集合中的数据解析为http可传输的字符串参数
            // key=value&key=value&...
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        request.setTag(VOLLEY_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);
    }

    /**
     * 上传文件
     *
     * @param url 上传url
     * @param uploadfile 上传文件
     * @param json 上传信息
     * @param callBack 回调接口
     * @param uploadListener 上传进度监听
     *
     */
//	 public void uploadFile(String url, File uploadfile, String json, final VolleyCallBack<File>
//             callBack, ProgressUpLoadListener uploadListener) {
//
//	 FileRequest fileRequest = new FileRequest(url, json,
//	 uploadfile.getAbsolutePath(), false, new Response.Listener<File>() {
//
//	 @Override
//     public void onResponse(File response) {
//         HLog.i(TAG, "onResponse  response = " + response.toString());
//
//         callBack.onResponse(response); } }, new Response.ErrorListener() {
//
//	  @Override
//      public void onErrorResponse(VolleyError error) {
//          HLog.i(TAG, "onResponse  error = " + error);
//          callBack.onErrorResponse(getErrorResult(error)); } });
//          fileRequest.setProgressUploadListener(uploadListener);
//          fileRequest.setTag(VOLLEY_TAG); mRequestQueue.add(fileRequest);
//     }
	 /**
     * 上传文件
     *
     * @param url 上传url
     * @param uploadfile 上传文件
     * @param json 上传信息
     * @param callBack 回调接口
     * @param uploadListener 上传进度监听
     *
     */
//	  public void uploadFile(String url, String uploadfile, String json, final
//	  VolleyCallBack<File> callBack, ProgressUpLoadListener uploadListener) {
//
//	  File file = new File(uploadfile); if (!file.exists()) {// 上传文件不存在
//	  VolleyError error = new VolleyError("Upload file " +
//	  file.getAbsolutePath() + " does not exist");
//	  callBack.onErrorResponse(getErrorResult(error)); return; }
//
//	  uploadFile(url, file, json, callBack, uploadListener); }

    /**
     * 下载文件
     *
     * @param url
     *            下载url
     * @param filename
     *            文件存放路径
     * @param callBack
     *            回调接口
     * @param downListener
     *            下载进度监听
     */
//	 public void downFile(String url, String filename, final
//	 VolleyCallBack<File> callBack, ProgressDownListener downListener) {
//	 FileRequest fileRequest = new FileRequest(Method.GET, url, null,
//	 filename, true, new Listener<File>() {
//
//	 @Override public void onResponse(File response) { HLog.i(TAG,
//	 "onResponse  response = " + response.getAbsolutePath());
//
//	 callBack.onResponse(response); } }, new Response.ErrorListener() {
//
//	 @Override public void onErrorResponse(VolleyError error) { HLog.i(TAG,
//	 "onResponse  error = " + error);
//	 callBack.onErrorResponse(getErrorResult(error)); } });
//	 fileRequest.setProgressDownListener(downListener);
//     fileRequest.setTag(VOLLEY_TAG); mRequestQueue.add(fileRequest); }


    // volley 图片加载: http://www.apihome.cn/view-detail-70212.html

    /**
     * <pre>
     * 1. imageRequest 请求图片
     *
     * ImageRequest imageRequest = new ImageRequest(
     * 		&quot;http://developer.android.com/images/home/aw_dac.png&quot;,
     * 		new Response.Listener&lt;Bitmap&gt;() {
     * 			&#064;Override
     * 			public void onResponse(Bitmap response) {
     * 				imageView.setImageBitmap(response);
     *            }
     *        }, 0, 0, Config.RGB_565, new Response.ErrorListener() {
     * 			&#064;Override
     * 			public void onErrorResponse(VolleyError error) {
     * 				imageView.setImageResource(R.drawable.default_image);
     *            }
     *        });
     * mQueue.add(imageRequest);
     * </pre>
     *
     *
     */
     /**
     * <pre>
     * 2. ImageLoader用法
     * 1. 创建一个RequestQueue对象。
     *
     * 2. 创建一个ImageLoader对象。
     *
     * 3. 获取一个ImageListener对象。
     *
     * 4. 调用ImageLoader的get()方法加载网络上的图片。
     *
     *
     * ImageLoader imageLoader = new ImageLoader(mQueue, new ImageCache() {
     *        @Override
     *         public void putBitmap(String url, Bitmap bitmap) {
     *       }
     *
     *         @Override
     *          public Bitmap getBitmap(String url) {
     *            return null;
     *         }
     *          });
     *
     *
     * ImageListener listener = ImageLoader.getImageListener(imageView,
     *         R.drawable.default_image, R.drawable.failed_image);
     *
     *
     *         imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener);
     * </pre>
     *
     */
     /**
     * 解析volley 错误
     *
     * @param error volley 错误
     * @return
     * @info 错误状态说明见:
     * http://www.cnblogs.com/lds85930/archive/2009/06/21/1507756.html
     */
    protected VolleyErrorResult getErrorResult(VolleyError error) {
        VolleyErrorResult result = new VolleyErrorResult();

        if (error.networkResponse == null) {
            if (error instanceof TimeoutError) {// 请求超时错误

                result.setCode(VOLLEY_TIMEOUT_ERROR);
                result.setValue(getErrorInfo(R.string.volley_timeout_error));

            } else if (error instanceof NetworkError) {// 网络请求错误

                result.setCode(VOLLEY_NETWORK_ERROR);
                result.setValue(getErrorInfo(R.string.volley_network_error));

            } else if (error instanceof NoConnectionError) {// 网络无连接错误
                result.setCode(VOLLEY_NO_CONNECT_ERROR);
                result.setValue(getErrorInfo(R.string.volley_no_connect_error));
            } else if (error instanceof ServerError) {// 服务端错误

                result.setCode(VOLLEY_SERVER_ERROR);
                result.setValue(getErrorInfo(R.string.volley_server_error));

            } else if (error instanceof AuthFailureError) {// 认证失败错误

                result.setCode(VOLLEY_AUTHFAILURE_ERROR);
                result.setValue(getErrorInfo(R.string.volley_authfailure_error));

            } else if (error instanceof ParseError) {// 解析错误
                result.setCode(VOLLEY_PARSE_ERROR);
                result.setValue(getErrorInfo(R.string.volley_parse_error));
            }
        } else {
            NetworkResponse response = error.networkResponse;
            result.setCode(error.networkResponse.statusCode);
            // 检测网络请求结果
            if (response != null) {
                switch (response.statusCode) {
                    case 400: {// BadRequest 请求出现语法错误
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 401: {// Unauthorized 客户试图未经授权访问受密码保护的页面
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 402: {// PaymentRequired 402年付款要求:保留以供将来使用。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 403: {// Forbidden 资源不可用
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 404: {// NotFound 无法找到指定位置的资源
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 405: {// MethodNotAllowed
                        // 请求方法（GET、POST、HEAD、DELETE、PUT、TRACE等）对指定的资源不适用
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 406: {// NotAcceptable
                        // 指定的资源已经找到，但它的MIME类型和客户在Accpet头中所指定的不兼容（HTTP
                        // 1.1新）。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 407: {// ProxyAuthenticationRequired
                        // 类似于401，表示客户必须先经过代理服务器的授权。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 408: {// RequestTime-out
                        // 在服务器许可的等待时间内，客户一直没有发出任何请求。客户可以在以后重复同一请求。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 409: {// Conflict 通常和PUT请求有关。由于请求和资源的当前状态相冲突，因此请求不能成功。（HTTP
                        // 1.1新）
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 410: {// Gone 所请求的文档已经不再可用，而且服务器不知道应该重定向到哪一个地址。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 411: {// LengthRequired
                        // 服务器不能处理请求，除非客户发送一个Content-Length头。（HTTP 1.1新）
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 412: {// PreconditionFailed 请求头中指定的一些前提条件失败（HTTP 1.1新）。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 413: {// RequestEntityTooLarge 目标文档的大小超过服务器当前愿意处理的大小
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 414: {// Request-URITooLarge URI太长（HTTP 1.1新）。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 415: {// UnsupportedMediaType 指示请求是不支持的类型。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 416: {// Requestedrangenotsatisfiable
                        // 服务器不能满足客户在请求中指定的Range头。（HTTP 1.1新）
                        result.setValue(getErrorInfo(R.string.volley_requested_range_not_satisfiable));
                        break;
                    }
                    case 417: {// ExpectationFailed 指示服务器未能符合 Expect 头中给定的预期值。
                        result.setValue(getErrorInfo(R.string.volley_client_error));
                        break;
                    }
                    case 500: {// InternalServerError 指示服务器上发生了一般错误。
                        result.setValue(getErrorInfo(R.string.volley_server_error));
                        break;
                    }
                    case 501: {// NotImplemented 指示服务器不支持请求的函数。
                        result.setValue(getErrorInfo(R.string.volley_server_error));
                        break;
                    }
                    case 502: {// BadGateway 指示中间代理服务器从另一代理或原始服务器接收到错误响应。
                        result.setValue(getErrorInfo(R.string.volley_server_error));
                        break;
                    }
                    case 503: {// ServiceUnavailable 指示服务器暂时不可用，通常是由于过多加载或维护。
                        result.setValue(getErrorInfo(R.string.volley_server_error));
                        break;
                    }
                    case 504: {// GatewayTimeout 指示中间代理服务器在等待来自另一个代理或原始服务器的响应时已超时。
                        result.setValue(getErrorInfo(R.string.volley_server_error));
                        break;
                    }
                    case 505: {// HttpVersionNotSupported 指示服务器不支持请求的 HTTP 版本。
                        result.setValue(getErrorInfo(R.string.volley_server_error));
                        break;
                    }

                    default: {
                        result.setValue(getErrorInfo(R.string.volley_request_fail_error));

                        break;
                    }
                }
            }
        }
        return result;
    }

    /** 获取错误信息 */
    protected String getErrorInfo(int resid) {
        return AppEnv.mAppContext.getResources().getString(resid);
    }
    // ---------added by chenhn end---------
}
