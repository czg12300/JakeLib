
package com.jake.library.ui.widget.emotion_input;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;


import com.jake.library.R;

import java.util.ArrayList;

public class EmotionViewPageAdapter extends PagerAdapter implements EmotionGridViewAdapter.ItemClickListener {


    private Context mContext;

    private ArrayList<View> emoticonViewList;

    private EditText mChatEditText;

    public EmotionViewPageAdapter(EditText edittext) {
        mChatEditText = edittext;
        mContext = edittext.getContext();
        emoticonViewList = new ArrayList<>(getCount());
        for (int i = 0; i < getCount(); i++) {
            GridView emotionGridView = createGridView();
            ArrayList<EmotionEntity> subEmList = new ArrayList<>();
            for (int j = 0; j < 27; j++) {
                int index = i * 27 + j;
                if (index >= EmotionDatas.emotionResList.length) {
                    break;
                }
                subEmList.add(new EmotionEntity(EmotionDatas.emotionResList[index], EmotionDatas.emotionTextList[index]));
            }
            subEmList.add(new EmotionEntity(R.drawable.ic_emotion_delete, "删除"));
            emotionGridView.setAdapter(new EmotionGridViewAdapter(mContext, subEmList, this));
            emoticonViewList.add(emotionGridView);
        }
    }

    private GridView createGridView() {
        GridView gridView = new GridView(mContext);
        int padding = dp(10);
        gridView.setPadding(padding, padding, padding, padding);
        gridView.setNumColumns(7);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setHorizontalSpacing(dp(3));
        gridView.setVerticalSpacing(dp(7));
        return gridView;
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
    }

    @Override
    public int getCount() {
        int emoticon = EmotionDatas.emotionResList.length;
        int pagecount = emoticon / 27;
        if (emoticon % 27 == 0) {
            return pagecount;
        } else {
            return pagecount + 1;
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = emoticonViewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(emoticonViewList.get(position));
    }

    @Override
    public void itemClicked(EmotionEntity item) {
        if (item == null) {
            return;
        }
        int rid = item.eid;
        int select = mChatEditText.getSelectionStart();
        if (rid == R.drawable.ic_emotion_delete) {
            String spanString = mChatEditText.getEditableText().toString().substring(0, select);
            int start = spanString.lastIndexOf("[");
            int last = spanString.lastIndexOf("]");
            if (last == select - 1) {
                if (start != last) {
                    mChatEditText.getEditableText().delete(start, last + 1);
                    mChatEditText.setSelection(start);
                }
            } else {
                mChatEditText.getEditableText().delete(select - 1, select);
                mChatEditText.setSelection(select - 1);
            }
        } else {
            String spanString = "[/" + item.name + "]";
            mChatEditText.getEditableText().insert(select,
                    EmotionHelper.getEmotionString(mChatEditText, spanString));
            mChatEditText.setSelection(mChatEditText.getText().length());
        }
    }
}
