package com.abbkit.module.xsearch.file;

import org.apache.lucene.document.Document;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by J on 2020/3/22.
 */
public interface FileIndex {

    boolean filter(Path file, BasicFileAttributes attrs);

    Document document(Path file, BasicFileAttributes attrs) throws Exception;

}
