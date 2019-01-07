package tyxo.mobilesafe;
/**
* @author ly
* @created at 2016/8/2 16:20
* @des : 常数类
*/
public class ConstantsMy {

	public static final int CODE_REQUEST_IMAGE = 3;			//从本地相册获取照片
	public static final int CODE_REQUEST_CAMERA = 4;		//从照相机获取照片
	public static final int CODE_REQUEST_CAMERABIG = 5;		//从照相机获取照片
	public static boolean isUtil = true;					//从照相机获取照片,是否通过三方工具类

	public static final String KEY_WHAT = "key_what";		// 数据类别
	public static final String KEY_ARG1 = "key_arg1";		// 有无数据  1有，0无
	public static final String KEY_ARG2 = "key_arg2";		//有效数据长度 length
	public static final String KEY_OBJ = "key_obj";			// 数据 对象
	public static final String KEY_BOOL = "key_bool";		//Boolean型数值
	public static final String KEY_ENTITY = "key_entity";	//Serializable   实体对象
	public static final String KEY_STRING = "key_string";	//String字符


}
