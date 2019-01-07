package tyxo.mobilesafe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 自定义ScrollView
 * <p/>
 * Created by HourGlassRemember on 2016/9/7.
 * by 福建-沙漏  qq: 283930450
 * description: 目标-竖向滑动viewpager,有问题待解决,向上滑不管用(一直向下),向下滑多一页.
 */
public class MyScrollView extends ViewGroup {

    private static final String TAG = "MyScrollView";

    //ViewGroup 的左上点与屏幕左上点的差值，正值代表 ViewGroup 的左上点位于屏幕左上点的上面，反之为下面
    private int mStart, mEnd;
    //当前坐标点在屏幕的值，数值越大代表离屏幕左上点越远，反之越近
    private int mLastY;
    //Scroller 辅助类，可以实现一种惯性的滚动过程和回弹效果
    //要注意的是，Scroller 本身不会去移动 View，它只是一个移动计算辅助类，用于跟踪控件滑动的轨迹，
    //只相当于一个滚动轨迹记录工具，最终还是通过 View 的 scrollTo、scrollBy 方法完成 View 的移动
    private Scroller mScroller;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //使用遍历的方式来通知子 View 对自身进行测量
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //使用遍历的方式来设定子 View 需要放置的位置
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                //主要修改每个子 View 的 top 和 bottom 属性，让他们能依次排列下来
                childView.layout(l, i * getHeight(), r, (i + 1) * getHeight());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取当前触点的y坐标
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                //getScrollX()：代表水平方向上的偏移，getScrollX()>0表示向左偏移，getScrollX()<0表示向右偏移
                //getScrollY()：代表垂直方向上的偏移，getScrollY()>0表示向上偏移，getScrollY()<0表示向下偏移
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                //计算本次手势滑动了多大距离
                int dy = mLastY - y;
                //getScrollY()表示之前已经偏移的距离
                if (getScrollY() < 0) {
                    dy = 0;
                }
                if (dy > (getChildCount() - 1) * getHeight()) {
                    dy = (getChildCount() - 1) * getHeight();
                }
                //在上一次偏移的情况下，进行偏移，偏移到x=0，y=dy的位置
                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int dScrollY = mEnd - mStart;
                if (dScrollY > 0) {//上滑
                    //mScroller.startScroll()：滑动动画控制，里面进行了一些轨迹参数的设置和计算
                    mScroller.startScroll(0, getScrollY(), 0, dScrollY < getHeight() / 6 ? -dScrollY : getHeight() - dScrollY);
                } else {//下滑
                    mScroller.startScroll(0, getScrollY(), 0, -dScrollY < getHeight() / 6 ? -dScrollY : getHeight() - dScrollY);
                }
                break;
        }
        //重绘
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            //将视图内容移动到x=0，y=mScroller.getCurrY()的位置，x和y代表偏移量
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

}
