package tyxo.functions.prettygirls.data.source.remote;

import android.util.Log;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tyxo.functions.prettygirls.data.bean.GirlsBean;
import tyxo.functions.prettygirls.data.source.GirlsDataSource;
import tyxo.functions.prettygirls.http.GirlsRetrofit;
import tyxo.functions.prettygirls.http.GirlsService;

/**
 * Created by oracleen on 2016/6/29.
 */
public class RemoteGirlsDataSource implements GirlsDataSource {

    @Override
    public void getGirls(int page, int size, final LoadGirlsCallback callback) {
        GirlsRetrofit.getRetrofit()
                .create(GirlsService.class)
                .getGirls("福利", size, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                        Log.i("tyxo","RemoteGirlsDataSource onError : "+e.toString());
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        callback.onGirlsLoaded(girlsBean);
                        Log.i("tyxo","onNext girlsBean : "+girlsBean.toString());
                    }
                });
    }

    @Override
    public void getGirl(final LoadGirlsCallback callback) {
        getGirls(1, 1, callback);
    }
}
