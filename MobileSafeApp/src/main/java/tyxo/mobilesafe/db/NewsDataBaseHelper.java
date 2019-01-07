package tyxo.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建于2016/3/15 15:50
 */
public class NewsDataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "hhClientApp.db";
    private static final int DATABASE_VERSION = 1;

    NewsDataBaseHelper(Context context) {
        // CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS local_hh_news"
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, oid INT, title VARCHAR, content VARCHAR, linkImg VARCHAR, publishDate DATE)");
    }

    // 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS local_hh_news");
        onCreate(db);
    }
}
