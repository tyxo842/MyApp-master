package tyxo.mobilesafe.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.TrafficStats;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import tyxo.mobilesafe.base.AppEnv;

public class NetUtil {
	/**
	 * 检查网络状态
	 */
	public static boolean CheckNetworkState() {

		ConnectivityManager manager = (ConnectivityManager) AppEnv.mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		// 如果3G、WIFI、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		// 在MI Pad上测试，遇到mMobile为null的情况，所以这里有必要加是否为空判断。
		if (mMobile != null){
			if(mMobile.getState() == State.CONNECTED || mMobile.getState() == State.CONNECTING)
				return true;
		}
		if (mWifi != null){
			if(mWifi.getState() == State.CONNECTED || mWifi.getState() == State.CONNECTING)
				return true;
		}

		return false;
	}

	/**
	 * 监控网络流量
	 */
	public static void MonitorTrafficStats(final Context context) {

		Runnable app = new Runnable() { // 线程对象

			private int flag = 0;

			@Override
			public void run() {
				while (flag == 0) {// 用于停止线程时的判断
					try {
						Thread.sleep(3000);// 每1000ms进行流量监控
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						PackageManager pManager = context.getPackageManager();

						List<PackageInfo> packinfos = pManager
								.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
										| PackageManager.GET_PERMISSIONS);

						for (PackageInfo info : packinfos) {
							String[] premissions = info.requestedPermissions;
							if (info.packageName.equalsIgnoreCase("com.haihong.framework")) {
								if (premissions != null
										&& premissions.length > 0) {
									for (String premission : premissions) {
										if ("android.permission.INTERNET".equals(premission)) {
											// System.out.println(info.packageName+"访问网络");
											int uid = info.applicationInfo.uid;
											long rx = TrafficStats.getUidRxBytes(uid);
											long tx = TrafficStats.getUidTxBytes(uid);
											if (rx < 0 || tx < 0) {
												Log.i("a", info.packageName + "没有产生流量");
											} else {
												Log.i("a", info.packageName+ "的流量信息:");
												Log.i("a","下载的流量"+ Formatter.formatFileSize(context,rx));
												Log.i("a","上传的流量"+ Formatter.formatFileSize(context,tx));
											}
										}
									}
								}
							}
						}
					} catch (Exception e) {
						System.out.println("监控流量异常");
					}
				}
			}
		};

		Thread AppThread = new Thread(app);
		AppThread.start();
	}
	
	/**
	 * 显示网络状态及设置
	 * @param context
	 */
	public static void showTips(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("没有可用网络");
		builder.setMessage("当前网络不可用，是否设置网络？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 如果没有网络连接，则进入网络设置界面
				context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				//				AccountManageActivity.this.finish();
			}
		});
		builder.create();
		builder.show();
	}
	
	/**
	 * 从指定路径获取文件流
	 * @param path
	 * @throws Exception
	 */
	public static InputStream getInputStreamWithURL(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000); 
		return conn.getInputStream(); 
	}
	
	/**
	 * 从指定路径获取文件流
	 * @param context
	 * @param resid URL资源id
	 * @return
	 * @throws Exception
	 */
	public static InputStream getInputStreamWithURL(Context context, int resid) throws Exception{
		String path = context.getResources().getString(resid);
		return getInputStreamWithURL(path);
	}
	
	private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时30秒钟
	private static final int SO_TIMEOUT = 60 * 1000; // 设置等待数据超时时间60秒钟 (socket timeout)
//	/**
//	 * 初始化HttpClient，并设置超时
//	 * @return
//	 */
//	public static HttpClient getHttpClient() {
//		BasicHttpParams httpParams = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
//		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
//		HttpClient client = new DefaultHttpClient(httpParams);
//		return client;
//	}
}
