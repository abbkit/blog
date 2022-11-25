package com.abbkit.project.module.xsearch.file;

import java.io.InputStream;

/**
 * Created by J on 2020/3/22.
 */
public interface FileContentExtract {

    String content(InputStream inputStream) throws Exception;

}
