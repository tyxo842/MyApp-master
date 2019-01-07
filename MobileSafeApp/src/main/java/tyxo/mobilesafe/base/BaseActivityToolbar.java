package tyxo.mobilesafe.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

import tyxo.mobilesafe.R;

/**
* @author ly
* @des : 使用 搭配(清单文件中) 	android:label=""
								android:theme="@style/MyToolbarTheme"
								//android:theme="Theme.AppCompat.Light.NoActionBar"(原始)
* 	设置标题(居中) 直接调用  		mToolbarTitle.setText("");
*/
public abstract class BaseActivityToolbar extends AppCompatActivity {

	public String mPageName = this.getClass().getSimpleName();	// 获取类名
	/**当前Activity的上下文 */
	protected Context mContext;
	public Toolbar mToolbar;
	private ToolbarHelper mToolbarHelper;
	protected TextView mToolbarTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;

		setMyContentView();
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		mToolbarHelper = new ToolbarHelper(this, layoutResID);
		mToolbar = mToolbarHelper.getToolbar();
		mToolbarTitle = mToolbarHelper.getmTitle();
		setContentView(mToolbarHelper.getContentView());
		// 把toolbar 设置到 Activity 中
		setSupportActionBar(mToolbar);
		// 自定义的一些操作
		onCreateCustomToolbar(mToolbar);
		//ViewUtil.setStateBar(this,R.color.bg_green);/** 设置状态栏颜色 */

		initView(mToolbarHelper.getContentView());		// 初始化 view
		initListener();	// 初始化 监听
		initData();		// 初始化 数据
	}

	/**
	 * 设置 Activity 使用的视图
	 * 在此方法实现里面,setContentView(R.layout.xxx);
	 */
	protected abstract void setMyContentView();

	protected void initView(View contentView){}

	protected void initListener(){}

	protected void initData(){}

	public void onCreateCustomToolbar(Toolbar toolbar){
		toolbar.setContentInsetsRelative(0,0);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//			getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_return);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (menu!=null) {
			if (menu.getClass()== MenuBuilder.class) {
				try{
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/** 结束Activity */
	@Override
	public void finish() {
		super.finish();
		// 设置切换动画，从右边进入，右边退出
		this.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		// 设置切换动画，从右边进入，右边退出
		this.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
	}

	/*@Override
	protected void onResume() {
		super.onResume();
		//极光推送统计
		JpushManager.getInstance(mContext.getApplicationContext()).onActivityResume(this);
		
		// 添加腾讯的统计服务
		// 如果本Activity是继承基类HHBaseActivity的，可注释掉此行。
//		StatService.onResume(this);
//		StatService.trackBeginPage(this, getClass().getCanonicalName());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JpushManager.getInstance(mContext.getApplicationContext()).onActivityPause(this);
		
		// 添加腾讯的统计服务
		// 如果本Activity是继承基类BaseActivity的，可注释掉此行。
//		StatService.onPause(this);
//		StatService.trackEndPage(this, getClass().getCanonicalName());
	}*/
}
