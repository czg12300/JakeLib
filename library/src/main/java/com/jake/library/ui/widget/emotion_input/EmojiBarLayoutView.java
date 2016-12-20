package com.jake.library.ui.widget.emotion_input;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jake.library.R;
import com.jake.library.utils.BaseUIUtils;
import com.jake.library.utils.ToastUtil;
import com.jake.library.ui.widget.ResizeLayoutView;


/**
 * 描述：拥有表情输入的layout
 *
 * @author jakechen
 * @since 2016/12/7 16:55
 */

public class EmojiBarLayoutView extends ResizeLayoutView {
    private static int MAX_TEXT_COUNT = 140;
    private FrameLayout mContentView;
    private TextView mTvTextCount;
    private View mVEmoji;
    private ViewPager mVpEmoji;
    private ImageView mIvEmojiToggle;
    private EditText mEvContent;

    public EmojiBarLayoutView(Context context) {
        this(context, null);
    }

    public EmojiBarLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContentView = new FrameLayout(context);
        addView(mContentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        addView(View.inflate(context, R.layout.layout_emoji_bar, null), lp);
        initView();
    }


    private void initView() {
        mVEmoji = findViewById(R.id.ll_emoji);
        mTvTextCount = (TextView) findViewById(R.id.tv_text_count);
        mVpEmoji = (ViewPager) findViewById(R.id.vp_emoji);
        mIvEmojiToggle = (ImageView) findViewById(R.id.iv_emoji_toggle);
        mVEmoji.setVisibility(View.GONE);
        mVpEmoji.setVisibility(View.GONE);
        mIvEmojiToggle.setSelected(false);
        mIvEmojiToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEmoji();
            }
        });
        setOnResizeListener(new OnResizeListener() {
            @Override
            public void onKeyboardHide() {
                if (!mIvEmojiToggle.isSelected()) {
                    mVEmoji.setVisibility(View.GONE);
                }
            }

            @Override
            public void onKeyboardShow(int height) {
                mIvEmojiToggle.setSelected(false);
                mIvEmojiToggle.setImageResource(R.drawable.img_toggle_emoji);
                mVpEmoji.setVisibility(View.GONE);
                mVEmoji.setVisibility(View.VISIBLE);
            }
        });
    }

    private void toggleEmoji() {
        if (mIvEmojiToggle.isSelected()) {
            BaseUIUtils.showSoftInput(getContext());
        } else {
            BaseUIUtils.hideSoftInput(getContext());
            mIvEmojiToggle.setSelected(true);
            mIvEmojiToggle.setImageResource(R.drawable.img_toggle_text);
            mVpEmoji.setVisibility(View.VISIBLE);
            mVEmoji.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 设置内容的view
     *
     * @param layoutId
     */
    public void setContentView(int layoutId) {
        setContentView(View.inflate(getContext(), layoutId, null));
    }

    private void setContentView(View view) {
        if (mContentView != null) {
            if (mContentView.getChildCount() > 0) {
                mContentView.removeAllViews();
            }
            mContentView.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置输入框
     *
     * @param editText
     */
    public void setEditText(@NonNull EditText editText) {
        mEvContent = editText;
        EmotionViewPageAdapter viewPageAdapter = new EmotionViewPageAdapter(mEvContent);
        mVpEmoji.setAdapter(viewPageAdapter);
        mVpEmoji.setOffscreenPageLimit(viewPageAdapter.getCount() - 1);
    }

    /**
     * 设置输入框可输入的最大字符数（可选配置）
     *
     * @param length
     */
    public void setEditTextMaxLength(int length, final boolean isCountdown) {
        setEditTextMaxLength(length, isCountdown, false);
    }

    public void setEditTextMaxLength(int length, final boolean isCountdown, final boolean isShowToast) {
        MAX_TEXT_COUNT = length;
        mEvContent.addTextChangedListener(new TextWatcher() {
            private CharSequence lastText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = 0;
                if (s != null && s.length() > 0) {
                    String temp = s.toString();
                    temp = temp.replaceAll("\\[\\/([\u4E00-\u9FA5]*)\\]", "#");
                    len = temp.length();
                }
                if (len > MAX_TEXT_COUNT) {
                    mEvContent.setText(lastText);
                    mEvContent.setSelection(lastText.length());
                    if (isShowToast) {
                        ToastUtil.show("最多能输入" + MAX_TEXT_COUNT + "个字符");
                    }
                } else {
                    showTextCount(isCountdown ? MAX_TEXT_COUNT - len : len);
                    lastText = new SpannableString(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 显示字数
     *
     * @param count
     */
    public void showTextCount(int count) {
        mTvTextCount.setText("" + count);
    }

    /**
     * 判断表情输入框是否在显示
     *
     * @return
     */
    public boolean isEmojiBarShow() {
        return mVEmoji.getVisibility() == VISIBLE;
    }

    /**
     * 隐藏表情框
     */
    public void hideEmojiBar() {
        mVEmoji.setVisibility(GONE);
    }
}