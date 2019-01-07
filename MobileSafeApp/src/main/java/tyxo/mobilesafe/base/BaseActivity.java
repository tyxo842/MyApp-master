package tyxo.mobilesafe.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.interfacehh.IBaseBarCallback;
import tyxo.mobilesafe.base.interfacehh.IBaseInit;
import tyxo.mobilesafe.widget.control.LogicProxy;

public abstract class BaseActivity extends AppCompatActivity implements IBaseBarCallback, IBaseInit {

	/**获取类名 */
	public String mPageName = this.getClass().getSimpleName();

	/**当前Activity的上下文 */
	protected Context mContext;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// BaseApplication.getInstance().addActivity(this);
		mContext = this;
		setContentView();


		initView();
		initData();
		initListener();
	}

	/** 设置Activity使用的视图 */
	protected abstract void setContentView();

	/** 初始化师徒控件 */
	@Override
	public void initView() { }

	/** 初始化数据 */
	@Override
	public void initData() { }

	/** 初始化事件监听器 */
	@Override
	public void initListener() { }

	/**
	 * 初始化ActionBar，
	 * @param isCustomActionBar 是否使用自定义的ActionBar布局
	 */
	public void initActionBar(boolean isCustomActionBar) {
		if (isCustomActionBar)
			setActionBarLayout();
	}

	/** 自定义ActionBar 标题 */
	private TextView actionBarTitle;

	/** 自定义ActionBar 图标 */
	private ImageView actionBarIcon;

	/** 设置ActionBa布局 */
	protected void setActionBarLayout() {
		mActionBar = getSupportActionBar();
		if (null != mActionBar) {
			// 定义 ActionBar 背景
			mActionBar.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.bottom_bar));			 
			// 由子类决定是否显示返回上一级指示符
			// actionBar.setDisplayHomeAsUpEnabled(true);
			//  开放自定义view
			mActionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.actionbar_demo_layout, null);

			actionBarTitle = (TextView) v.findViewById(R.id.actionbar_tv_title);
			actionBarIcon = (ImageView) v.findViewById(R.id.actionbar_iv_back);
			LayoutParams layout = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mActionBar.setCustomView(v, layout);
		}
	}

	//获得该页面的实例
	public <T> T getLogicImpl(Class cls, Object obj ){
		return LogicProxy.getInstance().bind(cls, obj);
	}

	@Override
	public TextView getBarTitle() {
		return actionBarTitle;
	}

	@Override
	public ImageView getBarIcon() {
		return actionBarIcon;
	}

	@Override
	public void setBarTitleVisibility(int visibility) {
		if (actionBarTitle != null)
			actionBarTitle.setVisibility(visibility);
	}

	@Override
	public void setBarIconVisibility(int visibility) {
		if (actionBarIcon != null)
			actionBarIcon.setVisibility(visibility);
	}

	/** 设置ActionBar标题 */
	@Override
	public void setBarTitle(String title) {
		// 设置自定义的ActionBar的title
		if (actionBarTitle != null)
			actionBarTitle.setText(title);
		else
			getSupportActionBar().setTitle(title);
	}

	@Override
	public void setBarTitle(int resid ) {
		if (actionBarTitle != null)
			actionBarTitle.setText(resid);
		else
			getSupportActionBar().setTitle(resid);
	}
	@Override
	public void setBarIcon(Bitmap bitmap) {
		if (actionBarIcon != null)
			actionBarIcon.setImageBitmap(bitmap);
	}

	@Override
	public void setBarIcon(int icon) {
		if (actionBarIcon != null)
			actionBarIcon.setImageResource(icon);
	}

	/** activity的返回结果码 */
	private int resultCode;

	/** activity的返回结果数据 */
	private Intent intentData;

	/**  获得返回结果数据 */
	public Intent getIntentData() {
		return intentData;
	}

	/** 设置Activity返回结果 */
	public void setIntentData(Intent intentData) {
		this.intentData = intentData;
	}

	/** 获得Activity返回结果码 */
	public int getResultCode() {
		return resultCode;
	}

	/** 设置Activity返回结果码 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
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

	/** 结束Activity时，设置返回结果 */
	public void finishWithResult(){
		this.setResult(getResultCode(), getIntentData());
		finish();
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
