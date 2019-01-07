package tyxo.functions.prettygirls.http;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import tyxo.functions.prettygirls.data.bean.GirlsBean;

/**
 * Created by gaohailong on 2016/5/17.
 */
public interface GirlsService {

    @GET("api/data/{type}/{count}/{page}")
    Observable<GirlsBean> getGirls(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );

}
