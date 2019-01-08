package tyxo.mobilesafe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import tyxo.functions.music.MusicActivity;
import tyxo.mobilesafe.activity.ActivityGirls;
import tyxo.mobilesafe.activity.ImageViewActivityMy;
import tyxo.mobilesafe.activity.PicActivity;
import tyxo.mobilesafe.activity.RecyclerActivity;
import tyxo.mobilesafe.activity.RichEditorActivity;
import tyxo.mobilesafe.activity.StaggeredGridLayoutActivity;
import tyxo.mobilesafe.activity.activityGrid.Aciticity1;
import tyxo.mobilesafe.activity.activityGrid.Aciticity2;
import tyxo.mobilesafe.activity.activityGrid.Aciticity3;
import tyxo.mobilesafe.activity.activityGrid.Aciticity4;
import tyxo.mobilesafe.activity.activityGrid.Aciticity5;
import tyxo.mobilesafe.activity.activityGrid.Aciticity6;
import tyxo.mobilesafe.adpter.AdapterMainGridView;
import tyxo.mobilesafe.adpter.AdapterMainRecycler;
import tyxo.mobilesafe.bean.MainGVItemBean;
import tyxo.mobilesafe.utils.AnimationUtil;
import tyxo.mobilesafe.utils.FingerPrintUtils;
import tyxo.mobilesafe.utils.StringUtils;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.ViewUtil;
import tyxo.mobilesafe.utils.bitmap.BitmapUtil;
import tyxo.mobilesafe.utils.bitmap.CropHandler;
import tyxo.mobilesafe.utils.bitmap.CropHelper;
import tyxo.mobilesafe.utils.bitmap.CropParams;
import tyxo.mobilesafe.utils.dialog.DialogUtil;
import tyxo.mobilesafe.utils.log.HLog;
import tyxo.mobilesafe.utils.permission.PermissionUtil;
import tyxo.mobilesafe.widget.GooeyMenu;
import tyxo.mobilesafe.widget.dragGridView.DragGridView;
import tyxo.mobilesafe.widget.dragGridView.GridViewMy;
import tyxo.mobilesafe.widget.recyclerdivider.DividerItemDecoration;
import tyxo.mobilesafe.widget.recyclerdivider.DoubleClickExitDetector;
import tyxo.mobilesafe.widget.recyclerdivider.WrapRecyclerView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterMainRecycler.OnItemClickListener,
        CropHandler, GooeyMenu.GooeyMenuInterface {

    private FloatingActionButton fab;
    private GooeyMenu gooey_menu;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;      // 左侧布局
    private ImageView iv_left_header1;          // 左侧头部 图像
    private String url;                         // webView 加载的url
    private View header;
    private TextView textView_title_left;       //左侧小标题
    private ShimmerTextView textView;                  //左侧小标题下边说明

    //不用notifyDataSetChanged,而是notifyItemInserted(position)与notifyItemRemoved(position),否则没动画效果
    private WrapRecyclerView mRecyclerView;     // 主界面(右) recyclerView
    private TextView tv_main_up_recycler_1;     // 主界面(右) 跳转recyclerView按钮
    private AdapterMainRecycler mAdapter;
    private ArrayList<String> mDatas;
    private SwipeRefreshLayout swipeRL_recyclerActivity;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private MyHandler handler;
    private EditText editText;
    private TextView main_up_tv_1;              //滚动炫酷的的TextView
    private GridViewMy mGridView;               //主界面 header gridview
    private DragGridView mDragGridView;         //主界面 可拖动 gridview
    private AdapterMainGridView mGridAdapter;   //主界面 header gridview 的适配器
    private SimpleAdapter mDragGridAdapter;     //主界面 可拖动 gridview 的适配器
    private List<MainGVItemBean> gvListInfos;   //用于填充gridview的数据集合
    private DoubleClickExitDetector exitDetector;//双击返回退出
    private Shimmer shimmer;
    private CropParams mCropParams;
    //private CropHandler cropHandler;          //获取图片 裁剪 回调

    private int[] iconIDs = {R.drawable.app_financial, R.drawable.app_donate, R.drawable.app_essential,
            R.drawable.app_citycard, R.drawable.app_inter_transfer, R.drawable.app_facepay};
    private String[] titles = {"待做功能1", "待做功能2", "蝴蝶", "沉浸式", "导航栏", "状态栏"};
    private List<HashMap<String, Object>> dataSourceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //ViewUtil.setStateBar(this, R.color.bg_green);/** 设置状态栏颜色 */
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCropParams = new CropParams(this);

        //initCropHandler();  // 初始化图片裁剪(但是只有截图,而且截图之后的内容设置不上)
        initView();         // 初始化View
        initListener();     // 初始化监听
        initData();         // 初始化数据

        new PermissionUtil(this);//申请清单文件全部权限.
    }

    protected void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        gooey_menu = (GooeyMenu) findViewById(R.id.gooey_menu);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.left_open, R.string.left_close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        textView_title_left = (TextView) header.findViewById(R.id.textView_title_left);
        textView = (ShimmerTextView) header.findViewById(R.id.textView);
        iv_left_header1 = (ImageView) header.findViewById(R.id.iv_left_header1);

        main_up_tv_1 = (TextView) findViewById(R.id.main_up_tv_1);
        tv_main_up_recycler_1 = (TextView) findViewById(R.id.tv_main_up_recycler_1);
        mRecyclerView = (WrapRecyclerView) findViewById(R.id.rv_main_recyclerview);
        swipeRL_recyclerActivity = (SwipeRefreshLayout) findViewById(R.id.swipeRL_recyclerActivity);

        /** 开启土豪金颜色 */
        if (shimmer != null && shimmer.isAnimating()) {
            shimmer.cancel();
        } else {
            shimmer = new Shimmer();
            shimmer.setDuration(3000)
                    .setStartDelay(300)
                    //.setRepeatCount(0)
                    //.setAnimatorListener(new Animator.AnimatorListener() { })
                    .setDirection(Shimmer.ANIMATION_DIRECTION_LTR);
            shimmer.start(textView);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));  // 设置布局管理器
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));//4列,竖着滑动,分割线要配合DividerGridItemDecoration使用
        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL));//4行,横着滑动

        setRecyclerHeader();/** 设置 recyclerView 头布局 */
        initEditText();     /** 初始化 输入框 默认弹出效果 等*/

        SpannableString ss = new SpannableString(this.getResources().getString(R.string.spannablestring_tv));
        ViewUtil.setSpannableStringStyle(this, ss); /** 设置 SpannableString 的样式 */
        main_up_tv_1.setMovementMethod(LinkMovementMethod.getInstance());//如果设置点击,必须加,否则没效果!!
        //main_up_tv_1.setHighlightColor(Color.parseColor("#666666"));   //控制点击的背景色
        main_up_tv_1.setText(ss);
    }

    protected void initListener() {
        fab.setOnClickListener(this);
        gooey_menu.setOnMenuListener(this);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        iv_left_header1.setOnClickListener(this);
        textView_title_left.setOnClickListener(this);
        tv_main_up_recycler_1.setOnClickListener(this);

        setBarStyle();                          /** 设置进度条 */
        initRecyclerViewOnloadMoreListener();   /** 初始化 上拉加载 监听 */

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        initGridViewListener();                 /** 初始化 gridview 监听 拖动,点击,长按 */
    }

    protected void initData() {
        url = ConstValues.MYPHOTO_URL;
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
        gvListInfos = new ArrayList<>();
        for (int i = 0; i < iconIDs.length; i++) {
            MainGVItemBean bean = new MainGVItemBean();
            bean.setIcon(iconIDs[i]);
            bean.setTitle(titles[i]);
            bean.setId(i);
            gvListInfos.add(bean);
        }
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> itemHashMap = new HashMap<>();
            itemHashMap.put("item_image", R.drawable.app_financial);
            itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
            dataSourceList.add(itemHashMap);
        }
        mAdapter = new AdapterMainRecycler(getApplicationContext(), mDatas);
        mGridAdapter = new AdapterMainGridView(this, gvListInfos, 3);
        mDragGridAdapter = new SimpleAdapter(this, dataSourceList,
                R.layout.layout_main_griditem, new String[]{"item_image", "item_text"},
                new int[]{R.id.main_header_gv_item_iv, R.id.main_header_gv_item_tv});
        mGridView.setAdapter(mGridAdapter);
        mDragGridView.setAdapter(mDragGridAdapter);
        AnimationUtil.setMainGridAnimtion(this, mGridView);  //gridView 设置动画
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));//list的分割线
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));//grid的分割线
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置item动画

        handler = new MyHandler();
        exitDetector = new DoubleClickExitDetector(this);
        mAdapter.setOnItemClickListener(this);  /** recyclerView 设置 条目点击 监听 */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_up_recycler_1: {
                //Utility.setTextViewState(this, tv_main_up_recycler_1, Utility.switchBtnClickState("",""));//设置drawleft的图片
                Intent intent = new Intent(this, StaggeredGridLayoutActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.fab:
                //Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                break;
            case R.id.iv_left_header1:
                Intent intent = new Intent(this, ImageViewActivityMy.class);
                intent.putExtra("url", url);
                startActivity(intent);

                //drawer.closeDrawer(GravityCompat.START); // 收起 侧拉
                break;
            case R.id.textView_title_left:
                Glide.with(this)
                        .load(url)
                        .asBitmap()
                        .placeholder(R.drawable.loading) //占位符 也就是加载中的图片，可放个gif//placeholder可能导致图片加载不出来,去掉即可
                        .error(R.drawable.icon_zanwu) //失败图片
                        .into(target);
                HLog.i("tyxo", "小标题 点击 ... url : " + url);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ToastUtil.showToastS(this, "条目 " + position + " 点击了");
    }

    @Override
    public void onItemLongClick(View view, int position) {
        ToastUtil.showToastS(this, "条目 " + position + " 长按了");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            DialogUtil.showDialogCamera(MainActivity.this, mCropParams);
        } else if (id == R.id.nav_gallery) {
            new FingerPrintUtils(this);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, MusicActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pic) {
            Intent intent = new Intent(this, PicActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this, RichEditorActivity.class);
            startActivity(intent);
        }

        /*switch (id) {
            case R.id.fab:
                break;
            case R.id.imageview:

                break;
        }*/

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void menuOpen() {    //googlemenu
        //ToastUtil.showToastS(this,"Menu Open ");
    }

    @Override
    public void menuClose() {   //googlemenu
        //ToastUtil.showToastS(this,"Menu Close ");
    }

    @Override
    public void menuItemClicked(int menuNumber) {   //googlemenu
        ToastUtil.showToastS(this, "Menu item clicked : " + menuNumber);
        gooey_menu.startHideAnimate();
    }

    /**
     * recyclerView 上拉下拉 监听
     */
    @Override
    public void onRefresh() {
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessageDelayed(msg, 1000);
        ToastUtil.showToastS(getBaseContext(), "下拉刷新");
    }

    private void setBarStyle() {
        // 设置进度条 颜色变化，最多可以设置4种颜色
        swipeRL_recyclerActivity.setColorSchemeResources(R.color.gray, R.color.green, R.color.order_text_code_color_2b5fc5);
        //swipeRL_recyclerActivity.setSize(SwipeRefreshLayout.LARGE);                //进度条大小,只有两个选择
        swipeRL_recyclerActivity.setProgressBackgroundColor(R.color.order_bg_f0f3f5);//进度条背景颜色
        /*第一个参数scale就是就是刷新那个圆形进度是是否缩放,如果为true表示缩放,圆形进度图像就会从小到大展示出来,为false就不缩放;
        第二个参数start和end就是那刷新进度条展示的相对于默认的展示位置,start和end组成一个范围，
        在这个y轴范围就是那个圆形进度ProgressView展示的位置*/
        swipeRL_recyclerActivity.setProgressViewOffset(true, 100, 200);
        swipeRL_recyclerActivity.setDistanceToTriggerSync(50);//设置手势操作下拉多少距离之后开始刷新数据
        swipeRL_recyclerActivity.setProgressViewEndTarget(true, 100);

        //swipeRL_recyclerActivity.setPadding(20, 20, 20, 20);
        swipeRL_recyclerActivity.setOnRefreshListener(this);
        // 第一次进入页面的时候显示加载进度条 ,调整进度条距离屏幕顶部的距离
        swipeRL_recyclerActivity.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    private void setRecyclerHeader() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT);
        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View header = inflater.inflate(R.layout.layout_main_header, null);
        View headerGridView = inflater.inflate(R.layout.layout_main_header_gridview, null);
        //header.setLayoutParams(params);           //不管用,回头查查代码设置重绘
        //headerGridView.setLayoutParams(params);   //不管用,回头查查代码设置重绘
        mGridView = (GridViewMy) headerGridView.findViewById(R.id.main_header_gridview);
        mDragGridView = (DragGridView) findViewById(R.id.main_DragGridView);
        //mDragGridView.setVisibility(View.VISIBLE);
        /** 添加两个头不成功,此方法不行,可以尝试在adapter内重写getItemViewType 参考AdapterRecyclerHeader 注释掉部分*/
        //mRecyclerView.addHeaderView(header);
        mRecyclerView.addHeaderView(headerGridView);
        mRecyclerView.addFootView(header);

        editText = (EditText) header.findViewById(R.id.et_main_1);
    }

    private void initRecyclerViewOnloadMoreListener() {
        /*mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 得到第一个item
                int firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {    // 当前可见item的第一个是否是列表的第一个 如果是第一个应用显示
                    if (!isshow) {              // 如果此时不在现实中 得显示
                        isshow = true;
                    }
                } else {                        // 不是第一个
                    if (disy > 25 && isshow) {  // 滑动距离大于25且显示状态 向下 就隐藏
                        isshow = false;
                        hideToolbar();
                        disy = 0;               // 归零
                    }
                    if (disy<-25 && ! isshow) { // 向上滑动且距离大于25且隐藏状态 就显示
                        isshow = true;
                        showToolbar();
                        disy = 0;               // 归零
                    }
                }
                if ((isshow && dy >0)||(!isshow && dy <0)) { // 增加滑动的距离 只有再触发两种状态的时候才进行叠加
                    disy += dy;
                }
            }
        });*/

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    swipeRL_recyclerActivity.setRefreshing(true);

                    /* 此处换成网络请求  上拉加载
                    ......
                    * */
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessageDelayed(msg, 1000);
                    ToastUtil.showToastS(getApplicationContext(), "上拉加载");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 加载图片
     */
    private SimpleTarget target = new SimpleTarget() {
        @Override
        public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
            //图片加载完成
            //iv_left_header1.setImageBitmap((Bitmap) resource);//第一/二处会报: java.lang.ClassCastException: com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable cannot be cast to android.graphics.Bitmap
            //加载圆形图片
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(
                    MainActivity.this.getResources(), (Bitmap) resource);
            drawable.setCircular(true);
            iv_left_header1.setImageDrawable(drawable);
        }
    };

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    swipeRL_recyclerActivity.setRefreshing(false);
                    break;
                case 1:
                    swipeRL_recyclerActivity.setRefreshing(false);
                    break;
                case 2:
                    break;
            }
        }
    }

    //初始化 gridview 监听
    private void initGridViewListener() {
        //拖拽 监听
        /*mGridView.setOnDragListener(new GridViewMy.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                HLog.v("tyxo","gridView 拖拽 start position: "+position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                HLog.v("tyxo",String.format("拖拽 位置 position 从 %d 到 %d",oldPosition,newPosition));
            }
        });*/

        // 长按 监听
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //mGridView.startEditMode(position); //开启可拖动模式
                return true;
            }
        });
        mDragGridView.setOnChangeListener(new DragGridView.OnChanageListener() {
            @Override
            public void onChange(int form, int to) {
                HashMap<String, Object> temp = dataSourceList.get(form);
                //直接交互item
//              dataSourceList.set(from, dataSourceList.get(to));
//              dataSourceList.set(to, temp);
//              dataSourceList.set(to, temp);

                //这里的处理需要注意下
                if (form < to) {
                    for (int i = form; i < to; i++) {
                        Collections.swap(dataSourceList, i, i + 1);
                    }
                } else if (form > to) {
                    for (int i = form; i > to; i--) {
                        Collections.swap(dataSourceList, i, i - 1);
                    }
                }
                dataSourceList.set(to, temp);
                mDragGridAdapter.notifyDataSetChanged();
            }
        });

        //条目点击 监听
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToastS(MainActivity.this, parent.getAdapter().getItem(position).toString());
                ToastUtil.showToastS(MainActivity.this, "点击的是 : " + position);
                MainGVItemBean itemBean = (MainGVItemBean) parent.getAdapter().getItem(position);
                HLog.v("tyxo", itemBean.getTitle());
                switch (itemBean.getId()) {
                    case 0:
                        //
                        Intent intent0 = new Intent(MainActivity.this, Aciticity1.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        //
                        Intent intent1 = new Intent(MainActivity.this, Aciticity2.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        //
                        Intent intent2 = new Intent(MainActivity.this, Aciticity3.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        //
                        Intent intent3 = new Intent(MainActivity.this, Aciticity4.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        //导航栏
                        Intent intent4 = new Intent(MainActivity.this, Aciticity5.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        //状态栏
                        Intent intent5 = new Intent(MainActivity.this, Aciticity6.class);
                        startActivity(intent5);
                        break;
                }
            }
        });
    }

    /*// 隐藏toolbar
    private void hideToolbar(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, View.TRANSLATION_Y, 0,-mToolbar.getHeight());
        animator.setDuration(500);
        animator.start();
    }
    // 显示 toolbar
    private void showToolbar(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, View.TRANSLATION_Y, -mToolbar.getHeight(),0);
        animator.setDuration(500);
        animator.start();
    }*/

    private void initEditText() {
        //String digists = "[^a-zA-Z0-9\\u4E00-\\u9FA5]";   // [\u4e00-\u9fa5]
        String digists = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //editText.setKeyListener(DigitsKeyListener.getInstance(digists));
        /*editText.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] c = {'a','b','c','d','e','1','2'};
                return c;
                //return new char[0];
            }

            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return 3;
            }
        });*/
        //editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        //editText.addTextChangedListener(watcher);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);           //et 默认数字软键盘,可输入字母汉字等
        /*//将软键盘搜索功能改为"搜索"两个字,同时修改xml文件处.
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    // 先隐藏键盘
                    ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //实现自己的搜索逻辑
                    return true;
                }
                return false;
            }
        });*/
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String editable = editText.getText().toString();
            String str = StringUtils.stringFilter(editable.toString());
            if (!editable.equals(str)) {
                editText.setText(str);
                editText.setSelection(str.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onBackPressed() {
        if (mGridView.isEditMode() || drawer.isDrawerOpen(GravityCompat.START)) {
            mGridView.stopEditMode();
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exitDetector.onClick()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //ToastUtil.showToastS("设置");
            Intent intent = new Intent(this, RecyclerActivity.class);
            startActivity(intent);
            return true;
        }
        /**热修复 注意 : 如果先点击测试，再点击打补丁，再测试是不会变化的，因为类一旦加载以后，不会重新再去重新加载了。*/
        switch (id) {
            case R.id.action_settings_fix:
                /*//准备补丁,从assert里拷贝到dex里
                File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "path_dex3.jar");
                Utils.prepareDex(this.getApplicationContext(), dexPath, "path_dex3.jar");
                HotFix.patch(this, dexPath.getAbsolutePath(), "tyxo.mobilesafe.activity.ImageViewerActivity");
                //HotFix.patch(this, dexPath.getAbsolutePath(), "dodola.hotfix.BugClass");
                //DexInjector.inject(dexPath.getAbsolutePath(), defaultDexOptPath, "dodola.hotfix.BugClass");*/
                break;
            case R.id.action_settings_test:
                /*LoadBugClass bugClass = new LoadBugClass();
                ToastUtil.showToastS(this,"测试调用方法:" + bugClass.getBugString());*/

                Intent intent = new Intent(this, ActivityGirls.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                mAdapter.removeData(0);
                break;
            case R.id.action_new:
                mAdapter.addData(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化 相机裁剪 Handler
    @Override
    public void onPhotoCropped(Uri uri) {
        if (!mCropParams.compress)
            iv_left_header1.setImageBitmap(BitmapUtil.decodeUriAsBitmap(MainActivity.this, uri));
    }

    @Override
    public void onCompressed(Uri uri) {
        iv_left_header1.setImageBitmap(BitmapUtil.decodeUriAsBitmap(MainActivity.this, uri));
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.showToastS(MainActivity.this, "失败 " + message);
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }
    /*private void initCropHandler(){
        cropHandler = new CropHandler() {
            @Override
            public void onPhotoCropped(Uri uri) {
                iv_left_header1.setImageBitmap(BitmapUtil.decodeUriAsBitmap(MainActivity.this, uri));
            }

            @Override
            public void onCropCancel() {}

            @Override
            public void onCropFailed(String message) {
                ToastUtil.showToastS(MainActivity.this,"失败 "+message);
            }

            @Override
            public CropParams getCropParams() {
                return mCropParams;
            }

            @Override
            public Activity getContext() {
                return MainActivity.this;
            }
        };
    }*/

    /*
    有时候我们会发现用相机拍摄获取照片的时候，得到的 uri 是 null 的，这是因为android把拍摄的图片封装到bundle中传递回来，
    但是根据不同的机器获得相片的方式不太一样，可能有的相机能够通过 inten.getData()获取到uri ,
    然后再根据uri获取数据的路径，在封装成bitmap，但有时候有的相机获取到的是null的，这时候我们该怎么办呢？
    其实这时候我们就应该从bundle中获取数据，通过 (Bitmap) bundle.get("data") 获取到相机图片的bitmap数据。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == ConstantsMy.CODE_REQUEST_IMAGE) {
            Uri uri = data.getData();
        } else if (requestCode == ConstantsMy.CODE_REQUEST_CAMERABIG) {
            if (data != null) { //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //返回有缩略图
                if (data.hasExtra("data")) {
                    Bitmap photo = data.getParcelableExtra("data");
                    iv_left_header1.setImageBitmap(photo);
                }
            } else {
                //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 通过目标uri，找到图片
                // 对图片的缩放处理
                // 操作
                Uri uri = Uri.fromFile(ViewUtil.getImageFile(ConstValues.SAVE_IMAGE_DIR_PATH_TEMP));
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    iv_left_header1.setImageBitmap(photo);
                    //destoryBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        //CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
        if (ConstantsMy.isUtil) {
            CropHelper.handleResult(this, requestCode, resultCode, data);
        } else {
            //从相册返回数据
            if (data != null) {
                Bitmap photo = null;
                //得到图片的全路径
                Uri uri = data.getData();
                try {
                    if (photo != null) photo.recycle();
                    photo = null;
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Bitmap photo2 = BitmapUtil.compressImagePercent(photo); //原图压缩,否则直接设置会影响主线程
                    iv_left_header1.setImageBitmap(photo2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    //销毁bitmap
    private void destoryBitmap(Bitmap photo) {
        if (photo != null && !photo.isRecycled()) {
            photo.recycle();
            photo = null;
        }
    }

    @Override
    protected void onDestroy() {
        CropHelper.clearCacheDir();
        /*if (cropHandler.getCropParams() != null){
            CropHelper.clearCachedCropFile(mCropParams.uri);//
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);//
        }*/
        super.onDestroy();
    }
}


























