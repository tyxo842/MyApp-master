package tyxo.functions.weather.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LY on 2016/8/18 15: 25.
 * Mail      1577441454@qq.com
 * Describe : 天气所有返回数据 bean
 */
public class WeatherBean implements Serializable{

    /**
     * city : {"aqi":"84","co":"2","no2":"41","o3":"28","pm10":"?","pm25":"84","qlty":"良","so2":"2"}
     */

    private AqiBean aqi;
    /**
     * city : 北京
     * cnty : 中国
     * id : CN101010100
     * lat : 39.904000
     * lon : 116.391000
     * update : {"loc":"2016-08-18 13:51","utc":"2016-08-18 05:51"}
     */

    private BasicBean basic;
    /**
     * cond : {"code":"300","txt":"阵雨"}
     * fl : 25
     * hum : 97
     * pcpn : 0.3
     * pres : 1001
     * tmp : 23
     * vis : 2
     * wind : {"deg":"50","dir":"东北风","sc":"3-4","spd":"15"}
     */

    private NowBean now;
    /**
     * aqi : {"city":{"aqi":"84","co":"2","no2":"41","o3":"28","pm10":"?","pm25":"84","qlty":"良","so2":"2"}}
     * basic : {"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.904000","lon":"116.391000","update":{"loc":"2016-08-18 13:51","utc":"2016-08-18 05:51"}}
     * daily_forecast : [{"astro":{"sr":"05:29","ss":"19:07"},"cond":{"code_d":"307","code_n":"300","txt_d":"大雨","txt_n":"阵雨"},"date":"2016-08-18","hum":"94","pcpn":"23.0","pop":"98","pres":"1001","tmp":{"max":"24","min":"22"},"vis":"7","wind":{"deg":"345","dir":"无持续风向","sc":"微风","spd":"4"}},{"astro":{"sr":"05:30","ss":"19:05"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-08-19","hum":"52","pcpn":"0.0","pop":"31","pres":"1006","tmp":{"max":"28","min":"22"},"vis":"10","wind":{"deg":"64","dir":"无持续风向","sc":"微风","spd":"8"}},{"astro":{"sr":"05:31","ss":"19:04"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-08-20","hum":"55","pcpn":"0.0","pop":"0","pres":"1007","tmp":{"max":"31","min":"23"},"vis":"10","wind":{"deg":"183","dir":"无持续风向","sc":"微风","spd":"8"}},{"astro":{"sr":"05:32","ss":"19:02"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-08-21","hum":"50","pcpn":"0.0","pop":"0","pres":"1008","tmp":{"max":"32","min":"22"},"vis":"10","wind":{"deg":"133","dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"sr":"05:33","ss":"19:01"},"cond":{"code_d":"100","code_n":"104","txt_d":"晴","txt_n":"阴"},"date":"2016-08-22","hum":"55","pcpn":"0.0","pop":"5","pres":"1010","tmp":{"max":"31","min":"24"},"vis":"10","wind":{"deg":"163","dir":"无持续风向","sc":"微风","spd":"5"}},{"astro":{"sr":"05:34","ss":"18:59"},"cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-08-23","hum":"89","pcpn":"9.6","pop":"56","pres":"1007","tmp":{"max":"26","min":"21"},"vis":"8","wind":{"deg":"44","dir":"无持续风向","sc":"微风","spd":"10"}},{"astro":{"sr":"05:35","ss":"18:58"},"cond":{"code_d":"300","code_n":"101","txt_d":"阵雨","txt_n":"多云"},"date":"2016-08-24","hum":"74","pcpn":"9.0","pop":"50","pres":"1004","tmp":{"max":"27","min":"22"},"vis":"10","wind":{"deg":"74","dir":"无持续风向","sc":"微风","spd":"7"}}]
     * hourly_forecast : [{"date":"2016-08-18 16:00","hum":"84","pop":"33","pres":"1001","tmp":"26","wind":{"deg":"131","dir":"东南风","sc":"微风","spd":"11"}},{"date":"2016-08-18 19:00","hum":"84","pop":"17","pres":"1002","tmp":"25","wind":{"deg":"246","dir":"西南风","sc":"微风","spd":"10"}},{"date":"2016-08-18 22:00","hum":"83","pop":"30","pres":"1003","tmp":"20","wind":{"deg":"352","dir":"北风","sc":"微风","spd":"9"}}]
     * now : {"cond":{"code":"300","txt":"阵雨"},"fl":"25","hum":"97","pcpn":"0.3","pres":"1001","tmp":"23","vis":"2","wind":{"deg":"50","dir":"东北风","sc":"3-4","spd":"15"}}
     * status : ok
     * suggestion : {"comf":{"brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},"flu":{"brf":"易发","txt":"相对于今天将会出现大幅度降温，空气湿度较大，易发生感冒，请注意适当增加衣服。"},"sport":{"brf":"较不宜","txt":"有较强降水，建议您选择在室内进行健身休闲运动。"},"trav":{"brf":"较不宜","txt":"温度适宜的一天，风力不大，但有较强降水，会给您的出行产生很多麻烦，建议您最好还是多选择在室内活动吧！"},"uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}
     */

    private String status;
    /**
     * comf : {"brf":"较舒适","txt":"白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。"}
     * cw : {"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
     * drsg : {"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"}
     * flu : {"brf":"易发","txt":"相对于今天将会出现大幅度降温，空气湿度较大，易发生感冒，请注意适当增加衣服。"}
     * sport : {"brf":"较不宜","txt":"有较强降水，建议您选择在室内进行健身休闲运动。"}
     * trav : {"brf":"较不宜","txt":"温度适宜的一天，风力不大，但有较强降水，会给您的出行产生很多麻烦，建议您最好还是多选择在室内活动吧！"}
     * uv : {"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}
     */

    private SuggestionBean suggestion;
    /**
     * astro : {"sr":"05:29","ss":"19:07"}
     * cond : {"code_d":"307","code_n":"300","txt_d":"大雨","txt_n":"阵雨"}
     * date : 2016-08-18
     * hum : 94
     * pcpn : 23.0
     * pop : 98
     * pres : 1001
     * tmp : {"max":"24","min":"22"}
     * vis : 7
     * wind : {"deg":"345","dir":"无持续风向","sc":"微风","spd":"4"}
     */

    private List<DailyForecastBean> daily_forecast;
    /**
     * date : 2016-08-18 16:00
     * hum : 84
     * pop : 33
     * pres : 1001
     * tmp : 26
     * wind : {"deg":"131","dir":"东南风","sc":"微风","spd":"11"}
     */

    private List<HourlyForecastBean> hourly_forecast;

    public AqiBean getAqi() {
        return aqi;
    }

    public void setAqi(AqiBean aqi) {
        this.aqi = aqi;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public NowBean getNow() {
        return now;
    }

    public void setNow(NowBean now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SuggestionBean getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionBean suggestion) {
        this.suggestion = suggestion;
    }

    public List<DailyForecastBean> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<HourlyForecastBean> getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(List<HourlyForecastBean> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public static class AqiBean implements Serializable{
        /**
         * aqi : 84
         * co : 2
         * no2 : 41
         * o3 : 28
         * pm10 : ?
         * pm25 : 84
         * qlty : 良
         * so2 : 2
         */

        private CityBean city;

        public CityBean getCity() {
            return city;
        }

        public void setCity(CityBean city) {
            this.city = city;
        }

        public static class CityBean implements Serializable{
            private String aqi;
            private String co;
            private String no2;
            private String o3;
            private String pm10;
            private String pm25;
            private String qlty;
            private String so2;

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getCo() {
                return co;
            }

            public void setCo(String co) {
                this.co = co;
            }

            public String getNo2() {
                return no2;
            }

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public String getO3() {
                return o3;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }

            public String getPm10() {
                return pm10;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getSo2() {
                return so2;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }
        }
    }

    public static class BasicBean implements Serializable{
        private String city;
        private String cnty;
        private String id;
        private String lat;
        private String lon;
        /**
         * loc : 2016-08-18 13:51
         * utc : 2016-08-18 05:51
         */

        private UpdateBean update;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCnty() {
            return cnty;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public static class UpdateBean implements Serializable{
            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }
    }

    public static class NowBean implements Serializable{
        /**
         * code : 300
         * txt : 阵雨
         */

        private CondBean cond;
        private String fl;
        private String hum;
        private String pcpn;
        private String pres;
        private String tmp;
        private String vis;
        /**
         * deg : 50
         * dir : 东北风
         * sc : 3-4
         * spd : 15
         */

        private WindBean wind;

        public CondBean getCond() {
            return cond;
        }

        public void setCond(CondBean cond) {
            this.cond = cond;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        public static class CondBean implements Serializable{
            private String code;
            private String txt;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class WindBean implements Serializable{
            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }
    }

    public static class SuggestionBean implements Serializable{
        /**
         * brf : 较舒适
         * txt : 白天有降雨，但会使人们感觉有些热，不过大部分人仍会有比较舒适的感觉。
         */

        private ComfBean comf;
        /**
         * brf : 不宜
         * txt : 不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。
         */

        private CwBean cw;
        /**
         * brf : 舒适
         * txt : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
         */

        private DrsgBean drsg;
        /**
         * brf : 易发
         * txt : 相对于今天将会出现大幅度降温，空气湿度较大，易发生感冒，请注意适当增加衣服。
         */

        private FluBean flu;
        /**
         * brf : 较不宜
         * txt : 有较强降水，建议您选择在室内进行健身休闲运动。
         */

        private SportBean sport;
        /**
         * brf : 较不宜
         * txt : 温度适宜的一天，风力不大，但有较强降水，会给您的出行产生很多麻烦，建议您最好还是多选择在室内活动吧！
         */

        private TravBean trav;
        /**
         * brf : 最弱
         * txt : 属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。
         */

        private UvBean uv;

        public ComfBean getComf() {
            return comf;
        }

        public void setComf(ComfBean comf) {
            this.comf = comf;
        }

        public CwBean getCw() {
            return cw;
        }

        public void setCw(CwBean cw) {
            this.cw = cw;
        }

        public DrsgBean getDrsg() {
            return drsg;
        }

        public void setDrsg(DrsgBean drsg) {
            this.drsg = drsg;
        }

        public FluBean getFlu() {
            return flu;
        }

        public void setFlu(FluBean flu) {
            this.flu = flu;
        }

        public SportBean getSport() {
            return sport;
        }

        public void setSport(SportBean sport) {
            this.sport = sport;
        }

        public TravBean getTrav() {
            return trav;
        }

        public void setTrav(TravBean trav) {
            this.trav = trav;
        }

        public UvBean getUv() {
            return uv;
        }

        public void setUv(UvBean uv) {
            this.uv = uv;
        }

        public static class ComfBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class CwBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class DrsgBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class FluBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class SportBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class TravBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        public static class UvBean implements Serializable{
            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }

    public static class DailyForecastBean implements Serializable{
        /**
         * sr : 05:29
         * ss : 19:07
         */

        private AstroBean astro;
        /**
         * code_d : 307
         * code_n : 300
         * txt_d : 大雨
         * txt_n : 阵雨
         */

        private CondBean cond;
        private String date;
        private String hum;
        private String pcpn;
        private String pop;
        private String pres;
        /**
         * max : 24
         * min : 22
         */

        private TmpBean tmp;
        private String vis;
        /**
         * deg : 345
         * dir : 无持续风向
         * sc : 微风
         * spd : 4
         */

        private WindBean wind;

        public AstroBean getAstro() {
            return astro;
        }

        public void setAstro(AstroBean astro) {
            this.astro = astro;
        }

        public CondBean getCond() {
            return cond;
        }

        public void setCond(CondBean cond) {
            this.cond = cond;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public TmpBean getTmp() {
            return tmp;
        }

        public void setTmp(TmpBean tmp) {
            this.tmp = tmp;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        public static class AstroBean implements Serializable{
            private String sr;
            private String ss;

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }
        }

        public static class CondBean implements Serializable{
            private String code_d;
            private String code_n;
            private String txt_d;
            private String txt_n;

            public String getCode_d() {
                return code_d;
            }

            public void setCode_d(String code_d) {
                this.code_d = code_d;
            }

            public String getCode_n() {
                return code_n;
            }

            public void setCode_n(String code_n) {
                this.code_n = code_n;
            }

            public String getTxt_d() {
                return txt_d;
            }

            public void setTxt_d(String txt_d) {
                this.txt_d = txt_d;
            }

            public String getTxt_n() {
                return txt_n;
            }

            public void setTxt_n(String txt_n) {
                this.txt_n = txt_n;
            }
        }

        public static class TmpBean implements Serializable{
            private String max;
            private String min;

            public String getMax() {
                return max;
            }

            public void setMax(String max) {
                this.max = max;
            }

            public String getMin() {
                return min;
            }

            public void setMin(String min) {
                this.min = min;
            }
        }

        public static class WindBean implements Serializable{
            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }
    }

    public static class HourlyForecastBean implements Serializable{
        private String date;
        private String hum;
        private String pop;
        private String pres;
        private String tmp;
        /**
         * deg : 131
         * dir : 东南风
         * sc : 微风
         * spd : 11
         */

        private WindBean wind;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        public static class WindBean implements Serializable{
            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }
    }
}
