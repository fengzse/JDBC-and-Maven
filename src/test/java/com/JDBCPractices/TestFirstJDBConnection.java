package com.JDBCPractices;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class TestFirstJDBConnection {

    @Test
    void TestConn()
            throws SQLException, IOException {
        JDBCUtil.getConnector();
    }
}
