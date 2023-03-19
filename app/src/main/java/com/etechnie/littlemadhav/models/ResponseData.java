package com.etechnie.littlemadhav.models;

import com.etechnie.littlemadhav.models.user_model.BaseStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseData<T> extends BaseStatus {
    @SerializedName("items")
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

