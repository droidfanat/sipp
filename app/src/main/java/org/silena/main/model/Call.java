package org.silena.main.model;

import java.util.Date;

public class Call {

    private String phone;
    private int type;
    private Date date;
    private String duration;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        return "Call{" +
                "phone='" + phone + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", duration='" + duration + '\'' +
                '}';
    }
}
