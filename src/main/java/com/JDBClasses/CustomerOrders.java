package com.JDBClasses;

import java.sql.Date;

public class CustomerOrders {
    private int orderId;
    private String orderName;
    private Date orderDate;

    public CustomerOrders(int orderId, String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public CustomerOrders(int orderId, String orderName) {
        this(orderId,orderName,null);
    }

    public CustomerOrders(int orderId) {
        this(orderId,null);
    }
    public CustomerOrders(){
        this(0);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
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
        return "CustomerOrders{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
