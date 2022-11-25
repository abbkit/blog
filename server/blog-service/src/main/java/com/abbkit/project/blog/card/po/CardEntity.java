package com.abbkit.project.blog.card.po;

import com.abbkit.project.model.BaseEntity;
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
 * @since 2022-11-19
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_card")
public class CardEntity extends BaseEntity {

      /**
     * 标题
     */
      @TableField("title")
    private String title;

      /**
     * 短内容
     */
      @TableField("short_content")
    private String shortContent;

      /**
     * 链接地址
     */
      @TableField("link_html")
    private String linkHtml;

      /**
     * 发布时间
     */
      @TableField("publish_time")
    private LocalDateTime publishTime;


}
