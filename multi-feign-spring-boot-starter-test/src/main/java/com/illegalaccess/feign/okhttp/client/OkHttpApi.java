package com.illegalaccess.feign.okhttp.client;

import com.illegalaccess.feign.annotation.MultiFeignClient;
import com.illegalaccess.feign.enums.LogLevelEnum;
import com.illegalaccess.feign.okhttp.bo.HelloBean;
import feign.Headers;
import feign.RequestLine;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by xiao on 2019/12/18.
 */
@Headers({"Accept: application/json","Content-Type: application/json"})
@MultiFeignClient(name = "okHttpApi", url = "http://127.0.0.1:8080", clientPoolName = "okHttpPool", logLevel = LogLevelEnum.FULL)
//@FeignClient(name = "okHttpApi", url = "http://127.0.0.1:8080")
public interface OkHttpApi {

    @RequestLine("POST /helloOkHttp")
//    @PostMapping("/helloOkHttp")
    HelloBean hello();
}
