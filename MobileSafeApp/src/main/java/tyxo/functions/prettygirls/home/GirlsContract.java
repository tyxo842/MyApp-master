package tyxo.functions.prettygirls.home;


import java.util.List;

import tyxo.functions.prettygirls.BasePresenter;
import tyxo.functions.prettygirls.BaseView;
import tyxo.functions.prettygirls.data.bean.GirlsBean;

/**
 * Created by oracleen on 2016/6/29.
 */
public interface GirlsContract {

    interface View extends BaseView {
        void refresh(List<GirlsBean.ResultsEntity> datas);

        void load(List<GirlsBean.ResultsEntity> datas);

        void showError();

        void showNormal();
    }

    interface Presenter extends BasePresenter {
        void getGirls(int page, int size, boolean isRefresh);
    }
}
