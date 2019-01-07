package tyxo.mobilesafe.base;

import android.support.multidex.MultiDexApplication;
/**
 * @author ly
 * @created at 2016/7/28 11:22
 * @des : 	将修改过后的.class文件打成jar包(正确代码运行之后生成的class文件).
 */

/**
 * 动态改变BaseDexClassLoader对象间接引用的dexElements.
 * 必须在应用启动的时候，将这个hack_dex.jar插入到dexElements，否则肯定会出事故的。
 */
public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;

    /**
     * 用法--->(先将改正后的class文件打成jar包,然后在类加载之前调用方法):
     * <p>
     * [---在自己目录 : E:\WorkPlace_Studio_Test\MobileSafe\hackdex\build\intermediates\classes\debug,运行(生成hack.jar):
     * jar cvf hack.jar dodola/hackdex/*
     * 在自己目录 : E:\StudioSDK\build-tools\23.0.3 , 运行dx命令 , 会在此路径的文件夹下生成 hack_dex.jar.
     * dx  --dex --output hack_dex.jar (此处将hack.jar 拖过来,直接打名字会找不到)---这个已经打包好,不用管.---]
     * <p>
     * 只需打下面修改的包 :
     * 下方两个命令,同上(在固定路径下,命令行运行):
     * jar cvf path.jar dodola/hotfix/BugClass.class
     * dx  --dex --output path_dex.jar path.jar
     * 方法 :
     * File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "path_dex.jar");
     * Utils.prepareDex(this.getApplicationContext(), dexPath, "path_dex.jar");
     * //HotFix.patch(this, dexPath.getAbsolutePath(), "dodola.hotfix.BugClass");
     * HotFix.patch(this, dexPath.getAbsolutePath(), "tyxo.mobilesafe.activity.ImageViewerActivity");
     * ps : 修改 ImageViewerActivity ,上面两个 HotFix.patch 都可以成功!!! why ???
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

		/*// 读取path_dex.jar文件
		File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "hackdex_dex.jar");
		Utils.prepareDex(this.getApplicationContext(),dexPath,"hackdex_dex.jar");	//调用Utils.prepareDex将assets中的hackdex_dex.jar写入该文件.
		HotFix.patch(this,dexPath.getAbsolutePath(),"dodola.hackdex.AntilazyLoad");	//HotFix.patch就是去反射去修改dexElements了.
		try {
			this.getClassLoader().loadClass("dadala.hackdex.AntilazyLoad");
		} catch (ClassNotFoundException e ) {
			e.printStackTrace();
		}*/
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}


















