package tyxo.mobilesafe.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.widget.TextView;

/**
 * Created by LY on 2016/7/21 18: 11.
 * Mail      1577441454@qq.com
 * Describe : 为了设置 多个跑马灯 在同一个界面
 */
public class FocusableTextView extends TextView {

    public FocusableTextView(Context context) {
        super(context);
    }

    public FocusableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FocusableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine();                            //单行
        setFocusable(true);                         //使控件获得焦点 针对物理键,同下一个要一起设置
        setFocusableInTouchMode(true);              //使控件获取焦点 针对手指  ,同上一个要一起设置
        setMarqueeRepeatLimit(-1);                  //切记如果通过代码实现循环。那么把值设置成-1
    }

    /** 这个方法注意解决2个控件同时获取焦点问题*/
    @Override
    @ViewDebug.ExportedProperty(category = "focus")//???
    public boolean isFocused() {
        return true;
        //return super.isFocused();
    }

    /** 不管哪个控件获取到焦点,当前跑马灯都可以滚动 */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    /** 防止窗体获取焦点(某个item点击,弹框之类的),跑马灯停止 */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
}




























