package com.example.victorseguido.myapplication.model;

/**
 * Created by victorseguido on 2/3/18.
 */

public class Pair {

    String coin;
    Float price;
    Float quantity;


    public Pair(){

    }

    public Pair(Float quantity, Float price,String coin ) {

        this.coin = coin;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCoin() {
        return coin;
    }

    public float getPrice() {
        return price;
    }

    public Float getQuantity() {
        return quantity;
    }


    public void setCoin(String coin) {
        this.coin = coin;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }



}
