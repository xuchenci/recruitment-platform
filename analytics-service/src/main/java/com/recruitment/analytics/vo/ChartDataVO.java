package com.recruitment.analytics.vo;

import lombok.Data;
import java.util.List;

/**
 * 通用图表数据 VO
 * 适用于：pie（饼图）、bar（柱状图）、line（折线图）等
 */
@Data
public class ChartDataVO {
    
    /**
     * 图表标题
     */
    private String title;
    
    /**
     * 图表类型（用于前端判断）
     * pie, bar, line, radar, funnel, gauge
     */
    private String chartType;
    
    /**
     * X轴数据（适用于 bar, line）
     */
    private List<String> xAxisData;
    
    /**
     * 系列数据
     * 对于饼图：[{name: 'xxx', value: 100}]
     * 对于柱状图/折线图：[{name: 'xxx', data: [1,2,3,4]}]
     */
    private List<SeriesData> series;
    
    /**
     * 图例数据
     */
    private List<String> legendData;
    
    /**
     * 系列数据内部类
     */
    @Data
    public static class SeriesData {
        private String name;
        private List<Object> data;  // 可以是数值或 {name, value} 对象
        private String type;  // 可选，指定系列类型
    }
}
