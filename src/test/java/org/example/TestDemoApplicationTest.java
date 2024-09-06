package org.example;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@SpringBootTest
public class TestDemoApplicationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void demo() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println("connection = " + connection);
        System.out.println("connection.getClass().getName() = " + connection.getClass().getName());
        connection.close();
    }

}