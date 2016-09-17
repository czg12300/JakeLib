package com.jake.library.ui;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 描述：fragment基类
 *
 * @author jakechen
 * @since 2016/9/17 12:47
 */

public class BaseFragment extends Fragment {


    protected View findViewById(int id) {
        return getView() != null ? getView().findViewById(id) : null;
    }
}
