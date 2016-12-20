
package com.jake.library.ui.widget.emotion_input;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 描述:表情处理类
 *
 * @author xiaxiaochuan
 * @since 2015/11/17 11:03
 */
public class EmotionHelper {

    public static void transform(TextView text, String origin) {
        if (text == null) {
            return;
        }
        if (origin == null) {
            origin = "";
        }
        Context context = text.getContext();
        SpannableString result = new SpannableString(origin);
        try {
            ArrayList<EmotionString> emotions = parseEmotion(origin);
            if (emotions != null && emotions.size() > 0) {
                for (EmotionString emo : emotions) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                            emo.emotinRid);
                    // 改变EditText中的图片大小，根据字体大小进行改变
                    float textSize = text.getTextSize();
                    textSize *= 1.2f;
                    bitmap = zoomBitmap(bitmap, (int) textSize, (int) textSize);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    result.setSpan(imageSpan, emo.textStartPos, emo.textEndPos,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            // 在个别手机上会报IndexOutOfBoundsException，原因可能是因为表情被换行符分隔
            text.setText(result);
        } catch (IndexOutOfBoundsException e) {
            text.setText(result.toString());
        } catch (OutOfMemoryError error) {
            text.setText(result.toString());
        }
    }

    public static SpannableString getEmotionString(Context context, TextPaint text, String origin) {
        if (origin == null) {
            origin = "";
        }
        SpannableString result = null;
        try {
            result = new SpannableString(origin);
            ArrayList<EmotionString> emotions = parseEmotion(origin);
            if (emotions != null && emotions.size() > 0) {
                for (EmotionString emo : emotions) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                            emo.emotinRid);
                    // 改变EditText中的图片大小，根据字体大小进行改变
                    float textSize = text.getTextSize();
                    textSize *= 1.2f;
                    bitmap = zoomBitmap(bitmap, (int) textSize, (int) textSize);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    result.setSpan(imageSpan, emo.textStartPos, emo.textEndPos,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (Exception e) {
            result = new SpannableString(origin);
        } catch (Error err) {
            result = new SpannableString(origin);
        }
        return result;
    }

    /**
     * @param text
     * @param origin
     * @return
     * @throws ArrayIndexOutOfBoundsException text显示带图片的SpannableString时，
     *                                        如果图片在换行处可能抛出ArrayIndexOutOfBoundsException异常 所以调用该方法时许捕捉该异常
     */
    public static SpannableString getEmotionString(TextView text, String origin)
            throws ArrayIndexOutOfBoundsException {
        if (origin == null) {
            origin = "";
        }
        if (text == null) {
            return new SpannableString(origin);
        }
        Context context = text.getContext();
        SpannableString result;
        try {
            result = new SpannableString(origin);
            ArrayList<EmotionString> emotions = parseEmotion(origin);
            if (emotions != null && emotions.size() > 0) {
                for (EmotionString emo : emotions) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                            emo.emotinRid);
                    // 改变EditText中的图片大小，根据字体大小进行改变
                    float textSize = text.getTextSize();
                    textSize *= 1.2f;
                    bitmap = zoomBitmap(bitmap, (int) textSize, (int) textSize);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    result.setSpan(imageSpan, emo.textStartPos, emo.textEndPos,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (Exception e) {
            result = new SpannableString(origin);
        } catch (Error err) {
            result = new SpannableString(origin);
        }
        return result;
    }

    public static SpannableString getEmotionString(TextView text, Spanned origin) {
        if (origin == null) {
            return null;
        }
        if (text == null) {
            return origin != null ? new SpannableString(origin) : new SpannableString("");
        }

        Context context = text.getContext();
        SpannableString result;
        try {
            result = new SpannableString(origin);
            ArrayList<EmotionString> emotions = parseEmotion(origin.toString());
            if (emotions != null && emotions.size() > 0) {
                for (EmotionString emo : emotions) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                            emo.emotinRid);
                    // 改变EditText中的图片大小，根据字体大小进行改变
                    float textSize = text.getTextSize();
                    textSize *= 1.2f;
                    bitmap = zoomBitmap(bitmap, (int) textSize, (int) textSize);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    result.setSpan(imageSpan, emo.textStartPos, emo.textEndPos,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (Exception e) {
            result = new SpannableString(origin);
        } catch (Error err) {
            result = new SpannableString(origin);
        }
        return result;
    }

    private static ArrayList<EmotionString> parseEmotion(String string) {
        if (!TextUtils.isEmpty(string)) {
            ArrayList<EmotionString> resultList = new ArrayList<EmotionString>();
            int start = 0;
            int end = 0;
            do {
                start = string.indexOf("[/", start);
                end = string.indexOf("]", start);
                if (start != -1 && end != -1 && end > start) {
                    EmotionString temp = new EmotionString();
                    temp.emotinText = string.substring(start + 2, end);
                    temp.textStartPos = start;
                    temp.textEndPos = end + 1;
                    temp.emotinRid = EmotionDatas.emotionResList[findIndex(temp.emotinText)];
                    resultList.add(temp);
                } else {
                    break;
                }
                start = end + 1;
            } while (true);
            if (resultList.size() > 0) {
                return resultList;
            }
        }
        return null;
    }

    private static int findIndex(String String) {
        int index = 0;
        int len=EmotionDatas.emotionTextList.length;
        for (int i = 0; i < len; i++) {
            if (String.equals(EmotionDatas.emotionTextList[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    private static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // bitmap.recycle();
        return newbmp;
    }

    private static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        Bitmap bitmap = null;
        if (source != null && !source.isRecycled()) {
            try {
                bitmap = Bitmap.createBitmap(source, 0, 0, width, height, m, true);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    bitmap = Bitmap.createBitmap(source, 0, 0, width, height, m, true);
                } catch (OutOfMemoryError e1) {
                }
            } catch (Exception e2) {
            }
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        }
        return bitmap;
    }

    public static class EmotionString {
        public String emotinText;

        public int textStartPos;

        public int textEndPos;

        public int emotinRid;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("emotinText: ").append(emotinText).append("|");
            sb.append("textStartPos: ").append(textStartPos).append("|");
            sb.append("textEndPos: ").append(textEndPos).append("|");
            sb.append("emotinRid: ").append(emotinRid);
            return sb.toString();
        }
    }
}
