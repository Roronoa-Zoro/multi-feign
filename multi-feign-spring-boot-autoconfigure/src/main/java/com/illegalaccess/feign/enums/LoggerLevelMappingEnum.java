package com.illegalaccess.feign.enums;

import feign.Logger;

/**
 * Created by xiao on 2019/12/18.
 */
public enum LoggerLevelMappingEnum {

    NONE(LogLevelEnum.NONE, Logger.Level.NONE),
    BASIC(LogLevelEnum.BASIC, Logger.Level.BASIC),
    HEADER(LogLevelEnum.HEADER, Logger.Level.HEADERS),
    FULL(LogLevelEnum.FULL, Logger.Level.FULL);

    private LogLevelEnum logLevelEnum;
    private Logger.Level feignLoggerLevel;

    public LogLevelEnum getLogLevelEnum() {
        return logLevelEnum;
    }

    public Logger.Level getFeignLoggerLevel() {
        return feignLoggerLevel;
    }

    LoggerLevelMappingEnum(LogLevelEnum logLevelEnum, Logger.Level feignLoggerLevel){
        this.logLevelEnum = logLevelEnum;
        this.feignLoggerLevel = feignLoggerLevel;
    }

    public static LoggerLevelMappingEnum getMappingByLogLevel(LogLevelEnum logLevel) {
        for (LoggerLevelMappingEnum loggerLevelMappingEnum : LoggerLevelMappingEnum.values()) {
            if (loggerLevelMappingEnum.getLogLevelEnum() == logLevel) {
                return loggerLevelMappingEnum;
            }
        }
        throw new IllegalArgumentException("incoming LogLevelEnum is invalid:" + logLevel);
    }
}
