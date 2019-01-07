package tyxo.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;
import tyxo.functions.dragphoto.DragPicActivity;
import tyxo.functions.smoothpic.SmoothPicActivity;
import tyxo.functions.weather.WeatherActivity;
import tyxo.mobilesafe.ConstValues;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.BaseActivityToolbar;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.utils.ViewUtil;
import tyxo.mobilesafe.widget.AutoClearEditText;
import tyxo.mobilesafe.widget.TouchImageView;
import tyxo.mobilesafe.widget.brokenview.BrokenCallback;
import tyxo.mobilesafe.widget.brokenview.BrokenTouchListener;
import tyxo.mobilesafe.widget.brokenview.BrokenView;

/**
* @author ly
* @des : 配合 清单设置:
*                   android:label=""
*                   android:theme="@style/MyToolbarTheme"
*/
public class ImageViewActivityMy extends BaseActivityToolbar implements RequestListener<String,
        GlideDrawable>, View.OnClickListener ,View.OnLongClickListener {

    private String url;
    private TouchImageView image;       // 图片显示
    private TextView tv_iv_layout_1;    // 点击切换
    private ImageView iv_iv_layout_1;   // 图片显示
    private AutoClearEditText numInput; // badge数字输入框
    private Button button;              // 设置按钮
    private Button removeBadgeBtn;      // 清除按钮
    private TextView tv_iv_layout_2;    // 接收 eventBus 发送的消息

    /** brokenview 用到的属性 */
    private BrokenView mBrokenView;
    private BrokenTouchListener colorfulListener;
    private BrokenTouchListener whiteListener;
    private Paint whitePaint;
    private ScrollView crollview;

    @Override
    protected void setMyContentView() {
        setContentView(R.layout.activity_iv_layout);
        EventBus.getDefault().register(this);   //注册EventBus

        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        TextView textViewHomePackage = (TextView) findViewById(R.id.imageActivity_tv_package);
        textViewHomePackage.setText("launcher:" + currentHomePackage);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        image = (TouchImageView) contentView.findViewById(R.id.image_tiv_picture);
        tv_iv_layout_2 = (TextView) contentView.findViewById(R.id.tv_iv_layout_2);
        tv_iv_layout_1 = (TextView) contentView.findViewById(R.id.tv_iv_layout_1);
        iv_iv_layout_1 = (ImageView) contentView.findViewById(R.id.iv_iv_layout_1);
        ViewUtil.setAimation(iv_iv_layout_1);    //设置属性动画
        numInput = (AutoClearEditText) contentView.findViewById(R.id.imageActivity_et_numinput);
        button = (Button) contentView.findViewById(R.id.imageActivity_btn_setBadge);
        removeBadgeBtn = (Button) contentView.findViewById(R.id.imageActivity_btn_setBadge);

        //url = getIntent().getStringExtra("url");
        url = ConstValues.MYPHOTO_URL;
        initGlide();

        crollview = (ScrollView)contentView.findViewById(R.id.activity_iv_scrollview);
        ViewUtil.initBottomRightMenu(this,crollview);/** 初始化 底部 右下角menu图标 */ /**第二个超长截图*/

        initBrokenView(); // 初始化特效
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_iv_layout_1.setOnClickListener(this);
        button.setOnClickListener(this);
        removeBadgeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_iv_layout_1:
                initGlideTarget();
                break;
            case R.id.imageActivity_btn_setBadge:
            {
                int badgeCount = 0;
                try {
                    badgeCount = Integer.parseInt(numInput.getText().toString());
                } catch (NumberFormatException e) {
                    ToastUtil.showToastS(getApplicationContext(),"Error input");
                }

                boolean success = ShortcutBadger.applyCount(this, badgeCount);
                ToastUtil.showToastS(mContext,"Set count=" + badgeCount + ", success=" + success);
                break;
            }

            case R.id.imageActivity_btn_removeBadge:
            {
                boolean success = ShortcutBadger.removeCount(this);
                ToastUtil.showToastS(mContext,"success=" + success);
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                image.setDrawingCacheEnabled(true);
                Bitmap imageBitmap = image.getDrawingCache();
                if (imageBitmap != null) {
                    new SaveImageTask().execute(imageBitmap);
                }
            }
        });
        builder.show();

        return true;
    }

    //设置 toolbar 标题等,可删
    @Override
    public void onCreateCustomToolbar(Toolbar toolbar) {
        super.onCreateCustomToolbar(toolbar);
        TextView tv_toolbar_title = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("ImageActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.menu_imageactivity, menu);
        MenuItem itemCompat = menu.findItem(R.id.action_search) ;
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(itemCompat);
        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);*/
        getMenuInflater().inflate(R.menu.menu_imageractivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_settings) { return true; }*/
        switch (id) {
            case R.id.image_action_1:   //跳转到recyclerActivity
                Intent intent = new Intent(this, RecyclerActivity.class);
                startActivity(intent);
                break;
            case R.id.image_action_weather:   //跳转到 天气
                Intent intent2 = new Intent(this, WeatherActivity.class);
                startActivity(intent2);
                break;
            case R.id.image_action_smooth_pic:   //跳转到 炫酷流畅动画
                Intent intent3 = new Intent(this, SmoothPicActivity.class);
                startActivity(intent3);
                break;
            case R.id.image_action_drag_pic:   //跳转到 可拖拽的pic
                Intent intent4 = new Intent(this, DragPicActivity.class);
                startActivity(intent4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private SimpleTarget target = new SimpleTarget() {
        @Override
        public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
            iv_iv_layout_1.setImageBitmap((Bitmap) resource);//图片加载完成,设置到iv
        }
    };

    private class SaveImageTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
            String result = "保存失败";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();

                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }

                File imageFile = new File(file.getAbsolutePath(), new Date().getTime() + ".jpg");
                FileOutputStream outStream = null;
                outStream = new FileOutputStream(imageFile);
                Bitmap imageBit = params[0];
                imageBit.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                result = getResources().getString(R.string.save_picture_success, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            ToastUtil.showToastS(getApplicationContext(), result);
            image.setDrawingCacheEnabled(false);
        }
    }

    /**BrokenView 特效*/
    private void initBrokenView() {
        mBrokenView = BrokenView.add2Window(this);
        final BrokenCallback callback = new MyCallBack();
        mBrokenView.setCallback(callback);
        whitePaint = new Paint();
        whitePaint.setColor(0xffffffff);
        colorfulListener = new BrokenTouchListener.Builder(mBrokenView).build();
        whiteListener = new BrokenTouchListener.Builder(mBrokenView).
                setPaint(whitePaint).
                build();
        image.setOnTouchListener(colorfulListener);  // 放开就使用破碎特效,ps:无数据bg时可点击看到.
    }
    // brokenview 的回调显示
    private void showCallback(View v, String s) {
        switch (v.getId()) {
            case R.id.image:
                //ToastUtil.showToastS(this,s);
                break;
        }
    }
    // brokenview 的回调
    private class MyCallBack extends BrokenCallback {
        @Override
        public void onStart(View v) {
            showCallback(v, "onStart");
        }
        @Override
        public void onCancel(View v) {
            showCallback(v, "onCancel");
        }
        @Override
        public void onRestart(View v) {
            showCallback(v, "onRestart");
        }
        @Override
        public void onFalling(View v) {
            showCallback(v, "onFalling");
        }
        @Override
        public void onFallingEnd(View v) {
            //pb_order_list.setVisibility(View.VISIBLE);
            //onRefresh();
            image.setVisibility(View.GONE);
        }
        @Override
        public void onCancelEnd(View v) {
            showCallback(v, "onCancelEnd");
        }
    }

    /** Glide的toBytes() 和transcode() 两个方法可以用来获取、解码和变换背景图片，并且transcode() 方法还能够改变图片的样式.
     *  默认选择HttpUrlConnection作为网络协议栈，还可以选择OkHttp和Volley作为网络协议.
     *  如在图片加载过程中，使用Drawables对象作为占位符、图片请求的优化、图片的宽度和高度可重新设定、缩略图和原图的缓存等功能.
     * */
    private void initGlide() {
        /*
        //加载(小屏图) 初次 不显示??(因为listener)
        Glide.with(this)
                .load(url)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)//设置磁盘缓存的内容,是个枚举类.all代码缓存源资源和转换后的资源.
                //.centerCrop()         //显示位置,或大小
                //.crossFade(0)         //显示时的动画效果(不包括从内存获取图片资源) 淡入淡出动画效果
                //.thumbnail("缩略比例") //加载一个缩略图,同时加载多张图片的时候用.
                //.decoder("对原数据的解码")
                //.cacheDecoder("对磁盘缓存数据进行解码")
                //.sourceEncoder("对请求的数据进行编码,然后存到缓存里,之后从缓存中拿出的时候要通过解码")
                //.priority("设置当次请求的优先级")
                //.transform("设置对资源进行转换的接口")
                //.dontTransform()      //移除当前的转换
                //.animate("设置资源加载完成后的动画,不包括从内存缓存中的获取")
                //.dontAnimate()          //移除设置的动画
                //.placeholder("设置加载的时候的图片")    //placeholder可能导致图片加载不出来,去掉即可
                //.error("设置加载失败后显示的图片")
                //.skipMemoryCache("设置是否跳过内存缓存")
                //.override("设置加载资源图片的像素宽 高")
                //.into(Y)              //用于 预加载
                //.into(ImageView)      //加载到view上
                //.into(int ,int )      //用于 预加载
                //.preload()            //预加载 实际调用的是两个into
                //.preload(int ,int)    //预加载 实际调用的是两个into
                .listener(this)         //设置加载监听
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);*/

        Glide.with(this)
                .load(url)
                .asBitmap()
                //.centerCrop()                               //大屏图
                .fitCenter()                                //适配图(这个也注释后,是小图)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存源资源和转换后的资源
                .placeholder(R.drawable.loading)            //占位符 也就是加载中的图片，可放个gif//placeholder可能导致图片加载不出来,去掉即可
                .error(R.drawable.icon_zanwu)               //失败图片
                .into(new BitmapImageViewTarget(image));

        /*//毛玻璃效果
        Glide.with(this)
                .load(url)
                .bitmapTransform(new BlurTransformation(this, 15))
                .into(image);*/
    }
    private void initGlideTarget(){
        //Glide.with(this).load(R.drawable.ic_launceher).centerCrop().transform(new GlideRoundTransform(this)).into(iv_demo);
        Glide.with( this )
//                        .load( url )
                .load( R.drawable.glide_photo )
                .asBitmap()
                .placeholder(R.drawable.loading) //占位符 也就是加载中的图片，可放个gif
                .error(R.drawable.icon_zanwu) //失败图片
                .into( target ) ;
    }

    //glide 加载图片 失败监听
    @Override
    public boolean onException(Exception e, String model,
                               com.bumptech.glide.request.target.Target<GlideDrawable> target,
                               boolean isFirstResource) {
        return false;
    }

    //glide 加载图片监听
    @Override
    public boolean onResourceReady(GlideDrawable resource, String model,
                                   com.bumptech.glide.request.target.Target<GlideDrawable> target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        image.setImageDrawable(resource);
        image.setOnLongClickListener(this);
        return false;
    }

    /*@Override
    public void onCreateCustomToolbar(Toolbar toolbar) {
        super.onCreateCustomToolbar(toolbar);
        toolbar.showOverflowMenu();
        //getLayoutInflater().inflate(R.layout.toolbar_demo_layout, toolbar);
        getLayoutInflater().inflate(R.layout.layout_toolbar, toolbar);
        TextView tv_toolbar_title = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("点击切换 测试");
    }*/

    /**
     * 显示为灰色,但是已经调用了,eventBus .
     * 执行在主线程,
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     * */
    @Subscribe
    public void onEventMainThread(Object event){
        String msg = (String) event;
        tv_iv_layout_2.setText(msg);
    }

    /** 与发布者在同一个线程 */
    @Subscribe
    public void onEvent(Object event){ }

    /**
     * 执行在子线程，如果发布者是子线程则直接执行，如果发布者不是子线程，则创建一个再执行,
     * 此处可能会有线程阻塞问题。
     */
    @Subscribe
    public void onEventBackgroundThread(Object event){ }

    /**
     * 执行在在一个新的子线程,
     * 适用于多个线程任务处理， 内部有线程池管理。
     */
    @Subscribe
    public void onEventAsync(Object event){ }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);  //友盟统计
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); //反注册EventBus
    }
}
