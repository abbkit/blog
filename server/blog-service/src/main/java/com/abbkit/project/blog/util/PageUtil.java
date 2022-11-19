package com.abbkit.project.blog.util;

import com.abbkit.kernel.model.DefaultPageImpl;
import com.abbkit.kernel.model.Page;
import com.abbkit.kernel.model.Pageable;

abstract public class PageUtil {

    static public <T> Page<T> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page, Pageable pageable){
        DefaultPageImpl<T> defaultPage=new DefaultPageImpl<>();
        defaultPage.setTotalPageNumber((int) page.getPages());
        defaultPage.setTotalRecordNumber(page.getTotal());
        defaultPage.setData(page.getRecords());
        defaultPage.setPageable(pageable);
        return defaultPage;
    }

}
