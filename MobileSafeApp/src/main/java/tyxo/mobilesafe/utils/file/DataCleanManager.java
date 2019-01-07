package tyxo.mobilesafe.utils.file;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.MessageFormat;

import tyxo.mobilesafe.base.interfacehh.IDialogAction;
import tyxo.mobilesafe.db.DatabaseHelper;
import tyxo.mobilesafe.net.volley.VolleyManager;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * 应用文件数据清除管理工具
 *
 * 2015-5-4 11:08
 */
public class DataCleanManager implements IDialogAction {
    private static final String TAG = "DataCleanManager";

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        HLog.i(TAG, "缓存路径：" + context.getCacheDir());
        /* /data/data/com.haihong.haihongtradeapp/cache */
        HLog.i(TAG, "删除webview缓存文件..."); // Android 4.4.2中
        // WebView的缓存文件不在cache文件夹下
        deleteFilesByDirectory(context.getCacheDir());

        // Android 4.4 以后，
        // The new Chromium WebView uses the internal cache dir + "app_webview"
        // to store cache, cookies and local storage by default.
        if (Build.VERSION.SDK_INT >= 18) {
            String appWebViewPath = MessageFormat.format("{0}/app_webview",
                    context.getCacheDir().getParent());
            HLog.i(TAG, "4.4+webview缓存文件路径..." + appWebViewPath); // Android
            // 4.4.2中
            // WebView的缓存文件不在cache文件夹下
            deleteFilesByDirectory(new File(appWebViewPath));
        }
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {

//		Log.i(TAG,
//				"数据库路径："
//						+ context.getDatabasePath(DatabaseHelper.DATABASE_NAME)
//								.getParentFile().getPath());
		/* /data/data/com.haihong.haihongtradeapp/databases */
        HLog.i(TAG, "删除database缓存文件...");
        deleteFilesByDirectory(context.getDatabasePath(
                DatabaseHelper.DATABASE_NAME).getParentFile());
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        // Log.i(TAG, "文件路径："+context.getFilesDir().toString());
        HLog.i(TAG, "删除files缓存文件...");
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        // Log.i(TAG, "SD卡路径："+context.getExternalCacheDir().getPath());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     *
     * @param context
     * @param filepaths
     */
    public static void cleanApplicationData(final Context context,
                                            final String... filepaths) {

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ToastUtil.showToastS(context, "数据清除成功");
                } else {
                    ToastUtil.showToastS(context, "数据清除失败");
                }
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {

                    cleanInternalCache(context);
                    cleanExternalCache(context);
                    cleanFiles(context);

                    // 清除Imageloader 本地缓存
//					ImageLoader.getInstance().clearDiskCache();
                    // 清除Imageloader 内存缓存
//					ImageLoader.getInstance().clearMemoryCache();
                    // 清除网络访问数据缓存
                    VolleyManager.getInstance(context).clear();

                    // 清除 自定义文件缓存
                    for (String filePath : filepaths) {
                        cleanCustomCache(filePath);
                    }

                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 清除应用用户所有的数据
     *
     * @param context
     * @param filepaths
     * @author chenhn 2015-5-4
     */
    public static void cleanUserData(Context context, String... filepaths) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        // cleanSharedPreference(context);
        cleanFiles(context);
        // DataModel.getInstance().deleteTopic();

		/*
		 * // 清除 数据库缓存 DatabaseManager dbManager = new DatabaseManager(context);
		 * dbManager.clearUserData();
		 */

        // 清除Imageloader 本地缓存
        ImageLoader.getInstance().clearDiskCache();
        // 清除Imageloader 内存缓存
        ImageLoader.getInstance().clearMemoryCache();
        // 清除网络访问数据缓存
        VolleyManager.getInstance(context).clear();

        // 清除 自定义文件缓存
        for (String filePath : filepaths) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 递归删除目录所有文件 文件/文件夹
     *
     * @param file
     * @author chenhn 2015-5-4
     */
    public static void deleteFile(File file) {

        // Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            // HLog.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 递归删除
     *
     * @param dir
     */
    private static int deleteFilesByDirectory(File dir) {

        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {

                    // first delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += deleteFilesByDirectory(child);
                    }

                    // then delete the files and subdirectories in this dir
                    // only empty directories can be deleted, so subdirs have
                    // been done first
                    if (child.delete()) {
                        deletedFiles++;
                    }

                    HLog.i(TAG, "删除缓存文件..." + child.getName());
                }
            } catch (Exception e) {
                HLog.e(TAG,String.format("Failed to clean the cache, error %s",e.getMessage()));
            }
        }

        return deletedFiles;
    }

    /**
     * 弹出窗口，点击确定后执行，清除数据
     */
    @Override
    public void PositiveAction(Context context, String... filepath) {
        cleanApplicationData(context, filepath);
    }

}
