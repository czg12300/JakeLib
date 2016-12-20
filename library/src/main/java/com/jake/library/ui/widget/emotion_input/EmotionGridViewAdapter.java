
package com.jake.library.ui.widget.emotion_input;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jake.library.ui.adapter.BaseListAdapter;
import com.jake.library.ui.adapter.BaseListViewHolder;

import java.util.ArrayList;

public class EmotionGridViewAdapter extends BaseListAdapter<EmotionEntity, EmotionGridViewAdapter.Holder> {


    private ItemClickListener mItemClickListener;

    public EmotionGridViewAdapter(Context context, ArrayList<EmotionEntity> datas, ItemClickListener listener) {
        super(context);
        mList = datas;
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        EmotionEntity entity = getItem(position);
        holder.itemView.setTag(entity);
        if (entity != null) {
            holder.emoticon.setImageResource(entity.eid);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = getHolder();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.itemClicked((EmotionEntity) view.getTag());
            }
        });
        return holder;
    }

    @NonNull
    private Holder getHolder() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(35), dp(35));
        layout.addView(imageView, lp);
        return new Holder(layout, imageView);
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
    }

    protected static class Holder extends BaseListViewHolder {
        ImageView emoticon;

        public Holder(View itemView, ImageView imageView) {
            super(itemView);
            emoticon = imageView;
        }
    }

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void itemClicked(EmotionEntity entity);
    }
}
