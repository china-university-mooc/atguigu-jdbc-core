package com.itutry.jdbc.iterate1.bean;

import com.itutry.jdbc.iterate1.annotaion.Table;
import java.sql.Date;

/*
ORM的编程思想
一个数据表对应一个Java类
表中的一条记录对应Java类的一个对象
表中的一个字段对应Java类的一个属性
 */
@Table("customers")
public class Customer {

  private Integer id;
  private String name;
  private String email;
  private Date birth;

  public Customer() {
  }

  public Customer(Integer id, String name, String email, Date birth) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.birth = birth;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getBirth() {
    return birth;
  }

  public void setBirth(Date birth) {
    this.birth = birth;
  }

  @Override
  public String toString() {
    return "Customer{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", birth=" + birth +
        '}';
  }
}
