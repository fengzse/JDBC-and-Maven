package com.JDBCPractices;

import static org.junit.jupiter.api.Assertions.*;
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
}
