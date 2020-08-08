package com.itutry.jdbc.iterate1.bean;

import com.itutry.jdbc.iterate1.annotaion.Column;
import com.itutry.jdbc.iterate1.annotaion.Id;
import java.sql.Date;

public class Order {

  @Id
  @Column("order_id")
  private Integer orderId;
  @Column("order_name")
  private String orderName;
  @Column("order_date")
  private Date orderDate;

  public Order(Integer orderId, String orderName, Date orderDate) {
    this.orderId = orderId;
    this.orderName = orderName;
    this.orderDate = orderDate;
  }

  public Order() {
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public String getOrderName() {
    return orderName;
  }

  public void setOrderName(String orderName) {
    this.orderName = orderName;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  @Override
  public String toString() {
    return "Order{" +
        "orderId=" + orderId +
        ", orderName='" + orderName + '\'' +
        ", orderDate=" + orderDate +
        '}';
  }
}
