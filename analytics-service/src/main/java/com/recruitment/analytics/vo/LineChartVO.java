package com.recruitment.analytics.vo;

import lombok.Data;
import java.util.List;

/**
 * 折线图 VO
 */
@Data
public class LineChartVO {
    private String title;
    private List<String> xAxisData;  // X轴数据（时间）
    private List<LineSeries> series;  // 系列数据
    
    @Data
    public static class LineSeries {
        private String name;  // 系列名称
        private List<Object> data;  // 数据
        private String type = "line";  // 图表类型
        private Boolean smooth = true;  // 平滑曲线
        private String areaStyle;  // 区域填充样式
    }
}
