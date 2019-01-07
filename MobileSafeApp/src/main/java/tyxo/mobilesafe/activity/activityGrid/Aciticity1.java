package tyxo.mobilesafe.activity.activityGrid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONObject;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.TaskHelp;
import tyxo.mobilesafe.bean.DataLuo;
import tyxo.mobilesafe.net.volley.VolleyCallBack;
import tyxo.mobilesafe.net.volley.VolleyErrorResult;
import tyxo.mobilesafe.utils.ViewUtil;
import tyxo.mobilesafe.utils.log.HLog;
import tyxo.mobilesafe.utils.md5.AESUtil;

/**
 * Created by LY on 2016/7/29 16: 04.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class Aciticity1 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setStateBar(this,R.color.darkSlateBlue);  /** 设置状态栏颜色 此activity通过样式设置的全屏 此处代码设置会遮挡,在布局加fitsys*/
        setContentView(R.layout.activity_recycler);
        ViewUtil.setStateBarHeight(this,(ViewGroup)findViewById(R.id.act_recycler_rl));/** 获取状态栏高度,并重绘上*/

        initData();
    }

    private String str;
    private void initData() {
        TaskHelp task = new TaskHelp();
        task.getDataLuo(this, 1, 10, new VolleyCallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HLog.i("tyxo","Activity1 返回 :"+response.toString());
                DataLuo data = new Gson().fromJson(response.toString(), DataLuo.class);
                try {
                    str = AESUtil.tes1t(data.getData());
                    HLog.i("tyxo","Activity1 解码 :"+str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyErrorResult result) {
                HLog.i("tyxo","Activity1 error 返回 :"+result);
            }
        });
    }
}
