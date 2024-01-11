package com.work;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Accidents implements Serializable {
    @ExcelProperty("事故ID")
    public int accident_id;
    @ExcelProperty("公路ID")
    public int road_id;
    @ExcelProperty("事故日期")
    public String accident_data;
    @ExcelProperty("事故描述")
    public String accident_description;
    @ExcelProperty("损伤程度")
    public String accident_situation;
    /**
     * 获取实体所有内容，用逗号隔开，主要用于字符流
     * @return 返回用逗号隔开的实体所有内容
     */
    @Override
    public String toString() {
        return
                accident_id +
                        "," + road_id  +
                        "," + accident_data  +
                        "," + accident_description  +

                        "," + accident_situation ;
    }
}
