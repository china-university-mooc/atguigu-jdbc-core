package com.itutry.jdbc.iterate1;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDaoImpl extends BaseDAO<Customer> implements CustomerDao {

  @Override
  public void insert(Connection conn, Customer cust) {
    String sql = "insert into customers(name, email, birth) values (?,?,?)";
    update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
  }

  @Override
  public void deleteById(Connection conn, int id) {
    String sql = "delete from customers where id = ?";
    update(conn, sql, id);
  }

  @Override
  public void update(Connection conn, Customer cust) {
    String sql = "update customers set name = ?, email = ?, birth = ? where id = ?";
    update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
  }

  @Override
  public Customer getById(Connection conn, int id) {
    String sql = "select id, name, email, birth from customers where id = ?";
    return getInstance(conn, sql, id);
  }

  @Override
  public List<Customer> getAll(Connection conn) {
    String sql = "select id, name, email, birth from customers";
    return getForList(conn, sql);
  }

  @Override
  public Long count(Connection conn) {
    String sql = "select count(*) from customers";
    return getValue(conn, sql);
  }

  @Override
  public Date getMaxBirth(Connection conn) {
    String sql = "select max(birth) from customers";
    return getValue(conn, sql);
  }
}
