package tyxo.mobilesafe.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import tyxo.mobilesafe.ConstValues;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.adpter.GirlsAdapterMy;
import tyxo.mobilesafe.base.mybase.BaseRecyclerActivity;
import tyxo.mobilesafe.base.mybase.BaseRecyclerStaggeredAdapter;
import tyxo.mobilesafe.bean.BeanGirls;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.log.HLog;
import tyxo.mobilesafe.widget.recyclerdivider.recyclerbase.HeaderViewRecyclerAdapter;
import tyxo.mobilesafe.widget.recyclerdivider.recyclerbase.LoadMoreView;

/**
 * Created by LY on 2016/8/25 10: 38.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class ActivityGirls extends BaseRecyclerActivity<BeanGirls>{

    private ArrayList<BeanGirls.ShowapiResBodyBean.NewslistBean> beanList; // 返回数据集合
    private GirlsAdapterMy mAdapter;
    private LoadMoreView mLoadMore;

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mToolbarTitle.setText("Girls");

        mLoadMore = (LoadMoreView) LayoutInflater.from(this).inflate(R.layout.base_load_more, mRecyclerView, false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        itemClickLitener = new BaseRecyclerStaggeredAdapter.OnItemClickLitener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(View view, Object bean, int position) {
                //ToastUtil.showToastS(getApplicationContext(),"条目%1$.2f点击了"+position);
                Intent intent = new Intent(ActivityGirls.this, ActivityGirl.class);
                //intent.putParcelableArrayListExtra("girls", beanList);
                //intent.putExtra("current", position);
                Bundle bun = new Bundle();
                bun.putSerializable("girls",beanList);
                bun.putInt("current",position);
                intent.putExtras(bun);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(
                        view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                startActivity(intent,options.toBundle());
                HLog.v("tyxo","点击位置 : "+position);
            }

            @Override
            public void onItemLongClick(View view, Object bean, int position) {

            }
        };
    }

    @Override
    protected void initData() {
        super.initData();
        beanList = new ArrayList<>();

        requestNet();
        mAdapter = new GirlsAdapterMy(this, beanList, R.layout.base_recycler_item);
        mAdapter.setOnItemClickLitener(itemClickLitener);
        HeaderViewRecyclerAdapter adapter = new HeaderViewRecyclerAdapter(mAdapter);
        adapter.setLoadingView(mLoadMore);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
    }

    /** 处理返回的数据 */
    @Override
    protected void handleData(BeanGirls beanB) {
        HLog.i("tyxo", "BeanGirls beanB 返回解析: " + beanB.toString());
        BeanGirls.ShowapiResBodyBean beanBody = beanB.getShowapi_res_body();

        if(beanBody.getNewslist()!=null && beanBody.getNewslist().size() > 0){
            HLog.i("tyxo", "BeanGirls response size(): " + beanBody.getNewslist().size());

            ArrayList<BeanGirls.ShowapiResBodyBean.NewslistBean> resList = (ArrayList<BeanGirls.ShowapiResBodyBean.NewslistBean>) beanB.getShowapi_res_body().getNewslist();
            if (isLoadMore) {
                beanList.addAll(resList);
            } else {
                beanList = resList;
            }
            mAdapter.substituteDatas(beanList);
            mAdapter.notifyDataSetChanged();
            stopLoading();

        }else {
            ToastUtil.showToastS(this,"无更多数据");
            HLog.i("tyxo", "BeanGirls size<=0 返回信息: " + ConstValues.SERVER_RESPONSE_EMPTY);
        }
    }

    @Override
    protected void requestNet() {
        super.requestNet();
        taskHelp.getGirls(this,pageSize,pageIndex,rand,callback);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        requestNet();
    }

    @Override
    protected void onLoadMore() {
        super.onLoadMore();
        pageIndex++;
        mLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
        requestNet();
    }

    @Override
    protected void stopLoading() {
        super.stopLoading();
    }
}



















