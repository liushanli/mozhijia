package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Cart implements Serializable {
    private Integer id;
    private String cartId;
    private String productId;
    private String userId;
    private String productName;
    private Integer oldPrice;
    private Integer price;
    private String addTime;

    public Cart() {
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", cartId='" + cartId + '\'' +
                ", productId='" + productId + '\'' +
                ", userId='" + userId + '\'' +
                ", productName='" + productName + '\'' +
                ", oldPrice=" + oldPrice +
                ", price=" + price +
                ", createTime='" + addTime + '\'' +
                '}';
    }

    public Cart(Integer id, String cartId, String productId, String userId, String productName, Integer oldPrice, Integer price, String addTime) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.userId = userId;
        this.productName = productName;
        this.oldPrice = oldPrice;
        this.price = price;
        this.addTime = addTime;
    }
}
