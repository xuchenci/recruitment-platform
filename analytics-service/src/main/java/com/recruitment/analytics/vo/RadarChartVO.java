package com.recruitment.analytics.vo;

import lombok.Data;
import java.util.List;

/**
 * 雷达图 VO
 * ECharts 类型：radar
 */
@Data
public class RadarChartVO {
    private String title;
    
    /**
     * 雷达图维度（指示器）
     */
    private List<RadarIndicator> indicators;
    
    /**
     * 系列数据
     */
    private List<RadarSeriesData> series;
    
    /**
     * 雷达图维度指示器
     */
    @Data
    public static class RadarIndicator {
        private String name;  // 维度名称
        private Integer max;    // 最大值
        private Integer min = 0;  // 最小值（默认0）
        
        public RadarIndicator(String name, Integer max) {
            this.name = name;
            this.max = max;
        }
    }
    
    /**
     * 雷达图系列数据
     */
    @Data
    public static class RadarSeriesData {
        private String name;  // 系列名称
        private List<Integer> value;  // 数值数组
        private String areaStyle;  // 区域填充样式
    }
}
