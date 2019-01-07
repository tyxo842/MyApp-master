package tyxo.mobilesafe.utils.file;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashSet;

import tyxo.mobilesafe.base.MyApp;


/**
 * @ClassName: SdCardMountReceiverh
 * SD卡插拔处理接收器
 * @date 2015-04-15 11:22
 */
public class SdCardMountReceiver {
	
	public interface SdCardMountChangeListener {
		public void onReceive(Context context, Intent intent);
	}
	
	private static final String TAG = SdCardMountReceiver.class.getSimpleName();
	private static SdCardMountReceiver mInstance = null;
	private static boolean isRegisteReceiver = false;
	
	private static HashSet<SdCardMountChangeListener> mSet = new HashSet<SdCardMountChangeListener>();

	public static void add(SdCardMountChangeListener listener) {
		if (listener != null) {
			synchronized (mSet) {
				mSet.add(listener);
			}
		}
	}
	
	public static void remove(SdCardMountChangeListener listener) {
		if (listener != null) {
			synchronized (mSet) {
				mSet.remove(listener);
			}
		}
	}
		
	
	private SdCardMountReceiver() {

	}

	public static SdCardMountReceiver getInstance() {
		if (mInstance == null) {
			mInstance = new SdCardMountReceiver();
		}
		return mInstance;
	}
	
	private static final BroadcastReceiver sdCardMountReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			synchronized (mSet) {
				for (SdCardMountChangeListener listener : mSet) {
					listener.onReceive(context, intent);
				}
			}
//			String action = intent.getAction();
//			mSaveDataRootPath = getStorePath(AppContext.get(), "file");
//			if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
//
//			} else {
//				if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
//					// 暂停所有正在下载的任务
//				}
//			}
		}
	};

	public void register() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);// 扩展介质（扩展卡）已经从 SD
															// 卡插槽拔出，但是挂载点
															// (mount point)
															// 还没解除 (unmount)
		filter.addAction(Intent.ACTION_MEDIA_EJECT);// 用户想要移除扩展介质（拔掉扩展卡）
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);// 扩展介质被插入，而且已经被挂载
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);// 扩展介质被移除。
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);// 扩展介质存在，但是还没有被挂载
														// (mount)
		filter.addDataScheme("file");// 这个很重要
		MyApp.getAppContext().registerReceiver(sdCardMountReceiver, filter);
		
		isRegisteReceiver = true;
	}

	public static void unregister() {
		if (isRegisteReceiver) {
			MyApp.getAppContext().unregisterReceiver(sdCardMountReceiver);
			isRegisteReceiver = false;
		}
	}
}

