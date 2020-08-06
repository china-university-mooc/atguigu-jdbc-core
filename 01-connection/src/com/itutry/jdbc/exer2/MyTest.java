package com.itutry.jdbc.exer2;

import com.itutry.jdbc.util.JDBCUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/*
CREATE TABLE IF NOT EXISTS examstudent (
    FlowID INT(10) PRIMARY KEY AUTO_INCREMENT,
    Type INT(5),
    IDCard VARCHAR(18),
    ExamCard VARCHAR(15),
    StudentName VARCHAR(20),
    Location VARCHAR(20),
    Grade INT(10)
);

INSERT INTO examstudent(Type, IDCard, ExamCard, StudentName, Location, Grade)
VALUES (4, '412824195263214584', '200523164754000', '张锋', '郑州', 85),
       (4, '222224195263214584', '200523164754001', '孙朋', '大连', 56),
       (4, '342824195263214584', '200523164754002', '刘明', '沈阳', 72),
       (4, '100824195263214584', '200523164754003', '赵虎', '哈尔滨', 95),
       (4, '454524195263214584', '200523164754004', '杨丽', '北京', 64),
       (4, '854524195263214584', '200523164754005', '王小红', '太原', 60);
 */
public class MyTest {

  @Test
  public void test1() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("四级/六级(4/6):");
    int type = scanner.nextInt();
    System.out.println("身份证号:");
    String idCard = scanner.next();
    System.out.println("准考证号:");
    String examCard = scanner.next();
    System.out.println("学生姓名:");
    String studentName = scanner.next();
    System.out.println("所在城市:");
    String location = scanner.next();
    System.out.println("学生成绩:");
    int grade = scanner.nextInt();

    String sql = "insert into examstudent(Type, IDCard, ExamCard, studentName, Location, Grade) values (?,?,?,?,?,?)";
    int insertCount = update(sql, type, idCard, examCard, studentName, location, grade);
    if (insertCount > 0) {
      System.out.println("添加成功");
    } else {
      System.out.println("添加失败");
    }
  }

  @Test
  public void test2() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("请选择您要输入的类型：");
    System.out.println("a:准好证号");
    System.out.println("b:身份证号");

    String selection = scanner.next();
    ExamStudent examStudent;
    if ("a".equalsIgnoreCase(selection)) {
      System.out.println("请输入准考证号：");
      String examCard = scanner.next();
      String sql = "select flowId, type, idCard, examCard, studentName, location, grade from examstudent where examCard = ?";
      examStudent = queryForOne(ExamStudent.class, sql, examCard);
    } else if ("b".equalsIgnoreCase(selection)) {
      System.out.println("请输入身份证号：");
      String idCard = scanner.next();
      String sql = "select flowId, type, idCard, examCard, studentName, location, grade from examstudent where idCard = ?";
      examStudent = queryForOne(ExamStudent.class, sql, idCard);
    } else {
      System.out.println("您的输入有误！请重新进入程序");
      return;
    }

    if (examStudent == null) {
      System.out.println("查无此人，请重新进入程序");
      return;
    }
    System.out.println("==========查询结果==========");
    System.out.println("流水号：\t\t" + examStudent.getFlowId());
    System.out.println("四级/六级：\t" + examStudent.getType());
    System.out.println("身份证号：\t" + examStudent.getIdCard());
    System.out.println("准考证号：\t" + examStudent.getExamCard());
    System.out.println("学生姓名：\t" + examStudent.getStudentName());
    System.out.println("区域：\t\t" + examStudent.getLocation());
    System.out.println("成绩：\t\t" + examStudent.getGrade());
  }

  @Test
  public void test3() {
    Scanner scanner = new Scanner(System.in);
//    ExamStudent examStudent;
//    String examCard;
//    while (true) {
//      System.out.println("请输入学生的考号：");
//      examCard = scanner.next();
//
//      String sql = "select flowId, type, idCard, examCard, studentName, location, grade from examstudent where examCard = ?";
//      examStudent = queryForOne(ExamStudent.class, sql, examCard);
//      if (examStudent == null) {
//        System.out.println("查无此人，请重新输入");
//      } else {
//        break;
//      }
//    }
//
//    String sql =  "delete from examstudent where examCard = ?";
//    int deleteCount = update(sql, examCard);
//    if (deleteCount > 0) {
//      System.out.println("删除成功！");
//    } else {
//      System.out.println("删除失败！");
//    }


    while (true) {
      System.out.println("请输入学生的考号：");
      String examCard = scanner.next();

      String sql =  "delete from examstudent where examCard = ?";
      int deleteCount = update(sql, examCard);
      if (deleteCount > 0) {
        System.out.println("删除成功！");
        break;
      } else {
        System.out.println("查无此人，请重新输入");
      }
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
//      ps.execute();

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
