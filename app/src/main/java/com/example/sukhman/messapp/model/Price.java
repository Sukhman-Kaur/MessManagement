package com.example.sukhman.messapp.model;

public class Price {
   public int breakfast;
   public int lunch;
   public int dinner;
   public Price(){

   }

    public Price(int breakfast, int lunch, int dinner) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    @Override
    public String toString() {
        return "Price{" +
                "breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                '}';
    }
}
