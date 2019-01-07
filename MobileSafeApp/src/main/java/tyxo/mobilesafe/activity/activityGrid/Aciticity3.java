package tyxo.mobilesafe.activity.activityGrid;

import tyxo.mobilesafe.base.BaseActivityToolbar;
import tyxo.mobilesafe.utils.dialog.DialogUtil;

/**
 * Created by LY on 2016/7/29 16: 04.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class Aciticity3 extends BaseActivityToolbar {

    @Override
    protected void setMyContentView() {
        DialogUtil.showFlyDialog(this);
    }
}
