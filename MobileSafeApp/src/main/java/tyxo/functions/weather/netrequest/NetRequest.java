package tyxo.functions.weather.netrequest;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import tyxo.functions.weather.bean.WeatherInfo;

/**
 * Created on 2016/4/29.
 */
public interface NetRequest {

    @GET("/heweather/weather/free?/")
    Call<WeatherInfo> getWeather(@Header("apiKey") String apiKey, @Query("city") String city);

    @GET("/heweather/weather/free?/")
    Observable<WeatherInfo> getWeatherByRxJava(@Header("apiKey") String apiKey, @Query("city") String city);

}
