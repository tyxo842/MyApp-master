package tyxo.functions.weather.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LY on 2016/8/18 17: 35.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class WeatherBeanResult {
    /*
    {"HeWeather data service 3.0":[{"aqi":{"city":{"aqi":"75","co":"2","no2":"33","o3":"46","pm10":"?","pm25":"75","qlty":"良","so2":"2"}},
    "basic":{"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.904000","lon":"116.391000",
    "update":{"loc":"2016-08-18 16:50","utc":"2016-08-18 08:50"}},"daily_forecast":[{"astro":{"sr":"05:29","ss":"19:07"},
    "cond":{"code_d":"306","code_n":"300","txt_d":"中雨","txt_n":"阵雨"},"date":"2016-08-18","hum":"90","pcpn":"28.7",
    "pop":"98","pres":"1002","tmp":{"max":"24","min":"22"},"vis":"2","wind":{"deg":"18","dir":"无持续风向","sc":"微风","spd":"0"}},
    {"astro":{"sr":"05:30","ss":"19:05"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-08-19",
    "hum":"76","pcpn":"1.5","pop":"39","pres":"1004","tmp":{"max":"28","min":"22"},"vis":"10","wind":{"deg":"265","dir":"无持续风向",
    "sc":"微风","spd":"3"}},{"astro":{"sr":"05:31","ss":"19:04"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},
    "date":"2016-08-20","hum":"56","pcpn":"0.0","pop":"0","pres":"1006","tmp":{"max":"31","min":"23"},"vis":"10","wind":{"deg":"170",
    "dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"sr":"05:32","ss":"19:02"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴",
    "txt_n":"晴"},"date":"2016-08-21","hum":"53","pcpn":"0.1","pop":"10","pres":"1010","tmp":{"max":"32","min":"22"},"vis":"10",
    "wind":{"deg":"181","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"sr":"05:33","ss":"19:01"},"cond":{"code_d":"100",
    "code_n":"104","txt_d":"晴","txt_n":"阴"},"date":"2016-08-22","hum":"49","pcpn":"0.0","pop":"5","pres":"1012","tmp":{"max":"31",
    "min":"24"},"vis":"10","wind":{"deg":"172","dir":"无持续风向","sc":"微风","spd":"2"}},{"astro":{"sr":"05:34","ss":"18:59"},
    "cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-08-23","hum":"48","pcpn":"0.0","pop":"55",
    "pres":"1007","tmp":{"max":"26","min":"21"},"vis":"10","wind":{"deg":"181","dir":"无持续风向","sc":"微风","spd":"0"}},
    {"astro":{"sr":"05:35","ss":"18:58"},"cond":{"code_d":"300","code_n":"101","txt_d":"阵雨","txt_n":"多云"},"date":"2016-08-24",
    "hum":"50","pcpn":"1.9","pop":"50","pres":"1005","tmp":{"max":"27","min":"22"},"vis":"10","wind":{"deg":"44","dir":"无持续风向",
    "sc":"微风","spd":"0"}}],"hourly_forecast":[{"date":"2016-08-18 19:00","hum":"90","pop":"24","pres":"1003","tmp":"24",
    "wind":{"deg":"17","dir":"东北风","sc":"微风","spd":"10"}},{"date":"2016-08-18 22:00","hum":"93","pop":"37","pres":"1004",
    "tmp":"24","wind":{"deg":"238","dir":"西南风","sc":"微风","spd":"9"}}],"now":{"cond":{"code":"300","txt":"阵雨"},"fl":"25",
    "hum":"96","pcpn":"0.6","pres":"1000","tmp":"23","vis":"3","wind":{"deg":"10","dir":"东北风","sc":"4-5","spd":"20"}},"status":"ok",
    "suggestion":{"comf":{"brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"},
    "cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},
    "drsg":{"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},
    "flu":{"brf":"易发","txt":"相对于今天将会出现大幅度降温，空气湿度较大，易发生感冒，请注意适当增加衣服。"},
    "sport":{"brf":"较不宜","txt":"有较强降水，建议您选择在室内进行健身休闲运动。"},"trav":{"brf":"较不宜",
    "txt":"温度适宜的一天，风力不大，但有较强降水，会给您的出行产生很多麻烦，建议您最好还是多选择在室内活动吧！"},
    "uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}}]}

    //整体 {"HeWeather data service 3.0":[{"aqi":{"city":{"aqi":"75","co":"2","no2":"33","o3":"46","pm10":"?","pm25":"75","qlty":"良","so2":"2"}},"basic":{"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.904000","lon":"116.391000","update":{"loc":"2016-08-18 16:50","utc":"2016-08-18 08:50"}},"daily_forecast":[{"astro":{"sr":"05:29","ss":"19:07"},"cond":{"code_d":"306","code_n":"300","txt_d":"中雨","txt_n":"阵雨"},"date":"2016-08-18","hum":"90","pcpn":"28.7","pop":"98","pres":"1002","tmp":{"max":"24","min":"22"},"vis":"2","wind":{"deg":"18","dir":"无持续风向","sc":"微风","spd":"0"}},{"astro":{"sr":"05:30","ss":"19:05"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-08-19","hum":"76","pcpn":"1.5","pop":"39","pres":"1004","tmp":{"max":"28","min":"22"},"vis":"10","wind":{"deg":"265","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"sr":"05:31","ss":"19:04"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-08-20","hum":"56","pcpn":"0.0","pop":"0","pres":"1006","tmp":{"max":"31","min":"23"},"vis":"10","wind":{"deg":"170","dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"sr":"05:32","ss":"19:02"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-08-21","hum":"53","pcpn":"0.1","pop":"10","pres":"1010","tmp":{"max":"32","min":"22"},"vis":"10","wind":{"deg":"181","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"sr":"05:33","ss":"19:01"},"cond":{"code_d":"100","code_n":"104","txt_d":"晴","txt_n":"阴"},"date":"2016-08-22","hum":"49","pcpn":"0.0","pop":"5","pres":"1012","tmp":{"max":"31","min":"24"},"vis":"10","wind":{"deg":"172","dir":"无持续风向","sc":"微风","spd":"2"}},{"astro":{"sr":"05:34","ss":"18:59"},"cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-08-23","hum":"48","pcpn":"0.0","pop":"55","pres":"1007","tmp":{"max":"26","min":"21"},"vis":"10","wind":{"deg":"181","dir":"无持续风向","sc":"微风","spd":"0"}},{"astro":{"sr":"05:35","ss":"18:58"},"cond":{"code_d":"300","code_n":"101","txt_d":"阵雨","txt_n":"多云"},"date":"2016-08-24","hum":"50","pcpn":"1.9","pop":"50","pres":"1005","tmp":{"max":"27","min":"22"},"vis":"10","wind":{"deg":"44","dir":"无持续风向","sc":"微风","spd":"0"}}],"hourly_forecast":[{"date":"2016-08-18 19:00","hum":"90","pop":"24","pres":"1003","tmp":"24","wind":{"deg":"17","dir":"东北风","sc":"微风","spd":"10"}},{"date":"2016-08-18 22:00","hum":"93","pop":"37","pres":"1004","tmp":"24","wind":{"deg":"238","dir":"西南风","sc":"微风","spd":"9"}}],"now":{"cond":{"code":"300","txt":"阵雨"},"fl":"25","hum":"96","pcpn":"0.6","pres":"1000","tmp":"23","vis":"3","wind":{"deg":"10","dir":"东北风","sc":"4-5","spd":"20"}},"status":"ok","suggestion":{"comf":{"brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},"flu":{"brf":"易发","txt":"相对于今天将会出现大幅度降温，空气湿度较大，易发生感冒，请注意适当增加衣服。"},"sport":{"brf":"较不宜","txt":"有较强降水，建议您选择在室内进行健身休闲运动。"},"trav":{"brf":"较不宜","txt":"温度适宜的一天，风力不大，但有较强降水，会给您的出行产生很多麻烦，建议您最好还是多选择在室内活动吧！"},"uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}}]}

    //简单 {"HeWeatherdataservice3.0":[{"aqi":{"city":{"aqi":"75","co":"2","no2":"33","o3":"46","pm10":"?","pm25":"75","qlty":"良","so2":"2"}},"daily_forecast":[{"cond":{"code_d":"309","code_n":"305","txt_d":"毛毛雨/细雨","txt_n":"小雨"},"date":"2016-08-18","hum":"90","pcpn":"28.7","pop":"98","pres":"1002","tmp":{"max":"24","min":"22"}},{"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-08-19","hum":"76","pcpn":"1.5","pop":"39","pres":"1004","tmp":{"max":"32","min":"22"}}]}]}

     */

    @SerializedName("HeWeather data service 3.0")
    public List<WeatherBean> weatherBean;

    public List<WeatherBean>  getWeatherBean() {
        return weatherBean;
    }

    public void setWeatherBean(List<WeatherBean>  weatherBean) {
        this.weatherBean = weatherBean;
    }


}
