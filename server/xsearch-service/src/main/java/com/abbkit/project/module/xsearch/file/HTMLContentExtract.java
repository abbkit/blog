package com.abbkit.project.module.xsearch.file;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;

/**
 * Created by J on 2020/3/22.
 */
public class HTMLContentExtract implements FileContentExtract {

    @Override
    public String content(InputStream inputStream) throws Exception {
        Document doc = Jsoup.parse(inputStream, "UTF-8", "");
        return doc.text();
    }


}
