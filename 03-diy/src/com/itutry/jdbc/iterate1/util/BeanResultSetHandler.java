package com.itutry.jdbc.iterate1.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

class BeanResultSetHandler<T> implements ResultSetHandler<T> {

  private final Class<T> type;

  public BeanResultSetHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public T handle(ResultSet rs) throws SQLException {

    try {
      if (rs.next()) {
        T obj = type.newInstance();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
          Object value = rs.getObject(i + 1);
          String columnLabel = rsmd.getColumnLabel(i + 1);

          Field field = type.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, value);
        }

        return obj;
      }
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }
}
