
package com.jake.jakelib.demo;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.Log;

import com.jake.jakelib.R;
import com.jake.library.ui.activity.BaseActivity;

/**
 * 描述:
 *
 * @author jakechen
 * @since 2016/8/23 11:13
 */
public class CoordinatorLayoutActivity extends BaseActivity {
    private AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        appBarLayout= (AppBarLayout) findViewById(R.id.al_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("tag", appBarLayout.getTop() + " " + appBarLayout.getBottom() + "  " + verticalOffset);
            }
        });
    }
}
