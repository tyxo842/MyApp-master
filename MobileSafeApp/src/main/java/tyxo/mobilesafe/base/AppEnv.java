package tyxo.mobilesafe.base;

import android.content.Context;


/**
 * Created on 2019/1/5.
 * 注入application上下文,供调用方使用;
 */

public class AppEnv {

    public static Context mAppContext;

    /**
     * initialize the context
     *
     * @param context
     */
    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is not null");
        }
        mAppContext = context.getApplicationContext();
    }
}
