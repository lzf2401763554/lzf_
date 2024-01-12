package com.work;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 维修信息类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance implements Serializable {
    @ExcelProperty("维修ID")
    public  int maintenance_id;
    @ExcelProperty("维修名称")
    public String maintenance_name;
    @ExcelProperty("公路ID")
    public int road_id;
    @ExcelProperty("维修日期")
    public String maintenance_date;
    @ExcelProperty("维修描述")
    public String description;
    @ExcelProperty("维修费用")
    public String cost;
    @ExcelProperty("维修天数")
    public  String time;
    @ExcelProperty("维修备注")
    public String maintenance_remark;
    /**
     * 获取实体所有内容，用逗号隔开，主要用于字符流
     * @return 返回用逗号隔开的实体所有内容
     */
    @Override
    public String toString() {
        return
                maintenance_id +
                        "," + maintenance_name  +
                        "," + road_id  +
                        "," + maintenance_date  +
                        "," + description +
                        "," + cost  +
                        "," + time +
                        "," + maintenance_remark;
    }
}
