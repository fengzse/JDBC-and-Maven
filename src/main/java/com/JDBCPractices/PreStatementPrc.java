package com.JDBCPractices;
import com.JDBCUtils.JDBCUtil;
import com.JDBClasses.Customers;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PreStatementPrc {

    public int insertTest(){
        // 调用工具类连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        int inserted=0;
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
            inserted=ps.executeUpdate();
            System.out.println("Insert successfully");
            return inserted;

        }catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps);
        }
        return inserted;
    }

    public int updateTest() {
        Connection conn=null;
        PreparedStatement ps = null;
        int updated=0;
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
            updated=ps.executeUpdate();
            System.out.println("Updated done");
            return updated;
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps);
        }
        return updated;
    }
    /*
        创建一个可以实现所有增删改的通用方法, 查询单写
        Object...args 也可以写成接收一个Object数组 Object[] args, 编写实参的时候直接构成数组传递
        但是写成可变参数是因为可变参数支持不传参，而写成Object[] args就必须传参
        增删改不需要泛型，因为直接通过预定义sql操作表，查询时调取模型记录，就需要泛型来调取不同的模型
     */
    public int commonUpdate(String sql, Object...args){
        Connection conn=null;
        PreparedStatement ps=null;
        int updated=0;
        try {
            conn=JDBCUtil.getConnector();
            // 只传递预编译的sql，其他参数通过ps的setObject方法传入
            ps=conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                //只能setObject，因为待传入实参类型未知
                ps.setObject(i+1,args[i]);
            }
            /*
                ps.execute()是一个通用的执行操作，返回一个boolean类型，如果执行的是查询操作，有返回结果，则此方法返回true
                如果执行的是增，删，改操作，没有返回值，则此方法返回false
                对于查询操作已经直接调用了ps.executeQuery()返回结果集，因此增删改部分最好也调用专用方法，即 ps.executeUpdate()
                ps.executeUpdate() 方法返回一个整数，update了几行记录就返回整数几，没有对任何记录执行操作就返回0
                将当前增删改方法的返回类型改为 int 以接收ps.executeUpdate()的返回值，同时在测试的时候可以直接测试返回整数值是否匹配
                将updated设置初始值为0，若增删改失败，未对任何记录行进行操作，则返回updated的时候就会返回0
                将上面的两个方法以及相关测试方法也进行相应的修改
             */
            updated=ps.executeUpdate();
            return updated;

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps);
        }
        return updated;
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

            /*获取结果集实例：增删改为execute(), 查询为executeQuery()，查询目标为某条记录，即模型的某个实例
              在这里注意一个问题，就是当查询的列少于数据表的列的时候，实际上在下面这一步就剔除或者说根本没查询预编译sql
              语句里没有被select的列，因为结果集rs的赋值对象是ps.executeQuery()，而ps是根据sql预编译的查询返回的
              因此rs结果集里只有通过sql语句传递给ps的要执行select的列。例如表有7列，但是只select其中3列，
              则ps.executeQuery()就只返回被select的3列给结果集。从而下文的获取元数据等信息也就仅为这3列的元数据，比如查询
              出的列数，列名等
             */

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
            JDBCUtil.closeResource(conn, ps,rs);
        }
        return null;
    }

    /*
    上一个类是查询泛型类的单条数据记录，而要查询某个类模型在数据库表中的记录集合(多条记录)就需要先创建一个集合，如列表
    下面编写通用多记录查询
    复习：public 后面的<T> 表示这是一个泛型方法，表示返回一个泛型的列表List<T>，这是针对整个方法的泛型声明
    而List<T>中的泛型<T>表示这个列表接收泛型类对象.因为根据集合列表定义，创建集合对象的时候必须在< >指明集合对象元素的类型
    这是针对列表本身的泛型声明。二者可以不同，如方法声明为T，列表声明为V，那么待接收的实参类型可以不一致。声明为一致，
    表明二者待接收实参类型是同一个
    Class<T> cls 如果不指定泛型Class cls也不会报错，但是cls的getInstance就是一个Object对象，而方法指定返回的必须是某个传给<T>的具体
    的类对象，那么就还需要一个强转，因此直接在参数中统一泛型对象即可。可以确保传给Class的对象类型和方法接收的返回类型是一致的
     */

    public <T> List<T> getAllQueries(Class<T> cls, String sql, Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> query_all= new ArrayList<>();

        try {
            conn = JDBCUtil.getConnector();
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
            System.out.println(query_all.toString());
            return query_all;
        }catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e){
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeResource(conn,ps,rs);
        }
        return null;
    }
}
