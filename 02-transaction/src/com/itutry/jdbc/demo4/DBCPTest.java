package com.itutry.jdbc.demo4;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {

  // 方式一
  @Test
  public void test1() throws SQLException {
    BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql:///test");
    dataSource.setUsername("root");
    dataSource.setPassword("12345678");

    dataSource.setInitialSize(10);
    dataSource.setMaxActive(10);

    Connection conn = dataSource.getConnection();
    System.out.println(conn);
  }

  // 方式二
  @Test
  public void test2() throws Exception {
    Properties pros = new Properties();
    try (InputStream is = ClassLoader.getSystemClassLoader()
        .getResourceAsStream("dbcp.properties")) {
      pros.load(is);
    }

    DataSource dataSource = BasicDataSourceFactory.createDataSource(pros);

    Connection conn = dataSource.getConnection();
    System.out.println(conn);
  }
}
