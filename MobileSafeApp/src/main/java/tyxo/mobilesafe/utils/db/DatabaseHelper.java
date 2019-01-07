package tyxo.mobilesafe.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


	/* DatabaseHelper*/
	private static DatabaseHelper instance;
	/* 数据库版本*/
	private static final int DATABASE_VERSION = 1;
	/* 数据库名称*/
	public static final String DATABASE_NAME = "data.db";
	/* 标签*/
	private static final String TAG = DatabaseHelper.class.getSimpleName();


	
	public synchronized static DatabaseHelper getInstance(Context context) {
		if(instance == null) {
			instance = new DatabaseHelper(context.getApplicationContext());
		}
		return instance;
	}

	public DatabaseHelper(Context context) {
		/*Create a helper object to create, open, and/or manage a database. 
		  The database is not actually created or opened until one of getWritableDatabase() 
		  or getReadableDatabase() is called.*/
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	private DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	// 数据库第一次被创建时执行onCreate
	// 创建新表类后，需要在这里执行onCreate方法
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE IF NOT EXISTS local_hh_news"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, oid INT, title VARCHAR, content VARCHAR, linkImg VARCHAR, publishDate DATE)");
	}

	// 数据库版本改动后，执行onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "升级数据库从版本 " + oldVersion + " 升级到 "+ newVersion);
		db.execSQL("DROP TABLE IF EXISTS local_hh_news");
		onCreate(db);

	}
	
	/**
	 * 清除用户数据
	 * 
	 * @author chenhn 2015-5-4
	 * @param db
	 */
	public void clearUserData(SQLiteDatabase db) {
		// db.execSQL("DETELE TABLE IF EXISTS table");
	}
}