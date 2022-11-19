package com.abbkit.kernel.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.time.LocalDateTime;


/**
 *
 * @author J
 */
@Data
public abstract class BaseEntity implements Model{

	/**
	 * the primary key , uuid 
	 */
	@TableId(value = "id",type=IdType.ASSIGN_ID)
	private String id;
	
	/**
	 * create user id
	 */
	@TableField(value = "create_id",fill = FieldFill.INSERT
	,insertStrategy= FieldStrategy.NOT_EMPTY
	,updateStrategy = FieldStrategy.NOT_EMPTY )
	private String createId;
	
	/**
	 * update user id 
	 */
	@TableField(value = "update_id",fill = FieldFill.UPDATE
			,updateStrategy = FieldStrategy.NOT_EMPTY)
	private String updateId;
	
	/**
	 * create time
	 */
	@TableField(value = "create_time"
			,typeHandler = LocalDateTimeTypeHandler.class
			,fill = FieldFill.INSERT
			,insertStrategy= FieldStrategy.NOT_EMPTY
			,updateStrategy = FieldStrategy.NOT_EMPTY)
	private LocalDateTime createTime;
	
	/**
	 * update time
	 */
	@TableField(value = "update_time"
			,typeHandler = LocalDateTimeTypeHandler.class
	,fill = FieldFill.UPDATE
			,updateStrategy = FieldStrategy.NOT_EMPTY)
	private LocalDateTime updateTime;
	
	/**
	 * marks whether the record is deleted. Value is Y|N
	 */
	@TableField(value = "deleted",jdbcType= JdbcType.VARCHAR
	,insertStrategy= FieldStrategy.NOT_EMPTY
	,fill = FieldFill.INSERT_UPDATE)
	@TableLogic(value = "N",delval = "Y")
	private String deleted;
	
	/**
	 * the property can limit the async consumer effectively
	 */
	@TableField(value = "version",jdbcType= JdbcType.INTEGER)
	@Version
	private int version;

}
