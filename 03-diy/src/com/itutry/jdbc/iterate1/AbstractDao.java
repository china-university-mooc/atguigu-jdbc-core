package com.itutry.jdbc.iterate1;

import com.itutry.jdbc.iterate1.annotaion.Column;
import com.itutry.jdbc.iterate1.annotaion.Table;
import com.itutry.jdbc.iterate1.util.BeanListResultSetHandler;
import com.itutry.jdbc.iterate1.util.BeanResultSetHandler;
import com.itutry.jdbc.iterate1.util.JdbcUtils;
import com.itutry.jdbc.iterate1.util.QueryUtils;
import com.itutry.jdbc.iterate1.util.ScalarResultSetHandler;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 封装了针对数据表的通用操作
 */
public abstract class AbstractDao<T> implements Dao<T> {

  private static final String ID = "id";
  private final Class<T> type;

  public AbstractDao() {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
    type = (Class<T>) actualTypeArguments[0];
  }

  public int insert(Connection conn, T obj) {
    Field[] fields = type.getDeclaredFields();
    String insertClause = Arrays.stream(fields)
        .map(this::getColumnName)
        .collect(Collectors.joining(", "));
    String placeholderStr = Arrays.stream(fields)
        .map(field -> "?")
        .collect(Collectors.joining(","));
    Object[] values = getValues(obj, Arrays.asList(fields));

    String template = "insert into %s(%s) values (%s)";
    String sql = String.format(template, getTableName(type), insertClause, placeholderStr);
    System.out.println(sql);
    return update(conn, sql, values);
  }

  public void deleteById(Connection conn, Object id) {
    String template = "delete from %s where id = ?";
    String sql = String.format(template, getTableName(type));
    System.out.println(sql);
    update(conn, sql, id);
  }

  public void update(Connection conn, T obj) {
    String setClause = getOtherFields(type).stream()
        .map(field -> getColumnName(field) + " = ?")
        .collect(Collectors.joining(", "));

    List<Field> fields = getOtherFields(type);
    fields.add(getIdField(type));
    Object[] values = getValues(obj, fields);

    String template = "update %s set %s where id = ?";
    String sql = String.format(template, getTableName(type), setClause);
    System.out.println(sql);
    update(conn, sql, values);
  }

  public T getById(Connection conn, Object id) {
    String template = "select %s from %s where id = ?";
    String sql = String.format(template, getSelectionStr(type), getTableName(type));
    System.out.println(sql);
    return getInstance(conn, sql, id);
  }

  public List<T> getAll(Connection conn) {
    String template = "select %s from %s";
    String sql = String.format(template, getSelectionStr(type), getTableName(type));
    System.out.println(sql);
    return getForList(conn, sql);
  }

  public Long count(Connection conn) {
    String template = "select count(*) from %s";
    String sql = String.format(template, getTableName(type));
    System.out.println(sql);
    return getValue(conn, sql);
  }

  private Object[] getValues(Object obj, List<Field> fields) {
    return fields.stream()
        .map(f -> getValue(obj, f))
        .toArray(Object[]::new);
  }

  private Object getValue(Object obj, Field field) {
    Object value = null;
    try {
      field.setAccessible(true);
      value = field.get(obj);
    } catch (IllegalAccessException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return value;
  }

  private Field getIdField(Class<?> type) {
    try {
      return type.getDeclaredField(ID);
    } catch (NoSuchFieldException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return null;
  }

  private List<Field> getOtherFields(Class<?> type) {
    return Arrays.stream(type.getDeclaredFields())
        .filter(field -> !"id".equalsIgnoreCase(field.getName()))
        .collect(Collectors.toList());
  }

  private String getSelectionStr(Class type) {
    return Arrays.stream(type.getDeclaredFields())
        .map(field -> getColumnName(field) + " " + getColumnLabel(field))
        .collect(Collectors.joining(", "));
  }

  private String getTableName(Class type) {
    return Arrays.stream(type.getAnnotations())
        .filter(a -> a instanceof Table)
        .findFirst()
        .map(a -> ((Table) a).value())
        .orElse(type.getSimpleName());
  }

  private String getColumnName(Field field) {
    return Arrays.stream(field.getAnnotations())
        .filter(a -> a instanceof Column)
        .map(a -> ((Column) a).value())
        .findFirst()
        .orElse(field.getName());
  }

  public String getColumnLabel(Field field) {
    return field.getName();
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
