package com.example.sukhman.messapp.model;

import java.io.Serializable;

public class Student implements Serializable{
    public int id;
    public String name;
    public String phone;
    public int year;
   static int bill;
   public int acc_id;

   public Student(){

   }

    public Student(int id, String name, String phone, int year, int acc_id) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.year = year;
        this.acc_id = acc_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public static int getBill() {
        return bill;
    }

    public static void setBill(int bill) {
        Student.bill = bill;
    }

    @Override
    public String toString() {
        return "id=" + id + "acc_id="+acc_id+
                ",name=" + name + ',' +
                "phone=" + phone + ',' +
                "year=" + year;
    }

//    public String getName() {
//        return name;
//    }
}
