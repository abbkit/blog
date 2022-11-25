package com.baomidou.mybatisplus.samples.generator.blog.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
  @TableName("lemon_meta_column")
public class LemonMetaColumnEntity {

    @TableField("columnName")
    private String columnName;

    @TableField("`type`")
    private String type;

    @TableField("`schema`")
    private String schema;

    @TableField("tableName")
    private String tableName;

    @TableField("`desc`")
    private String desc;

    @TableField("length")
    private Integer length;


}
