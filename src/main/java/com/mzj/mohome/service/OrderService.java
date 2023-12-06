package com.mzj.mohome.service;
import com.mzj.mohome.entity.PayRecord;
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

    /**
     * 添加订单信息
     * @return
     */
    Map<String,Object> addOrderInfoWx(Map<String,Object> map);

    //根据状态查询
    List<Map<String,Object>> findOrerListInfo(Map<String,Object> map);

    //接单时，所触发的修改操作
    int updateOrderInfo(Map<String,Object> map);

    Map<String,Object> findOrderStatusMsg(Map<String,Object> map);
    //修改订单退款
    int updateReturnOrder(Map<String,Object> map);

    Map<String,Object> orderStatusList(Map<String,Object> map);

    List<Map<String,Object>> findEvalChooseDetail();

    List<Map<String,Object>> findOrerListInfoByCon(Map<String,Object> map);

    Map<String,Object> addPhoneUp(Map<String,Object> map);

    List<Map<String,Object>> findOrderList(Map<String,Object> map);

    /**
     * 根据订单删除信息
     * @param orderId
     * @return
     */
    int delOrderInfo(String orderId);

    /**
     * 修改支付记录表
     * @param payRecord
     * @return
     */
    int updOrderInfo(PayRecord payRecord);

    /**
     * 根据订单ID，来查询相关信息
     * @param orderId
     * @return
     */
    OrderVo findOrderDetail(String orderId);
}
