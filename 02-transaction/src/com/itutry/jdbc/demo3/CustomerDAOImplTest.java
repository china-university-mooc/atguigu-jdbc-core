package com.itutry.jdbc.demo2;

import com.itutry.jdbc.bean.Customer;
import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import org.junit.Test;

public class CustomerDAOImplTest {

  private CustomerDAOImpl dao = new CustomerDAOImpl();

  @Test
  public void insert() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();
      Customer cust = new Customer(1, "于小飞", "xfyu@126.com", new Date(1234567890L));
      dao.insert(conn, cust);
      System.out.println("添加成功");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void deleteById() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();

      dao.deleteById(conn, 13);
      System.out.println("删除成功");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void update() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();
      Customer cust = new Customer(18, "贝多芬", "beiduofen@126.com", new Date(1234567890L));
      dao.update(conn, cust);
      System.out.println("");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void getById() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();

      Customer cust = dao.getById(conn, 19);
      System.out.println(cust);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void getAll() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();
      List<Customer> list = dao.getAll(conn);
      list.forEach(System.out::println);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void count() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();
      Long count = dao.count(conn);
      System.out.println(count);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void getMaxBirth() {
    Connection conn = null;
    try {
      conn = JDBCUtils.getConnection();
      Date maxBirth = dao.getMaxBirth(conn);
      System.out.println(maxBirth);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, null);
    }
  }
}
