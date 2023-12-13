package com.mzj.mohome.service;

import com.mzj.mohome.entity.Order;
import com.mzj.mohome.entity.Worker;
import com.mzj.mohome.entity.WorkerPic;
import com.mzj.mohome.vo.PageUtil;
import com.mzj.mohome.vo.WorkerVo;
import com.mzj.mohome.vo.WorkerWxInfo;
import com.winnerlook.model.VoiceResponseResult;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface WorkerService {

    //根绝店铺来查询员工的信息
    List<Map<String,Object>> findWorkerList(PageUtil pageUtil);

    //根绝店铺来查询员工的信息
    List<Map<String,Object>> findWorkerList_1(PageUtil pageUtil);

    //根绝店铺来查询员工的信息
    List<Map<String,Object>> findWorkerList_2(PageUtil pageUtil);

    //根据工作人员的工号，来查询员工的照片信息
    List<WorkerPic> findWorkerPicById(PageUtil pageUtil);

    //根据用户手机号，验证码来判断用户是否存在
    Map<String,Object> findWorkerInfo(String phone,String sendCode);

    //根据用户手机号，验证码来判断用户是否存在
    Map<String,Object> findWorkerInfoWx(String phone,String sendCode,String openId);

    //查询员工工作时间
    List<Map<String,Object>> findWorkTime(Map<String,Object> map);

    //查询评价
    List<Map<String,Object>> findEvaluate(Map<String,Object> map);


    //删除和员工关联图片
    int delWorkPic(Map<String,Object> map);

    //添加和员工关联图片
    int addWorkPic(Map<String,Object> map);

    //根据员工id，来查询员工图片
    List<Map<String,Object>> findWorkPicList(Map<String,Object> map);

    //修改员工信息
    int updateWorkInfo(Map<String,Object> map);

    //根据技师的id/时间来修改是否繁忙
    int updateWorkBusy(Map<String,Object> map);

    //添加资格证的信息
    int addWorkConPic(Map<String,Object> map);

    //修改图片的信息
    int updWorkPic(Map<String,Object> map);

    List<Map<String,Object>> findWorkTypePicList(Map<String,Object> map);

    //解绑两个手机号
    VoiceResponseResult httpsPrivacyUnbind(Map<String,String> map);

    //绑定两个手机号
    VoiceResponseResult httpsPrivacyBindAxb(Map<String,String> map);

    List<Map<String,Object>> findEvaluateByProductId(Map<String,Object> map);

    String updateLoginImg(Map<String,Object> map);

    Map<String,Object> uploadWorkerVideo(Map<String,Object> map);

    List<Map<String,Object>> findEvaluateByWorkId(Map<String,Object> map);

    Map<String,Object> findWorkOrderNum(Map<String,Object> map);

    Map<String,Object> findWorkOrderNumInfo(Map<String,Object> map);

    //支付成功后，进行修改技师的忙时状态，根据订单id
    int findUpdateOrder(Map<String,Object> map);

    Order findOrderInfo(String orderId);

    Map<String,Object> findShopByCode(Map<String,Object> map);

    Map<String,Object> updShopByCode(Map<String,Object> map);

    Map<String,Object> findWorkInfo(Map<String,Object> map);

    Map<String,Object> findWorkerInfoBy(String phone,String version);

    List<Map<String,Object>> workerInfoListByInfo(String shopId,String city);

    /**
     * 查询技师与订单的距离
     * @param map
     * @return
     */
    Map<String,Object> queryWorkerInfo(Map<String,Object> map);

    /**
     * 根据经纬度获取距离
     * @param start
     * @param end
     * @return
     */
    Integer getDistances(String start,String end);

    /**
     * 修改技师的所在区域信息
     * @param map
     * @return
     */
    int updWorkerInfoJW(Map<String,Object> map);

    /**
     * 根据用户id进行查询信息
     * @param workerId
     * @return
     */
    WorkerVo findWorkLocation(String workerId);

    /**
     * 添加技师绑定微信
     * @param workerWxInfo
     * @return
     */
    int addWorkerWxInfo(WorkerWxInfo workerWxInfo);

    /**
     * 修改绑定
     * @param workerWxInfo
     * @return
     */
    int updWorkerWxInfo(WorkerWxInfo workerWxInfo);

    /**
     * 解除绑定
     * @param workerId
     * @return
     */
    int delWorkerWxInfo(String workerId);

    /**
     * 查询技师是否已经绑定
     * @param workerId
     * @return
     */
    int findWorkerWxInfo(String workerId);
}
