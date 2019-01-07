package tyxo.mobilesafe.widget.brokenview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Random;

public class Utils {

    private Utils() {}
    static int screenWidth;
    static int screenHeight;
    private static Random random = new Random();
    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static final Canvas mCanvas = new Canvas();

    static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }

    static Bitmap convertViewToBitmap(View view) {
        view.clearFocus();
        Bitmap bitmap = createBitmapSafely(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_4444, 2);
        if (bitmap != null) {
            mCanvas.setBitmap(bitmap);
            mCanvas.translate(-view.getScrollX(), -view.getScrollY());
            view.draw(mCanvas);
            mCanvas.setBitmap(null);
        }
        return bitmap;
    }

    static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        while(retryCount-- > 0) {
            try {
                return Bitmap.createBitmap(width, height, config);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                System.gc();
            }
        }
        return null;
    }

    static int nextInt(int a, int b){
        return Math.min(a,b) + random.nextInt(Math.abs(a - b));
    }

    static int nextInt(int a){
        return random.nextInt(a);
    }

    static float nextFloat(float a, float b){
        return Math.min(a,b) + random.nextFloat() * Math.abs(a - b);
    }

    static float nextFloat(float a){
        return random.nextFloat() * a;
    }

    static boolean nextBoolean(){
        return random.nextBoolean();
    }


    /**
     * 解决listview被嵌套,显示不全的问题
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    /**
     * 根据枚举的索引获取枚举
     * @param clazz
     * @param ordinal
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T enmuIndexOf(Class<T> clazz, int ordinal) {
        return (T)clazz.getEnumConstants()[ordinal];
    }

}
