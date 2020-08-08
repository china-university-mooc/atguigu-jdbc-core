package com.itutry.jdbc.iterate1.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeanListResultSetHandler<T> implements ResultSetHandler<List<T>> {

  private final Class<T> type;
  private final RowProcessor rowProcessor = new RowProcessor();

  public BeanListResultSetHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public List<T> handle(ResultSet rs) throws SQLException {
    List<T> list = new ArrayList<>();
    while (rs.next()) {
      list.add(rowProcessor.toBean(rs, type));
    }
    return list;
  }
}
