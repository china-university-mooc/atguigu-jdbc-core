package com.itutry.jdbc.iterate1.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryUtils {

  public static int update(Connection conn, String sql, Object... params) throws SQLException {
    PreparedStatement ps = null;
    int rows;
    try {
      ps = conn.prepareStatement(sql);
      fillStatement(ps, params);
      rows = ps.executeUpdate();
    } finally {
      JdbcUtils.close(ps);
    }
    return rows;
  }

  public static <T> T query(Connection conn, ResultSetHandler<T> rsHandler, String sql,
      Object... params) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    T result;
    try {
      ps = conn.prepareStatement(sql);
      fillStatement(ps, params);
      rs = ps.executeQuery();
      result = rsHandler.handle(rs);
    } finally {
      try {
        JdbcUtils.close(rs);
      } finally {
        JdbcUtils.close(ps);
      }
    }

    return result;
  }

  private static void fillStatement(PreparedStatement ps, Object[] params) throws SQLException {
    for (int i = 0; i < params.length; i++) {
      ps.setObject(i + 1, params[i]);
    }
  }
}
