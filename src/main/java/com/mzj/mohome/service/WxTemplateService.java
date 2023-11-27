package com.mzj.mohome.service;

import java.util.Map;

public interface WxTemplateService {
    //给用户发送私信
    Map<String,String> getAccessToken();

    Map<String,Object> fn_GetShareData(String url);

    Map<String,Object> sendMessage(String orderId);
}
