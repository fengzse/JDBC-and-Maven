package com.JDBCPractices;

import com.JDBClasses.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
 CustomersDaoImpl类定义具体实现针对Customers模型的增删改查方法，但不是调用，还需要一个类实现调用
 因此创建数据库连接，和数据库事务都是在调用类中创建实现的，关闭连接也在调用类中
 */
public class CustomersDaoImpl extends BaseDAO implements CustomerDao{

    /*
    在调用BaseDao定义的通用方法的时候，可变形参args由cstm获取实例属性并传参替换?占位符,或由方法形参传入
    而反过来，在查询的时候，则由通过反射出Customers类的Class对象，反射出对应的类属性，最后创建并赋值该实例的类属性再返回实例对象
     */

    @Override
    public void insertInto(Connection conn, Customers cstm) {
        String sql="insert into customers(name,email,birth) values(?,?,?)";
        transactionUpdate(conn,sql,cstm.getName(),cstm.getEmail(),cstm.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql="delete from customers where id = ?";
        transactionUpdate(conn,sql,id);
    }

    @Override
    public void updateById(Connection conn, int id, Customers cstm) {
        String sql="update customers set name=?, email=?, birth=? where id=?";
        transactionUpdate(conn,sql,cstm.getName(),cstm.getEmail(),cstm.getBirth(),id);
    }

    @Override
    public void updateInstance(Connection conn, Customers cstm) {
        String sql="update customers set name=?, email=?, birth=? where id=?";
        transactionUpdate(conn,sql,cstm.getName(),cstm.getEmail(),cstm.getBirth(),cstm.getId());
    }

    @Override
    public Customers getCustomerById(Connection conn, int id) {
        String sql="select * from customers where id = ?";
        Customers cstm=transactionQuery(conn,Customers.class,sql,id);
        return cstm;
    }

    @Override
    public List<Customers> getCustomersAll(Connection conn) {
        String sql="select * from customers";
        List<Customers> listCustomers=transactionQueryAll(conn,Customers.class,sql);
        return listCustomers;
    }

    @Override
    public List<Long> getCount(Connection conn) {
        String sql="select count(*) as COUNT from customers group by id";
        List<Long> value=getFuncValues(conn,sql);
        return value;
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql="select max(birth) as MaxBirth from customers";
        // 必须要确保传入的label能和数据库指定列的指定别名完全一样，否则找不到
        Date value=getFuncValue(conn,sql,"MaxBirth");
        return value;
    }
}
