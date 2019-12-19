package com.illegalaccess.feign.support;

import com.illegalaccess.feign.enums.ClientPoolTypeEnum;
import com.illegalaccess.feign.enums.LogLevelEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xiao on 2019/12/18.
 */
@Getter
@Setter
@ToString
public class MultiFeignBuildProperties {

    private Class<?> type;

    private String name;

    private String url;

    private String clientPoolName;

    private ClientPoolTypeEnum clientPoolType;

    private LogLevelEnum logLevel;

    private int retry;
    /**
     * retry period, unit is ms
     * @return
     */
    private int retryPeriod;

    /**
     * retry max period, unit is ms
     * @return
     */
    private int retryMaxPeriod;
}
