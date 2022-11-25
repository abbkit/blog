package com.baomidou.mybatisplus.samples.generator.blog.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author J
 * @since 2022-11-20
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("lemon_meta_table")
public class LemonMetaTableEntity {

    @TableField("`schema`")
    private String schema;

    @TableField("tableName")
    private String tableName;

    @TableField("`owner`")
    private String owner;

    @TableField("`desc`")
    private String desc;

    @TableField("sync")
    private String sync;

    @TableField("latestSyncTime")
    private LocalDateTime latestSyncTime;

    @TableField("limitCount")
    private Integer limitCount;

    @TableField("disabled")
    private String disabled;

    @TableField("`index`")
    private String index;

    @TableField("latestIndexTime")
    private LocalDateTime latestIndexTime;

    @TableField("count")
    private Long count;

    @TableField("sync_count")
    private Long syncCount;

    @TableField("ttl")
    private Integer ttl;

    @TableField("properties")
    private String properties;

    @TableField("`engine`")
    private String engine;

    @TableField("kv")
    private String kv;


}
