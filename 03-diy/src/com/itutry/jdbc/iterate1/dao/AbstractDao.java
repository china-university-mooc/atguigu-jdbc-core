package com.itutry.jdbc.iterate1.dao;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;
import com.itutry.jdbc.iterate1.dao.sql.CountStatement;
import com.itutry.jdbc.iterate1.dao.sql.DeleteByIdStatement;
import com.itutry.jdbc.iterate1.dao.sql.GetAllStatement;
import com.itutry.jdbc.iterate1.dao.sql.GetByIdStatement;
import com.itutry.jdbc.iterate1.dao.sql.InsertStatement;
import com.itutry.jdbc.iterate1.dao.sql.SqlStatement;
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
    return update(conn, new InsertStatement(table, obj));
  }

  public int update(Connection conn, T obj) {
    return update(conn, new UpdateStatement(table, obj));
  }

  public int deleteById(Connection conn, Object id) {
    return update(conn, new DeleteByIdStatement(table, id));
  }

  public T getById(Connection conn, Object id) {
    return queryBean(conn, new GetByIdStatement(table, id));
  }

  public List<T> getAll(Connection conn) {
    return queryBeanList(conn, new GetAllStatement(table));
  }

  public Long count(Connection conn) {
    return queryValue(conn, new CountStatement(table));
  }

  protected int update(Connection conn, SqlStatement stat) {
    try {
      System.out.println(stat);
      return QueryUtils.update(conn, stat.getSql(), stat.getParams());
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return 0;
  }

  protected T queryBean(Connection conn, SqlStatement stat) {
    try {
      System.out.println(stat.getSql());
      BeanResultSetHandler<T> handler = new BeanResultSetHandler<>(type);
      return QueryUtils.query(conn, handler, stat.getSql(), stat.getParams());
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return null;
  }

  protected List<T> queryBeanList(Connection conn, SqlStatement stat) {
    try {
      System.out.println(stat.getSql());
      BeanListResultSetHandler<T> handler = new BeanListResultSetHandler<>(type);
      return QueryUtils.query(conn, handler, stat.getSql(), stat.getParams());
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return Collections.emptyList();
  }

  protected <E> E queryValue(Connection conn, SqlStatement stat) {
    System.out.println(stat.getSql());
    return queryValue(conn, stat.getSql(), stat.getParams());
  }

  protected <E> E queryValue(Connection conn, String sql, Object... params) {
    try {
      ScalarResultSetHandler handler = new ScalarResultSetHandler();
      @SuppressWarnings("unchecked")
      E result = (E) QueryUtils.query(conn, handler, sql, params);
      return result;
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return null;
  }
}
