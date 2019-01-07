package tyxo.mobilesafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.utils.ViewUtil;
import tyxo.mobilesafe.widget.recyclerdivider.DoubleClickExitDetector;

/**
 * Created by LY on 2017/2/28 16: 51.
 * Mail      tyxo842@163.com
 * Describe : 测试图片加载类:Glide
 *
        中文博客地址: http://blog.csdn.net/fancylovejava/article/details/44747759
        英文地址: https://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en
        Glide默认的Bitmap格式是RGB_565 ，比ARGB_8888格式(Picasso的)的内存开销要小一半。
        让Glide既缓存全尺寸又缓存其他尺寸：
        Glide.with(this)
        .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(ivImgGlide);

        Picasso是加载了全尺寸的图片到内存，然后让GPU来实时重绘大小。而Glide加载的大小和ImageView的大小是一致的，因此更小。
        Picasso指定加载图片:
        Picasso.with(this)
        .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
        .resize(768, 432)
        .into(ivImgPicasso);
        Picasso.with(this)
                .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
                .fit()
                .centerCrop()
                .into(ivImgPicasso);
 */
// TODO: 2017/3/1 不显示!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
public class PicActivity2 extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_nav_pic1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewUtil.setStateBar(this,R.color.bg_green);/** 设置状态栏颜色 */
        setContentView(R.layout.activity_nav_pic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initIds();
        initView();
        initListener();
    }

    private void initIds() {
        iv_nav_pic1 = (ImageView) findViewById(R.id.iv_nav_pic1);
    }

    private void initView() {
        setGlideGifView(R.drawable.zym);
    }

    protected void initListener() {
        iv_nav_pic1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_pic1:
                DoubleClickExitDetector doubleClick = new DoubleClickExitDetector(this);
                if (doubleClick.onClick()) {
                    setGlideGifView(R.drawable.zymk_ppp);
                }else{
                    setGlideGifView(R.drawable.zym_n);
                }
                break;
        }
    }

    private void setGlideGifView(int zym) {
        //Glide.with(this).load(url).asGif().into(iv_nav_pic1);
        //中缓存策略可以为:Source及None,None及为不缓存,Source缓存原型.如果为ALL和Result就不行.
        //.load(this.getResources().getDrawable(R.drawable.zym)) 崩溃
        Glide.with(this)
                .load(zym)
                .asGif()
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_nav_pic1);
    }
}
