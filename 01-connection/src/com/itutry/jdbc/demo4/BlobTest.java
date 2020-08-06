package com.itutry.jdbc.demo4;

import com.itutry.jdbc.bean.Customer;
import com.itutry.jdbc.util.JDBCUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.Test;

public class BlobTest {

  // 插入blob类型
  @Test
  public void test1() throws Exception {
    Connection conn = JDBCUtils.getConnection();

    String sql = "INSERT INTO customers(name, email, birth, photo) VALUES (?,?,?,?)";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setObject(1, "刘彻");
    ps.setObject(2, "lc@qq.com");
    ps.setObject(3, "1972-09-08");
    // 用InputStream插入Blob字段
    FileInputStream fis = new FileInputStream("shan.jpg");
    ps.setBlob(4, fis);

    ps.execute();

    fis.close();
    JDBCUtils.closeResource(conn, ps);
  }

  @Test
  public void test2() throws Exception {
    Connection conn = JDBCUtils.getConnection();

    String sql = "select id, name, email, birth, photo from customers where id = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setObject(1, 26);

    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
//      int id = rs.getInt(1);
//      String name = rs.getString(2);
//      String email = rs.getString(3);
//      Date birth = rs.getDate(4);
      int id = rs.getInt("id");
      String name = rs.getString("name");
      String email = rs.getString("email");
      Date birth = rs.getDate("birth");

      Customer cust = new Customer(id, name, email, birth);
      System.out.println(cust);

      Blob photo = rs.getBlob("photo");
      InputStream is = photo.getBinaryStream();

      FileOutputStream fos = new FileOutputStream("shan.jpeg");
      byte[] buff = new byte[1024];
      int len;
      while ((len = is.read(buff)) != -1) {
        fos.write(buff, 0, len);
      }

      fos.close();
      is.close();
    }

    JDBCUtils.closeResource(conn, ps, rs);
  }
}
