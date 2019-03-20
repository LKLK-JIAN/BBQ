package com.example.benetech.bbq.bean;

public class TemValue {

    private float temp;
    private int did;

    public TemValue(float temp, int did) {
        this.temp = temp;
        this.did = did;
    }
    public TemValue(float temp){
        this.temp=temp;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }
}
