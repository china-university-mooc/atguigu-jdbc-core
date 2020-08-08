package com.itutry.jdbc.demo5;

import com.itutry.jdbc.bean.Customer;
import com.itutry.jdbc.util.JDBCUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

public class QueryRunnerTest {

  // 更新
  @Test
  public void test1() throws Exception {
    Connection conn = JDBCUtils.getConnection3();

    QueryRunner runner = new QueryRunner();

    String sql = "insert into customers(name,email,birth) values (?,?,?)";
    int insertCount = runner.update(conn, sql, "蔡徐坤", "chcai@126.com", "1980-05-12");

    System.out.println("插入了" + insertCount + "条数据");
    DbUtils.closeQuietly(conn);
  }

  // 查询
  @Test
  public void test2() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select id, name, email, birth from customers where id = ?";
    // BeanHandler: ResultSetHandler接口的实现类，用于封装表中的一条数据
    BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
    Customer cust = runner.query(conn, sql, handler, 27);

    System.out.println(cust);
    JDBCUtils.closeResource(conn, null);
  }

  @Test
  public void test3() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select id, name, email, birth from customers where id < ?";
    // BeanListHandler：ResultSetHandler接口的实现类，用于封装表中的多条记录
    BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
    List<Customer> list = runner.query(conn, sql, handler, 27);

    list.forEach(System.out::println);
    JDBCUtils.closeResource(conn, null);
  }

  @Test
  public void test4() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select id, name, email, birth from customers where id = ?";
    MapHandler handler = new MapHandler();
    Map<String, Object> map = runner.query(conn, sql, handler, 27);

    System.out.println(map);
    JDBCUtils.closeResource(conn, null);
  }

  @Test
  public void test5() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select id, name, email, birth from customers where id = ?";
    ArrayHandler handler = new ArrayHandler();
    Object[] array = runner.query(conn, sql, handler, 27);

    System.out.println(Arrays.toString(array));
    JDBCUtils.closeResource(conn, null);
  }

  @Test
  public void test6() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select count(*) from customers";
    ScalarHandler handler = new ScalarHandler();
    Long value = (Long)runner.query(conn, sql, handler);

    System.out.println(value);
    JDBCUtils.closeResource(conn, null);
  }

  @Test
  public void test7() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select max(birth) from customers";
    ScalarHandler handler = new ScalarHandler();
    Date value = (Date) runner.query(conn, sql, handler);

    System.out.println(value);
    JDBCUtils.closeResource(conn, null);
  }

  // 自定义ResultSetHandler实现类
  @Test
  public void test8() throws Exception {
    QueryRunner runner = new QueryRunner();

    Connection conn = JDBCUtils.getConnection3();
    String sql = "select id, name, email, birth from customers where id = ?";
    MyResultSetHandler<Customer> handler = new MyResultSetHandler(Customer.class);
    Customer cust = runner.query(conn, sql, handler, 27);

    System.out.println(cust);
    JDBCUtils.closeResource(conn, null);
  }
}

class MyResultSetHandler<T> implements ResultSetHandler<T> {

  private final Class<T> type;

  public MyResultSetHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public T handle(ResultSet rs) throws SQLException {

    if (rs.next()) {
      try {
        T obj = type.newInstance();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
          Object value = rs.getObject(i + 1);
          String columnLabel = rsmd.getColumnLabel(i + 1);

          Field field = type.getDeclaredField(columnLabel);
          field.setAccessible(true);
          field.set(obj, value);
        }

        return obj;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
