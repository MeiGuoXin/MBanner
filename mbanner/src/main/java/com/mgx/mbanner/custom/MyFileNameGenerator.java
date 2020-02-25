package com.mgx.mbanner.custom;

import com.danikula.videocache.file.FileNameGenerator;

public class MyFileNameGenerator implements FileNameGenerator {
    @Override
    public String generate(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1, url.length());
        return name;
    }
}
