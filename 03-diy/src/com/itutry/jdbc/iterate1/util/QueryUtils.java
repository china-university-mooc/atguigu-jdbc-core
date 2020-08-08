package com.itutry.jdbc.iterate1.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class QueryUtils {

  public static int update(Connection conn, String sql, Object... args) {
    PreparedStatement ps = null;
    try {
      // 1. 预编译SQL语句，返回PreparedStatement实例
      ps = conn.prepareStatement(sql);
      // 2. 填充占位符
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      // 3. 执行SQL
      return ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 4. 关闭资源
      JdbcUtils.closeResource(null, ps);
    }
    return 0;
  }

  public static <T> List<T> queryList(Connection conn, Class<T> clazz, String sql, Object... args) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 准备PreparedStatement
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      // 执行并返回结果
      rs = ps.executeQuery();

      // 处理结果
      // 获取结果集元数据
      ResultSetMetaData rsMetaData = rs.getMetaData();
      // 获取列数
      int columnCount = rsMetaData.getColumnCount();

      List<T> list = new ArrayList<>();
      while (rs.next()) {
        T obj = clazz.newInstance();

        for (int i = 0; i < columnCount; i++) {
          // 获取列值
          Object columnValue = rs.getObject(i + 1);
          // 获取列的别名
          String columnLabel = rsMetaData.getColumnLabel(i + 1);

          // 通过反射设置对象属性
          Field field = clazz.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, columnValue);
        }

        list.add(obj);
      }
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 关闭资源
      JdbcUtils.closeResource(conn, ps, rs);
    }

    return Collections.emptyList();
  }

  public static <T> T queryBean(Connection conn, Class<T> clazz, String sql, Object... args) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 2. 准备PreparedStatement
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      // 3. 执行并返回结果集
      rs = ps.executeQuery();

      // 4. 处理结果集
      // 4.1 获取结果集元数据
      ResultSetMetaData rsMetaData = rs.getMetaData();
      // 4.1 获取列数
      int columnCount = rsMetaData.getColumnCount();

      if (rs.next()) {
        T obj = clazz.newInstance();

        // 4.2 遍历一行数据的每一列
        for (int i = 0; i < columnCount; i++) {
          // 4.2.1 获取列值
          Object columnValue = rs.getObject(i + 1);
          // 4.2.2 获取列的别名
          String columnLabel = rsMetaData.getColumnLabel(i + 1);

          // 4.2.3 通过反射设置对象属性
          Field field = clazz.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, columnValue);
        }

        return obj;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 5. 关闭资源
      JdbcUtils.closeResource(null, ps, rs);
    }

    return null;
  }

  private static <T> T query(Function<ResultSet, T> rsHandler, T defaultValue, String sql,
      Object... args) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 获取连接
      conn = JdbcUtils.getConnection();

      // 准备PreparedStatement
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      // 执行并返回结果
      rs = ps.executeQuery();
      // 处理结果集
      return rsHandler.apply(rs);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 关闭资源
      JdbcUtils.closeResource(conn, ps, rs);
    }

    return defaultValue;
  }

  public static  <E> E queryScalar(Connection conn, String sql, Object... args) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      rs = ps.executeQuery();
      if (rs.next()) {
        return (E) rs.getObject(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeResource(null, ps, rs);
    }

    return null;
  }
}
