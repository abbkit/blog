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
@Table( schema ="hot", name = "blog_node")
public class BlogNode extends KVBaseModel {

    @Column(name = "ip")
    private String ip;

    @Column(name = "accessTime",type = TypeString.DATE)
    private long accessTime;

    @Column(name = "browser")
    private String browser;

    @Column(name = "userAgent")
    private String userAgent;

    @Column(name = "holdTime",type = TypeString.INT)
    private int holdTime;

}
