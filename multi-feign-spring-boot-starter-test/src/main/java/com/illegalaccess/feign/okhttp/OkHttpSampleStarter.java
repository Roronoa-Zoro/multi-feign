package com.illegalaccess.feign.okhttp;

import com.illegalaccess.feign.annotation.EnableMultiFeignClient;
import com.illegalaccess.feign.okhttp.bo.HelloBean;
import com.illegalaccess.feign.okhttp.service.ApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiao on 2019/12/18.
 */
@EnableMultiFeignClient
@RestController
@SpringBootApplication
public class OkHttpSampleStarter {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(OkHttpSampleStarter.class, args);
        ApiService api = ctx.getBean(ApiService.class);
        System.out.println("okHttp==========" + api.invokeOkHttp());

        ctx.close();
    }

    @PostMapping(value = "/helloOkHttp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelloBean hello() {
        HelloBean hb = new HelloBean();
        hb.setName("helloOkHttp");
        return hb;
    }

}
