package com.example.sukhman.messapp.model;

public class Bill {
   public String date;
     public int bid;
    public int status;
    public int price;
    public String description;

    public Bill(){

    }

    public Bill(String date,int id,int status, int ePrice, String description) {
        this.status = status;
        this.price = ePrice;
        this.description = description;
        this.date=date;
        this.bid=id;

    }

    @Override
    public String toString() {
        return "Bill{"+date+" "+bid+
                "status=" + status +
                ", ePrice=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return bid;
    }

    public void setId(int id) {
        this.bid = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
