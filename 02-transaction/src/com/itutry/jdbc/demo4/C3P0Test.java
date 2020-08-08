package com.itutry.jdbc.demo4;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class C3P0Test {

  // 方式一

  @Test
  public void test1() throws Exception {
    // 获取c3p0数据库连接池
    ComboPooledDataSource dataSource = new ComboPooledDataSource();
    dataSource.setDriverClass( "com.mysql.jdbc.Driver" );
    dataSource.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
    dataSource.setUser("root");
    dataSource.setPassword("12345678");

    // 设置池中初始的连接数
    dataSource.setInitialPoolSize(10);
    dataSource.setMaxPoolSize(10);


    Connection conn = dataSource.getConnection();
    System.out.println(conn);

    // 销毁连接池
    DataSources.destroy(dataSource);
  }

  // 方式二
  @Test
  public void test2() throws Exception {
    ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

    Connection conn = cpds.getConnection();
    System.out.println(conn);

    DataSources.destroy(cpds);
  }

  @Test
  public void test3() throws SQLException {
    ComboPooledDataSource dataSource = new ComboPooledDataSource();

    Connection conn = dataSource.getConnection();
    System.out.println(conn);
  }
}
