package com.lilan.exctool.pojo;

import java.util.ArrayList;
import java.util.HashMap;

public class ExcMessage {
    private String orderNum;
    private String orderNumERP;
    private String clientName;//客户名称、发货地址及联系人
    private String partOrder;//分单原则
    private int scAll;
    private int scA;
    private int scB;
    private int scC;
    private ArrayList<Message2> messageList=new ArrayList<Message2>();


    @Override
    public String toString() {
        return "ExcMessage{" +
                "orderNum='" + orderNum + '\'' +
                ", orderNumERP='" + orderNumERP + '\'' +
                ", clientName='" + clientName + '\'' +
                ", partOrder='" + partOrder + '\'' +
                ", scAll='" + scAll + '\'' +
                ", scA='" + scA + '\'' +
                ", scB='" + scB + '\'' +
                ", scC='" + scC + '\'' +
                ", messageList=" + messageList +
                '}';
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

    public int getScAll() {
        return scAll;
    }

    public void setScAll(int scAll) {
        this.scAll = scAll;
    }

    public int getScA() {
        return scA;
    }

    public void setScA(int scA) {
        this.scA = scA;
    }

    public int getScB() {
        return scB;
    }

    public void setScB(int scB) {
        this.scB = scB;
    }

    public int getScC() {
        return scC;
    }

    public void setScC(int scC) {
        this.scC = scC;
    }

    public ArrayList<Message2> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<Message2> messageList) {
        this.messageList = messageList;
    }
}
