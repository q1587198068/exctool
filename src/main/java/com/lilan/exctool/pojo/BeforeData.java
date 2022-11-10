package com.lilan.exctool.pojo;

//原始数据
public class BeforeData {

    private String orderNum;
    private String orderNumERP;
    private String clientName;//客户名称、发货地址及联系人
    private String partOrder;//分单原则
    private String id;
    private String type;

    @Override
    public String toString() {
        return "BeforeData{" +
                "orderNum='" + orderNum + '\'' +
                ", orderNumERP='" + orderNumERP + '\'' +
                ", clientName='" + clientName + '\'' +
                ", partOrder='" + partOrder + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderNumERP() {
        return orderNumERP;
    }

    public void setOrderNumERP(String orderNumERP) {
        this.orderNumERP = orderNumERP;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPartOrder() {
        return partOrder;
    }

    public void setPartOrder(String partOrder) {
        this.partOrder = partOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
