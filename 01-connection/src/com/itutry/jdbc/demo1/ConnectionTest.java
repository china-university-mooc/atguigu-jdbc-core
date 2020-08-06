package com.itutry.jdbc.demo1;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.Test;

public class ConnectionTest {

  // 方式一
  @Test
  public void test1() throws SQLException {
    // 1. 实例化驱动
    Driver driver = new com.mysql.jdbc.Driver();

    // 2. 提供URL
    String url = "jdbc:mysql://localhost:3306/test";

    // 3. 提供用户名和密码
    Properties info = new Properties();
    info.setProperty("user", "root");
    info.setProperty("password", "12345678");

    // 4. 获取连接
    Connection conn = driver.connect(url, info);
    System.out.println(conn);
  }

  // 方式二：对方式1的迭代
  // 目的，不出现第三方API，是的程序具有更好的移植性
  @Test
  public void test2() throws Exception {
    // 1. 用反射实例化Driver
    Class clazz = Class.forName("com.mysql.jdbc.Driver");
    Driver driver = (Driver) clazz.newInstance();

    // 2. 提供URL
    String url = "jdbc:mysql://localhost:3306/test";

    // 3. 提供用户名和密码
    Properties info = new Properties();
    info.setProperty("user", "root");
    info.setProperty("password", "12345678");

    // 4. 从driver获取连接
    Connection conn = driver.connect(url, info);
    System.out.println(conn);
  }

  // 方式三：使用DriverManager替换Driver
  @Test
  public void test3() throws Exception {
    // 1. 用反射实例化Driver
    Class clazz = Class.forName("com.mysql.jdbc.Driver");
    Driver driver = (Driver) clazz.newInstance();

    // 2. 注册驱动
    DriverManager.registerDriver(driver);

    // 3. 提供URL、用户名和密码
    String url = "jdbc:mysql://localhost:3306/test";
    String user = "root";
    String password = "12345678";

    // 4. 从DriverManager获取连接
    Connection conn = DriverManager.getConnection(url, user, password);
    System.out.println(conn);
  }

  // 方式四：只加载驱动，不用显式注册
  @Test
  public void test4() throws Exception {

    // 1. 加载Driver，这一步MySQL可以省略
    Class.forName("com.mysql.jdbc.Driver");

    // 省略：Driver实现类的静态代码块已经做了
//    Driver driver = (Driver) clazz.newInstance();
//    // 注册驱动
//    DriverManager.registerDriver(driver);

    // 2. 提供三个连接的基本信息
    String url = "jdbc:mysql://localhost:3306/test";
    String user = "root";
    String password = "12345678";

    // 3. 获取连接
    Connection conn = DriverManager.getConnection(url, user, password);
    System.out.println(conn);
  }

  // 方式五:最终版
  // 将数据库连接需要的信息声明在配置文件中，通过读取配置文件的方式获取连接
  // 好处：
  //  1. 实现了数据和代码的分离，解耦
  //  2. 修改配置信息时，不需要重新打包代码
  @Test
  public void test5() throws Exception {
    // 1.读取配置文件
    Properties pros = new Properties();
    try (InputStream is = ConnectionTest.class.getClassLoader()
        .getResourceAsStream("jdbc.properties")) {
      pros.load(is);
    }

    String user = pros.getProperty("user");
    String password = pros.getProperty("password");
    String url = pros.getProperty("url");
    String driverClass = pros.getProperty("driverClass");
    // 2. 加载驱动
    Class.forName(driverClass);

    // 3. 获取连接
    Connection conn = DriverManager.getConnection(url, user, password);
    System.out.println(conn);
  }

  @Test
  public void test6() {
    ClassLoader cl1 = ConnectionTest.class.getClassLoader();
    System.out.println(cl1);
    ClassLoader cl2 = ClassLoader.getSystemClassLoader();
    System.out.println(cl2);
  }
}
