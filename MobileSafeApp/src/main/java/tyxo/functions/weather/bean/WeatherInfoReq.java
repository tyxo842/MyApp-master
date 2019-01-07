package tyxo.functions.weather.bean;

import java.io.Serializable;

/**
 * Created on 2016/5/4.
 */
public class WeatherInfoReq implements Serializable {

    public String apiKey;
    public String city;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
