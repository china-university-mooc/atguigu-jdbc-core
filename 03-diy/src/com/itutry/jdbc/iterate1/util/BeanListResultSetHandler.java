package com.itutry.jdbc.iterate1.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanListResultSetHandler<T> implements ResultSetHandler<List<T>> {

  private final Class<T> type;

  public BeanListResultSetHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public List<T> handle(ResultSet rs) throws SQLException {
    try {
      ResultSetMetaData rsMetaData = rs.getMetaData();
      // 获取列数
      int columnCount = rsMetaData.getColumnCount();

      List<T> list = new ArrayList<>();
      while (rs.next()) {
        T obj = type.newInstance();

        for (int i = 0; i < columnCount; i++) {
          // 获取列值
          Object columnValue = rs.getObject(i + 1);
          // 获取列的别名
          String columnLabel = rsMetaData.getColumnLabel(i + 1);

          // 通过反射设置对象属性
          Field field = type.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, columnValue);
        }

        list.add(obj);
      }
      return list;
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }
}
