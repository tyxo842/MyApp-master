package tyxo.functions.prettygirls.about;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import coder.mylibrary.base.BaseFragment;
import coder.mylibrary.base.GestureActivity;
import jp.wasabeef.glide.transformations.BlurTransformation;
import tyxo.functions.prettygirls.app.MyApplication;
import tyxo.mobilesafe.R;

/**
 * Created on 2016/7/4.
 */
public class AboutActivity extends GestureActivity {

    private ImageView mBackdrop;
    private Toolbar mAboutToolbar;
    private CollapsingToolbarLayout mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected void doFinish() {
        finishActivity();
    }

    private void initView() {
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mBackdrop = (ImageView) findViewById(R.id.backdrop);
        mAboutToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        mAboutToolbar.setTitle("关于我");
        setSupportActionBar(mAboutToolbar);
        mAboutToolbar.setNavigationIcon(R.drawable.ic_back);
        mAboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        //毛玻璃效果
        Glide.with(this)
                .load(MyApplication.currentGirl)
                .bitmapTransform(new BlurTransformation(this, 15))
                .into(mBackdrop);
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onClick(View v) {

    }
}
