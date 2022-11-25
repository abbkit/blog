package com.abbkit.project.blog.search;

import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import com.abbkit.lemon.data.type.TypeDef;
import com.abbkit.lemon.server.model.KVBaseModel;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table(name = "blog_keyword")
public class BlogKeyWord extends KVBaseModel {

    @Column(name = "seq")
    private String seq;

    @Column(name = "switch",type = TypeDef.BOOLEAN)
    private boolean switchKeyword=true;


}
