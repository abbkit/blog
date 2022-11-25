package com.abbkit.project.blog.search;

import com.abbkit.lemon.client.model.HBaseBaseModel;
import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import com.abbkit.lemon.data.type.TypeDef;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table(name = "blog_keyword")
public class BlogKeyWord extends HBaseBaseModel {

    @Column(name = "cf:seq")
    private String seq;

    @Column(name = "cf:switch",type = TypeDef.BOOLEAN)
    private boolean switchKeyword=true;


}
