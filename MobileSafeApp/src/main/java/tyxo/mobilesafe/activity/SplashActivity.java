package tyxo.mobilesafe.activity;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import tyxo.mobilesafe.MainActivity;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.BaseActivity;
import tyxo.mobilesafe.utils.AnimationUtil;
import tyxo.mobilesafe.utils.StatusBarUtil;

/**
 * Created by LY on 2016/7/12 16: 04.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class SplashActivity extends BaseActivity{

    private ImageView splashView;
    long milliseconds = 1500;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setImmersedStateBar(this);    /**真正的 沉浸式 状态栏 和 底部导航栏*/
    }

    @Override
    public void initView() {
        super.initView();
        splashView = (ImageView) findViewById(R.id.splash_view);
        //开始执行动画,开始跳转
        startScaleAnimation();
    }

    private void startScaleAnimation() {
        /** 设置位移动画 向右位移150 */
        ScaleAnimation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(milliseconds);//设置动画持续时间
        animation.setFillAfter(true);
        splashView.setAnimation(animation);
        animation.startNow();
        AnimationUtil.setAnimationListener(animation, new AnimationUtil.AnimListener() {
            @Override
            public void onAnimFinish() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                //关闭当前页面
                SplashActivity.this.finish();
                // 设置切换动画，从右边进入，右边退出
                SplashActivity.this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });
    }
}
