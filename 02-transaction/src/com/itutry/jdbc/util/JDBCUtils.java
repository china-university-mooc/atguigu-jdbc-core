package com.itutry.jdbc.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

public class JDBCUtils {

  // 数据库连接池只需提供一个即可
  private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

  public static Connection getConnection1() throws SQLException {
    return cpds.getConnection();
  }

  private static DataSource dataSource;

  static {
    try {
      Properties pros = new Properties();
      try (InputStream is = ClassLoader.getSystemClassLoader()
          .getResourceAsStream("dbcp.properties")) {
        pros.load(is);
      }

      dataSource = BasicDataSourceFactory.createDataSource(pros);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection2() throws SQLException {
    return dataSource.getConnection();
  }

  private static DataSource druid;

  static {
    try {
      Properties pros = new Properties();
      try (InputStream is = ClassLoader.getSystemClassLoader()
          .getResourceAsStream("druid.properties")) {
        pros.load(is);
      }

      druid = DruidDataSourceFactory.createDataSource(pros);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection3() throws SQLException {
    return druid.getConnection();
  }

  public static Connection getConnection() throws Exception {
    // 1.读取配置文件
    Properties pros = new Properties();
    try (InputStream is = ClassLoader.getSystemClassLoader()
        .getResourceAsStream("jdbc.properties")) {
      pros.load(is);
    }

    String user = pros.getProperty("user");
    String password = pros.getProperty("password");
    String url = pros.getProperty("url");
    String driverClass = pros.getProperty("driverClass");
    // 2. 加载驱动
    Class.forName(driverClass);

    // 3. 获取连接
    Connection conn = DriverManager.getConnection(url, user, password);

    return conn;
  }

  public static void closeResource(Connection conn, Statement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static void closeResource1(Connection conn, Statement ps, ResultSet rs) {
//    try {
//      DbUtils.close(conn);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    try {
//      DbUtils.close(ps);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    try {
//      DbUtils.close(rs);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }

//    DbUtils.closeQuietly(conn);
//    DbUtils.closeQuietly(ps);
//    DbUtils.closeQuietly(rs);

    DbUtils.closeQuietly(conn, ps, rs);
  }
}
