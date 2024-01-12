package com.work;

import java.sql.SQLException;
import java.util.List;

/**
 * JDBC（DAO）接口
 */
public interface JDBC<T,K> {
    /**
     * 向数据库中插入一条数据实体。
     * @param entity
     * @return
     * @throws SQLException
     */
    int insert(T entity) throws SQLException;  // 向数据库中插入一条数据实体。


    /**
     * 根据主键更新数据库中的一条数据实体。
     * @param key
     * @param entity
     * @throws SQLException
     */
    void updateByKey(K key, T entity) throws SQLException; //根据主键更新数据库中的一条数据实体。

    /**
     * 根据主键删除数据库中的一条数据。
     * @param key
     * @throws SQLException
     */
    /**
     * 根据主键删除数据库中的一条数据。
     * @param key
     * @throws SQLException
     */
    /**
     * 根据主键删除数据库中的一条数据。
     * @param key
     * @throws SQLException
     */
    void deleteByKey(K key) throws SQLException;  //根据主键删除数据库中的一条数据。


    /**
     * 查询数据库中的所有数据实体。
     * @return
     * @throws SQLException
     */
    List<T> getAll() throws SQLException; //查询数据库中的所有数据实体。

    /**
     * 根据指定的条件查询数据库中的数据实体
     * @param condition
     * @return
     * @throws SQLException
     */
    List<T> getByCondition(String condition) throws SQLException; //根据指定的条件查询数据库中的数据实体

    /**
     * 分页查询数据库中的数据实体
     * @param offset
     * @param limit
     * @return
     * @throws SQLException
     */
    List<T> getPage(int offset, int limit) throws SQLException; //分页查询数据库中的数据实体。
}