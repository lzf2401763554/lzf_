package com.work;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class aest {

    @Test
    void main()throws SQLException {
        for (int i=1;i<=1000;i++)  {
            Random rand = new Random();
            try (Connection c = JDBCUtils.getConnection()) {
                String sql = "INSERT ignore INTO accidents VALUES (?,?,?,?,?)";
          String[]  a = {
                "车辆追尾，导致交通拥堵。",
                "行驶过程中发生侧翻事故，造成交通中断。",
                "驾驶员分神导致交叉路口碰撞。",
                "雨雪天气造成路面湿滑，发生多起刮擦事故。",
                "超速行驶导致失控，撞上护栏。",
                "行人横穿马路未注意交通信号灯，被车辆撞伤。",
                "交叉路口闯红灯，引发多车相撞事故。",
                "违规变道导致两车相撞。",
                "驾驶员疲劳驾驶，发生单车事故。",
                "行驶中发生爆胎，造成紧急刹车。",
                "夜间能见度差，导致多车追尾。",
                "交叉路口行人与车辆未能协调，发生事故。",
                "酒驾导致驾驶失控，发生严重碰撞。",
                "交通信号灯故障，引发交叉路口混乱。",
                "行驶中发生机械故障，紧急停车。",
                "闯入逆行车道，引发对向碰撞。",
                "车辆突发故障，堵塞道路。",
                "违规占用应急车道，导致拥堵。",
                "雾霾天气导致能见度极低，发生多起相撞事故。",
                "车辆疾驰超速，引发交叉路口事故。",
                "违规使用手机导致注意力分散，发生刮擦。",
                "闯入禁行区域，被交警拦停。",
                "驾驶员逆行进入高速，引发追尾事故。",
                "行驶中车辆起火，紧急停车。",
                "超载货车导致车辆失衡，翻车事故。",
                "行驶中发生轻微刮擦，无人员伤亡。",
                "交叉路口未礼让行人，发生事故。",
                "夜晚驾驶未打开车灯，造成交通危险。",
                "违规倒车导致与后方车辆相撞。",
                "交叉路口未遵守交通规则，发生拥堵。"
        };
                PreparedStatement p = c.prepareStatement(sql);
                p.setInt(1, i);
                p.setInt(2, rand.nextInt(1, 301));
                p.setString(3, rand.nextInt(1949, 2024) + "/" + rand.nextInt(1, 13) + "/" + rand.nextInt(1, 29));
                p.setString(4, a[rand.nextInt(a.length)]);
                int j =rand.nextInt(0,3);
                if (j==0)
                p.setString(5, "低");
                else if (j==1)p.setString(5, "中");
                else p.setString(5, "高");
                System.out.println(p.executeUpdate());

            }
        }

    }
}