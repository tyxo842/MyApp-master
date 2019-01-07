package tyxo.mobilesafe.activity;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.BaseActivityToolbar;

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

public class PicActivity extends BaseActivityToolbar implements View.OnClickListener{

    private ImageView iv_nav_pic1;
    private long spaceTime = 2000;//时间间隔
    long[] mHits = new long[2];//要设置多少连击事件

    @Override
    protected void setMyContentView() {
        setContentView(R.layout.activity_nav_pic);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);

        iv_nav_pic1 = (ImageView) contentView.findViewById(R.id.iv_nav_pic1);

        setGlideGifView(R.drawable.zym);
    }

    protected void initListener() {
        iv_nav_pic1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nav_pic1:
                //每点击一次 实现左移一格数据
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //给数组的最后赋当前时钟值
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                //当0出的值大于当前时间-spaceTime时  证明在spaceTime内点击了2次
                if(mHits[0] > SystemClock.uptimeMillis() - spaceTime){  //双击
                    //Toast.makeText(this, "被双击了", Toast.LENGTH_SHORT).show();
                    setGlideGifView(R.drawable.zymk_ppp);
                }else{  //单击
                    setGlideGifView(R.drawable.zym_n);
                }

                /*DoubleClickExitDetector doubleClick = new DoubleClickExitDetector(this);
                if (doubleClick.onClick()) {
                    setGlideGifView(R.drawable.zymk_ppp);
                }else{
                    setGlideGifView(R.drawable.zym_n);
                }*/
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
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_nav_pic1);
    }

    long[] mArrs = new long[3];

    public void clicks(long intervalTime, int clickTimes) {
        //src    源数组
        //srcPos 源数组的开始拷贝位置
        //dst    目标数组
        //dstPos 目标数组的开始拷贝位置
        //length 数组的拷贝长度
        System.arraycopy(mArrs, 1, mArrs, 0, mArrs.length - 1); //拷贝数组
        mArrs[mArrs.length - 1] = SystemClock.uptimeMillis();
        if (mArrs[0] >= (SystemClock.uptimeMillis() - intervalTime)) {
            Log.d("clickEvent", "clickTimes次点击事件");
            mArrs = new long[clickTimes];
        }
    }






































}
