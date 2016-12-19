
package com.jake.library.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述:所有fragment的基类,在onCreateView引入contentView，在onViewCreated中初始化view
 *
 * @author jakechen
 * @since 2016/8/9 10:06
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 初始化跳转数据
     *
     * @param bundle
     */
    protected void initVar(Bundle bundle) {

    }

    /**
     * 通过id找到view
     *
     * @param id
     * @return
     */
    protected View findViewById(int id) {
        return getView() != null ? getView().findViewById(id) : null;
    }

    /**
     * inflate布局
     *
     * @param layoutId
     * @return
     */
    protected View inflate(int layoutId) {
        return inflate(layoutId, null);
    }

    protected View inflate(int layoutId, ViewGroup viewGroup) {
        return getActivity() != null ? View.inflate(getActivity(), layoutId, viewGroup) : null;
    }
}
