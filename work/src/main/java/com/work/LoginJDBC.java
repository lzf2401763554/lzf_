package com.work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员信息数据库JDBC
 * @param <T>实体类
 * @param <K>主键
 */
public class LoginJDBC<T,K> implements JDBC<T,K>{
    @Override
    public int insert(T entity) throws SQLException {
        return 0;
    }

    @Override
    public void deleteByKey(K key) throws SQLException {

    }

    @Override
    public List<T> getAll() throws SQLException {

            return null;
        }

    /**
     * 根据条件查询对应密码
     * @param condition 查询密码的条件
     * @return 返回查询到的密码
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public List<T> getByCondition(String condition) throws SQLException {
        List<String> list = new ArrayList<>();
        try (Connection c = JDBCUtils.getConnection()) {
            String sql = "select password from administrator " + condition;
            PreparedStatement p = c.prepareStatement(sql);
            ResultSet r = p.executeQuery();
            while (r.next()) list.add(r.getString(1));
        return (List<T>) list;
    }
    }

    /**
     * 根据条件查询对应数据类型
     * @param e 要查询的数据类型
     * @param condition 查询数据的条件
     * @return 返回查询到的数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    public List<T> getByCondition(String e,String condition) throws SQLException {
        List<T> list = new ArrayList<>();
        try (Connection c = JDBCUtils.getConnection()) {
            String sql = "select " + e + " from administrator " + condition;
            PreparedStatement p = c.prepareStatement(sql);
            ResultSet r = p.executeQuery();
            if (e.equals("*")||e.contains(","))while (r.next()){
                //查询多个数据类型
                Administrator administrator = new Administrator();
               administrator.setAdministrator_id(r.getInt(1));
                administrator.setAdministrator_name(r.getString(2));
                administrator.setAge(r.getInt(3));
                administrator.setSex(r.getString(4));
                administrator.setPhone(r.getString(5));
                administrator.setCity(r.getString(6));
                administrator.setUsername(r.getString(7));
                administrator.setPassword(r.getString(8));
                list.add((T)administrator);
            }
           else while (r.next()) list.add((T)r.getString(1)); //查询单个数据类型
            return  list;
        }
    }
    @Override
    public List<T> getPage(int offset, int limit) throws SQLException {
        return null;
    }

    @Override
    public void updateByKey(K key, T entity) throws SQLException {

    }

    /**
     * 忘记密码操作，根据主键管理员ID更改对应用户名的密码
     * @param key 管理员ID
     * @param uN 用户名
     * @param uP 密码
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    public void update_(K key,String uN,String uP)throws SQLException{
        try(Connection c = JDBCUtils.getConnection()){
            String sql =
                    "update administrator set username=?,password=? where administrator_id=?";

            PreparedStatement p = c.prepareStatement(sql);
            p.setString(3,(String) key);
            p.setString(1,uN);
            p.setString(2, uP);
            p.executeUpdate();
        }
    }

    /**
     * 修改个人信息，除了某些信息不能改，例如主键、用户名和辖区，更新剩余的数据
     * @param e 将进行更新的实体
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    public void update_1(T e)throws SQLException{
        try(Connection c = JDBCUtils.getConnection()){
            String sql =
                    "update  administrator set administrator_name=?,age=?,sex=?,phone=?,password=? where administrator_id=?";
Administrator administrator =(Administrator)e;
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(6,administrator.getAdministrator_id());
            p.setString(1,administrator.getAdministrator_name());
            p.setInt(2, administrator.getAge());
            p.setString(3, administrator.getSex());
            p.setString(4, administrator.getPhone());
            p.setString(5, administrator.getPassword());
             p.executeUpdate();
        }
    }

}
