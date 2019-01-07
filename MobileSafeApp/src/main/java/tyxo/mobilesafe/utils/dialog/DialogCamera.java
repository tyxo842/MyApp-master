package tyxo.mobilesafe.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import tyxo.mobilesafe.R;


/**
 * Created by tyxo on 2016/4/18 15:48.
 * 相机\相册 跳转dialog
 */
public class DialogCamera extends Dialog {

    public DialogCamera(Context context) {
        super(context);
    }

    public DialogCamera(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private int layouId;
        private OnClickListener item1CameraClickListener;
        private OnClickListener item2CameraClickListener;
        private OnClickListener item3CameraClickListener;
        private OnClickListener item4CameraClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, int layouId) {
            this.context = context;
            this.layouId = layouId;
        }

        public void setItem1ClickListener(OnClickListener click){
            this.item1CameraClickListener = click;
        }
        public void setItem2ClickListener(OnClickListener click){
            this.item2CameraClickListener = click;
        }
        public void setItem3ClickListener(OnClickListener click){
            this.item3CameraClickListener = click;
        }
        public void setItem4ClickListener(OnClickListener click){
            this.item4CameraClickListener = click;
        }

        public DialogCamera create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogCamera dialog = new DialogCamera(context, R.style.my_dialog_bg);
            //final CustomDialog dialog = new CustomDialog(context);
            View layout = inflater.inflate(layouId, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (null != item1CameraClickListener) {
                layout.findViewById(R.id.camera_tv_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item1CameraClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            if (null != item2CameraClickListener) {
                layout.findViewById(R.id.camera_tv_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item2CameraClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            if (null != item3CameraClickListener) {
                layout.findViewById(R.id.camera_tv_camera_nusub).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item3CameraClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            if (null != item4CameraClickListener) {
                layout.findViewById(R.id.camera_tv_photo_nusub).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item4CameraClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
