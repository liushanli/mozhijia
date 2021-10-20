package com.mzj.mohome.util;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.util.Map;

public class PushUtil {

    private String URL = "";

    private String key = "";
    private String secret = "";

    private String pushAppId = "";

    private String pushAppKey = "";

    /**
     * 推送消息
     */
    public Map<String, Object> pushMessage(String userId, String remark) {
        //IGtPush push = new IGtPush(URL, config.getPushAppKey(), config.getPushMasterSecret());
        IGtPush push = new IGtPush(URL, key, secret);
        // 使用透传模板
        TransmissionTemplate template = new TransmissionTemplate();
        // 1:收到通知直接激活app,2:客服端自行处理
        template.setTransmissionType(2);
        // 透传内容
        template.setTransmissionContent(remark);
        template.setAppId(pushAppId);
        template.setAppkey(pushAppKey);

        String intent = "intent:#Intent;action=android.intent.action.oppopush;launchFlags=0x14000000;package=xxx;component=xxx;end";
        Notify notify = new Notify();
        // 通知栏显示标题
        notify.setTitle("您有新的任务下达");
        // 通知栏内容
        notify.setContent(remark);
        notify.setIntent(intent);
        // 设置第三方通知
        template.set3rdNotifyInfo(notify);

        SingleMessage message = new SingleMessage();
        message.setData(template);
        // 设置消息离线
        message.setOffline(true);
        // 离线消息有效时间为7天
        message.setOfflineExpireTime(1000 * 3600 * 24 * 7);
        //添加要推送的终端
        Target target = new Target();
       /* MstUserPo userPo = userDao.selectByPrimaryKey(userId);
        if (userPo.getPushId() != null) {
            target.setAppId(config.getPushAppId());
            target.setClientId(userPo.getPushId());
        }else {
            return null;
        }*/

        //MstUserPo userPo = userDao.selectByPrimaryKey(userId);
        /*if (userPo.getPushId() != null) {
            target.setAppId(config.getPushAppId());
            target.setClientId(userPo.getPushId());
        }else {
            return null;
        }*/
        String clientId = "";
        target.setAppId(pushAppId);
        target.setClientId(clientId);
        IPushResult result;
        Map<String, Object> response = null;
        // 执行推送
        try {
            result = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            result = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (result != null) {
            response = result.getResponse();
        }
        return response;
    }

    public static void main(String[] args){
        PushUtil pushUtil = new PushUtil();
        //pushMessage
        pushUtil.pushMessage("","fdsafddfsd");
        System.out.println("====--");
    }

}
