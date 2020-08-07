package com.itutry.jdbc.demo2;

import com.itutry.jdbc.bean.Customer;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public interface CustomerDAO {

  void insert(Connection conn, Customer cust);

  void deleteById(Connection conn, int id);

  void update(Connection conn, Customer cust);

  Customer getById(Connection conn, int id);

  List<Customer> getAll(Connection conn);

  Long count(Connection conn);

  Date getMaxBirth(Connection conn);
}
