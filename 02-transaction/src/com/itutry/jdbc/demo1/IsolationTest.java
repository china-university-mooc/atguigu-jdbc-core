package com.itutry.jdbc.demo1;

import com.itutry.jdbc.bean.User;
import com.itutry.jdbc.util.DAOUtils;
import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import org.junit.Test;

public class IsolationTest {

  @Test
  public void test1() throws Exception {
    Connection conn = JDBCUtils.getConnection();
    // 获取当前连接的隔离级别
    System.out.println(conn.getTransactionIsolation());
    // 设置数据库的隔离级别
    conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    conn.setAutoCommit(false);

    String sql = "select user, password, balance from user_table where user = ?";
    User user = DAOUtils.queryForOne(conn, User.class, sql, "CC");
    System.out.println(user);
  }

  @Test
  public void test2() throws Exception {
    Connection conn = JDBCUtils.getConnection();
    conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    conn.setAutoCommit(false);

    String sql = "update user_table set balance = ? where user = ?";
    DAOUtils.update(conn, sql, 5000, "CC");

    Thread.sleep(15000);
    System.out.println("修改结束");
  }
}
