package com.abbkit.module.xsearch.file;

import com.abbkit.module.xsearch.BaseDoc;

import java.util.Map;

/**
 * Created by J on 2020/3/22.
 */
public class FileDoc extends BaseDoc {

    private String uri;

    private String content;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String uri() {
        return getUri();
    }

    @Override
    public String content() {
        return getContent();
    }

    @Override
    public Map<String, Object> meta() {
        return null;
    }
}
