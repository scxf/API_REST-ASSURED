package com.test.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 数据库连接
 * @ClassName: JDBCUtils
 * @Author: Ming         // 创建者
 * @Date: 2020/12/26 15:55   // 时间
 * @Version: 1.0     // 版本
 */
public class JDBCUtils {
    public static Connection getConnection(){
        //定义数据库连接
        //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
        //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
        //MySql：jdbc:mysql://localhost:3306/DBName
        String url="jdbc:mysql://8.129.91.152:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user="future";
        String password="123456";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user,password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    /**
     *  数据库插入 修改 操作 删除
     * @author Ming
     * @param sql sql语句
     * @return void
     * @date 2020/12/26 16:31
     */
    public static void update(String sql){
        //获取到数据库连接对象
        Connection conn = getConnection();
        //数据库新增
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(conn,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭数据库
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     *  查询所有的结果集
     * @author Ming
    * @param sql  查询语句
     * @return 返回查询到的所有符合条件的数据， 保存为list
     * @date 2020/12/26 16:51
     */
    public static  List<Map<String, Object>>  queryAll(String sql){
        //获取到数据库连接对象
        Connection conn = getConnection();
        //数据库新增
        QueryRunner queryRunner = new QueryRunner();
        try {
            List<Map<String, Object>> result = queryRunner.query(conn, sql, new MapListHandler());
           return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭数据库
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  null;
    }
    /**
     *  查询单个结果
     * @author Ming
     * @param sql   查询语句
     * @return 返回单个结果 存在map中
     * @date 2020/12/26 16:52
     */
    public static Map<String, Object> query(String sql){
        //获取到数据库连接对象
        Connection conn = getConnection();
        //数据库新增
        QueryRunner queryRunner = new QueryRunner();
        try {
            Map<String, Object> result = queryRunner.query(conn, sql, new MapHandler());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭数据库
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  null;
    }
    /**
     *  查询结果集中的单个数据
     * @author Ming
     * @param sql 查询语句
     * @return 返回单个数据
     * @date 2020/12/26 16:59
     */
    public static Object querySinger(String sql){
        //获取到数据库连接对象
        Connection conn = getConnection();
        //数据库新增
        QueryRunner queryRunner = new QueryRunner();
        try {
            Object result = queryRunner.query(conn, sql, new ScalarHandler());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭数据库
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  null;
    }
}

