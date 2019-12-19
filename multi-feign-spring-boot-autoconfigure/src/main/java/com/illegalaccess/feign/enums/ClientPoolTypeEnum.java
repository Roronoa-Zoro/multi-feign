package com.illegalaccess.feign.enums;

/**
 * Created by Jimmy on 2019/12/17.
 * connection pool type enums
 */
public enum ClientPoolTypeEnum {

    /**
     * using http client connection pool
     */
    HTTP_CLIENT,

    /**
     * using okhttp connection pool
     */
    OK_HTTP;
}
