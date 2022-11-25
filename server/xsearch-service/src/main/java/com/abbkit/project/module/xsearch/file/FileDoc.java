package com.abbkit.project.module.xsearch.file;

import com.abbkit.project.module.xsearch.BaseDoc;
import lombok.Data;

import java.util.Map;

/**
 * Created by J on 2020/3/22.
 */
@Data
public class FileDoc extends BaseDoc {

    private String uri;

    private String content;

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
