package tyxo.mobilesafe.base.interfacehh;

import android.content.Context;

/**
 * Created by LiYang on 2016/7/6 13: 31.
 * Mail      1577441454@qq.com
 * Describe : AlertDialog弹出框点确定按钮执行的操作接口，删除程序数据
 */
public interface IDialogAction {
    void PositiveAction(Context context, String... filepath);
}
