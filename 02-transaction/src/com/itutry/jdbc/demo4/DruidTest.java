package com.itutry.jdbc.demo4;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.Test;

public class DruidTest {

  // 方式一
  @Test
  public void test1() throws SQLException {
    DruidDataSource dataSource = new DruidDataSource();

    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql:///test");
    dataSource.setUsername("root");
    dataSource.setPassword("12345678");

    dataSource.setInitialSize(10);
    dataSource.setMaxActive(10);

    Connection conn = dataSource.getConnection();
    System.out.println(conn);
  }

  @Test
  public void test2() throws Exception {
    Properties pros = new Properties();
    try (InputStream is = ClassLoader.getSystemClassLoader()
        .getResourceAsStream("druid.properties")) {
      pros.load(is);
    }

    DataSource dataSource = DruidDataSourceFactory.createDataSource(pros);

    Connection conn = dataSource.getConnection();
    System.out.println(conn);
  }
}
