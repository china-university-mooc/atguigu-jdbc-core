package com.itutry.jdbc.demo1;

import com.itutry.jdbc.util.DAOUtils;
import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import org.junit.Test;

public class TransactionTest {

  // 转账过程，未考虑数据库事务
  // update user_table set balance = balance - 100 where user = 'AA';
  // update user_table set balance = balance + 100 where user = 'BB';
  @Test
  public void test1() {
    String sql1 = "update user_table set balance = balance - 100 where user = ?";
    DAOUtils.update(sql1, "AA");

    // 模拟网络异常
    System.out.println(10 / 0);

    String sql2 = "update user_table set balance = balance + 100 where user = ?";
    DAOUtils.update(sql2, "BB");

    System.out.println("转账成功");
  }

  @Test
  public void test2() throws Exception {
    Connection conn = JDBCUtils.getConnection();
    System.out.println(conn.getAutoCommit());

    // 取消自动提交
    conn.setAutoCommit(false);

    try {
      String sql1 = "update user_table set balance = balance - 100 where user = ?";
      DAOUtils.update(conn, sql1, "AA");

      // 模拟异常
      System.out.println(10 / 0);

      String sql2 = "update user_table set balance = balance + 100 where user = ?";
      DAOUtils.update(conn,sql2, "BB");

      System.out.println("转账成功");

      // 提交数据
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      conn.rollback();
    }

    conn.setAutoCommit(true);
    JDBCUtils.closeResource(conn, null);
  }
}
