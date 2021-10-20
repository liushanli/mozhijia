package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目类别
 */
@Data
public class ProductType implements Serializable {
    private Integer id;
    /// <summary>
///项目类别id
/// <summary>
    private String productTypeId;
    /// <summary>
///项目类别名称
/// <summary>
    private String productTypeName;
    private Date addTime;
}
