package tyxo.functions.weather.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2016/5/4.
 */
public class Temperature {
    @SerializedName("max")
    public String maxTem;
    @SerializedName("min")
    public String minTem;

    public String getMaxTem() {
        return maxTem;
    }

    public void setMaxTem(String maxTem) {
        this.maxTem = maxTem;
    }

    public String getMinTem() {
        return minTem;
    }

    public void setMinTem(String minTem) {
        this.minTem = minTem;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "maxTem='" + maxTem + '\'' +
                ", minTem='" + minTem + '\'' +
                '}';
    }
}
