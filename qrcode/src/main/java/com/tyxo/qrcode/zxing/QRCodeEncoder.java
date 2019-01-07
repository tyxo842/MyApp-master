package com.tyxo.qrcode.zxing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeEncoder {

    private QRCodeEncoder() {
    }

    /**
     * 创建黑色的二维码图片
     *
     * @param content
     * @param size     图片宽高，单位为px
     * @param delegate 创建二维码图片的代理
     */
    public static void encodeQRCode(String content, int size, Delegate delegate) {
        QRCodeEncoder.encodeQRCode(content, size, Color.BLACK, delegate);
    }

    /**
     * 创建黑色的一维码图片
     *
     * @param content
     * @param delegate 创建一维码图片的代理
     */
    public static void encodeOneCode(final String content, final Delegate delegate) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 500, 200);
                    int width = matrix.getWidth();
                    int height = matrix.getHeight();
                    int[] pixels = new int[width * height];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if (matrix.get(x, y)) {
                                pixels[y * width + x] = 0xff000000;
                            }
                        }
                    }

                    Bitmap bitmap = Bitmap.createBitmap(width, height,
                            Bitmap.Config.ARGB_8888);
                    // 通过像素数组生成bitmap,具体参考api
                    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                    return bitmap;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (delegate != null) {
                    if (bitmap != null) {
                        delegate.onEncodeQRCodeSuccess(bitmap);
                    } else {
                        delegate.onEncodeQRCodeFailure();
                    }
                }
            }
        }.execute();
    }

    /**
     * 创建指定颜色的二维码图片
     *
     * @param content
     * @param size     图片宽高，单位为px
     * @param color    二维码图片的颜色
     * @param delegate 创建二维码图片的代理
     */
    public static void encodeQRCode(final String content, final int size, final int color, final Delegate delegate) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size);
                    int[] pixels = new int[size * size];
                    for (int y = 0; y < size; y++) {
                        for (int x = 0; x < size; x++) {
                            if (matrix.get(x, y)) {
                                pixels[y * size + x] = color;
                            }
                        }
                    }
                    Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                    bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
                    return bitmap;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (delegate != null) {
                    if (bitmap != null) {
                        delegate.onEncodeQRCodeSuccess(bitmap);
                    } else {
                        delegate.onEncodeQRCodeFailure();
                    }
                }
            }
        }.execute();
    }

    public interface Delegate {
        /**
         * 创建二维码图片成功
         *
         * @param bitmap
         */
        void onEncodeQRCodeSuccess(Bitmap bitmap);

        /**
         * 创建二维码图片失败
         */
        void onEncodeQRCodeFailure();
    }
}