package com.abbkit.module.xsearch;

import java.util.Map;

/**
 * Created by J on 2020/3/22.
 */
public interface Doc {

    float score();

    String uri();

    String content();

    Map<String, Object> meta();


}
