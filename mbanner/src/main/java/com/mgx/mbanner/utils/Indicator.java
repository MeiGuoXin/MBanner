package com.mgx.mbanner.utils;

public class Indicator {
    /**
     * 指示器方向
     */
    public enum IndicatorGravity {
        LEFT, RIGHT, CENTER
    }

    /**
     * 指示器类型
     */
    public enum IndicatorStyle {
        //没有指示器
        NONE,
        //数字指示器
        NUMBER,
        //普通指示器
        ORDINARY
    }
}
