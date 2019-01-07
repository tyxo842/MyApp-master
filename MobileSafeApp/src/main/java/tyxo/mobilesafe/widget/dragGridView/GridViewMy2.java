package tyxo.mobilesafe.widget.dragGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by LY on 2016/7/21 16: 50.
 * Mail      1577441454@qq.com
 * Describe : 原来继承 DynamicGridView, 有问题,重写copy代码; 后加重绘方法,否则添加头布局,会显示不全.
 */
public class GridViewMy2 extends GridView{

    public GridViewMy2(Context context) {
        super(context);
    }

    public GridViewMy2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewMy2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
