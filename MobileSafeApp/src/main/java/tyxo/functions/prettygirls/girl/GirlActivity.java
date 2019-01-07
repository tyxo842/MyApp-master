package tyxo.functions.prettygirls.girl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import coder.mylibrary.base.AppActivity;
import coder.mylibrary.base.BaseFragment;
import tyxo.functions.prettygirls.util.ColorUtil;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.log.HLog;
import tyxo.mobilesafe.utils.permission.PermissionUtil;

/**
 * Created by oracleen on 2016/7/4.
 */
public class GirlActivity extends AppActivity implements GirlFragment.OnGirlChange {

    private Toolbar mToolbar;
    private GirlFragment mGirlFragment;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_girl;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.girl_fragment;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        mGirlFragment = GirlFragment.newInstance(getIntent().getParcelableArrayListExtra("girls"), getIntent().getIntExtra("current", 0));
        return mGirlFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.meizhi);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_girl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            mGirlFragment.shareGirl();
            return true;
        } else if (id == R.id.action_save) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,    //申请检查 权限
                    PermissionUtil.MY_PERMISSIONS_WRITE_STORAGE)) {
                mGirlFragment.saveGirl();
            }else{}
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void change(int color) {
        mToolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(ColorUtil.colorBurn(color));
            window.setNavigationBarColor(ColorUtil.colorBurn(color));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishActivity();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void finishActivity() {
        finish();
        //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HLog.v("tyxo","权限回调处理");
        //PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
        if (requestCode == PermissionUtil.MY_PERMISSIONS_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                mGirlFragment.saveGirl();
            } else {
                // Permission Denied
                ToastUtil.showToastS(this,"请提供权限允许");
            }
        }
    }
}
