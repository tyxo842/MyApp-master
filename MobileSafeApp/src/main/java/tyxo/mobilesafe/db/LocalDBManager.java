package tyxo.mobilesafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LocalDBManager {
	private DatabaseHelper helper;
	private SQLiteDatabase db;


	public LocalDBManager(Context context) {
		helper = DatabaseHelper.getInstance(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		db.close();
	}


	// {{ 新闻管理

	/**
	 * 新增新闻
	 */
	public void addNewsItem(List<?> list) {
		db.beginTransaction(); // 开始事务
		try {
			for (Object item : list) {
				/*db.execSQL(
						"INSERT INTO local_hh_news VALUES(null,?,?, ?, ?, ?)",
						new Object[] { item.OID, item.Title, item.Content, item.LinkImg,
								item.PublishDate.toString() });*/
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 删除消息
	 */
	public void deleteOldNews() {
		db.delete("local_hh_news", "", null);
	}

	/**
	 * 查询本地消息数据，封装数据集
	 */
	public List<?> getLocalStoreNews() {
		ArrayList<?> newList = new ArrayList<>();
		Cursor c = db.rawQuery("SELECT * FROM local_hh_news order by id", null);
		while (c.moveToNext()) {
			/*News item = new News();
			item.OID = c.getString(c.getColumnIndex("oid"));
			item.Title = c.getString(c.getColumnIndex("title"));
			item.Content = c.getString(c.getColumnIndex("content"));
			item.LinkImg = c.getString(c.getColumnIndex("linkImg"));

			String strJson = c.getString(c.getColumnIndex("publishDate"));
			//String strDate = MyHelper.getWCFDateFromJsonDate(strJson);
			String strDate = strJson;
			item.PublishDate = strDate;
			newList.add(item);*/
		}
		c.close();
		return newList;
	}


}