package com.work;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils { //使用Druid数据库连接池技术
    private static DataSource dataSource;


    static {  //初始化和管理MySQL数据库的连接池
        try {
            Properties properties = new Properties();
            InputStream inputStream = DruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");

            properties.load(inputStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return 返回数据库连接
     */
    public static Connection getConnection() {  //数据库连接方法
        Connection conn = null;
        try {
            conn= dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
    private static class DruidUtils {
    }
}

