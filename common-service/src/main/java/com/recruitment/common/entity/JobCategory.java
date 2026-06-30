package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职位分类实体类
 */
@Data
@TableName("job_category")
public class JobCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String categoryName;

    private Long parentId;

    private Integer sortOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
