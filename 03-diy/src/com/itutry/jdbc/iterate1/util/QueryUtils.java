package com.itutry.jdbc.iterate1.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class QueryUtils {

  public static int update(Connection conn, String sql, Object... args) {
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      return ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(ps);
    }
    return 0;
  }

  public static <T> List<T> queryList(Connection conn, Class<T> type, String sql, Object... args) {
    BeanListResultSetHandler<T> handler = new BeanListResultSetHandler<>(type);
    return query(conn, handler, sql, args);
  }

  public static <T> T queryBean(Connection conn, Class<T> type, String sql, Object... args) {
    BeanResultSetHandler<T> handler = new BeanResultSetHandler<>(type);
    return query(conn, handler, sql, args);
  }

  public static <E> E queryScalar(Connection conn, String sql, Object... args) {
    ScalarResultSetHandler handler = new ScalarResultSetHandler();
    return (E) QueryUtils.query(conn, handler, sql, args);
  }

  private static <T> T query(Connection conn, ResultSetHandler<T> rsHandler, String sql,
      Object... args) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      rs = ps.executeQuery();
      return rsHandler.handle(rs);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(null, ps, rs);
    }

    return null;
  }
}
