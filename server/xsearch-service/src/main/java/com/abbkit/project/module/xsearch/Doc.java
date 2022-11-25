package com.abbkit.project.module.xsearch;

import java.util.Map;

/**
 * Created by J on 2020/3/22.
 */
public interface Doc {

    /**
     * document score
     * @return
     */
    float score();

    /**
     * document uri, file path / http path
     * @return
     */
    String uri();

    /**
     * document content
     * @return
     */
    String content();

    /**
     * document meta data
     * @return
     */
    Map<String, Object> meta();


}
