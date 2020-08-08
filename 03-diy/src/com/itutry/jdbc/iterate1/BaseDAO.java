package com.itutry.jdbc.iterate1;

import com.itutry.jdbc.iterate1.util.BeanListResultSetHandler;
import com.itutry.jdbc.iterate1.util.BeanResultSetHandler;
import com.itutry.jdbc.iterate1.util.JdbcUtils;
import com.itutry.jdbc.iterate1.util.QueryUtils;
import com.itutry.jdbc.iterate1.util.ScalarResultSetHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * 封装了针对数据表的通用操作
 */
public abstract class BaseDAO<T> {

  private final Class<T> type;

  public BaseDAO() {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
    type = (Class<T>) actualTypeArguments[0];
  }

  protected int update(Connection conn, String sql, Object... args) {
    try {
      return QueryUtils.update(conn, sql, args);
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return 0;
  }

  protected T getInstance(Connection conn, String sql, Object... args) {
    try {
      BeanResultSetHandler<T> handler = new BeanResultSetHandler<>(type);
      return QueryUtils.query(conn, handler, sql, args);
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return null;
  }

  protected List<T> getForList(Connection conn, String sql, Object... args) {
    try {
      BeanListResultSetHandler<T> handler = new BeanListResultSetHandler<>(type);
      return QueryUtils.query(conn, handler, sql, args);
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return Collections.emptyList();
  }

  protected <E> E getValue(Connection conn, String sql, Object... args) {
    try {
      ScalarResultSetHandler handler = new ScalarResultSetHandler();
      @SuppressWarnings("unchecked")
      E result = (E) QueryUtils.query(conn, handler, sql, args);
      return result;
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return null;
  }
}
