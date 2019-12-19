package com.illegalaccess.feign.annotation;

import com.illegalaccess.feign.config.DefaultMultiFeignConfiguration;
import com.illegalaccess.feign.enums.ClientPoolTypeEnum;
import com.illegalaccess.feign.enums.LogLevelEnum;
import feign.RequestInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by xiao on 2019/12/17.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiFeignClient {

    String name();

    /**
     * 需要使用第连接池bean的名称
     * @return
     */
    String clientPoolName();

    /**
     * 服务的url
     * @return
     */
    String url();

    /**
     * 连接池的类型，和上面的熟悉成对使用
     * @see com.illegalaccess.feign.enums.ClientPoolTypeEnum
     * @return
     */
    ClientPoolTypeEnum clientPoolType() default ClientPoolTypeEnum.OK_HTTP;

    Class<?> configuration() default DefaultMultiFeignConfiguration.class;

    LogLevelEnum logLevel() default LogLevelEnum.BASIC;

    /**
     * 0 means disable retry
     * @return
     */
    int retry() default 0;

    /**
     * retry period, unit is ms
     * @return
     */
    int retryPeriod() default 200;

    /**
     * retry max period, unit is ms
     * @return
     */
    int retryMaxPeriod() default 1000;

    // TODO
//    List<RequestInterceptor> interceptors() default {};
}
