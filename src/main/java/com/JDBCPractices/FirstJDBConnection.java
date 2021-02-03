package com.JDBCPractices;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class FirstJDBConnection {
    public void JDBConnector()
            throws SQLException, IOException {
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
         */
        InputStream is= FirstJDBConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro=new Properties();
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
        try{
            // 加载一下这个类就可以注册驱动,因为mysql Driver类的静态代码块中已经调用了registerDriver()来注册驱动程序.
            Class.forName(driver);
        }
        catch (ClassNotFoundException exp){
            System.out.println("Error:Unable to load Driver class");
            System.exit(1);
        }


        // 获取数据库连接
        Connection conn = DriverManager.getConnection(url,user,password);

        System.out.println(conn);

    }
}

class TestC{
    public static void main(String[] args) throws SQLException, IOException {
        FirstJDBConnection fj=new FirstJDBConnection();
        fj.JDBConnector();
    }
}