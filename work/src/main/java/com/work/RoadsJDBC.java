package com.work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * 公路信息数据库JDBC
 * @param <T>实体类
 * @param <K>主键
 */
public class RoadsJDBC <T,K> implements JDBC<T,K>{
    //sql中获取当前辖区对应公路ID的条件
   private  String l ="location='" + LoginController.Information.getInstance().getILocation()+"'";

    /**
     * 单条插入，主键重复则跳过
     * @param entity 插入数据
     * @return 根据返回值判断是否主键重复
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public int insert(T entity) throws SQLException {
        try (Connection c = JDBCUtils.getConnection()) {
            String sql = "INSERT ignore INTO roads VALUES (?,?,?,?,?,?,?,?)";
            Roads t = (Roads) entity;
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, t.getRoad_id());
            p.setString(2, t.getRoad_name());
            p.setString(3, t.getLocation());
            p.setString(4, t.getStarting_point());
            p.setString(5, t.getTerminal());
            p.setString(6, t.getLength());
            p.setString(7, "无事故发生");
            p.setString(8, t.getRoad_grade());
            return p.executeUpdate();
        }
    }

    /**
     * 根据传入的主键key单条删除对应数据，并删除与该主键相关联外键的数据
     * @param key 删除条件的判断依据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public void deleteByKey(K key) throws SQLException {
        try(Connection c = JDBCUtils.getConnection()){
            String sql = "delete from accidents where road_id=?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1,(int)key);
            p.executeUpdate();
            p = c.prepareStatement("delete from maintenance where road_id=?");
            p.setInt(1,(int)key);
            p.executeUpdate();
            p = c.prepareStatement("delete from roads where road_id=?");
            p.setInt(1,(int)key);
            p.executeUpdate();
        }
    }

    /**
     * 查询所有当前辖区的公路信息
     * @return 返回查询到的数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public List<T> getAll() throws SQLException {
        try (Connection c = JDBCUtils.getConnection()) {
            String sql = "select *from roads where ";
            PreparedStatement p = c.prepareStatement(sql+l);
            ResultSet r = p.executeQuery();
            List<Roads> list = new ArrayList<>();
            while (r.next()) {
                Roads t = new Roads();
                t.setRoad_id(r.getInt(1));
                t.setRoad_name(r.getString(2));
                t.setLocation(r.getString(3));
                t.setStarting_point(r.getString(4));
                t.setTerminal(r.getString(5));
                t.setLength(r.getString(6));
                t.setAccident_situation(r.getString(7));
                t.setRoad_grade(r.getString(8));
                list.add(t);
            }
            return (List<T>) list;
        }
    }

    /**
     * 根据传入的条件查询当前辖区的公路信息
     * @param condition 查询条件
     * @return 返回查询到的数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public List<T> getByCondition(String condition) throws SQLException {
        try (Connection c = JDBCUtils.getConnection()) {
            String sql = "select *from roads  " + condition;
            PreparedStatement p;
            if (condition.isEmpty()) p= c.prepareStatement(sql+" where "+l);
            else  p = c.prepareStatement(sql+" and "+l);
            ResultSet r = p.executeQuery();
            List<Roads> list = new ArrayList<>();
            while (r.next()) {
                Roads t = new Roads();
                t.setRoad_id(r.getInt(1));
                t.setRoad_name(r.getString(2));
                t.setLocation(r.getString(3));
                t.setStarting_point(r.getString(4));
                t.setTerminal(r.getString(5));
                t.setLength(r.getString(6));
                t.setAccident_situation(r.getString(7));
                t.setRoad_grade(r.getString(8));
                list.add(t);
            }
            return (List<T>) list;
        }
    }

    /**
     * 分页查询，查询对应页码的具体数量的当前辖区的公路信息
     * @param offset 页码
     * @param limit 每页最大数量
     * @return 返回查询到的数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public List<T> getPage(int offset, int limit) throws SQLException {
        try (Connection c = JDBCUtils.getConnection()) {
            offset = offset*limit;
            String sql = "select *from roads where " + l + " order by road_id  limit " + offset + "," + limit;
            PreparedStatement p = c.prepareStatement(sql);
            ResultSet r = p.executeQuery();
            List<Roads> list = new ArrayList<>();
            while (r.next()) {
                Roads t = new Roads();
                t.setRoad_id(r.getInt(1));
                t.setRoad_name(r.getString(2));
                t.setLocation(r.getString(3));
                t.setStarting_point(r.getString(4));
                t.setTerminal(r.getString(5));
                t.setLength(r.getString(6));
                t.setAccident_situation(r.getString(7));
                t.setRoad_grade(r.getString(8));
                list.add(t);
            }
            return (List<T>) list;
        }
    }

    /**
     * 根据主键和传入实体更新对应主键的数据
     * @param key 更新条件的判断依据
     * @param entity 将更新的数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @Override
    public void updateByKey(K key, T entity) throws SQLException {
        try(Connection c = JDBCUtils.getConnection()){
            String sql =
                    "update roads set road_name=?,location=?,starting_point=?,terminal=?,length=?,road_grade=? where road_id=?";
            Roads t = (Roads) entity;
            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(7, t.getRoad_id());
            p.setString(1, t.getRoad_name());
            p.setString(2, t.getLocation());
            p.setString(3, t.getStarting_point());
            p.setString(4, t.getTerminal());
            p.setString(5, t.getLength());
            p.setString(6, t.getRoad_grade());
            p.executeUpdate();
        }
    }

    /**
     * 获取当前辖区的数据总数
     * @return 返回总记录数
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    public int getTotalRecords() throws SQLException {
// SQL 查询语句，统计总记录数
        String sql = "SELECT COUNT(*) FROM roads where " + l;
        try (PreparedStatement pstmt = JDBCUtils.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
// 如果查询结果包含数据，返回第一列的值（总记录数）
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
// 如果没有数据或发生异常，返回0
        return 0;
    }

    /**
     * 根据每页最大数据数量获取页码
     * @return 返回页码
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    public int calculatePageCount() throws SQLException{
        //向上取整
        return (int) Math.ceil((double) getTotalRecords() / RoadsController.itemsPerPage);
    }

    /**
     * 公路信息的关联初始化，根据事故信息与公路ID关联的外键的有无，更新对应辖区公路信息的出险情况
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    public void init()throws SQLException{
        try(Connection c = JDBCUtils.getConnection()){
            String sql =
                    "update roads set accident_situation=? where roads.road_id not in (select road_id from accidents)";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1,"无事故发生");
            p.executeUpdate();
            sql="update roads set accident_situation=? where roads.road_id  in (select road_id from accidents)";
            p = c.prepareStatement(sql);
            p.setString(1,"有事故发生");
            p.executeUpdate();
        }
    }
}
