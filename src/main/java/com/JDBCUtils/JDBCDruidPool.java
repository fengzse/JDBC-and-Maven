package com.JDBCUtils;


import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// 创建方法与DBCP基本相同
public class JDBCDruidPool {

    private static DataSource druid=null;
    static {
        Properties pros =new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        try {
            pros.load(is);
            druid= DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getDruidConnector() throws SQLException {
        Connection conn = druid.getConnection();
        System.out.println(conn);
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
