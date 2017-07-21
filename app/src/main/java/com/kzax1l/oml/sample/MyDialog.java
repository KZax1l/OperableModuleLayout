package com.kzax1l.oml.sample;

import android.view.Gravity;

/**
 * Created by Zsago on 2017/7/21.
 *
 * @author Zsago
 */
public class MyDialog extends ContentViewDialogFragment {
    @Override
    protected int layoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    protected int gravity() {
        return Gravity.END | Gravity.BOTTOM;
    }

    @Override
    protected int width() {
        return 500;
    }

    @Override
    protected int height() {
        return 800;
    }
}
