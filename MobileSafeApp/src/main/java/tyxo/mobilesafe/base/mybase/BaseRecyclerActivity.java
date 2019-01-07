package tyxo.mobilesafe.base.mybase;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import tyxo.mobilesafe.ConstValues;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.TaskHelp;
import tyxo.mobilesafe.base.BaseActivityToolbar;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * Created by LY on 2016/8/25 10: 38.
 * Mail      1577441454@qq.com
 * Describe : <T> 限定数据 bean 的类型,
 *            子类使用的时候: public class GirlsActivity extends BaseRecyclerActivity<BeanGirls>{....}.
 *            参考 GirlsActivity 代码.
 */
public abstract class BaseRecyclerActivity<T extends Object> extends BaseActivityToolbar implements SwipeRefreshLayout.OnRefreshListener{

    /*
    public static <T> String arrayToString(ArrayList<T> list) {
    Gson g = new Gson();
    return g.toJson(list);
    }
    public static <T> ArrayList<T> stringToArray(String s) {
    Gson g = new Gson();
    Type listType = new TypeToken<ArrayList<T>>(){}.getType();
    ArrayList<T> list = g.fromJson(s, listType);
    return list;
    }

    正确:
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
    T[] arr = new Gson().fromJson(s, clazz);
    return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public <T> List<T> deserializeList(String json) {
    Gson gson = new Gson();
    Type type = (new TypeToken<List<T>>() {}).getType();
    return  gson.fromJson(json, type);
    }

    正确:
    public <T> List<T> deserializeList(String json, Type type) {
    Gson gson = new Gson();
    return  gson.fromJson(json, type);
    }

     */

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected BaseRecyclerStaggeredAdapter.OnItemClickLitener itemClickLitener;

    protected TaskHelp taskHelp;
    protected VolleyCallBack<JSONObject> callback;
    private GridLayoutManager mLayoutManager;
    private int lastVisibleItem;

    protected boolean isRefresh = true;            //是否是下拉刷新
    protected boolean isLoadMore = false;         //是否是上拉加载
    protected int pageSize;
    protected int pageIndex;
    protected int rand;


    @Override
    protected void setMyContentView() {
        setContentView(R.layout.base_fragment_refresh_recycler);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_refresh);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    @Override
    protected void initListener() {
        super.initListener();
        // 具体可以由子类去实现 itemClickLitener 的click内容.
        itemClickLitener = new BaseRecyclerStaggeredAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, Object bean, int position) {
                //ToastUtil.showToastS(getApplicationContext(),"条目%1$.2f点击了"+position);
                ToastUtil.showToastS(getApplicationContext(),String.format("%1$d点击了,这是第%2$d个条目",position,position));
            }

            @Override
            public void onItemLongClick(View view, Object bean, int position) {

            }
        };
    }

    @Override
    protected void initData() {
        super.initData();
        pageSize = 10;
    }

    /** 处理数据 */
    protected abstract void handleData(T beanB);
        /** 子类建议做如下判断, 打印size,便于调试接口,按自己情况去写 */
        //Type type = new TypeToken<BeanGirls>() {}.getType();
        //BeanGirls bean = new Gson().fromJson(response.toString(), type);

        /*if(bean.getData()!=null && bean.getData().size() > 0){
            HLog.i("tyxo", " response size(): " + bean.getData().size());
        }else {
            HLog.i("tyxo", " size<=0 返回信息: " + ConstValues.SERVER_RESPONSE_EMPTY);
        }*/


    /** 网络请求 */
    protected void requestNet(){

        callback =  new VolleyCallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HLog.i("tyxo", " response : " + response.toString());
                try {
                    if (!TextUtils.isEmpty(response.toString())) {
                        handleData(parseObject(response.toString()));  //处理数据

                    } else {
                        stopLoading();
                        HLog.i("tyxo", " response为空 返回信息: " + ConstValues.SERVER_RESPONSE_EMPTY);
                    }
                } catch (Exception e) {
                    stopLoading();
                    e.printStackTrace();
                    HLog.i("tyxo", " 解析数据错误" + e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyErrorResult result) {
                stopLoading();
                HLog.i("tyxo", " 请求失败: " + result.toString());
            }
        };

        taskHelp = new TaskHelp();
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                    >= mLayoutManager.getItemCount() - 1;
            //if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {//下拉刷新
            if(newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadMore && reachBottom) {
                isLoadMore = true;
                onLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                    >= mLayoutManager.getItemCount() - 1;
            if(!isLoadMore && reachBottom) {
                onLoadMore();
            }
        }
    };

    // 下拉刷新
    @Override
    public void onRefresh() {
        pageIndex = 1;
        rand = 0;
        isRefresh = true;
        isLoadMore = false;
    }

    // 加载更多
    protected void onLoadMore() {
        isLoadMore = true;
        isRefresh = false;
    }

    protected void stopLoading(){
        isLoadMore = false;
        mRefreshLayout.setRefreshing(false);
    }

    /**解析 返回数据*/
    protected T parseObject(String response) throws Exception{
        try {
            return new Gson().fromJson(response, getClazz());
        }catch (Exception e){
            HLog.e("tyxo","BaseRecyclerActivity parseObject 数据解析失败"+e);
            throw new Exception(e);
        }
    }
    public Class<T> getClazz() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) t;
        Class<T> c = (Class<T>) p.getActualTypeArguments()[0];
        return c;
    }
}



















