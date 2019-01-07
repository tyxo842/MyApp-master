package tyxo.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.bean.TextValue;
import tyxo.mobilesafe.utils.IDCardUtil;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.dialog.DialogListViewAdapter;
import tyxo.mobilesafe.utils.dialog.DialogUtil;
import tyxo.mobilesafe.widget.richeditor.RichTextEditor;

/**
 * Created by LY on 2016/8/19 15: 46.
 * Mail      1577441454@qq.com
 * Describe :
 */
@SuppressLint("SimpleDateFormat")
public class RichEditorActivity extends FragmentActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor editor;
    private View btn1, btn2, btn3, btn4, btn5;
    private View.OnClickListener btnListener;

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片

    private List<TextValue> dataList;
    private int checkedPostion = 0;
    private Dialog typeDialog;
    private View.OnClickListener dialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cancel_action) {
                if (typeDialog != null) {
                    typeDialog.dismiss();
                }
            } else if (v.getId() == R.id.ok_action) {
                if (typeDialog != null) {
                    typeDialog.dismiss();
                }
                // TODO: 2016/10/8 有问题,待获取输入内容(光标位置)
                String getValue = dataList.get(checkedPostion).getValue();//选择的条目内容
                editor.addEditTextAtIndex(getRichEditStr().length()-1,getValue);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_activity_main);

        editor = (RichTextEditor) findViewById(R.id.richEditor);

        initData();

        initListener();

    }

    private void initListener() {
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);

        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);
        btn3.setOnClickListener(btnListener);
        btn4.setOnClickListener(btnListener);
        btn5.setOnClickListener(btnListener);
    }

    private void initData() {
        dataList = new ArrayList<>();
        dataList.add(new TextValue("first", "这是第一条"));
        dataList.add(new TextValue("second", "这是第二条"));

        btnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.hideKeyBoard();
                if (v.getId() == btn1.getId()) {
                    // 打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");// 相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                } else if (v.getId() == btn2.getId()) {
                    // 打开相机
                    openCamera();
                } else if (v.getId() == btn3.getId()) {
                    List<RichTextEditor.EditData> editList = editor.buildEditData();
                    // 下面的代码可以上传、或者保存，请自行实现
                    dealEditData(editList);
                } else if (v.getId() == btn4.getId()) {//点击验证是否为身份证号
                    if (IDCardUtil.isIDCard(getRichEditStr().trim())) {
                        ToastUtil.showToastS(RichEditorActivity.this, "是身份证号");
                    } else {
                        ToastUtil.showToastS(RichEditorActivity.this, "不是身份证号");
                    }
                } else if (v.getId() == btn5.getId()) {
                    typeDialog = DialogUtil.createBottomDialog(RichEditorActivity.this,
                            "条码类型", dataList, dialogListener, new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ((DialogListViewAdapter) parent.getAdapter()).setCheckPosition(position);
                                    checkedPostion = position;
                                }
                            });
                    if (typeDialog != null) {
                        typeDialog.setCanceledOnTouchOutside(false);
                        typeDialog.show();
                    }
                }
            }
        };
    }

    private String getRichEditStr() {//获取richEdit的str内容
        String strValue = null;
        List<RichTextEditor.EditData> editList = editor.buildEditData();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                strValue = itemData.inputStr;
            }
        }
        return strValue;
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
            }
        }
    }

    protected void openCamera() {
        try {
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            insertBitmap(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        editor.insertImage(imagePath);
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
