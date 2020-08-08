package com.itutry.jdbc.demo1;

import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import org.junit.Test;

public class ConnectionTest {

  @Test
  public void test1() throws Exception {
    Connection conn = JDBCUtils.getConnection();
    System.out.println(conn);
    JDBCUtils.closeResource(conn, null);
  }
}
