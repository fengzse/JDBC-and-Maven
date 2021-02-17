/*
 * BaseDao 的作用是等待被具体的接口及其实现类调用其中的方法，本身相当于就是一个工具类，只提供内部通用方法供其它类调用
 * 针对JDBC的实现，首先一个类对应一个表，因此具体查询删改操作的时候，通常都会针对具体的表创建对应的接口和执行实现类，实现类中再调用DAO中
 * 定义的通用方法
 * BaseDAO也因此可以被声明为抽象方法，用于禁止创建实例，但不声明为抽象方法也可以
 * 同时BaseDAO也作为接口实现类的父类被继承，则其内部定义的方法都被子类全部继承，自由调用（抽象类作为父类，抽象方法必须被子类重写，
 * 自己的实现方法可以被子类直接继承）
 */
package com.JDBCPractices;

import com.JDBCUtils.JDBCUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAO {

    /*
    DAO封装了针对数据表的通用操作，出于解耦性及灵活调用事务的考虑，将数据库连接作为参数传入，连接由调用DAO方法的方法穿件及关闭
    前面的方法都是在方法内建立数据库连接，增删改查全部封装在方法里，导致在方法外部无法灵活使用数据库事务，因为不是所有的数据库操作都需要使用
    事务，因此自动提交的设置就不能写死在增删改查的方法里，而应该由调用方法自己定义
    1. 调用方法需要自行建立连接，Connection conn = null; 在try块中建立conn = JDBCUtil.getConnector();
    2. 调用update或query方法，因为此时update或query方法内部不再需要创建数据库连接，因此需要将conn作为参数传入方法
    3. 可以根据调用方法需要灵活设置事务的开启与关闭，数据的提交与回滚
    4. ps和rs是update或query方法定义的，因此需要在方法内定义资源关闭，但是conn是外部定义的，不能定义关闭，因此关闭方法对应参数传入null，同理
       外部方法定义连接conn，则必须负责关闭连接conn，而ps和rs因为已经被update或query方法关闭，因此对应参数传入null
     */

    public void transactionUpdate(Connection conn, String sql, Object...args){
        PreparedStatement ps = null;

        try {
            ps=conn.prepareStatement(sql);
            for(int i=0; i< args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(null,ps);
        }
    }

    public <T> T transactionQuery(Connection conn,Class<T> cls, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i=0; i<args.length; i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery(); // 通过连接查询得到结果集对象

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount=meta.getColumnCount();

            if(rs.next()){
                String columnLabel;
                Field field;
                Object columnValue;
                T inst= cls.newInstance();

                for(int i=0; i<columnCount; i++){
                    columnLabel= meta.getColumnLabel(i+1);
                    columnValue=rs.getObject(columnLabel);
                    field=cls.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(inst,columnValue);
                }
                System.out.println(inst);
                return inst;
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return null;
    }

    public <T> List<T> transactionQueryAll(Connection conn, Class<T> cls, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> query_all = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sql);
            for(int i=0; i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // 将if写为while循环，使next指针随循环下移到下一条记录，直到下一个没有记录返回false终止循环
            while(rs.next()){
                // 随循环创建新实例，每个实例对应一条表中的记录
                T instance = cls.newInstance();
                Field field;
                Object columnValue;
                String columnLabel;

                for(int i=0; i<columnCount;i++){
                    columnLabel=meta.getColumnLabel(i+1);
                    columnValue=rs.getObject(columnLabel);
                    field=cls.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(instance,columnValue);
                }
                query_all.add(instance);
            }
            return query_all;
        }catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e){
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return null;
    }

    /*
    在数据库中还有一些可调用的函数如COUNT(*)等，以上的查询方法无法返回这些函数的返回值，因此需要特别定义一个方法，获取这些函数的值
    函数返回值可能是各种类型，如String，Long, Date等，因此方法的返回值类型需要定义为泛型
    通常而言，SQL函数的调用就返回一行及一个计算结果值，但是如果有分组的情况下，可能会依据分组返回函数值，因此最好用列表接收
     */

    public <T> T getFuncValue(Connection conn,String sql,String label,Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount=meta.getColumnCount();
            String columnLabel;
            if (rs.next()) {
                for(int i=0;i<columnCount;i++){
                    columnLabel=meta.getColumnLabel(i+1);
                    if(columnLabel.equals(label)){
                        // 原本同下面方法一样，但是其实用别名查询更合理一些，但要确保别名正确
                        return ((T) rs.getObject(columnLabel));
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return null;
    }

    public <T> List<T> getFuncValues(Connection conn, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list=new ArrayList<>();
        try {
            ps=conn.prepareStatement(sql);
            for(int i=0; i<args.length; i++){
                ps.setObject(i+1,args[i]);
            }
            rs= ps.executeQuery();
            while(rs.next()){
                list.add((T)rs.getObject(1)); // 指定查询第一列的值，因此注意执行查询的时候不要查询多列
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(null,ps,rs);
        }
        return null;
    }
}
