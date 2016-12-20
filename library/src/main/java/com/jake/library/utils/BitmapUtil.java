
package com.jake.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Base64;

import com.jake.library.global.LibraryController;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2015/8/16.
 */
public final class BitmapUtil {
    public static Bitmap decodeResource(Context context, int drawableId) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeResource(int drawableId) {

        return decodeResource(LibraryController.getInstance().getContext(), drawableId);
    }

    public static Bitmap decodeResource(Context context, int drawableId, int width, int height) {
        return ThumbnailUtils.extractThumbnail(decodeResource(context, drawableId), width, height);
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                               int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h
                / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bmRes, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bmRes.getWidth();
        float height = bmRes.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bmRes, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bmRes, double newWidth) {
        float scaleWidth = ((float) newWidth) / bmRes.getWidth();
        double newHeight = bmRes.getHeight() * scaleWidth;
        return scaleBitmap(bmRes, newWidth, newHeight);
    }

    public static Bitmap scaleBitmapH(Bitmap bmRes, double newHeight) {
        float scaleWidth = ((float) newHeight) / bmRes.getHeight();
        double newWidth = bmRes.getWidth() * scaleWidth;
        return scaleBitmap(bmRes, newWidth, newHeight);
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
