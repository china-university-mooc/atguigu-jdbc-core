package com.itutry.jdbc.demo3;

import com.itutry.jdbc.bean.Customer;
import com.itutry.jdbc.util.JDBCUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.junit.Test;

public class QueryForCustomerTest {

  @Test
  public void test2() {
//    String sql = "SELECT id, name, email, birth FROM customers where id = ?";
//    Customer customer = queryForCustomer(sql, 13);
//    System.out.println(customer);

    String sql = "SELECT name, email FROM customers where name = ?";
    Customer customer = queryForCustomer(sql, "周杰伦");
    System.out.println(customer);
  }

  public Customer queryForCustomer(String sql, Object... params) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 1. 获取数据库连接
      conn = JDBCUtils.getConnection();

      // 2. 预编译SQL语句，返回PreparedStatement实例
      ps = conn.prepareStatement(sql);
      // 3. 填充占位符
      for (int i = 0; i < params.length; i++) {
        ps.setObject(i + 1, params[i]);
      }

      // 4. 执行SQL, 并返回结果集
      rs = ps.executeQuery();

      // 获取结果集的元数据：ResultSetMetaData
      ResultSetMetaData rsmd = rs.getMetaData();
      // 通过ResultSetMetaData获取结果集的列数
      int columnCount = rsmd.getColumnCount();

      // 5. 处理结果集
      if (rs.next()) {
        Customer cust = new Customer();
        // 处理结果集一行数据中的每一列
        for (int i = 0; i < columnCount; i++) {
          // 获取列值
          Object columnValue = rs.getObject(i + 1);

          // 获取列名
          String columnName = rsmd.getColumnName(i + 1);

          // 通过反射设置对象的对应字段
          Field field = Customer.class.getDeclaredField(columnName);
          field.setAccessible(true);
          field.set(cust, columnValue);
        }
        return cust;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 6. 关闭资源
      JDBCUtils.closeResource(conn, ps, rs);
    }
    return null;
  }

  @Test
  public void test() {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      // 1. 获取数据库连接
      conn = JDBCUtils.getConnection();

      // 2. 预编译SQL语句，返回PreparedStatement实例
      String sql = "SELECT id, name, email, birth FROM customers where id = ?";
      ps = conn.prepareStatement(sql);
      // 3. 填充占位符
      ps.setInt(1, 1);

      // 4. 执行SQL, 并返回结果集
      rs = ps.executeQuery();

      // 5. 处理结果集
      if (rs.next()) {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String email = rs.getString(3);
        Date birth = rs.getDate(4);
        // 方式一：直接显示
//        System.out.println("id = " + id + ", name = " + name + ", email = " + email + ", birth = " + birth);
        // 方式二：封装成数组
  //      Object[] data = new Object[]{id, name, email, birth};
        // 方式三：封装成对象
        Customer customer = new Customer(id, name, email, birth);
        System.out.println(customer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 6. 关闭资源
      JDBCUtils.closeResource(conn, ps, rs);
    }
  }
}
