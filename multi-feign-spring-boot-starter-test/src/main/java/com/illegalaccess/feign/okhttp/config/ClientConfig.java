package com.illegalaccess.feign.okhttp.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiao on 2019/12/18.
 */
@Configuration
public class ClientConfig {

    @Bean("okHttpPool")
    public OkHttpClient createOkHttp() {
        ConnectionPool cp = new ConnectionPool(10, 5, TimeUnit.MINUTES);
        return new OkHttpClient().newBuilder().connectionPool(cp).connectTimeout(10, TimeUnit.SECONDS).build();
    }

}
