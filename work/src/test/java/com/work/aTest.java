package com.work;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class aTest {

    @Test
    void main() throws SQLException{
        for (int i=1;i<=1000;i++)  {
            Random rand = new Random();
            try (Connection c = JDBCUtils.getConnection()) {
                String sql = "INSERT ignore INTO maintenance VALUES (?,?,?,?,?,?,?,?)";
                String[] roadMaintenanceItems = {
                        "修复破损的公路路面",
                        "清理路边的垃圾",
                        "更换损坏的路灯灯泡",
                        "修剪路边的树木",
                        "检查并修理损坏的交通监控设备",
                        "更换损坏的交通信号灯",
                        "修理破损的护栏",
                        "清理路边的积水",
                        "检查并更换损坏的路标",
                        "修理破损的公路路面涂料",
                        "检查并更换损坏的路面材料",
                        "清理路边的动物尸体",
                        "修复破损的公路景观设施",
                        "修理破损的公路安全设施",
                        "检查并更换损坏的井盖",
                        "清理路边的落叶",
                        "更换损坏的路边座椅",
                        "检查并修理损坏的路灯供电线路",
                        "修复破损的公路路面结构",
                        "检查并修理损坏的公路标志",
                        "修理破损的标线",
                        "更换损坏的路边垃圾桶",
                        "修复破损的排水系统",
                        "检查并更换损坏的交通监控设备",
                        "清理路面的油渍",
                        "修复破损的护栏",
                        "检查并更换损坏的路面材料",
                        "修复破损的路面涂料",
                        "检查并修理损坏的路灯供电线路",
                        "清理路边的垃圾堆放点"
                };
                PreparedStatement p = c.prepareStatement(sql);
                p.setInt(1, i);
                p.setInt(3, rand.nextInt(1, 301));
                p.setString(2, "维修记录" + i);
                p.setString(4, rand.nextInt(1949, 2024) + "/" + rand.nextInt(1, 13) + "/" + rand.nextInt(1, 29));
                p.setString(5, roadMaintenanceItems[rand.nextInt(roadMaintenanceItems.length)]);
                p.setString(6, rand.nextInt(100,1000000) + "元");
                p.setString(7, rand.nextInt(1, 101) + "天");
                if (rand.nextInt(0, 2) == 1)
                    p.setString(8, "已完成");
                else p.setString(8, "未完成");
                System.out.println(p.executeUpdate());

            }
        }
    }
}