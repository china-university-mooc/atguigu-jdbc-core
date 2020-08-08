package com.itutry.jdbc.iterate1.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;

public class JdbcUtils {


  private static DataSource dataSource;

  static {
    try {
      Properties pros = new Properties();
      try (InputStream is = ClassLoader.getSystemClassLoader()
          .getResourceAsStream("druid.properties")) {
        pros.load(is);
      }

      dataSource = DruidDataSourceFactory.createDataSource(pros);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public static void close(Connection conn) throws SQLException {
    if (conn != null) {
      conn.close();
    }
  }

  public static void close(Statement ps) throws SQLException {
    if (ps != null) {
      ps.close();
    }
  }

  public static void close(ResultSet rs) throws SQLException {
    if (rs != null) {
      rs.close();
    }
  }

  public static void closeQuietly(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        quietlyHandleSQLException(e);
      }
    }
  }

  public static void closeQuietly(Statement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        quietlyHandleSQLException(e);
      }
    }
  }

  public static void closeQuietly(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        quietlyHandleSQLException(e);
      }
    }
  }

  public static void closeQuietly(Connection conn, Statement ps, ResultSet rs) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        quietlyHandleSQLException(e);
      }
    }
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        quietlyHandleSQLException(e);
      }
    }
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        quietlyHandleSQLException(e);
      }
    }
  }

  public static void quietlyHandleSQLException(SQLException e) {
    throw new RuntimeException(e);
  }
}
