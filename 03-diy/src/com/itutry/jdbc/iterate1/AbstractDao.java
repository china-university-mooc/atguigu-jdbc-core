package com.itutry.jdbc.iterate1;

import com.itutry.jdbc.iterate1.util.BeanListResultSetHandler;
import com.itutry.jdbc.iterate1.util.BeanResultSetHandler;
import com.itutry.jdbc.iterate1.util.ColumnMeta;
import com.itutry.jdbc.iterate1.util.JdbcUtils;
import com.itutry.jdbc.iterate1.util.QueryUtils;
import com.itutry.jdbc.iterate1.util.ScalarResultSetHandler;
import com.itutry.jdbc.iterate1.util.TableMeta;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 封装了针对数据表的通用操作
 */
public abstract class AbstractDao<T> implements Dao<T> {

  private static final String ID = "id";
  public static final String INSERT_TEMPLATE = "insert into `%s`(%s) values (%s)";
  public static final String DELETE_BY_ID_TEMPLATE = "delete from `%s` where %s";
  public static final String UPDATE_BY_ID_TEMPLATE = "update `%s` set %s where %s";
  public static final String GET_BY_ID_TEMPLATE = "select %s from `%s` where %s";
  public static final String GET_ALL_TEMPLATE = "select %s from `%s`";
  public static final String COUNT_TEMPLATE = "select count(*) from `%s`";

  private final Class<T> type;
  private final TableMeta table;

  public AbstractDao() {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
    type = (Class<T>) actualTypeArguments[0];
    table = new TableMeta(type);
  }

  public int insert(Connection conn, T obj) {
    String insertClause = table.getColumns().stream()
        .map(ColumnMeta::getName)
        .collect(Collectors.joining(", "));
    String placeholderStr = table.getColumns().stream()
        .map(field -> "?")
        .collect(Collectors.joining(","));
    Object[] values = table.getValues(obj).toArray();

    String sql = String.format(INSERT_TEMPLATE, table.getName(), insertClause, placeholderStr);
    System.out.println(sql);
    return update(conn, sql, values);
  }

  public int update(Connection conn, T obj) {
    String setClause = table.getPlainColumns().stream()
        .map(c -> c.getName() + " = ?")
        .collect(Collectors.joining(", "));
    String whereClause = table.getIdColumn().getName() + " = ?";

    List<Object> values = table.getPlainValues(obj);
    values.add(table.getIdValue(obj));

    String sql = String.format(UPDATE_BY_ID_TEMPLATE, table.getName(), setClause, whereClause);
    System.out.println(sql);
    return update(conn, sql, values.toArray());
  }

  public int deleteById(Connection conn, Object id) {
    String whereClause = table.getIdColumn().getName() + " = ?";
    String sql = String.format(DELETE_BY_ID_TEMPLATE, table.getName(), whereClause);
    System.out.println(sql);
    return update(conn, sql, id);
  }

  public T getById(Connection conn, Object id) {
    String whereClause = table.getIdColumn().getName() + " = ?";
    String sql = String
        .format(GET_BY_ID_TEMPLATE, getSelectClause(), table.getName(), whereClause);
    System.out.println(sql);
    return queryBean(conn, sql, id);
  }

  public List<T> getAll(Connection conn) {
    String sql = String.format(GET_ALL_TEMPLATE, getSelectClause(), table.getName());
    System.out.println(sql);
    return queryBeanList(conn, sql);
  }

  public Long count(Connection conn) {
    String sql = String.format(COUNT_TEMPLATE, table.getName());
    System.out.println(sql);
    return queryValue(conn, sql);
  }

  private String getSelectClause() {
    return table.getColumns().stream()
        .map(c -> c.getName() + " " + c.getLabel())
        .collect(Collectors.joining(", "));
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
