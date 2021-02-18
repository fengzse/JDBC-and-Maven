package com.JDBCUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
创建c3p0数据库连接池，这是Hibernate官方推荐使用的连接池
 */
public class JDBC3P0Pool {
    /*
     通过xml配置文件获取连接池，并初始化连接池。连接池对象创建为静态,也就确保了连接池始终只创建一个，而不管有多少外部类及方法调用了下面的
     getCpConnector方法获取连接池中的连接。连接池创建一个就够了，内部已经包含了大量连接
     如果将这个对象放在getCpConnector方法内部，就会每次随着getCpConnector方法被调用而创建一个新的连接池，那还不如原来的只创建一个连接
     */
    private static DataSource cpds=new ComboPooledDataSource("mysql");

    public static Connection getCpConnector() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }

    /*
    关闭连接，在连接池中是将调用的连接归还连接池，而不是断开连接
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        // 关闭statement语句通道
        try{
            if(ps != null){
                ps.close();
                System.out.println("Statement close successfully");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        // 关闭结果集
        try {
            if(rs != null){
                rs.close();
                System.out.println("ResultSet closed");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        // 关闭数据库连接
        try{
            if(conn != null){
                conn.close();
                System.out.println("Connection break successfully");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
