package tyxo.mobilesafe.base.interfacehh;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

/** 操作栏回调接口 */
public interface IBaseBarCallback {
 
	/** 设置自定义actionbar 标题可见性 */
	void setBarTitleVisibility(int visibility);

	/** 设置自定义actionbar 图标可见性 */
	void setBarIconVisibility(int visibility);
	 
	/** 设置自定义 bar 标题 */
	void setBarTitle(String title);

    /** 设置自定义 bar 标题 */
	void setBarTitle(int resid);

	/** 设置自定义 bar 图标 */
	void setBarIcon(Bitmap bitmap);

	/** 设置自定义 bar 图标 */
	void setBarIcon(int resid);

	/** 获取 bar 标题 */
	TextView getBarTitle();

	/** 获取 bar 图标 */
	ImageView getBarIcon();
}
