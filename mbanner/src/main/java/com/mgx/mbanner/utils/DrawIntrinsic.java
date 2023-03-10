package com.mgx.mbanner.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;

/**
 * 绘制指示器
 */
public class DrawIntrinsic {

    public Drawable getDividerDrawable(int interval) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(Color.TRANSPARENT);
        drawable.setIntrinsicWidth(interval);
        return drawable;
    }
}
