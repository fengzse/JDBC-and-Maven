package com.JDBCPractices;
/*
为具体的customers表也是对应的Customers类提供一个接口，用于提供并规范可提供操作的项目类别
因此，除了工具类BaseDao，实际上每个表对应的类模型在JDBC操作中都需要三个类和一个调用类
1. 对应表的类模型
2. 提供操作规范的接口
3. 接口的实现类，实现类重写接口方法，继承并调用BaseDao类定义的方法。但这个类仍然是定义具体的实现，而不是调用
4. 调用或者说是执行类，真实调用接口和实现类，实现针对具体模型的增删改查（也可以是客户端的动态调用等）
 */

import com.JDBClasses.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

// 这是专用于Customers模型及表的接口，因此接口方法如果要接收对象参数，只能是Customers类对象
public interface CustomerDao {
    // 插入记录, 将模型对象（其属性即为表的column，属性值为row）插入到数据库中
    void insertInto(Connection conn, Customers cstm);

    // 通过指定的id删除表中的一条记录, 实际上也可以根据Customers中的任意属性来进行增删改，如 String name
    void deleteById(Connection conn, int id);

    // 通过指定id，update指定的Customers对象
    void updateById(Connection conn, int id, Customers cstm);

    // 也可不通过指定属性查找并update具体实例记录，因为传入的实例本身就是要用于update其对应的数据库记录的
    void updateInstance(Connection conn, Customers cstm);

    // 通过指定id从数据库中查询对应记录，并返回Customers实例，即查询操作
    Customers getCustomerById(Connection conn, int id);

    // 查询返回Customers模型的多个实例，并存储与列表中
    List<Customers> getCustomersAll(Connection conn);

    // 调用数据库函数，查询函数返回值
    // 要调用SQL下的COUNT()函数
    List<Long> getCount(Connection conn);  // 注意，返回类型是Long

    // 要调用SQL下的Date函数
    Date getMaxBirth(Connection conn);
}

