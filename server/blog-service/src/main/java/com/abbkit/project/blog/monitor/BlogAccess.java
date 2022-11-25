package com.abbkit.project.blog.monitor;

import com.abbkit.lemon.client.model.HBaseBaseModel;
import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import com.abbkit.lemon.data.type.TypeDef;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table( name = "blog_access")
public class BlogAccess extends HBaseBaseModel {

    @Column(name = "cf:seq")
    private String seq;

    @Column(name = "cf:track")
    private String track;

    @Column(name = "cf:accessTime",type = TypeDef.DATE)
    private long accessTime;

    @Column(name = "cf:holdTime",type = TypeDef.INT)
    private int holdTime;

}
