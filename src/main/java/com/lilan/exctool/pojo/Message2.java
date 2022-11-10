package com.lilan.exctool.pojo;

public class Message2 {
    public String type;
    public String id;

    @Override
    public String toString() {
        return "Message2{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
