package tyxo.mobilesafe.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import tyxo.mobilesafe.R;

public class AnimationUtil {

    public static void setAnimationListener(Animation aninm, final AnimListener listener) {
        aninm.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onAnimFinish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    public interface AnimListener {
        void onAnimFinish();
    }

    /** 主界面 grid 动画 */
    public static void setMainGridAnimtion(Context context, ViewGroup view){
        //初始化一个布局动画
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.main_grid_item_anim));
        lac.setOrder(LayoutAnimationController.ORDER_RANDOM);//设置顺序 这里设置为随机
        view.setLayoutAnimation(lac);                        //设置一个布局动画
        view.startLayoutAnimation();                         //开启动画
    }
}





























