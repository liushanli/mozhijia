package com.mzj.mohome.util;

import com.mzj.mohome.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class AutoLoad implements InitializingBean {

    @Autowired
    private UserService userService;
    @Override
    public void afterPropertiesSet(){
        //userService.init();
    }
}