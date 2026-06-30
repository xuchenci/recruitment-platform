package com.recruitment.analytics.vo;

import lombok.Data;
import java.util.List;

/**
 * 漏斗图 VO
 * ECharts 类型：funnel
 */
@Data
public class FunnelChartVO {
    private String title;
    private List<FunnelData> data;
    private String sort = "descending";  // 排序方式
    private Integer gap = 2;  // 数据图形间距
    
    @Data
    public static class FunnelData {
        private String name;
        private Object value;  // 可以是数值或百分比
        
        public FunnelData() {
        }
        
        public FunnelData(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
