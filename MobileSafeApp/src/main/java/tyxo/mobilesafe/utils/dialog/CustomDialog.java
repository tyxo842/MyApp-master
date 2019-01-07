package tyxo.mobilesafe.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tyxo.mobilesafe.R;


/**
 * Created by tyxo on 2016/4/18 15:48.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private int layouId;
        private EditTextListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private OnClickListener positiveClickListener;
        private String msg;

        public EditText getEditText(int layouId,int etID){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(layouId, null);
            EditText editText = (EditText) layout.findViewById(etID);
            return editText;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, int layouId) {
            this.context = context;
            this.layouId = layouId;
        }

        public Builder setPositiveButton(EditTextListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setMessage(String msg){
            this.msg = msg;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.my_dialog_bg);
//            final CustomDialog dialog = new CustomDialog(context);
            View layout = inflater.inflate(layouId, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            final EditText bill_num = (EditText) layout.findViewById(R.id.bill_num);
            final EditText remarks = (EditText) layout.findViewById(R.id.remarks);

            //确定按钮点击事件
            if (positiveButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.textContent(bill_num.getText().toString(), remarks.getText().toString());
                                positiveButtonClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }

            //取消按钮的点击事件
            if (negativeButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }

            dialog.setContentView(layout);
            return dialog;
        }

        public CustomDialog createSend() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,R.style.my_dialog_bg);
//            final CustomDialog dialog = new CustomDialog(context);
            View layout = inflater.inflate(layouId, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView message = (TextView) layout.findViewById(R.id.order_dialog_message);
            message.setText(msg);

            //确定按钮点击事件
            if (positiveClickListener != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }

            //取消按钮的点击事件
            if (negativeButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }

            dialog.setContentView(layout);
            return dialog;
        }

        public CustomDialog createSave() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,R.style.my_dialog_bg);
//            final CustomDialog dialog = new CustomDialog(context);
            View layout = inflater.inflate(layouId, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            final EditText bill_num = (EditText) layout.findViewById(R.id.ems_num);

            //确定按钮点击事件
            if (positiveButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.textContent(bill_num.getText().toString());
                                positiveButtonClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }

            /*//取消按钮的点击事件
            if (negativeButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }*/

            dialog.setContentView(layout);
            return dialog;
        }
    }

    public interface EditTextListener extends OnClickListener {
        public void textContent(String billText, String remarkText);
        public void textContent(String emsNumber);
    }
}
