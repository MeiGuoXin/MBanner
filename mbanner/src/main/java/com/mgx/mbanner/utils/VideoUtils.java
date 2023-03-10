package com.mgx.mbanner.utils;

import android.webkit.MimeTypeMap;

public class VideoUtils {

    /**
     * 支持视频的格式
     */
    public static boolean videoType(String url) {
        if (MimeTypeMap.getFileExtensionFromUrl(url).equals("mp4")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("m3u8")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("flv")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("avi")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("file")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("mkv")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("https")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("http")
        ) {
            return true;
        } else {
            return false;
        }
    }
}
