package com.mzj.mohome.entity;



import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Product implements Serializable {
        private Integer id;
        /// <summary>
///商家ID
/// <summary>
        private String shopId;
        /// <summary>
///产品ID
/// <summary>
        private String productId;
        /// <summary>
///店内分类
/// <summary>
        private String productTypeId;
        /// <summary>
///平台分类
/// <summary>
        private Integer platformType;
        /// <summary>
///banner 图
/// <summary>
        private String imgUrl;
        /// <summary>
///产品名称
/// <summary>
        private String productName;
        /// <summary>
///筛选项
/// <summary>
        private String selectTypeId;
        /// <summary>
///产品简介
/// <summary>
        private String detail;
        /// <summary>
///项目服务时间
/// <summary>
        private Integer productTime;
        /// <summary>
///排序
/// <summary>
        private Integer orderNum;
        /// <summary>
///服务项目详细介绍
/// <summary>
        private String contentText;
        /// <summary>
///原价格
/// <summary>
        private Float oldPrice;
        /// <summary>
///现价格
/// <summary>
        private Float price;
        /// <summary>
///会员价格
/// <summary>
        private Float memberPrice;
        /// <summary>
///团购价格
/// <summary>
        private Float groupPrice;
        /// <summary>
///服务次数
/// <summary>
        private Integer serviceNumber;
        /// <summary>
///服务城市
/// <summary>
        private String serviceArea;
        /// <summary>
///员工人数
/// <summary>
        private Integer personnel;
        /// <summary>
///单位
/// <summary>
        private String unit;
        /// <summary>
///销售数量
/// <summary>
        private Integer sellSum;
        /// <summary>
///状态，1：上架，0：未上架
/// <summary>
        private Integer status;
        /// <summary>
///添加时间
/// <summary>
        private Date addTime;
        /// <summary>
///更新时间
/// <summary>
        private Date updateTime;

        private Integer number = 0;
        private String showNumber="none";
        private String showButtons = "";
        private String showButtons_1 = "none";

        private String orderInfo;

        private Integer sellNum; //销售量
    }
