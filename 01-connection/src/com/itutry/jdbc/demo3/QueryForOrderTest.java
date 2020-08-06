package com.itutry.jdbc.demo3;

import com.itutry.jdbc.bean.Order;
import com.itutry.jdbc.util.JDBCUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.junit.Test;

public class QueryForOrderTest {

  @Test
  public void test1() {
    String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
    Order order = queryForOrder(sql, 2);
    System.out.println(order);
  }

  public Order queryForOrder(String sql, Object... params) {
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

      // 5. 处理结果集

      // 获取结果集的元数据：ResultSetMetaData
      ResultSetMetaData rsmd = rs.getMetaData();
      // 通过ResultSetMetaData获取结果集的列数
      int columnCount = rsmd.getColumnCount();

      if (rs.next()) {
        Order order = new Order();
        // 处理结果集一行数据中的每一列
        for (int i = 0; i < columnCount; i++) {
          // 通过ResultSet获取列值
          Object columnValue = rs.getObject(i + 1);

          // 获取列名：getColumnName(),不推荐使用
          // 通过ResultSetMetaData获取列的别名
          String columnLabel = rsmd.getColumnLabel(i + 1);

          // 通过反射给对象的对应属性赋值
          Field field = Order.class.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(order, columnValue);
        }
        return order;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // 6. 关闭资源
      JDBCUtils.closeResource(conn, ps, rs);
    }
    return null;
  }
}
