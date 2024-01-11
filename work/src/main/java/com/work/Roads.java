package com.work;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roads implements Serializable {
    @ExcelProperty("公路ID")
   public int road_id;
    @ExcelProperty("公路名称")
   public String road_name;
    @ExcelProperty("公路位置")
    public String location;
    @ExcelProperty("起点")
    public String starting_point;
    @ExcelProperty("终点")
    public String terminal;
    @ExcelProperty("长度")
    public String length;
    @ExcelProperty("出险情况")
    public String accident_situation;
    @ExcelProperty("公路等级")
    public String road_grade;

    /**
     * 获取实体所有内容，用逗号隔开，主要用于字符流
     * @return 返回用逗号隔开的实体所有内容
     */
    @Override
    public String toString() {
        return
                 road_id +
                "," + road_name  +
                "," + location  +
                "," + starting_point  +
                "," + terminal +
                "," + length  +
                "," + accident_situation +
                "," + road_grade;
    }


}
