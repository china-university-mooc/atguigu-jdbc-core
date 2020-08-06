package com.itutry.jdbc.demo5;

import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Test;

/*
CREATE TABLE goods(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(25)
);
插入两万条数据
 */
public class BatchInsertTest {

  // 方式一：PreparedStatement
  // cost：4498ms
  @Test
  public void test1() throws Exception {
    long start = System.currentTimeMillis();
    Connection conn = JDBCUtils.getConnection();
    String sql = "insert into goods(name) values(?)";
    PreparedStatement ps = conn.prepareStatement(sql);

    for (int i = 0; i <= 20000; i++) {
      ps.setObject(1, "name_" + i);
      ps.execute();
    }
    long end = System.currentTimeMillis();
    System.out.println("花费的时间为：" + (end - start) + "ms");

    JDBCUtils.closeResource(conn, ps);
  }

  // 方式二：
  // 1. addBatch, executeBatch, clearBatch
  // 20000: cost: 579ms
  // 1000000: cost: 5167ms
  @Test
  public void test2() {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      long start = System.currentTimeMillis();
      conn = JDBCUtils.getConnection();
      String sql = "insert into goods(name) values(?)";
      ps = conn.prepareStatement(sql);

      for (int i = 0; i <= 1000000; i++) {
        ps.setObject(1, "name_" + i);

        // 1. "攒"sql
        ps.addBatch();
        if (i % 500 == 0) {
          // 2. 执行batch
          ps.executeBatch();
          // 3. 清空batch
          ps.clearBatch();
        }
      }
      long end = System.currentTimeMillis();
      System.out.println("花费的时间为：" + (end - start) + "ms");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, ps);
    }
  }

  // 方式三：终极方案
  // 100000: cost: 4779ms
  @Test
  public void test3() {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      long start = System.currentTimeMillis();
      conn = JDBCUtils.getConnection();
      // 设置不允许自动提交
      conn.setAutoCommit(false);

      String sql = "insert into goods(name) values(?)";
      ps = conn.prepareStatement(sql);

      for (int i = 0; i <= 1000000; i++) {
        ps.setObject(1, "name_" + i);

        // 1. "攒"sql
        ps.addBatch();
        if (i % 500 == 0) {
          // 2. 执行batch
          ps.executeBatch();
          // 3. 清空batch
          ps.clearBatch();
        }
      }
      // 提交数据
      conn.commit();
      long end = System.currentTimeMillis();
      System.out.println("花费的时间为：" + (end - start) + "ms");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.closeResource(conn, ps);
    }
  }
}
