package com.abbkit.project.blog.collect;

import com.abbkit.lemon.client.kv.KVBaseModel;
import com.abbkit.lemon.data.TypeString;
import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table( name = "blog_access")
public class BlogAccess extends KVBaseModel {

    @Column(name = "track")
    private String track;

    @Column(name = "accessTime",type = TypeString.DATE)
    private long accessTime;

    @Column(name = "holdTime",type = TypeString.INT)
    private int holdTime;

}
