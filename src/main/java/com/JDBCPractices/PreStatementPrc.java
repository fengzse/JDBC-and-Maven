package com.JDBCPractices;
import com.JDBCUtils.JDBCUtil;
import com.JDBClasses.Customers;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PreStatementPrc {
    // 声明boolean是为了在测试中进行断言, forTest=false设置默认值，每个新建实例的forTest都会是false
    private boolean forTest=false;

    public void setForTest(boolean t){forTest=t;}
    public boolean getForTest(){return forTest;}

    public void insertTest(){
        // 调用工具类连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            // 获取数据库连接
            conn=JDBCUtil.getConnector();
            //sql语句， ？的作用是占位符
            String sql="insert into customers(name, email, birth) values(?,?,?)";
            // 创建连接实例并注入sql语句
            ps=conn.prepareStatement(sql);
            // 可以改为通用的setObject()
            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@gmail.com");
            /*
             在注入Date的时候要注意，不能直接给java.sql.Date注入时间，需要创建java.util.Date 对象获取指定的时间
             再调用getTime()方法获取指定时间的时间戳，传递给java.sql.Date实例再注入数据库
             */
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            // 注意调用的是java.util.Date类， parse()方法作用是将给定的字符串按照SimpleDateFormat指定的日期格式解析为一个Date对象。
            java.util.Date date=sdf.parse("1000-02-04");
            ps.setDate(3,new Date(date.getTime()));
            ps.execute();
            System.out.println("Insert successfully");

        }catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps);
        }
    }

    public void updateTest() {
        Connection conn=null;
        PreparedStatement ps = null;
        try{
            conn = JDBCUtil.getConnector();
            String sql = "update customers set birth=? where name=?";
            /*
                还是注意util的Date和sql下的Date不是同一个类，
                需要先用util下Date获取时间，再调用getTime()获取时间戳传给sql下的Date()，new Date()空参为当前时间
                需要java的Date类的时候要声明和new全类名, sql的Date是util的Date的子类。Java的时间操作都是使用util下的Date，要
                update或insert进数据库就需要一个转换
                但是如果是使用setObject就可以避免这个麻烦
             */
            java.util.Date date=new java.util.Date();
            ps=conn.prepareStatement(sql);
            ps.setDate(1, new Date(date.getTime()) );
            ps.setObject(2,"哪吒");
            ps.execute();
            System.out.println("Updated done");
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps);
        }
    }
    /*
        创建一个可以实现所有增删改的通用方法, 查询单写
        Object...args 也可以写成接收一个Object数组 Object[] args, 编写实参的时候直接构成数组传递
        增删改不需要泛型，因为直接通过预定义sql操作表，查询时调取模型记录，就需要泛型来调取不同的模型
     */
    public void commonUpdate(String sql, Object...args){
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            conn=JDBCUtil.getConnector();
            // 只传递预编译的sql，其他参数通过ps的setObject方法传入
            ps=conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                //只能setObject，因为待传入实参类型未知
                ps.setObject(i+1,args[i]);
            }
            ps.execute();
            /*
                在try块中只有正确执行execute()完毕，才顺序设定forTest=true，
                如果执行中出错抛出异常，就会直接跳过boolean设置，跳到catch块捕获异常，可以在测试中编写会出错的代码以assertFalse
             */
            setForTest(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps);
        }
    }

    /*
     获取指定模型数据库实例查询，Java中一切皆对象，数据库记录的查询结果要在Java中提取就需要封装在对应的类对象中
     因此，所有的query查询都要返回一个对象实例
     */
    public Customers getCustomer(String sql, Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        // 查询结果保存在结果集对象中，ResultSet是一个接口，实现对象为PreparedStatement对象实现调用的executeQuery()
        ResultSet rs = null;

        try {
            conn=JDBCUtil.getConnector();
            ps=conn.prepareStatement(sql);
            /*
             args 参数都是为了填充预定义的sql语句中的占位符”？“的，而不是对应SELECT语句中的列的
             在查询中占位符通常都是只作为查询条件WHERE出现的
             */
            for(int i=0; i<args.length; i++){
                ps.setObject(i+1,args[0]);
            }

            //获取结果集实例：增删改为execute(), 查询为executeQuery()，查询目标为某条记录，即模型的某个实例
            rs=ps.executeQuery();

            /*
            接下来要从查询出的单条记录中取出对应各列（也就是模型中的属性）的值，就需要知道记录有对应多少个列，列名分别是什么等等
            需要调用ResultSetMetaData类对象获取表的元数据
             */
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount=rsmd.getColumnCount(); // 取得列数
            String columnName;

            //next()判断是否有下一个查询目标，不是下一条记录，而是当前记录的各列
            if(rs.next()){
                Customers ctm=new Customers();
                Object value;
                Field field;
                // 遍历每个列，获取当前查询出的记录的每个列的值 rs.getObject
                for(int i=0; i<columnCount; i++){
                    /*
                    getColumnLabel（） 获取列名，如果有别名则获取别名
                    getColumnName（）获取列名
                    获取列名称之后需要和模型（类）的属性名称做比对，要获取类属性名称，需要调用反射获取Field
                     */
                    columnName=rsmd.getColumnName(i+1);
                    value=rs.getObject(i+1); //数据库索引从1开始
                    field = Customers.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(ctm,value);

                }
                System.out.println(ctm);
                setForTest(true);
                return ctm;
            }
        }catch (SQLException | NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
        }

        finally {
            JDBCUtil.closeResource(conn,ps,rs);
        }
        return null;
    }

    /*
    以上是针对某个具体模型类的查询，下面尝试编写通用的查询
    1. 使用泛型，能够接收所有类
    2. 在参数中需要传递一个Class对象，上面的查询时直接实例化Customers类，通过Customers.class反射出属性field。不能直接用实例ctm调用属性
       因为属性名称都是确定的，而查询中需要通过数据库列名反射Field获得对应的属性，有一个比对名称的过程。直接获取实例属性没有对应的方法进行比对
    3. 在查询类定义中可以直接传递泛型Class对象，通过Class对象获取需要返回的实例，同时Class对象可以直接反射需要的属性或方法，不再需要
       Customers.class获取类的内存加载
     */
    public <T> T getQuery(Class<T> cls, String sql, Object...args){
        Connection conn = null;
        PreparedStatement ps =null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnector();
            ps = conn.prepareStatement(sql);

            for (int i=0; i<args.length; i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();

            ResultSetMetaData re = rs.getMetaData();
            int columnCount=re.getColumnCount();

            if(rs.next()){
                String columnLabel;
                Field field;
                Object columnValue;
                T inst= cls.newInstance();

                for(int i=0; i<columnCount; i++){
                    columnLabel= re.getColumnLabel(i+1);
                    columnValue=rs.getObject(columnLabel);
                    field=cls.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(inst,columnValue);
                }
                setForTest(true);
                System.out.println(inst);
                return inst;
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn, ps,rs);
        }
        return null;
    }
}
