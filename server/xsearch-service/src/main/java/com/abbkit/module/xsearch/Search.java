package com.abbkit.module.xsearch;

import java.util.List;

/**
 * Created by J on 2020/3/22.
 */
public interface Search<T extends Doc> {

    List<T> search(String field, String queryString, int hitsPerPage) throws Exception;

    List<T> search(String field, String queryString, int hitsPerPage, boolean highlight) throws Exception;
}
