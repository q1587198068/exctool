package com.lilan.exctool.pojo;

import java.util.ArrayList;
import java.util.HashMap;

public class ExcMessage {
    private String orderNum;
    private String orderNumERP;
    private String clientName;//客户名称、发货地址及联系人
    private String partOrder;//分单原则
    private String scAll;
    private String scA;
    private String scB;
    private String scC;
    private ArrayList<HashMap> messageList;


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

    public String getScAll() {
        return scAll;
    }

    public void setScAll(String scAll) {
        this.scAll = scAll;
    }

    public String getScA() {
        return scA;
    }

    public void setScA(String scA) {
        this.scA = scA;
    }

    public String getScB() {
        return scB;
    }

    public void setScB(String scB) {
        this.scB = scB;
    }

    public String getScC() {
        return scC;
    }

    public void setScC(String scC) {
        this.scC = scC;
    }

    public ArrayList<HashMap> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<HashMap> messageList) {
        this.messageList = messageList;
    }
}
