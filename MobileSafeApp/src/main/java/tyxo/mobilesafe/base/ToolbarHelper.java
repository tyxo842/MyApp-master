package tyxo.mobilesafe.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import tyxo.mobilesafe.R;

/**
 * Created by LY on 2016/7/7 14: 10.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class ToolbarHelper {
    private Context mContext;
    private FrameLayout mContentView;   // base view
    private View mUseView;              // 自定义view
    private Toolbar mToolbar;
    private TextView mTitle;
    private LayoutInflater mInflater;   // 视图构造器

    // toolbar 是否悬浮在窗口之上; toolbar 的高度获取
    private static int [] attrs = {R.attr.windowActionBarOverlay,R.attr.actionBarSize};

    public ToolbarHelper(Context context, int layouId){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

        initContentView();          // 初始化整个内容
        initUserView(layouId);      // 初始化自定义的布局
        initToolbar();              // 初始化 toolbar
    }

    /** 直接创建一个帧布局，作为视图容器的父容器 */
    private void initContentView() {
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    /** 通过inflater获取toolbar的布局文件 */
    private void initToolbar() {
        View toolbar = mInflater.inflate(R.layout.layout_toolbar, mContentView);
        mToolbar = (Toolbar) toolbar.findViewById(R.id.id_tool_bar);
        mTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
    }

    private void initUserView(int id) {
        mUseView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);

        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs);
        // 获取主题中的定义的悬浮标志
        boolean overly = typedArray.getBoolean(0, false);
        // 获取主题中的定义的toolbar 的高度
        int toolbarSize = (int) typedArray.getDimension(1,(int)mContext.getResources().getDimension(R.dimen.bar_height));
        typedArray.recycle();
        // 如果是悬浮状态,则不需要设置间距
        params.topMargin = overly?0:toolbarSize;
        mContentView.addView(mUseView,params);
    }

    public FrameLayout getContentView(){
        return mContentView;
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public TextView getmTitle(){return mTitle;}
}
