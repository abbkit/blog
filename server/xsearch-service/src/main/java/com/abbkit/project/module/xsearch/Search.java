package com.abbkit.project.module.xsearch;

import org.apache.lucene.index.IndexableField;

import java.util.List;

/**
 * Created by J on 2020/3/22.
 */
public interface Search<T extends Doc> {

    /**
     * 检索文件, 不需要高亮显示
     * {@link #search(String, String, int,boolean)}
     * @param field 检索的field，field为建立索引时候定义的field {#{@link IndexableField#name()}}
     * @param queryString 检索的值
     * @param hitsPerPage hit值
     * @return
     * @throws Exception
     */
    List<T> search(String field, String queryString, int hitsPerPage) throws Exception;

    /**
     * 检索文件
     * @param field 检索的field，field为建立索引时候定义的field {#{@link IndexableField#name()}}
     * @param queryString 检索的值
     * @param hitsPerPage hit值
     * @param highlight 是否高亮
     * @return
     * @throws Exception
     */
    List<T> search(String field, String queryString, int hitsPerPage, boolean highlight) throws Exception;
}
