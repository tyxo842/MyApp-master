package tyxo.functions.music;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import tyxo.mobilesafe.base.BaseActivityToolbar;

/**
 * Created on 2016/5/30.
 */

public class BaseMusicActivity extends BaseActivityToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断SDK版本是否大于等于19，大于就让他显示，小于就要隐藏，不然低版本会多出来一个
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //setTranslucentStatus();   //添加上,状态栏就没了
            //还有设置View的高度，因为每个型号的手机状态栏高度都不相同
        }
    }

    @Override
    protected void setMyContentView() { }

    @TargetApi(19)
    private void setTranslucentStatus() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;    // 设置Activity高亮显示
        window.setAttributes(params);
    }
}
