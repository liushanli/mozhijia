package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class CommenReturn implements Serializable {
    private Integer id;
    private String commentId;
    private String returnMessage;
    private Date addTime;

    public CommenReturn() {
    }

    @Override
    public String toString() {
        return "CommenReturn{" +
                "id=" + id +
                ", commentId='" + commentId + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
