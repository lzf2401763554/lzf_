package com.work;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class aTes {

    @Test
    void main() throws SQLException {
        Random rand = new Random();
        try (Connection c = JDBCUtils.getConnection()) {
            for (int i = 1; i <= 1000; i++) {
                String[] b = {"路", "大道", "铁路","公路","高铁","北路","南路","东路","西路"};
                String[] e = {"张家界", "郴州", "北京", "深圳", "天津", "吉首", "黑龙江", "长沙", "衡阳","娄底","重庆","张家界","武汉","永州","株洲","张家界"};
                String[] f = {"国道", "省道", "县道", "乡道"};

                String g=e[rand.nextInt(e.length)];
                String fj=e[rand.nextInt(e.length)];
                Roads t = new Roads(i,String.valueOf(g.charAt(0))+String.valueOf(fj.charAt(0))+b[rand.nextInt(b.length)] + rand.nextInt(10)+"号", g,g, fj, rand.nextInt(200)+"公里", "无事故发生",f[rand.nextInt(f.length)]);
                String sql = "INSERT ignore INTO roads VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement p = c.prepareStatement(sql);
                p.setInt(1, t.getRoad_id());
                p.setString(2, t.getRoad_name());
                p.setString(3, t.getLocation());
                p.setString(4, t.getStarting_point());
                p.setString(5, t.getTerminal());
                p.setString(6, t.getLength());
                p.setString(7, "无事故发生");
                p.setString(8, t.getRoad_grade());
                p.executeUpdate();
                //
                String name[]={"刘","上官","成","陈","李","唐","何","蒋","毛","邓","欧阳","颜","彭","王","孙"};
                String it[]={"柱","锋","龙","领","城","靖","泽","楷","皇","星","海","哲","之","晓","宇","航","嘉","岳","鹏","美","鑫"};
                String n[]={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","s","y","z","x","1","2","3","4","5","6","7","8","9"};
                sql = "INSERT ignore INTO administrator VALUES (?,?,?,?,?,?,?,?)";
                Administrator administrator = new Administrator(i,name[rand.nextInt(name.length)]+it[rand.nextInt(it.length)]+it[rand.nextInt(it.length)], rand.nextInt(18,66),"男",rand.nextInt(1000000000,1999999999)+ rand.nextInt(10)+ "",e[rand.nextInt(e.length)],
                        n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]
                        ,n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]+n[rand.nextInt(n.length)]);
                if(rand.nextInt(2)==1)administrator.setSex("女");
                if(rand.nextInt(2)==1)administrator.setAdministrator_name(name[rand.nextInt(name.length)]+it[rand.nextInt(it.length)]);
                p = c.prepareStatement(sql);
                p.setInt(1, administrator.getAdministrator_id());
                p.setString(2, administrator.getAdministrator_name());
                p.setInt(3, administrator.getAge());
                p.setString(4, administrator.getSex());
                p.setString(5, administrator.getPhone());
                p.setString(6, administrator.getCity());
                p.setString(7, administrator.getUsername());
                p.setString(8, administrator.getPassword());
                p.executeUpdate();

            }
        }
    }
}