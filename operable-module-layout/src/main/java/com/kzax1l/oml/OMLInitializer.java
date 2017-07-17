package com.kzax1l.oml;

import android.support.annotation.NonNull;

/**
 * Created by Zsago on 2017/7/17.
 *
 * @author Zsago
 */
public class OMLInitializer {
    private static OMLInitialization sInitialization;

    public static void initialize(@NonNull OMLInitialization initialization) {
        sInitialization = initialization;
    }

    @NonNull
    public static OMLInitialization initialization() {
        return sInitialization;
    }
}
