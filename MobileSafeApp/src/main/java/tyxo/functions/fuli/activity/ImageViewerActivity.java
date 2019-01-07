package tyxo.functions.fuli.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import io.realm.Realm;
import io.realm.RealmResults;
import tyxo.functions.fuli.Fragment.ImageViewerFragment;
import tyxo.functions.fuli.model.ImageFuli;
import tyxo.functions.fuli.widget.ExtendedViewPager;
import tyxo.mobilesafe.R;

/**
* @author ly
* @created at 2016/9/13 11:31
* @des : mm详情界面依附的Activity,具体显示操作在ImageViewerFragment.
*/
public class ImageViewerActivity extends AppCompatActivity {

    Realm realm;
    private RealmResults<ImageFuli> images;
    private int currentIndex;
    private ExtendedViewPager mViewPager;
    private int type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpager);
        mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

        currentIndex = getIntent().getIntExtra("CURRENT_INDEX", 0);
        type = getIntent().getIntExtra("TYPE",0);

        realm= Realm.getInstance(this);
        images=realm.where(ImageFuli.class).equalTo("type",type).findAll();

        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(currentIndex);
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImageViewerFragment.newInstance(
                    images.get(position).getUrl());
        }
    }
}
