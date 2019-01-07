package tyxo.mobilesafe.utils.file;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.util.HashSet;

import tyxo.mobilesafe.base.AppEnv;
import tyxo.mobilesafe.utils.log.HLog;


/**
 * @ClassName:FileDownloadPath
 *  文件下载根目录的管理，包含对SD卡插拔情况的处理
 * @date 2015-04-15 11:18
 */
public class FileDownloadPath {

	private static final String ROOT_NAME = "hhcache";
	private static String mRootPath = "";
	private static String mRootPathInternal = "";

	//后面的固定路径都在这里面申明好
	public static final SubPath mImage = new SubPath("cache" + File.separator + "image");// cache/image
	public static final SubPath mLog = new SubPath("cache" + File.separator + "log");// cache/log
	public static final SubPath mFile = new SubPath("file");

	public static final String TAG = "FileDownloadPath";

	public static final SubPath mThumb = new SubPath("cache" + File.separator + "thumb");// cache/thumb


	private static HashSet<SubPath> mSet = new HashSet<SubPath>();

	//这里记得添加，才会跟随SD卡插拔情况
	static {
		mSet.add(mImage);
		mSet.add(mLog);
		mSet.add(mFile);
		mSet.add(mThumb);
	}

	public static void addSubPath(SubPath subPath) {
		mSet.add(subPath);
	}

	private static SdCardMountReceiver.SdCardMountChangeListener mSdCardMountChangeListener = new SdCardMountReceiver.SdCardMountChangeListener() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mRootPath = "";
			mRootPathInternal = "";
			for (SubPath item : mSet) {
				item.reGetPath();
			}
		}
	};

	static {
		SdCardMountReceiver.add(mSdCardMountChangeListener);
	}

	public static class SubPath {

		private String mSubPath = "";
		private String mSubPathInternal = "";
		private String subDirName = "";

		public SubPath(String dirname) {
			subDirName = dirname;
			if (subDirName.startsWith(File.separator)) {
				subDirName = subDirName.substring(1, subDirName.length());
			}
			if (subDirName.endsWith(File.separator)) {
				subDirName = subDirName.substring(0, subDirName.length() - 1);
			}
		}
		
		public String getPath() {
			return getPath(false);
		}

		public String getPath(boolean forceInternal) {
			if (forceInternal) {
				
					// 取下载路径
					mSubPathInternal = getRootPath(forceInternal) + subDirName;
					File f = new File(mSubPathInternal);
					if (!f.exists()) {
						boolean ret = 	f.mkdirs();
					}
					mSubPathInternal = mSubPathInternal + File.separator;
				
				return mSubPathInternal;
			} else {
			
					// 取下载路径
					mSubPath = getRootPath(forceInternal) + subDirName;
					File f = new File(mSubPath);
					if (!f.exists()) {
						boolean ret = 		f.mkdirs();
						HLog.d(TAG, "create dir"+ mSubPath + ret);
					}
					mSubPath = mSubPath + File.separator;
				
				return mSubPath;
			}
		}

		private void reGetPath() {
			mSubPath = null;
			mSubPath = getPath(false);
			mSubPathInternal = null;
			mSubPathInternal = getPath(true);
		}
	}
    /**
     *  获取存储根目录 
     * @param forceInternal
     * @return
     */
	private static String getRootPath(boolean forceInternal) {
		if (forceInternal) {
			if (mRootPathInternal == null || mRootPathInternal.length() == 0) {
				mRootPathInternal = getStorePath(AppEnv.mAppContext, ROOT_NAME, forceInternal);
			}
			return mRootPathInternal;
		} else {
			if (mRootPath == null || mRootPath.length() == 0) {
				mRootPath = getStorePath(AppEnv.mAppContext, ROOT_NAME, forceInternal);
			}
			return mRootPath;
		}
	}
    /**
     * 获得存储文件夹路径值
     * @param context
     * @param path    文件夹名
     * @param forceInternal 是否强制寻找地址
     * @return
     */
	public static String getStorePath(Context context, String path, boolean forceInternal) {
		String absolutePath = "";
		if (forceInternal == false) {
			// 获取SdCard状态
			String state = Environment.getExternalStorageState();
			// 判断SdCard是否存在并且是可用的
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				if (Environment.getExternalStorageDirectory().canWrite()) {
					// // SD根目录/Android/data/包名/,可跟随应用卸载而一起删除，目前一些手机卫士会定期清理这些目录
					// String rootPath =
					// Environment.getExternalStorageDirectory().getAbsolutePath() +
					// File.separator + "Android" + File.separator + "data"
					// + File.separator + context.getPackageName() + File.separator
					// + "cache";
					absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
				}
			}
		}
		if (absolutePath == null || absolutePath.length() == 0) {
			absolutePath = context.getFilesDir().getAbsolutePath();
		}
		File file = new File(absolutePath + File.separator + path);
		if (!file.exists()) {
			file.mkdirs();
		}
		absolutePath = file.getAbsolutePath();
		if (!absolutePath.endsWith(File.separator)) {// 保证以"/"结尾
			absolutePath += File.separator;
		}
		return absolutePath;
	}

}
