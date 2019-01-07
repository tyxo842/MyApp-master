package tyxo.mobilesafe.utils.file;

import android.os.Environment;

import java.io.File;

/**
 * @ClassName:FileLocalManager
 *  应用本地文件夹管理类
 * @date 2015-05-04 16:50
 */
public class FileLocalManager {

	/**
	 * 本地文件夹根目录 ： sdcard.../haihong/
	 * 
	 * @param value
	 *            Environment.getExternalStorageDirectory()+ "/haihong/"
	 */
	public static final String ROOT_FILE_DIRECTORY = Environment
			.getExternalStorageDirectory() + "/haihong/";

	/**
	 * 本地文件 app存储目录 sdcard.../haihong/apps/
	 */
	public static final String FILE_APPS_DIRECTORY = ROOT_FILE_DIRECTORY
			+ "/apps/";
	/**
	 * 本地文件 临时缓存目录 sdcard.../haihong/cache/
	 */
	public static final String FILE_CACHE_DIRECTORY = ROOT_FILE_DIRECTORY
			+ "/cache/";
	/**
	 * 本地文件 日志目录 sdcard.../haihong/log/
	 */
	public static final String FILE_LOG_DIRECTORY = ROOT_FILE_DIRECTORY
			+ "/log/";
	/**
	 * 本地文件 下载文件目录 sdcard.../haihong/download/
	 */
	public static final String FILE_DOWNLOAD_DIRECTORY = ROOT_FILE_DIRECTORY
			+ "/download/";
	/**
	 * 本地文件 图片目录 sdcard.../haihong/image/
	 */
	public static final String FILE_IMAGE_DIRECTORY = ROOT_FILE_DIRECTORY
			+ "/image/";
	/**
	 * 本地文件 数据目录 sdcard.../haihong/data/
	 */
	public static final String FILE_DATA_DIRECTORY = ROOT_FILE_DIRECTORY
			+ "/data/";

	public static FileLocalManager manager;

	public FileLocalManager getInstance() {
		if (manager == null)
			manager = new FileLocalManager();
		return manager;
	}
    /**
     * 获取root文件夹
     * @return
     */
	public File getRootFile() {
		File file = new File(ROOT_FILE_DIRECTORY);
		if (!file.exists())
			file.mkdirs();
		return file;

	}
	/**
	 * 获取文件目录
	 * @param filedir
	 * @return
	 */
	public File getFileDirctory(String filedir) {
		File file = new File(filedir);
		if (!file.exists())
			file.mkdirs();
		return file;
	}
	 
	/**
	 * 获取清除本地文件目录
	 * @return
	 */
	public String[] clearUserFileDir(){
		return new String[]{FILE_CACHE_DIRECTORY,FILE_LOG_DIRECTORY,FILE_DOWNLOAD_DIRECTORY,FILE_IMAGE_DIRECTORY};
	}

}
