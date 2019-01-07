package tyxo.functions.weather.model;

import android.content.Context;

import retrofit2.Call;
import rx.Observable;
import tyxo.functions.weather.RetrofitWrapper;
import tyxo.functions.weather.bean.WeatherInfo;
import tyxo.functions.weather.bean.WeatherInfoReq;
import tyxo.functions.weather.netrequest.NetRequest;


/**
 * Created on 2016/5/4.
 */
public class WeatherInfoModel {
    private static  WeatherInfoModel weatherInfoModel;
    private NetRequest netRequest;

    public WeatherInfoModel(Context context) {
        netRequest = (NetRequest) RetrofitWrapper.getInstance().create(NetRequest.class);
    }

    public static WeatherInfoModel getInstance(Context context) {
        if (weatherInfoModel == null) {
            weatherInfoModel = new WeatherInfoModel(context);
        }
        return weatherInfoModel;
    }

    /**
     * 查询天气
     *
     * @param weatherInfoReq
     * @return
     */
    public Call<WeatherInfo> queryWeather(WeatherInfoReq weatherInfoReq) {
        Call<WeatherInfo> infoCall = netRequest.getWeather(weatherInfoReq.apiKey, weatherInfoReq.city);
        return infoCall;
    }

    public Observable<WeatherInfo> queryWeatherByRxJava(WeatherInfoReq weatherInfoReq) {
        Observable<WeatherInfo> infoCall = netRequest.getWeatherByRxJava(weatherInfoReq.apiKey, weatherInfoReq.city);
        return infoCall;
    }
}
