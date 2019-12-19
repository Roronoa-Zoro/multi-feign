package com.illegalaccess.feign.okhttp.service;

import com.illegalaccess.feign.okhttp.bo.HelloBean;
import com.illegalaccess.feign.okhttp.client.OkHttpApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiao on 2019/12/18.
 */
@Service
public class ApiService {

    @Autowired
    OkHttpApi okHttpApi;

    public HelloBean invokeOkHttp() {
        return okHttpApi.hello();
    }
}
