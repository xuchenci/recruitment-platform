package com.recruitment.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recruitment.common.entity.ImportHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 导入历史记录Mapper
 */
@Mapper
public interface ImportHistoryMapper extends BaseMapper<ImportHistory> {

    /**
     * 根据操作人ID查询导入历史
     */
    List<ImportHistory> selectByOperatorId(Long operatorId);

    /**
     * 根据导入类型查询历史
     */
    List<ImportHistory> selectByImportType(String importType);
}
