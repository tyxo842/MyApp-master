package tyxo.mobilesafe.utils.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import tyxo.mobilesafe.utils.log.HLog;

/**
 * Created by LY on 2016/9/12 12: 08.
 * Mail      tyxo842@163.com
 * Describe : android 6.0 运行时权限申请, 都有哪些见 类尾.
 *            当前用到权限,用法:
                  if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        PermissionUtil.MY_PERMISSIONS_WRITE_STORAGE)) {
                        mGirlFragment.saveGirl();   //具体操作
                  }else{}
                  重写 onRequestPermissionsResult 用户选择允许或拒绝后，会回调.
                  根据requestCode和grantResults(授权结果)做相应的具体操作.
                     if (requestCode == PermissionUtil.MY_PERMISSIONS_WRITE_STORAGE) {
                         if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                             // Permission Granted
                             mGirlFragment.saveGirl();
                         } else {
                             // Permission Denied
                             ToastUtil.showToastS(this,"请提供权限允许");
                         }
                     }
               申请清单全部权限,用法:
                    new PermissionUtil(this);
 *
 */
public class PermissionUtil {
    private Activity activity;
    public static int MY_PERMISSIONS_WRITE_STORAGE = 1;

    public PermissionUtil(Activity activity) {
        this.activity = activity;
        iniAttribute();             //初始化 一些用到的属性.
        initRequestPermissions();   //new PermissionUtil ,会在开始,申请清单里面所有权限,之后哪里用到还需再检查.
    }

    private void iniAttribute() {}

    private void initRequestPermissions() {
        // Requesting all the permissions in the manifest
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(activity, new PermissionsResultAction() {
            @Override
            public void onGranted() {                   //允许的操作
            }

            @Override
            public void onDenied(String permission) {   //拒绝的操作
            }
        });

        boolean hasPermission = PermissionsManager.getInstance().hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        HLog.v("tyxo", "例如,是否有: " + Manifest.permission.READ_EXTERNAL_STORAGE + " permission: " + hasPermission);
    }

    //检查是否有权限,有就继续操作,没有就申请(ps:禁止并不再提示,未解决) ---> Activity -->GirlActivity(Gank)
    public static boolean checkPermission(Activity activity1,String permission, int ReqPermission){
        if (ContextCompat.checkSelfPermission(activity1, permission)
                != PackageManager.PERMISSION_GRANTED) {
            //申请 权限
            ActivityCompat.requestPermissions(activity1,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},ReqPermission);
            return false;   //没有权限,申请,后走到Activity回调 onRequestPermissionsResult.
        }else{
            return true;    //已经有权限,不走回调,直接读写操作等.
        }
    }
    //检查是否有权限,有就继续操作,没有就申请(ps:禁止并不再提示,未解决) ---> Fragment -->ImageViewerFragment(Fuli)
    public static boolean checkPermission(Fragment fragment, String permission, int ReqPermission){
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            //申请 权限
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},ReqPermission);
            return false;   //没有权限,申请,后走到Activity回调 onRequestPermissionsResult.
        }else{
            return true;    //已经有权限,不走回调,直接读写操作等.
        }
    }

    //鸿洋代码,未完成,不用.
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(Object object, int requestCode, String[] permissions){
        /*
        if (!isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return;
        }
        List<String> deniedPermissions = findDeniedPermissions(activity, permissions);
        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + "is not supported ---> PermissionUtil");
            }
        } else {
            doExecuteSuccess(object,requestCode);
        }*/
    }
    /*@TargetApi(Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String ... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }*/

}


/*
  Fragment中运行时权限的特殊处理
在Fragment中申请权限，不要使用ActivityCompat.requestPermissions, 直接使用Fragment的requestPermissions方法，否则会回调到Activity的onRequestPermissionsResult

如果在Fragment中嵌套Fragment，在子Fragment中使用requestPermissions方法，onRequestPermissionsResult不会回调回来，建议使用getParentFragment().requestPermissions方法，
这个方法会回调到父Fragment中的onRequestPermissionsResult，加入以下代码可以把回调透传到子Fragment

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      List<Fragment> fragments = getChildFragmentManager().getFragments();
      if (fragments != null) {
          for (Fragment fragment : fragments) {
              if (fragment != null) {
                  fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
              }
          }
      }
  }

身体传感器
日历
摄像头
通讯录
地理位置
麦克风
电话
短信
存储空间
group:android.permission-group.CONTACTS
  permission:android.permission.WRITE_CONTACTS
  permission:android.permission.GET_ACCOUNTS
  permission:android.permission.READ_CONTACTS

group:android.permission-group.PHONE
  permission:android.permission.READ_CALL_LOG
  permission:android.permission.READ_PHONE_STATE
  permission:android.permission.CALL_PHONE
  permission:android.permission.WRITE_CALL_LOG
  permission:android.permission.USE_SIP
  permission:android.permission.PROCESS_OUTGOING_CALLS
  permission:com.android.voicemail.permission.ADD_VOICEMAIL

group:android.permission-group.CALENDAR
  permission:android.permission.READ_CALENDAR
  permission:android.permission.WRITE_CALENDAR

group:android.permission-group.CAMERA
  permission:android.permission.CAMERA

group:android.permission-group.SENSORS
  permission:android.permission.BODY_SENSORS

group:android.permission-group.LOCATION
  permission:android.permission.ACCESS_FINE_LOCATION
  permission:android.permission.ACCESS_COARSE_LOCATION

group:android.permission-group.STORAGE
  permission:android.permission.READ_EXTERNAL_STORAGE
  permission:android.permission.WRITE_EXTERNAL_STORAGE

group:android.permission-group.MICROPHONE
  permission:android.permission.RECORD_AUDIO

group:android.permission-group.SMS
  permission:android.permission.READ_SMS
  permission:android.permission.RECEIVE_WAP_PUSH
  permission:android.permission.RECEIVE_MMS
  permission:android.permission.RECEIVE_SMS
  permission:android.permission.SEND_SMS
  permission:android.permission.READ_CELL_BROADCASTS
*/

































