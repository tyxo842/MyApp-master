package tyxo.mobilesafe.activity;

import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.tyxo.qrcode.core.QRCodeView;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.BaseActivityToolbar;
import tyxo.mobilesafe.utils.ToastUtil;

/**
 * 描述：扫描界面
 * 时间：2016/1/29
 */
public class ScanActivity extends BaseActivityToolbar implements QRCodeView.Delegate {

    private static final String TAG = ScanActivity.class.getSimpleName();
    QRCodeView mQRCodeView;
    public static final String SCAN_RESULT = "scan_result";

    @Override
    protected void setMyContentView() {
        setContentView(R.layout.activity_scan);
        mToolbarTitle.setText("扫描");
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mQRCodeView = (QRCodeView) findViewById(R.id.zxingview);
    }

    @Override
    protected void initData() {
        mQRCodeView.setResultHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        /*Intent intent = new Intent();
        intent.putExtra(SCAN_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
        vibrate();*/
        ToastUtil.showToastS(this, "扫描结果: " + result);
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_flashlight:
                mQRCodeView.openFlashlight();
                break;
            case R.id.close_flashlight:
                mQRCodeView.closeFlashlight();
                break;
        }
    }
}
