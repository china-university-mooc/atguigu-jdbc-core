package com.itutry.jdbc.exer2;

public class ExamStudent {

  private int flowId;
  private int type;
  private String idCard;
  private String examCard;
  private String studentName;
  private String location;
  private int grade;

  public ExamStudent(int flowId, int type, String idCard, String examCard,
      String studentName, String location, int grade) {
    this.flowId = flowId;
    this.type = type;
    this.idCard = idCard;
    this.examCard = examCard;
    this.studentName = studentName;
    this.location = location;
    this.grade = grade;
  }

  public ExamStudent() {
  }

  public int getFlowId() {
    return flowId;
  }

  public void setFlowId(int flowId) {
    this.flowId = flowId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getIdCard() {
    return idCard;
  }

  public String getExamCard() {
    return examCard;
  }

  public void setExamCard(String examCard) {
    this.examCard = examCard;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }

  @Override
  public String toString() {
    return "ExamStudent{" +
        "flowId=" + flowId +
        ", type=" + type +
        ", idCard='" + idCard + '\'' +
        ", examCard='" + examCard + '\'' +
        ", studentName='" + studentName + '\'' +
        ", location='" + location + '\'' +
        ", grade=" + grade +
        '}';
  }
}
