package com.itutry.jdbc.iterate1;

import com.itutry.jdbc.iterate1.annotaion.Table;
import com.itutry.jdbc.iterate1.bean.Order;
import com.itutry.jdbc.iterate1.util.JdbcUtils;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderDaoImplTest {

  private OrderDaoImpl dao = new OrderDaoImpl();
  private Connection conn;

  @Before
  public void init() {
    try {
      conn = JdbcUtils.getConnection();
    } catch (SQLException e) {
      JdbcUtils.quietlyHandleException(e);
    }
  }

  @After
  public void destroy() {
    JdbcUtils.closeQuietly(conn);
  }

  @Test
  public void test1() {
    List<Order> list = dao.getAll(conn);
    list.forEach(System.out::println);
  }

  @Test
  public void test2() {
    Order order = dao.getById(conn, 1);
    System.out.println(order);
  }

  @Test
  public void test3() {
    Order order = new Order(null, "HH", new Date(1234567890L));
    int rows = dao.insert(conn, order);
    System.out.println(rows);
  }

  @Test
  public void test4() {
    Order order = new Order(6, "JJ", new Date(1234567890L));
    int rows = dao.update(conn, order);
    System.out.println(rows);
  }

  @Test
  public void test5() {
    int rows = dao.deleteById(conn, 7);
    System.out.println(rows);
  }

  @Test
  public void test6() {
    Long count = dao.count(conn);
    System.out.println(count);
  }
}
