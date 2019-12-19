package com.illegalaccess.feign.okhttp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.UUID;

/**
 * Created by xiao on 2019/12/19.
 */
public class GlobalIDInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("reqId", UUID.randomUUID().toString());
    }
}
