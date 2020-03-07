package com.example.twenfour;

public class Bean {

    private int bitmapId;//图片Id

    private String name;//图片编号
    private int color;
    public Bean(int bitmap, String name,int color) {
        this.bitmapId = bitmap;
        this.name = name;
        this.color=color;
    }

    public int getBitmapId() {
        return bitmapId;
    }

    public void setBitmapId(int bitmapId) {
        this.bitmapId = bitmapId;
    }
    public int getColor(){return color;}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setColor(int color) {
        this.color = color;
    }
}