package tyxo.mobilesafe.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.TaskHelp;
import tyxo.mobilesafe.adpter.AdapterDynamic;
import tyxo.mobilesafe.adpter.AdapterRecyclerHeader;
import tyxo.mobilesafe.base.BaseActivityToolbar;
import tyxo.mobilesafe.bean.MainGVItemBean;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.log.HLog;
import tyxo.mobilesafe.widget.dragGridView.GridViewMy;
import tyxo.mobilesafe.widget.recyclerdivider.DividerItemDecoration;

/**
 * Created by LY on 2016/7/26 10: 49.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class RecyclerActivity extends BaseActivityToolbar implements View.OnClickListener {

    private RecyclerView mRecyclerview;
    private GridLayoutManager mLayoutManager;
    private AdapterRecyclerHeader mAdapter;
    private GridViewMy mGridView;
    private AdapterDynamic mGridAdapter;
    private int num;
    private SwipeRefreshLayout swipeRL;
    private MyHandler handler;
    private int lastVisibleItem;
    private TextView tv_recycler_up_recycler_1; //顶部按钮

    @Override
    protected void setMyContentView() {
        //ViewUtil.setStateBar(this,R.color.bg_green);  /** 设置状态栏颜色 此activity通过样式设置的全屏 此处代码设置会遮挡,在布局加fitsys*/
        setContentView(R.layout.activity_recycler);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tv_recycler_up_recycler_1 = (TextView) findViewById(R.id.tv_recycler_up_recycler_1);
        mRecyclerview = (RecyclerView) findViewById(R.id.activity_recycler_recyclerview);
        //mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());//条目动画
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));//list的分割线

        mAdapter = new AdapterRecyclerHeader();
        mAdapter.addDatas(getAdapterData(10));
        mRecyclerview.setAdapter(mAdapter);
        setHeader();    /** 添加 头布局 可拖拽的 GridView*/

        swipeRL = (SwipeRefreshLayout) findViewById(R.id.recycler_swipeRL_recyclerActivity);
        swipeRL.setColorSchemeResources(R.color.gray, R.color.green, R.color.order_text_code_color_2b5fc5);
        swipeRL.setProgressBackgroundColor(R.color.order_bg_f0f3f5);//进度条背景颜色

        //getSupportActionBar().hide();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_recycler_up_recycler_1.setOnClickListener(this);
        initOnRefreshListener();    //初始化下拉刷新监听
        initOnLoadMoreListener();   //初始化上拉加载监听
    }

    @Override
    protected void initData() {
        super.initData();
        num = 10;
        handler = new MyHandler();
    }

    public void setHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_recycler_header_gridview,mRecyclerview,false);
        mGridView = (GridViewMy)header.findViewById(R.id.recycler_header_gridview);
        mGridAdapter = new AdapterDynamic(this,getAdapterData(6),3);
        mGridView.setAdapter(mGridAdapter);
        initGridViewListener();     /** 初始化 GridView 监听*/
        mAdapter.setHeaderView(header);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recycler_up_recycler_1:
                EventBus.getDefault().post("从 RecyclerActivity 发送的消息");
                break;
        }
    }

    private void initGridViewListener() {
        mGridView.setOnDragListener(new GridViewMy.OnDragListener(){
            @Override
            public void onDragStarted(int position) {
                HLog.v("tyxo","拖拽 开始 位置: "+position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                HLog.v("tyxo",String.format("拖拽 条目 从 %d 到 %d",oldPosition,newPosition));
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToastS(RecyclerActivity.this,parent.getAdapter().getItem(position).toString());
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mGridView.startEditMode(position);
                return true;
            }
        });
    }

    //上拉加载
    private void initOnLoadMoreListener() {
        mRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    swipeRL.setRefreshing(true);

                    /* 此处换成网络请求  上拉加载
                    ......
                    * */
                    num+=10;
                    Message msg = new Message();
                    msg.what = 1;
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

    //下拉刷新
    private void initOnRefreshListener() {
        swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                num = 10;
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessageDelayed(msg, 1000);
                requestNet();
                ToastUtil.showToastS(getBaseContext(), "下拉刷新");
            }
        });
    }

    /** 网络请求 */
    private void requestNet() {
        TaskHelp task = new TaskHelp();
        task.orderModifyState(mContext, "", "", new VolleyCallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HLog.i("tyxo","返回数据 response : "+response.toString());
                Type type = new TypeToken<MainGVItemBean>() {}.getType();
                MainGVItemBean bean = new Gson().fromJson(response.toString(), type);
                //处理 返回 数据 bean
                // ...判空,增减,刷新ui等等操作....
                //
            }

            @Override
            public void onErrorResponse(VolleyErrorResult result) {
                HLog.e("tyxo","请求失败 error : "+result);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mAdapter.setDatas(getAdapterData(num));
                    swipeRL.setRefreshing(false);
                    break;
                case 1:
                    mAdapter.setDatas(getAdapterData(num));
                    swipeRL.setRefreshing(false);
                    break;
                case 2:
                    break;
            }
        }
    }

    @Override
    public void onCreateCustomToolbar(Toolbar toolbar) {
        super.onCreateCustomToolbar(toolbar);
        TextView tv_toolbar_title = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("RecyclerActivity");
    }

    //设置传入 adapter 内的数据
    private List<String> getAdapterData(int size) {
        List<String> list = new ArrayList<>();
        for (int i=0;i<size;i++) {
            list.add(i,"条目 "+i);
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        if (mGridView.isEditMode()) {
            mGridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }

    /*
    // 判断 多长时间之前?
    public static String formatIntelligible(Date date) {
        Date now = new Date();

        long diff = (now.getTime() - date.getTime());
        if (diff < 0) { // 未来时刻
            if (Math.abs(diff) < (MINUTE_SECS * 1000)) { // 两个时间之间允许有1分钟的误差
                diff = 0;
            } else {
                return format(date, "y-M-d H:mm");
            }
        }
        diff /= 1000;

        if (diff < MINUTE_SECS) { // 一分钟内
            return "刚刚";
        } else if ((diff >= MINUTE_SECS) && (diff < HOUR_SECS)) { // 一小时内
            return (diff / MINUTE_SECS) + "分钟前";
        } else if ((diff >= HOUR_SECS) && (diff < DAY_SECS)) { // 一天内
            return (diff / HOUR_SECS) + "小时前";
        } else if ((diff >= DAY_SECS) && (diff < TWO_DAY_SECS)) { // 昨天
            return "昨天 " + format(date, "H:mm");
        } else if ((diff >= TWO_DAY_SECS) && isSameYear(date, now)) { // 年内
            return format(date, "M-d H:mm");
        }

        return format(date, "y-M-d H:mm");
    }*/

}

