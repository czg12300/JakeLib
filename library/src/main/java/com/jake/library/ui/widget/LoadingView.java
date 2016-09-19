
package com.jake.library.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class LoadingView extends ImageView {

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnimationDrawable();
        } else {
            stopAnimationDrawable();
        }
    }

    public void startAnimationDrawable() {
        if (getWidth() > 0 && getVisibility() == View.VISIBLE) {
            Drawable d = getDrawable();
            if (d != null && d instanceof AnimationDrawable) {
                if (!((AnimationDrawable) d).isRunning()) {
                    ((AnimationDrawable) d).start();
                }
            }
        }
    }

    public void stopAnimationDrawable() {
        Drawable d = getDrawable();
        if (d != null && d instanceof AnimationDrawable) {
            if (((AnimationDrawable) d).isRunning()) {
                ((AnimationDrawable) d).stop();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimationDrawable();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimationDrawable();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0)
            startAnimationDrawable();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            stopAnimationDrawable();
            return;
        }
        startAnimationDrawable();
    }
}
