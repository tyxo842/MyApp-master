package tyxo.functions.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jn.chart.charts.LineChart;
import com.jn.chart.data.Entry;
import com.jn.chart.manager.LineChartManager;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tyxo.functions.weather.bean.DailyForecast;
import tyxo.functions.weather.bean.WeatherBean;
import tyxo.functions.weather.bean.WeatherBeanResult;
import tyxo.functions.weather.bean.WeatherInfo;
import tyxo.functions.weather.bean.WeatherInfoReq;
import tyxo.functions.weather.bean.WeatherResult;
import tyxo.functions.weather.model.WeatherInfoModel;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.TaskHelp;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.utils.log.HLog;

public class WeatherActivity extends Activity {
    private Button request;
    private LineChart chart;
    private LineChart weatherChart_rain;
    private WeatherInfoModel weatherInfoModel;
    private int i = 0;
    private ProgressDialog pd;

    private ArrayList<String> xValues ;
    private ArrayList<Entry> yValues2Max ;
    private ArrayList<Entry> yValues2Min ;
    private ArrayList<String> xValues_rain ;
    private ArrayList<Entry> yValues2Max_rain ;
    private ArrayList<Entry> yValues2Min_rain ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherInfoModel = WeatherInfoModel.getInstance(getApplicationContext());
        initViews();    // 初始化控件
        initData();
        initParams();   // 初始化请求参数
        initEvent();

        initWeather();  // volley 访问网络 请求天气数据
    }

    /*
    *初始化请求参数
     */
    private WeatherInfoReq initParams() {
        WeatherInfoReq weatherInfoReq = new WeatherInfoReq();
        weatherInfoReq.apiKey = Constant.API_KEY;
        weatherInfoReq.city = Constant.CITY;
        return weatherInfoReq;
    }

    /*
    *初始化控件
     */
    private void initViews() {
        request = (Button) this.findViewById(R.id.request);
        chart = (LineChart) this.findViewById(R.id.weatherChart);
        weatherChart_rain = (LineChart) this.findViewById(R.id.weatherChart_rain);
    }

    private void initData(){
        xValues = new ArrayList<>();
        yValues2Max = new ArrayList<Entry>();
        yValues2Min = new ArrayList<Entry>();
        xValues_rain = new ArrayList<>();
        yValues2Max_rain = new ArrayList<Entry>();
        yValues2Min_rain = new ArrayList<Entry>();
    }

    // volley 访问网络 请求天气数据
    private void initWeather() {
        clearDataRain();
        pd = new ProgressDialog(WeatherActivity.this);
        pd.setMessage("请稍后...");
        pd.show();
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey","2600907be4021f9979ecc9554a4065ac");

        TaskHelp.requstWeatherDatas(this, new VolleyCallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HLog.i("tyxo","WeatherActivity response: "+ response);
                Type type = new TypeToken<WeatherBeanResult>() {}.getType();
                WeatherBeanResult beanResult = new Gson().fromJson(response.toString(), type);
                WeatherBean bean = beanResult.getWeatherBean().get(0);
                List<WeatherBean.DailyForecastBean> dailyForecastList = bean.getDaily_forecast();

                for (int k = 0; k < dailyForecastList.size(); k++) {
                    xValues_rain.add(bean.getDaily_forecast().get(k).getDate());
                    yValues2Max_rain.add(new Entry(Float.valueOf(bean.getDaily_forecast().get(k).getTmp().getMax()), k));
                    yValues2Min_rain.add(new Entry(Float.valueOf(bean.getDaily_forecast().get(k).getTmp().getMin()), k));
                }

                pd.dismiss();
                weatherChart_rain.setDescription("北京气温预测");
                LineChartManager.setLineName("最高温度");
                LineChartManager.setLineName1("最低温度");
                LineChartManager.initDoubleLineChart(WeatherActivity.this, weatherChart_rain, xValues_rain, yValues2Max_rain, yValues2Min_rain);
            }

            @Override
            public void onErrorResponse(VolleyErrorResult result) {
                HLog.i("tyxo","WeatherActivity result: "+ result);
            }
        },headers);
    }

    private void initEvent() {
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                //创建访问的API请求
                weatherInfoModel.queryWeatherByRxJava(initParams())
                        .subscribeOn(Schedulers.io())// 指定观察者在io线程（第一次指定观察者线程有效）
                        .doOnSubscribe(new Action0() {//在启动订阅前（发送事件前）执行的方法
                            @Override
                            public void call() {
                                pd = new ProgressDialog(WeatherActivity.this);
                                pd.setMessage("请稍后...");
                                pd.show();
                            }
                        })
                        .flatMap(new Func1<WeatherInfo, Observable<WeatherResult>>() {
                            @Override
                            public Observable<WeatherResult> call(WeatherInfo weatherInfo) {
                                return Observable.from(weatherInfo.getHeWeatherDataList());
                            }
                        })
                        .flatMap(new Func1<WeatherResult, Observable<DailyForecast>>() {
                            @Override
                            public Observable<DailyForecast> call(WeatherResult weatherResult) {
                                return Observable.from(weatherResult.getDaily_forecast());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())//指定订阅者在主线程
                        .subscribe(new Subscriber<DailyForecast>() {
                            @Override
                            public void onCompleted() {
                                pd.dismiss();
                                /*if ( xValues==null || xValues.isEmpty()) {
                                    return;
                                }*/
                                HLog.v("tyxo","xValues size: "+xValues.size()+" ;max size: "+yValues2Max.size()+" ;min size: "+yValues2Min.size());
                                //chart.setDescription("广州气温预测");
                                chart.setDescription("北京气温预测");
                                LineChartManager.setLineName("最高温度");
                                LineChartManager.setLineName1("最低温度");
                                chart.notifyDataSetChanged();
                                LineChartManager.initDoubleLineChart(WeatherActivity.this, chart, xValues, yValues2Max, yValues2Min);
                            }

                            @Override
                            public void onError(Throwable e) {
                                pd.dismiss();
                            }

                            @Override
                            public void onNext(DailyForecast dailyForecast) {
                                HLog.i("tyxo","WeatherActivity dailyForecast 返回: "+dailyForecast);
                                int j = i++;
                                xValues.add(dailyForecast.getDate());
                                yValues2Max.add(new Entry(Float.valueOf(dailyForecast.getTmp().getMaxTem()), j));
                                yValues2Min.add(new Entry(Float.valueOf(dailyForecast.getTmp().getMinTem()), j));
                            }
                        });
            }
        });
    }

    private void clearData(){
        xValues.clear();
        yValues2Max.clear();
        yValues2Min.clear();
//        xValues = new ArrayList<>();
//        yValues2Max = new ArrayList<Entry>();
//        yValues2Min = new ArrayList<Entry>();
//        chart.clear();
    }
    private void clearDataRain(){
        xValues_rain.clear();
        yValues2Max_rain.clear();
        yValues2Min_rain.clear();
        //weatherChart_rain.clear();
    }
}
