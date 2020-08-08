package com.itutry.jdbc.iterate1;

import java.sql.Connection;
import java.sql.Date;

public class CustomerDaoImpl extends AbstractDao<Customer> implements CustomerDao {

  @Override
  public Date getMaxBirth(Connection conn) {
    String sql = "select max(birth) from customers";
    return getValue(conn, sql);
  }
}
