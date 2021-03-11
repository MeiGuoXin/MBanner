package com.mgx.mbanner.utils;

import android.content.Context;
import android.util.TypedValue;

public class PixelConversion {

    public int sp2px(int sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
