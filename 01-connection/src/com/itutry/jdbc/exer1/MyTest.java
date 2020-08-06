package com.itutry.jdbc.exer1;

import com.itutry.jdbc.util.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.util.Scanner;
import org.junit.Test;

public class MyTest {

  @Test
  public void test1() throws ParseException {
    Scanner scanner = new Scanner(System.in);

    System.out.println("请输入用户名：");
    String name = scanner.next();
    System.out.println("请输入用户邮箱：");
    String email = scanner.next();
    System.out.println("请输入用户生日(格式为yyyy-MM-dd)：");
    String birthStr = scanner.next();
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    Date birth = sdf.parse(birthStr);

    String sql = "insert into customers(name, email, birth) values (?, ?, ?)";
    int insertCount = update(sql, name, email, birthStr);
    if (insertCount > 0) {
      System.out.println("添加成功");
    } else {
      System.out.println("添加失败");
    }
  }

  public int update(String sql, Object... params) {
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
      // 如果执行查询操作，有返回结果，则此方法返回true
      // 如果是更新操作，则返回false
      ps.execute();

      return ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 4. 关闭资源
      JDBCUtils.closeResource(conn, ps);
    }

    return 0;
  }
}
