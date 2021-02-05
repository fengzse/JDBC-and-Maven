package com.JDBCPractices;

import static org.junit.jupiter.api.Assertions.*;

import com.JDBClasses.CustomerOrders;
import com.JDBClasses.Customers;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestFirstJDBConnection {

    @Test
    void TestConn() {
        PreStatementPrc pstm=new PreStatementPrc();
        pstm.insertTest();
    }

    @Test
    void TestUpdate(){
        PreStatementPrc pstm = new PreStatementPrc();
        pstm.updateTest();
    }

    @Test
    void TestCommonUpdate() throws ParseException {
        String sql="insert into customers(name, email, birth) values(?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1983-06-28");
        java.sql.Date jsd=new java.sql.Date(date.getTime());
        PreStatementPrc pstm = new PreStatementPrc();
        pstm.commonUpdate(sql, "刘涛","azhu@gmail.com",jsd);
        assertFalse(pstm.getForTest());
    }

    @Test
    void TestQueryCustomer(){
        String sql = "select id, name, email,birth from customers where name=?";
        PreStatementPrc pstm = new PreStatementPrc();
        pstm.getCustomer(sql,"迪丽热巴");
        assertTrue(pstm.getForTest());
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
}
