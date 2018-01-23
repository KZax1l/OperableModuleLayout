package com.andova.oml.sample;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Zsago on 2017/7/21.
 *
 * @author Zsago
 */
public abstract class ContentViewDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(layoutResource());
            Window window = dialog.getWindow();
            assert window != null;
            window.setGravity(gravity());
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = width();
            lp.height = height();
            window.setAttributes(lp);
            return dialog;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @LayoutRes
    protected abstract int layoutResource();

    protected abstract int gravity();

    protected abstract int width();

    protected abstract int height();
}
