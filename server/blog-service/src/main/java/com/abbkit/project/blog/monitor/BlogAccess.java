package com.abbkit.project.blog.monitor;

import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import com.abbkit.lemon.data.type.TypeDef;
import com.abbkit.lemon.server.model.KVBaseModel;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table( name = "blog_access")
public class BlogAccess extends KVBaseModel {

    @Column(name = "seq")
    private String seq;

    @Column(name = "track")
    private String track;

    @Column(name = "accessTime",type = TypeDef.DATE)
    private long accessTime;

    @Column(name = "holdTime",type = TypeDef.INT)
    private int holdTime;

}
