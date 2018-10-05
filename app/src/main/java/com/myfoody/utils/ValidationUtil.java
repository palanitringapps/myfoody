package com.myfoody.utils;

import android.os.Build;

/**
 * Created by tringapps-admin on 3/3/17.
 */

public class ValidationUtil {

    private ValidationUtil() {
    }

    private static class SingletonHolder {
        private static final ValidationUtil instance = new ValidationUtil();
    }

    public static ValidationUtil getInstance() {
        return ValidationUtil.SingletonHolder.instance;
    }

    public boolean isKitkat() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }
}
