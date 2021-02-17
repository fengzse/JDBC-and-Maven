package com.JDBCPractices;

import static org.junit.jupiter.api.Assertions.*;

import com.JDBCUtils.JDBCUtil;
import com.JDBClasses.CustomerOrders;
import com.JDBClasses.Customers;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestFirstJDBConnection {

    @Test
    void TestConn() {
        int ins;
        PreStatementPrc pstm=new PreStatementPrc();
        ins = pstm.insertTest();
        assertEquals(1,ins);
    }

    @Test
    void TestUpdate(){
        int actual;
        PreStatementPrc pstm = new PreStatementPrc();
        actual = pstm.updateTest();
        assertEquals(1, actual);
    }

    @Test
    void TestCommonUpdate() throws ParseException {
        int actual;
        String sql="insert into customers(name, email, birth) values(?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1983-06-28");
        java.sql.Date jsd=new java.sql.Date(date.getTime());
        PreStatementPrc pstm = new PreStatementPrc();
        actual=pstm.commonUpdate(sql, "刘涛","azhu@gmail.com",jsd);
        assertEquals(1,actual);
    }

    @Test
    void TestDelete(){
        int actual;
        String sql = "delete from customers where name = ?";
        PreStatementPrc pstm = new PreStatementPrc();
        actual = pstm.commonUpdate(sql, "哪吒");
        assertEquals(1,actual);
    }

    @Test
    void TestQueryCustomer(){
        String sql = "select id, name, email,birth from customers where name=?";
        PreStatementPrc pstm = new PreStatementPrc();
        assertNotNull(pstm.getCustomer(sql,"迪丽热巴"));
    }

    @Test
    void TestGetQuery(){
        String sql = "select id, name, email,birth from customers where name=?";
        PreStatementPrc pstm = new PreStatementPrc();
        String expected = "Customers{id=8, name='陈道明', email='bdf@126.com', birth=2014-01-17}";
        String actual;
        actual = pstm.getQuery(Customers.class,sql,"陈道明").toString();
        assertEquals(expected,actual);
    }

    @Test
    void TestGetQuery_2(){
        String sql = "select order_id as orderId, order_name as orderName, " +
                "order_date as orderDate from ordering where order_id=?";
        PreStatementPrc pstm = new PreStatementPrc();
        String expected = "CustomerOrders{orderId=1, orderName='AA', orderDate=2010-03-04}";
        String actual = pstm.getQuery(CustomerOrders.class,sql,1).toString();
        assertEquals(expected,actual);

    }

    @Test
    void TestGetQuery_All(){
        String sql = "select id, name, email,birth from customers where id < 10";
        PreStatementPrc pstm = new PreStatementPrc();
        assertTrue(pstm.getAllQueries(Customers.class,sql).size()>0);
    }

    @Test
    void TestTranUpdate(){
        Connection conn = null;
        BaseDAO bdao;
        try {
            conn= JDBCUtil.getConnector();
            conn.setAutoCommit(false);
            bdao= new BaseDAO();

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            bdao.transactionUpdate(conn,sql1,"AA");

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            bdao.transactionUpdate(conn,sql2,"BB");

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally {
                JDBCUtil.closeResource(conn,null,null);
            }
        }

    }
}
