package com.abbkit.project.blog.search;

import com.abbkit.lemon.client.kv.KVBaseModel;
import com.abbkit.lemon.data.TypeString;
import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table(name = "blog_keyword")
public class BlogKeyWord extends KVBaseModel {

    @Column(name = "switch",type = TypeString.BOOLEAN)
    private boolean switchKeyword=true;


}
