package com.recruitment.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导入错误详情DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorDTO {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 字段名
     */
    private String field;

    /**
     * 错误原因
     */
    private String reason;
}
