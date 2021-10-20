package com.mzj.mohome.service;

import com.mzj.mohome.entity.Order;
import com.mzj.mohome.vo.OrderVo;

import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 查询订单
     * @return
     */
    public List<OrderVo> findOrerList(Map<String,Object> map);

    /**
     * 添加订单信息
     * @return
     */
    Map<String,Object> addOrderInfo(Map<String,Object> map);

    //根据状态查询
    List<Map<String,Object>> findOrerListInfo(Map<String,Object> map);

    //接单时，所触发的修改操作
    int updateOrderInfo(Map<String,Object> map);

    //修改订单退款
    int updateReturnOrder(Map<String,Object> map);

    Map<String,Object> orderStatusList(Map<String,Object> map);

    List<Map<String,Object>> findEvalChooseDetail();

    List<Map<String,Object>> findOrerListInfoByCon(Map<String,Object> map);

    Map<String,Object> addPhoneUp(Map<String,Object> map);

    List<Map<String,Object>> findOrderList(Map<String,Object> map);
}