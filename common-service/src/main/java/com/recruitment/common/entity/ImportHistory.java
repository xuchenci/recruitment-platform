package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导入历史记录实体
 */
@Data
@TableName("import_history")
public class ImportHistory extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 总条数
     */
    private Integer totalCount;

    /**
     * 成功条数
     */
    private Integer successCount;

    /**
     * 失败条数
     */
    private Integer failCount;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operator;

    /**
     * 导入类型（student-学生导入）
     */
    private String importType;

    /**
     * 错误详情（JSON格式）
     */
    private String errorDetails;
}
