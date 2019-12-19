package com.illegalaccess.feign.okhttp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by xiao on 2019/12/19.
 */
public class SeparateInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("access-time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
    }
}
