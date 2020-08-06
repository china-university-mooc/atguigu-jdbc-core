package com.itutry.jdbc.demo2;

import com.itutry.jdbc.util.JDBCUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;
import org.junit.Test;

// PreparedStatement代替Statement解决SQL注入问题
public class PreparedStatementTest {

  @Test
  public void test1() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("请输入用户名：");
    String username = scanner.nextLine();
    System.out.println("请输入密码：");
    String password = scanner.nextLine();

    // 需要拼接SQL语句， 存在SQL注入问题
    // SELECT user, password FROM user_table WHERE user = '1' or ' AND password = '=1 or '1' = '1';
    String sql = "SELECT user, password FROM user_table WHERE user = ? AND password = ?;";
    User user = queryForOne(User.class, sql, username, password);
    if (user != null) {
      System.out.println("登录成功");
    } else {
      System.out.println("用户名不存在或密码错误");
    }
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
