package tyxo.mobilesafe.base.abstracthh;

public abstract class EntityBase {

	// 如果主键没有命名名为id或_id的时，需要为主键添加此注解
	// @Id(column = "_id")
	// int,long类型的id默认自增，不想使用自增时添加此注解
	// @NoAutoIncrement
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	 

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}


//	public static LoginInfo info;
//
//	public synchronized static LoginInfo getLoginInfo(Context context, boolean isRead) {
//		if (info == null)
//			info = new LoginInfo();
//
//		if (info.userName == null || isRead) {
//			SharedPreferences mSharedPreferences_UserName = context
//					.getSharedPreferences(ConstValues.USER_DATA_FILE,
//							Context.MODE_PRIVATE);
//			info.userName = mSharedPreferences_UserName
//					.getString("userName", "");
//			info.pwd = mSharedPreferences_UserName.getString("pwd", "");
//		}
//		return info;
//	}
	 
}
