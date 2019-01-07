package tyxo.mobilesafe.activity.activityGrid;

import android.view.View;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.BaseActivityToolbar;
import tyxo.mobilesafe.utils.StatusBarUtil;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.widget.snakemenu.TumblrRelativeLayout;

/**
 * Created by LY on 2016/7/29 16: 04.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class Aciticity5 extends BaseActivityToolbar {

    private View.OnClickListener menuClickListener; //menu 点击事件

    @Override
    protected void setMyContentView() {
        setContentView(R.layout.activity_gride5_snake);
        StatusBarUtil.setNavigationBar(this);   /**设置导航栏透明*/
        //mToolbar.setBackgroundResource(R.color.transparent);
        //mToolbar.setVisibility(View.GONE);
        mToolbar.setTitle("set navigation");
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToastS(Aciticity5.this,"menu 点击了");
            }
        };
        TumblrRelativeLayout rootLayout = (TumblrRelativeLayout) findViewById(R.id.activity5_snake_menu_trl);
        rootLayout.setMenuListener(menuClickListener);
    }
}
