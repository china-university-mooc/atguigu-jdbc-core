package com.itutry.jdbc.demo3;

import com.itutry.jdbc.bean.Customer;
import com.itutry.jdbc.util.JDBCUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class PreparedStatementQueryTest {

  @Test
  public void test1() {
//    String sql = "select order_name orderName, order_date orderDate from `order` where order_id = ?";
//    Order order = query(Order.class, sql, 4);
//    System.out.println(order);

    String sql = "select name, email from customers where id = ?";
    Customer customer = queryForOne(Customer.class, sql, 1);
    System.out.println(customer);
  }

  @Test
  public void test2() {
//    String sql = "select name, email from customers where id < ?";
//    List<Customer> customers = queryForList(Customer.class, sql, 12);
//    customers.forEach(System.out::println);

    String sql = "select name, email from customers";
    List<Customer> customers = queryForList(Customer.class, sql);
    customers.forEach(System.out::println);
  }

  public <T> List<T> queryForList(Class<T> clazz, String sql, Object... args) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 获取连接
      conn = JDBCUtils.getConnection();

      // 准备PreparedStatement
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      // 执行并返回结果
      rs = ps.executeQuery();

      // 处理结果
      // 获取结果集元数据
      ResultSetMetaData rsMetaData = rs.getMetaData();
      // 获取列数
      int columnCount = rsMetaData.getColumnCount();

      List<T> list = new ArrayList<>();
      while (rs.next()) {
        T obj = clazz.newInstance();

        for (int i = 0; i < columnCount; i++) {
          // 获取列值
          Object columnValue = rs.getObject(i + 1);
          // 获取列的别名
          String columnLabel = rsMetaData.getColumnLabel(i + 1);

          // 通过反射设置对象属性
          Field field = clazz.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, columnValue);
        }

        list.add(obj);
      }
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 关闭资源
      JDBCUtils.closeResource(conn, ps, rs);
    }

    return Collections.emptyList();
  }

  public <T> T queryForOne(Class<T> clazz, String sql, Object... args) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 1. 获取连接
      conn = JDBCUtils.getConnection();

      // 2. 准备PreparedStatement
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      // 3. 执行并返回结果集
      rs = ps.executeQuery();

      // 4. 处理结果集
      // 4.1 获取结果集元数据
      ResultSetMetaData rsMetaData = rs.getMetaData();
      // 4.1 获取列数
      int columnCount = rsMetaData.getColumnCount();

      if (rs.next()) {
        T obj = clazz.newInstance();

        // 4.2 遍历一行数据的每一列
        for (int i = 0; i < columnCount; i++) {
          // 4.2.1 获取列值
          Object columnValue = rs.getObject(i + 1);
          // 4.2.2 获取列的别名
          String columnLabel = rsMetaData.getColumnLabel(i + 1);

          // 4.2.3 通过反射设置对象属性
          Field field = clazz.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, columnValue);
        }

        return obj;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 5. 关闭资源
      JDBCUtils.closeResource(conn, ps, rs);
    }

    return null;
  }
}
