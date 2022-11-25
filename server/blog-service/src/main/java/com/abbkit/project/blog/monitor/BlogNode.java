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
@Table( schema ="hot", name = "blog_node")
public class BlogNode extends KVBaseModel {

    @Column(name = "seq")
    private String seq;

    @Column(name = "ip")
    private String ip;

    @Column(name = "accessTime",type = TypeDef.DATE)
    private long accessTime;

    @Column(name = "browser")
    private String browser;

    @Column(name = "userAgent")
    private String userAgent;

    @Column(name = "holdTime",type = TypeDef.INT)
    private int holdTime;

}
