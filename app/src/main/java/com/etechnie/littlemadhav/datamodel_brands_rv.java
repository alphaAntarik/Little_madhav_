package com.etechnie.littlemadhav;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class datamodel_brands_rv {

    @SerializedName("Brand name")
    @Expose
    private String Brand_name;

    @SerializedName("Brand image")
    @Expose
    private String Brand_image;
    @SerializedName("price")
    @Expose
    private String price;

    public String getBrand_name() {
        return Brand_name;
    }

    public void setBrand_name(String brand_name) {
        Brand_name = brand_name;
    }

    public String getBrand_image() {
        return Brand_image;
    }

    public void setBrand_image(String brand_image) {
        Brand_image = brand_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

