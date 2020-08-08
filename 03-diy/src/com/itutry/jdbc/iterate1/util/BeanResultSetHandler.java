package com.itutry.jdbc.iterate1.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanResultSetHandler<T> implements ResultSetHandler<T> {

  private final Class<T> type;
  private final RowProcessor rowProcessor = new RowProcessor();

  public BeanResultSetHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public T handle(ResultSet rs) throws SQLException {
    return rs.next() ? rowProcessor.toBean(rs, type) : null;
  }
}
