package tyxo.functions.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.service.music.MusicService;
import tyxo.mobilesafe.utils.ApplicationUtils;
import tyxo.mobilesafe.utils.ViewUtil;

/**
 * Created on 2016/5/24.
 */

public class MusicActivity extends BaseMusicActivity implements CompoundButton.OnCheckedChangeListener {

    private ServiceConnection serviceConnection;
    private Intent serviceIntent;
    private TextView versionName;
    private CheckBox checkBox;

    private MusicService musicService;

    @Override
    protected void setMyContentView() {
        setContentView(R.layout.activity_music);

        initAllViews();
        bindService();
        startService(serviceIntent);

        ViewUtil.initCenterColorMenu(this);     //初始化menu CircularFloatingActionMenu
        ViewUtil.initCenterBottomMenu(this);    //初始化menu CircularFloatingActionMenu
        ViewUtil.initCircleMenu(this);          //初始化menu CircularFloatingActionMenu
    }

    private void initAllViews() {
        mToolbarTitle.setText("悬浮迷你音乐盒");
        checkBox = (CheckBox) findViewById(R.id.main_check_box);
        checkBox.setOnCheckedChangeListener(this);

        versionName = (TextView) findViewById(R.id.main_version_name);
        versionName.setText(ApplicationUtils.getAppVersionName(this));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            musicService.show();
        } else {
            musicService.dismiss();
        }
        SharedPreferences preferences = getSharedPreferences("FloatMusicPlayer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isCheck", isChecked);
        editor.commit();
    }

    private void bindService() {
        serviceIntent = new Intent(MusicActivity.this, MusicService.class);
        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    musicService = ((MusicService.MusicBinder)service).getService();

                    SharedPreferences preferences = getSharedPreferences("FloatMusicPlayer", Context.MODE_PRIVATE);
                    boolean isCheck = preferences.getBoolean("isCheck", false);
                    checkBox.setChecked(isCheck);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) { }
            };
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void unbindService() {
        if(null != serviceConnection) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        unbindService();
        super.onPause();
    }

    @Override
    protected void onResume() {
        bindService();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        bindService();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        unbindService();
        super.onStop();
    }
}
