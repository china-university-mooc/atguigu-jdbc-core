package com.itutry.jdbc.iterate1;

import com.itutry.jdbc.iterate1.bean.Customer;
import com.itutry.jdbc.iterate1.util.JdbcUtils;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import org.junit.Test;

public class CustomerDaoImplTest {

  private CustomerDaoImpl dao = new CustomerDaoImpl();

  @Test
  public void insert() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();
      Customer cust = new Customer(null, "于小飞", "xfyu@126.com", new Date(1234567890L));
      dao.insert(conn, cust);
      System.out.println("添加成功");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }

  @Test
  public void deleteById() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();

      dao.deleteById(conn, 29);
      System.out.println("删除成功");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }

  @Test
  public void update() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();
      Customer cust = new Customer(18, "莫扎特", "beiduofen@126.com", new Date(1234567890L));
      dao.update(conn, cust);
      System.out.println("");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }

  @Test
  public void getById() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();

      Customer cust = dao.getById(conn, 19);
      System.out.println(cust);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }

  @Test
  public void getAll() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();
      List<Customer> list = dao.getAll(conn);
      list.forEach(System.out::println);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }

  @Test
  public void count() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();
      Long count = dao.count(conn);
      System.out.println(count);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }

  @Test
  public void getMaxBirth() {
    Connection conn = null;
    try {
      conn = JdbcUtils.getConnection();
      Date maxBirth = dao.getMaxBirth(conn);
      System.out.println(maxBirth);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeQuietly(conn);
    }
  }
}
