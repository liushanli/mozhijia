package com.mzj.mohome.util;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @Author admin
 * @ClassName FacePlusThrowErrorHandler
 * @Description RestTemplate默认不处理HTTP响应码为400、500这类响应结果，直接抛异常,但是，该请求的响应结果内容却是我需要用到的，
 *  所以解决办法是：需要重写ResponseErrorHandler ，，对响应的错误信息不进行处理
 * @Date 2023/10/24 10:54
 */
@Configuration
public class FacePlusThrowErrorHandler implements ResponseErrorHandler {

    //写ResponseErrorHandler
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return false;
    }

    //写ResponseErrorHandler
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

    }

    //设置自定义restTemplate
    /**
     * 常用远程调用RestTemplate
     * @return restTemplate
     **/
    @Bean
    public RestTemplate myRestTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(300000);
        requestFactory.setReadTimeout(300000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.setErrorHandler(new FacePlusThrowErrorHandler());
        return restTemplate;
    }

//    @Bean("restTemplate")
//    public RestTemplate restTemplate(){
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new AcceptResponseErrorHandler());
//        return restTemplate ;

}
