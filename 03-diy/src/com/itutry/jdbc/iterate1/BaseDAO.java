package com.itutry.jdbc.iterate1;

import com.itutry.jdbc.iterate1.util.QueryUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

/**
 * 封装了针对数据表的通用操作
 */
public abstract class BaseDAO<T> {

  private final Class<T> type;

  public BaseDAO() {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    // 获取父类的类型参数
    Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
    type = (Class<T>) actualTypeArguments[0];
  }

  protected int update(Connection conn, String sql, Object... args) {
    return QueryUtils.update(conn, sql, args);
  }

  protected T getInstance(Connection conn, String sql, Object... args) {
    return QueryUtils.queryBean(conn, type, sql, args);
  }

  protected List<T> getForList(Connection conn, String sql, Object... args) {
    return QueryUtils.queryList(conn, type, sql, args);
  }

  protected <E> E getValue(Connection conn, String sql, Object... args) {
    return QueryUtils.queryScalar(conn, sql, args);
  }
}
