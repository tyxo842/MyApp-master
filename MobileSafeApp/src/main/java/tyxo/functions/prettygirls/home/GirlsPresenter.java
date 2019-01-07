package tyxo.functions.prettygirls.home;

import android.util.Log;

import tyxo.functions.prettygirls.data.bean.GirlsBean;
import tyxo.functions.prettygirls.data.source.GirlsDataSource;
import tyxo.functions.prettygirls.data.source.GirlsResponsitory;
import tyxo.mobilesafe.utils.log.HLog;


/**
 * Created by oracleen on 2016/6/29.
 */
public class GirlsPresenter implements GirlsContract.Presenter {

    public static final String TAG = "GirlsPresenter";

    private GirlsContract.View mView;
    private GirlsResponsitory mGirlsResponsitory;

    public GirlsPresenter(GirlsContract.View view) {
        mView = view;
        mGirlsResponsitory = new GirlsResponsitory();
    }

    @Override
    public void start() {
        getGirls(1, 20, true);
    }

    @Override
    public void getGirls(int page, int size, final boolean isRefresh) {
        Log.d(TAG, "getGirls");
        mGirlsResponsitory.getGirls(page, size, new GirlsDataSource.LoadGirlsCallback() {
            @Override
            public void onGirlsLoaded(GirlsBean girlsBean) {
                HLog.i("tyxo","onGirlsLoaded girlsBean : "+girlsBean);
                if (isRefresh) {
                    mView.refresh(girlsBean.getResults());
                } else {
                    mView.load(girlsBean.getResults());
                }
                mView.showNormal();
            }

            @Override
            public void onDataNotAvailable() {
                if (isRefresh) {
                    mView.showError();
                }
            }
        });
    }

}
