package tyxo.mobilesafe.activity.activityGrid;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.widget.snakemenu.TumblrRelativeLayout;

/**
 * Created by LY on 2016/7/29 16: 04.
 * Mail      1577441454@qq.com
 * Describe : 沉浸式 模式
 */
public class Aciticity4 extends AppCompatActivity {

    private View.OnClickListener menuClickListener; //menu 点击事件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gride4_snake);

        initView();
    }


    private void initView() {
        menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToastS(Aciticity4.this,"menu 点击了");
            }
        };
        TumblrRelativeLayout rootLayout = (TumblrRelativeLayout) findViewById(R.id.activity4_snake_menu_trl);
        rootLayout.setMenuListener(menuClickListener);
    }

    /**设置沉浸式*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
