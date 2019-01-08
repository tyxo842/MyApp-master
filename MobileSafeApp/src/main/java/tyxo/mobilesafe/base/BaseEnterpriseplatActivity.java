package tyxo.mobilesafe.base;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tyxo.mobilesafe.R;


/**
* @author  liyang
	// 第一种情况 返回键 + 标题(居中)
	TextView mTitlebar = (TextView) findViewById(R.id.tv_actionbar_textview);
	mTitlebar.setText("activity 标题");

	// 第二种情况 返回键 + 标题 + 保存键
	extView mTitlebar = (TextView) findViewById(R.id.tv_actionbar_textview);
	mTitlebar.setVisibility(View.GONE);
	extView mTitlebar2 = (TextView) findViewById(R.id.tv_actionbar_textview2);
	mTitlebar2.setVisibility(View.VISIBLE);
	mTitlebar2.setText("activity 标题");
*/
public abstract class BaseEnterpriseplatActivity extends BaseActivity implements View.OnClickListener {

	private ActionBar mActionBar;
	private ImageView iv_main_back;

	/**
	 * 自定义ActionBar 标题
	 */
	private TextView actionBarTitle;
	private TextView actionBarTitle2;
	/**
	 * 自定义ActionBar 图标
	 */
	private ImageView actionBarIcon;
	/**
	 * 自定义ActionBar 保存
	 */
	private TextView actionBarSave;

	protected  void setContentView(){
		initActionBar(true);
	}

	/** 设置ActionBa布局 */
	@Override
	protected void setActionBarLayout() {
//		super.setActionBarLayout();
		mActionBar = getSupportActionBar();
		if (null != mActionBar) {
			// 定义 ActionBar 背景
			// 由子类决定是否显示返回上一级指示符
			mActionBar.setDisplayHomeAsUpEnabled(false);
			//  开放自定义view
			mActionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.actionbar_demo_layout2, null);

			actionBarTitle = (TextView) v.findViewById(R.id.tv_actionbar_textview);
			actionBarTitle2 = (TextView) v.findViewById(R.id.tv_actionbar_textview2);
			actionBarSave = (TextView) v.findViewById(R.id.iv_main_save);
			iv_main_back = (ImageView) v.findViewById(R.id.iv_main_back);

			actionBarSave.setOnClickListener(this);
			iv_main_back.setOnClickListener(this);

			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
			mActionBar.setCustomView(v, layout);
//			mActionBar.setHomeAsUpIndicator(R.drawable.icon_return);
		}
	}

	@Override
	public void initData() {
		super.initData();
	}

	/** 设置ActionBar */
	public void setActionBarSave(String s) {
		// 设置自定义的ActionBar
		if (actionBarTitle2 != null){
			actionBarTitle2.setText(s);
		}else {
			getSupportActionBar().setTitle(s);
		}

		if (actionBarTitle != null)
			actionBarTitle.setText(s);
		else
			getSupportActionBar().setTitle(s);
	}

	public void setActionBarSave(int resid ) {
		if (actionBarTitle2 != null){
			actionBarTitle2.setText(resid);
		}else {
			getSupportActionBar().setTitle(resid);
		}

		if (actionBarTitle != null)
			actionBarTitle.setText(resid);
		else
			getSupportActionBar().setTitle(resid);
	}

	@Override
	public TextView getBarTitle() {
		return actionBarTitle;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_main_back: {
				this.finish();
			}

			break;
			case R.id.iv_main_save: {
				// 相应操作
			}

			break;
			default:
				break;
		}
	}
}
