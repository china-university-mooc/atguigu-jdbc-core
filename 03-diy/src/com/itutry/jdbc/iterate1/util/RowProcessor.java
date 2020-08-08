package com.itutry.jdbc.iterate1.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RowProcessor {

  public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
    T bean = null;
    try {
      bean = type.newInstance();

      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();
      for (int i = 0; i < columnCount; i++) {
        Object columnValue = rs.getObject(i + 1);
        String columnLabel = metaData.getColumnLabel(i + 1);

        Field field = type.getDeclaredField(columnLabel);
        field.setAccessible(true);
        field.set(bean, columnValue);
      }

    } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
      JdbcUtils.quietlyHandleException(e);
    }

    return bean;
  }
}
