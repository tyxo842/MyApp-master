package tyxo.mobilesafe.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by LY on 2016/7/8 15: 38.
 * Mail      1577441454@qq.com
 * Describe : 这是一个将网络图片转换为圆形的方法
 */
public class GlideCircleTransform extends BitmapTransformation {

    public GlideCircleTransform(Context context) {
        super(context);
    }

    public GlideCircleTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool,toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private Bitmap circleCrop(BitmapPool pool , Bitmap toTransform){
        if (toTransform == null) {
            return null;
        } else { }
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth()-size)/2;
        int y = (toTransform.getHeight() - size)/2;

        Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        } else { }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size/2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }
}

