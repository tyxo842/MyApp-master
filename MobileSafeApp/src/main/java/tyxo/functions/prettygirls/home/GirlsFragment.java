package tyxo.functions.prettygirls.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import coder.mylibrary.base.BaseFragment;
import tyxo.functions.prettygirls.data.bean.GirlsBean;
import tyxo.functions.prettygirls.girl.GirlActivity;
import tyxo.functions.prettygirls.util.LogUtil;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.utils.log.HLog;

/**
 * Created by oracleen on 2016/6/21.
 */
public class GirlsFragment extends BaseFragment implements GirlsContract.View, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    public static final String TAG = "GirlsFragment";
    private ViewStub mNetworkErrorLayout;
    private EasyRecyclerView mGirlsRecyclerView;
    private View networkErrorView;

    private ArrayList<GirlsBean.ResultsEntity> data;
    private GirlsAdapter mAdapter;

    private GirlsPresenter mPresenter;
    private int page = 1;
    private int size = 20;

    //private Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    public static GirlsFragment getInstance() {
        GirlsFragment mainFragment = new GirlsFragment();
        return mainFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //unbinder = ButterKnife.bind(this, view);

        mGirlsRecyclerView = (EasyRecyclerView) view.findViewById(R.id.girls_recycler_view);
        mNetworkErrorLayout = (ViewStub) view.findViewById(R.id.network_error_layout);
        mPresenter = new GirlsPresenter(this);

        initRecyclerView();

        //初始化数据
        mPresenter.start();
    }

    private void initRecyclerView() {
        data = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mGirlsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new GirlsAdapter(getContext());

        mGirlsRecyclerView.setAdapterWithProgress(mAdapter);

        mAdapter.setMore(R.layout.load_more_layout, this);
        mAdapter.setNoMore(R.layout.no_more_layout);
        mAdapter.setError(R.layout.error_layout);
//        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(mActivity, GirlActivity.class);
//                intent.putParcelableArrayListExtra("girls", data);
//                intent.putExtra("current", position);
//                startActivity(intent);
//            }
//        });

        mAdapter.setOnMyItemClickListener(new GirlsAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                Intent intent = new Intent(mActivity, GirlActivity.class);
                intent.putParcelableArrayListExtra("girls", data);
                intent.putExtra("current", position);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(holder.itemView, holder.itemView.getWidth() / 2, holder.itemView.getHeight() / 2, 0, 0);
                startActivity(intent, options.toBundle());
            }
        });

        mGirlsRecyclerView.setRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mPresenter.getGirls(1, size, true);
        page = 1;
    }

    @Override
    public void onLoadMore() {
        if (data.size() % 20 == 0) {
            LogUtil.d(TAG, "onloadmore");
            page++;
            mPresenter.getGirls(page, size, false);
        }
    }

    @Override
    public void refresh(List<GirlsBean.ResultsEntity> datas) {
        HLog.i("tyxo","refresh 返回 datas : "+datas);
        data.clear();
        data.addAll(datas);
        mAdapter.clear();
        mAdapter.addAll(datas);
    }

    @Override
    public void load(List<GirlsBean.ResultsEntity> datas) {
        HLog.i("tyxo","load 返回 datas : "+datas);
        data.addAll(datas);
        mAdapter.addAll(datas);
    }

    @Override
    public void showError() {
        mGirlsRecyclerView.showError();

        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.VISIBLE);
            return;
        }

        networkErrorView = mNetworkErrorLayout.inflate();
    }

    public void showNormal() {
        if (networkErrorView != null) {
            networkErrorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }
}
