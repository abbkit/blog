package com.abbkit.project.blog.monitor;

import com.abbkit.lemon.client.model.KVBaseModel;
import com.abbkit.lemon.data.ProtocolCons;
import com.abbkit.lemon.data.annotation.Column;
import com.abbkit.lemon.data.annotation.Table;
import com.abbkit.lemon.data.type.TypeDef;
import lombok.Data;

/**
 * Created by J on 2020/3/25.
 */
@Data
@Table( schema ="hot", name = "blog_node" ,engine = ProtocolCons.ENGINE_MYSQL)
public class BlogNode extends KVBaseModel {

    @Column(name = "seq",primary = true)
    private long seq;

    @Column(name = "nodeSign")
    private String nodeSign;

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
