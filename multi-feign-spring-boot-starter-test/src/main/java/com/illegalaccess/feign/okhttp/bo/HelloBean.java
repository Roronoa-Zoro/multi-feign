package com.illegalaccess.feign.okhttp.bo;

/**
 * Created by xiao on 2019/12/18.
 */

public class HelloBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HelloBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
