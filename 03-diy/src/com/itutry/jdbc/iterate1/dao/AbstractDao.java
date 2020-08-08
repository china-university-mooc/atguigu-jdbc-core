package com.itutry.jdbc.iterate1.dao;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;
import com.itutry.jdbc.iterate1.dao.sql.CountStatement;
import com.itutry.jdbc.iterate1.dao.sql.DeleteByIdStatement;
import com.itutry.jdbc.iterate1.dao.sql.GetAllStatement;
import com.itutry.jdbc.iterate1.dao.sql.GetByIdStatement;
import com.itutry.jdbc.iterate1.dao.sql.InsertStatement;
import com.itutry.jdbc.iterate1.dao.sql.UpdateStatement;
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
public abstract class AbstractDao<T> implements Dao<T> {

  private final Class<T> type;
  private final TableMeta table;

  public AbstractDao() {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
    type = (Class<T>) actualTypeArguments[0];
    table = new TableMeta(type);
  }

  public int insert(Connection conn, T obj) {
    InsertStatement stat = new InsertStatement(table, obj);
    System.out.println(stat);
    return update(conn, stat.getSql(), stat.getParams());
  }

  public int update(Connection conn, T obj) {
    UpdateStatement stat = new UpdateStatement(table, obj);
    System.out.println(stat.getSql());
    return update(conn, stat.getSql(), stat.getParams());
  }

  public int deleteById(Connection conn, Object id) {
    DeleteByIdStatement stat = new DeleteByIdStatement(table, id);
    System.out.println(stat.getSql());
    return update(conn, stat.getSql(), stat.getParams());
  }

  public T getById(Connection conn, Object id) {
    GetByIdStatement stat = new GetByIdStatement(table, id);
    System.out.println(stat.getSql());
    return queryBean(conn, stat.getSql(), stat.getParams());
  }

  public List<T> getAll(Connection conn) {
    GetAllStatement stat = new GetAllStatement(table);
    System.out.println(stat.getSql());
    return queryBeanList(conn, stat.getSql(), stat.getParams());
  }

  public Long count(Connection conn) {
    CountStatement stat = new CountStatement(table);
    System.out.println(stat.getSql());
    return queryValue(conn, stat.getSql(), stat.getParams());
  }

  protected int update(Connection conn, String sql, Object... args) {
    try {
      return QueryUtils.update(conn, sql, args);
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return 0;
  }

  protected T queryBean(Connection conn, String sql, Object... args) {
    try {
      BeanResultSetHandler<T> handler = new BeanResultSetHandler<>(type);
      return QueryUtils.query(conn, handler, sql, args);
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return null;
  }

  protected List<T> queryBeanList(Connection conn, String sql, Object... args) {
    try {
      BeanListResultSetHandler<T> handler = new BeanListResultSetHandler<>(type);
      return QueryUtils.query(conn, handler, sql, args);
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return Collections.emptyList();
  }

  protected <E> E queryValue(Connection conn, String sql, Object... args) {
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
