package tyxo.mobilesafe.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import tyxo.functions.fuli.activity.FuliActivity;
import tyxo.functions.prettygirls.home.HomeActivity;
import tyxo.mobilesafe.ConstValues;
import tyxo.mobilesafe.ConstantsMy;
import tyxo.mobilesafe.R;
import tyxo.mobilesafe.widget.SystemBarTintManager;
import tyxo.mobilesafe.widget.annotation.SlideInAnimationHandler;

/**
 * Created by LY on 2016/7/21 11: 14.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class ViewUtil {
    /**
     * @author tyxo
     * @created at 2016/7/21 10:21
     * @des : 设置 SpannableString 的样式
     *  setSpan(Object what, int start, int end, int flags)方法 参数:
     *      what : 表示设置的格式是什么
     *      start: 表示需要设置格式的子字符串起始下标
     *      end  : 表示终了下标
     *      flags: 属性,共有四种
     *          Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
     *          Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
     *          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
     *          Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
     */
    public static void setSpannableStringStyle(Context context,SpannableString ss) {
        /*
        //字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099ee"));
        //ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.white));
        ss.setSpan(colorSpan,9,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //背景颜色
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor("#ac00ff30"));
        ss.setSpan(colorSpan,9,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //字体大小不一
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);
        RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan07 = new RelativeSizeSpan(1.2f);
        ss.setSpan(sizeSpan01,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan02,1,2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan03,2,3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan04,3,4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan05,4,5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan06,5,6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan07,6,7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //设置中划线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        ss.setSpan(strikethroughSpan,5,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //设置下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ss.setSpan(underlineSpan,5,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //设置上标
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        ss.setSpan(superscriptSpan,5,8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //设置下标
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        ss.setSpan(subscriptSpan,5,8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //设置粗体,斜体
        StyleSpan styleSpan01 = new StyleSpan(Typeface.BOLD);//粗体
        StyleSpan styleSpan02 = new StyleSpan(Typeface.ITALIC);//斜体
        ss.setSpan(styleSpan01,5,8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan02,9,11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        main_up_tv_1.setHighlightColor(Color.parseColor("#36969696"));//?*/
        /*
        //设置表情
        Drawable drawable = context.getResources().getDrawable(R.drawable.trade_person);
        drawable.setBounds(0,0,42,42);
        ImageSpan imageSpan = new ImageSpan(drawable);
        ss.setSpan(imageSpan,6,8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
        /*
        //可点击的富文本
        ClickableSpanMy clickableSpanMy = new ClickableSpanMy(context, "www.baidu.com");
        ss.setSpan(clickableSpanMy,5,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/

        //设置超链接文本,其实此类继承clickableSpan
        URLSpan urlSpan = new URLSpan("http://www.baidu.com");
        ss.setSpan(urlSpan,5,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        /*除此之外，还有MaskFilterSpan可以实现模糊和浮雕效果，
        RasterizerSpan可以实现光栅效果，因为以上两个使用频率不高，而且效果也不是很明显*/
    }

    /**
     * 设置属性动画, 参数:
     *  第一个target :      目标
     *  第二个propertyName: 动画的名称
     *  第三 values       : 可变参数,旋转的角度
     * */
    public static void setAimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotationY",0,90,270,360);
        animator.setDuration(3000);                 //设置动画时间
        animator.setRepeatCount(animator.INFINITE); //设置无限旋转
        animator.start();
    }

    /***
     * 设置状态栏颜色
     * @param context
     */
    public static void setStateBar(Activity context, int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(context,true);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);//通知栏所需颜色
        }
    }
    @TargetApi(19)
    public static void setTranslucentStatus(Activity context, boolean on) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置状态栏高度(对应上边方法设置颜色后,全屏显示问题)
     * @param viewGroup
     */
    public static void setStateBarHeight(Activity activity,ViewGroup viewGroup){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            viewGroup.setPadding(0,getStateBarHeight(activity),0,0);
        }
    }
    public static int getStateBarHeight(Activity activity){  //状态栏高度算法
        int stateHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        stateHeight = localRect.top;
        if (0==stateHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                stateHeight = activity.getResources().getDimensionPixelSize(i5);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return stateHeight;
    }

    /*
    //一步一步退回到首页之后，就不能再往后退出了。想要到首页之后，在往后退出的时候，进入主程序，关闭这个内置的webview
    public static void goBack(Context context,WebView mWebView,String url){//判断是否是刚才的url
        if (mWebView.canGoBack()) {
            if (mWebView.getUrl().contains(url)) {
                super.onBackPressed();
            } else {
                mWebView.goBack();
            }
        } else {
            super.onBackPressed();
        }
    }*/

    /** 从本地相册获取照片 */
    public static void getImageFromAlbum(Activity context){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        context.startActivityForResult(intent, ConstantsMy.CODE_REQUEST_IMAGE);
    }
    /** 从照相机获取照片 图片被压缩*/
    public static void getImageFromCamera(Activity activity){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            activity.startActivityForResult(intent, ConstantsMy.CODE_REQUEST_CAMERA);
        } else {
            ToastUtil.showToastS(activity,"请确认已经插入SD卡");
        }
    }

    /** 从照相机获取照片 */
    public static void getImageFromCameraBig(Activity activity){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            String out_file_path = ConstValues.SAVE_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            ConstValues.SAVE_IMAGE_DIR_PATH_TEMP = ConstValues.SAVE_IMAGE_DIR_PATH +System.currentTimeMillis()+ ".jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getImageFile(ConstValues.SAVE_IMAGE_DIR_PATH_TEMP)));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//加上这两句,onActivityResult里的data会报空
            activity.startActivityForResult(intent, ConstantsMy.CODE_REQUEST_CAMERABIG);
        } else {
            ToastUtil.showToastS(activity,"请确认已经插入SD卡");
        }
    }

    /** 保存从相机返回的图片 :通过bitmap转化成相应的图片文件了,不过得到最终的图片是被压缩了的。 */
    public static boolean saveImage(Bitmap photo, String spath){
        try{
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true ;
    }

    /** 获取图片的uri */
    public static File getImageFile(String capturePath){
        File file = new File(capturePath);
        return file;
    }

    /** 设置页面右下角 白色menu 图标 动画 */
    public static void initBottomRightMenu(final Activity activity, final ScrollView scrollView) {
        final ImageView fabIconNew = new ImageView(activity);
        fabIconNew.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_jia_new_light));
        FloatingActionButton rightLButton = new FloatingActionButton.Builder(activity)
                .setContentView(fabIconNew)
                .build();
        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(activity);
        ImageView rlIcon1 = new ImageView(activity);
        ImageView rlIcon2 = new ImageView(activity);
        ImageView rlIcon3 = new ImageView(activity);
        ImageView rlIcon4 = new ImageView(activity);

        rlIcon1.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_chat_light));
        rlIcon2.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_camera_light));
        rlIcon3.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_video_light));
        rlIcon4.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_place_light));

        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .attachTo(rightLButton)
                .build();
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION,45);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fabIconNew,pvhR);
                animator.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION,0);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animator.start();
            }
        });

        ArrayList<FloatingActionMenu.Item> itemList = rightLowerMenu.getSubActionItems();

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(activity, MapActivity.class);
                activity.startActivity(intent);*/
                rightLowerMenu.close(true);
            }
        });
        rlIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HomeActivity.class);
                activity.startActivity(intent);
                rightLowerMenu.close(true);
            }
        });
        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new SaveImageTask(activity).execute(getBitmapScreenShot(scrollView));
                showDialogScreenShot(activity,null,scrollView);
                rightLowerMenu.close(true);
            }
        });
    }

    /** 设置页面右下角 彩色menu 图标 动画 */
    public static void initCenterColorMenu(final Activity activity) {
        int redActionButtonSize = activity.getResources().getDimensionPixelSize(R.dimen.red_action_button_size2);
        int redActionButtonMargin = activity.getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = activity.getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = activity.getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = activity.getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = activity.getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = activity.getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        ImageView fabIconStar = new ImageView(activity);
        fabIconStar.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_important));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(activity)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_LEFT_CENTER)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(activity);
        lCSubBuilder.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_action_blue_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        ImageView lcIcon1 = new ImageView(activity);
        ImageView lcIcon2 = new ImageView(activity);
        ImageView lcIcon3 = new ImageView(activity);
        ImageView lcIcon4 = new ImageView(activity);
        ImageView lcIcon5 = new ImageView(activity);

        lcIcon1.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_camera));
        lcIcon2.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_picture));
        lcIcon3.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_video));
        lcIcon4.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_location_found));
        lcIcon5.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_headphones));

        // Build another menu with custom options
        final FloatingActionMenu leftCenterMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(lCSubBuilder.setContentView(lcIcon1, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon2, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon3, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon4, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon5, blueContentParams).build())
                .setRadius(redActionMenuRadius)
                .setStartAngle(70)
                .setEndAngle(-70)
                .attachTo(leftCenterButton)
                .build();

        lcIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FuliActivity.class);
                activity.startActivity(intent);
                leftCenterMenu.close(true);
            }
        });
    }

    /** 设置页面底部中央 menu 图标 动画 */
    public static void initCenterBottomMenu(Activity activity) {
        ImageView fabContent = new ImageView(activity);
        fabContent.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_settings));

        FloatingActionButton darkButton = new FloatingActionButton.Builder(activity)
                .setTheme(FloatingActionButton.THEME_DARK)
                .setContentView(fabContent)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(activity)
                .setTheme(SubActionButton.THEME_DARK);
        ImageView rlIcon1 = new ImageView(activity);
        ImageView rlIcon2 = new ImageView(activity);
        ImageView rlIcon3 = new ImageView(activity);
        ImageView rlIcon4 = new ImageView(activity);
        ImageView rlIcon5 = new ImageView(activity);

        rlIcon1.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_chat));
        rlIcon2.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_camera));
        rlIcon3.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_video));
        rlIcon4.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_place));
        rlIcon5.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_headphones));

        // Set 4 SubActionButtons
        FloatingActionMenu centerBottomMenu = new FloatingActionMenu.Builder(activity)
                .setStartAngle(0)
                .setEndAngle(-180)
                .setAnimationHandler(new SlideInAnimationHandler())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
                .attachTo(darkButton)
                .build();
    }

    /** 设置页面中央 圆形包围menu 图标 动画 有bug待解决 */
    public static void initCircleMenu(Activity activity){
        TextView a = new TextView(activity); a.setText("a"); a.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView b = new TextView(activity); b.setText("b"); b.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView c = new TextView(activity); c.setText("c"); c.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView d = new TextView(activity); d.setText("d"); d.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView e = new TextView(activity); e.setText("e"); e.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView f = new TextView(activity); f.setText("f"); f.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView g = new TextView(activity); g.setText("g"); g.setBackgroundResource(android.R.drawable.btn_default_small);
        TextView h = new TextView(activity); h.setText("h"); h.setBackgroundResource(android.R.drawable.btn_default_small);
        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        a.setLayoutParams(tvParams);
        b.setLayoutParams(tvParams);
        c.setLayoutParams(tvParams);
        d.setLayoutParams(tvParams);
        e.setLayoutParams(tvParams);
        f.setLayoutParams(tvParams);
        g.setLayoutParams(tvParams);
        h.setLayoutParams(tvParams);

        int redActionButtonSize = activity.getResources().getDimensionPixelSize(R.dimen.action_center_size);
        int marginL = activity.getResources().getDimensionPixelOffset(R.dimen.action_center_l);
        int marginT = activity.getResources().getDimensionPixelOffset(R.dimen.action_center_t);
        int marginR = activity.getResources().getDimensionPixelOffset(R.dimen.action_center_r);
        int marginB = activity.getResources().getDimensionPixelOffset(R.dimen.action_center_b);

        ImageView ivStar = new ImageView(activity);
        ivStar.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_action_important));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(marginL,marginT,marginR, marginB);
        ivStar.setLayoutParams(starParams);

        FloatingActionButton centerButton = new FloatingActionButton.Builder(activity)
                .setContentView(ivStar)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_LEFT_CENTER)
                .setLayoutParams(starParams)
                .build();

        FloatingActionMenu circleMenu = new FloatingActionMenu.Builder(activity)
                .setStartAngle(0)
                .setEndAngle(360)
                .setRadius(activity.getResources().getDimensionPixelSize(R.dimen.radius_large))
                .addSubActionView(a)
                .addSubActionView(b)
                .addSubActionView(c)
                .addSubActionView(d)
                .addSubActionView(e)
                .addSubActionView(f)
                .addSubActionView(g)
                .addSubActionView(h)
                .attachTo(centerButton)
                .build();
    }


    /** 保存 屏幕超长截图 */
    public static void showDialogScreenShot(final Context context, final ListView listView,final ScrollView scrollView){
        //ScreenShot.saveImageToGallery(context,ScreenShot.createBitmap(listView,context),"名字");//保存listview超长图片并插入图库
        //ScreenShot.savePic(ScreenShot.getBitmapByView(scrollView),"/sdcard/tests.png"); //保存 scrollView 屏幕超长截图

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
        builder.setItems(new String[]{"保存超长截图"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bitmap imageBitmap;
                if (listView != null) {
                    imageBitmap = getBitmapScreenShot(listView,context);
                } else {
                    imageBitmap = getBitmapScreenShot(scrollView);
                }
                if (imageBitmap != null) {
                    new SaveImageTask(context).execute(imageBitmap);
                }
            }
        });
        builder.show();
    }
    /** 获取超长截图的 Bitmap listview*/
    public static Bitmap getBitmapScreenShot(ListView listView,Context context){
        return ScreenShot.createBitmap(listView,context);
    }
    /** 获取超长截图的 Bitmap scrollView*/
    public static Bitmap getBitmapScreenShot(ScrollView scrollView){
        return ScreenShot.getBitmapByView(scrollView);
    }
    /** 保存 屏幕超长截图 异步任务 */
    public static class SaveImageTask extends AsyncTask<Bitmap, Void, String> {
        Context context;

        SaveImageTask(Context context){
            this.context = context;
        }
        @Override
        protected String doInBackground(Bitmap... params) {
            String result = "保存失败";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();

                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }

                File imageFile = new File(file.getAbsolutePath(),new Date().getTime()+".jpg");
                FileOutputStream outStream = null;
                outStream = new FileOutputStream(imageFile);
                Bitmap image = params[0];
                image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                result = context.getResources().getString(R.string.save_picture_success,  file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("tyxo", "保存结果 : "+result);
        }
    }
}




























