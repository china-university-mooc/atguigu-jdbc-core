package com.itutry.jdbc.iterate1.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScalarResultSetHandler implements ResultSetHandler<Object> {

  @Override
  public Object handle(ResultSet rs) throws SQLException {
    if (rs.next()) {
      return rs.getObject(1);
    }
    return null;
  }
}
