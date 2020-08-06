package com.itutry.jdbc.demo3;

import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import org.junit.Test;

public class PreparedStatementUpdateTest {

  @Test
  public void test3() {
    String sql = "delete from customers where id = ?";
    update(sql, 3);

//    String sql = "update `order` set order_name = ? where order_id = ?";
//    update(sql, "DD", 2);
  }


  // 通用的增删改操作
  public void update(String sql, Object... params) {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      conn = JDBCUtils.getConnection();

      // 1. 预编译SQL语句，返回PreparedStatement实例
      ps = conn.prepareStatement(sql);
      // 2. 填充占位符
      for (int i = 0; i < params.length; i++) {
        ps.setObject(i + 1, params[i]);
      }

      // 3. 执行SQL
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 4. 关闭资源
      JDBCUtils.closeResource(conn, ps);
    }
  }

  @Test
  public void test2() {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      conn = JDBCUtils.getConnection();

      // 1. 预编译SQL语句，返回PreparedStatement实例
      String sql = "update customers set name = ? WHERE id = ?";
      ps = conn.prepareStatement(sql);
      // 2. 填充占位符
      ps.setObject(1, "莫扎特");
      ps.setObject(2, 18);

      // 3. 执行SQL
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 4. 关闭资源
      JDBCUtils.closeResource(conn, ps);
    }
  }

  @Test
  public void test1()  {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      conn = JDBCUtils.getConnection();

      // 1. 预编译SQL语句，返回PreparedStatement实例
      // ?为占位符
      String sql = "insert into customers(name, email, birth) VALUES (?, ?, ?)";
      ps = conn.prepareStatement(sql);

      // 2. 填充占位符,索引从1开始
      ps.setString(1, "哪吒");
      ps.setString(2, "nezha@gamil.com");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      java.util.Date date = sdf.parse("1000-01-01");
      ps.setDate(3, new Date(date.getTime()));

      // 3. 执行SQL
      ps.execute();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 4. 关闭资源
      JDBCUtils.closeResource(conn, ps);
    }
  }
}
