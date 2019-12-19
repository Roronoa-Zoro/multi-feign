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
 * Created by Jimmy on 2019/12/17.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiFeignClient {

    String name();

    /**
     * the name of httpclient/okhttp bean
     * @return
     */
    String clientPoolName();

    /**
     * service url
     * @return
     */
    String url();

    /**
     * connection pool type
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

    // TODO interceptors
    Class<? extends RequestInterceptor>[] interceptors() default {};
}
