package com.JDBCUtils;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/*
 工具类内部的方法都创建为静态方法，因为其功能单一，只是创建连接和销毁连接的工厂，没有必要创建实例和方法副本消耗资源，
 如果要创建多个连接等待调用，应该使用连接池
 */
public class JDBCUtil {
    // 获取JDBC连接
    public static Connection getConnector(){
        Connection conn = null;
        Properties pro;
        /*
         * 将数据库的配置信息存放进单独的properties文件
         * properties 文件要放在module目录下的resources目录下才能被Properties类读取到（load）
         * 创建InputStream对象通过动态反射的方式将jdbc.properties文件内容作为流读取到内存
         * 加载配置文件jdbc.properties后，把配置信息封装到Properties对象中
         * jdbc.properties可以被看作类似编译好的文件，不写入源码，因此需要通过Class类实例的反射进行动态读取
         * 每一种类型包括类和接口等，都有一个 class 静态变量可以获得 Class 实例。
         * 另外，每一个对象都有 getClass() 方法可以获得 Class 实例，该方法是由 Object 类提供的实例方法。
         * FirstJDBConnection.class.getClassLoader()这样的编写就是将当前定义类（现在是源码），
         * 在运行加载被编译成的自身的.class文件后，在运行中通过加载器再把自己拿回来，获取一个Class类实例，
         * 然后再动态执行后面的如.getResourceAsStream这样的命令
         * 原方法写为FirstJDBConnection.class.getClassLoader()用于动态加载类，现在将本类工具化，需要将其改为
         * 通用的 ClassLoader.getSystemClassLoader()
         */
        try{
            InputStream is= ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            pro=new Properties();
            pro.load(is);

            // 获取配置信息
            String driver,user,password,url;

            url=pro.getProperty("url");
            driver=pro.getProperty("driver");
            user=pro.getProperty("user");
            password=pro.getProperty("password");

            /*
               通过Class.forName动态加载数据库驱动的类文件到内存。需要反射动态加载是因为驱动是已经编译好的class文件
               在驱动方面，MySQl可以直接简化为只写Class.forName(driver)，但是其他数据库需要完整驱动
               例如oracle的数据库驱动就需要完整写明
               Class cls= Class.forName("oracle.jdbc.driver.OracleDriver")
               Driver myDriver = (Driver) cls.newInstance());
               DriverManager.registerDriver( myDriver );
             */

            // 加载一下这个类就可以注册驱动,因为mysql Driver类的静态代码块中已经调用了DriverManager.registerDriver()来注册驱动程序.
            Class.forName(driver);
            // 获取数据库连接,DriverManager内部已经注册了驱动，再用DriverManager来创建连接即可
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("JDBC successfully connected with "+conn);
            return conn;
        }
        catch (ClassNotFoundException | SQLException | IOException exp){
            System.out.println("Error:Unable to load Driver class");
            System.exit(1);
        }
        return conn;
    }

    /*
        在数据库操作中会使用PreparedStatement，是Statement的子接口，因此这里的形参类型可以用父接口
        Statement需要导入，java.sql.*
     */
    public static void closeResource(Connection conn, Statement ps){
        try{
            if(ps != null){
                ps.close();
                System.out.println("Statement close successfully");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if(conn != null){
                conn.close();
                System.out.println("Connection break successfully");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

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

