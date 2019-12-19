package com.illegalaccess.feign.support;

import com.illegalaccess.feign.enums.ClientPoolTypeEnum;
import com.illegalaccess.feign.enums.LogLevelEnum;
import com.illegalaccess.feign.enums.LoggerLevelMappingEnum;
import feign.Feign;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.Target;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * Created by xiao on 2019/12/18.
 */
@Slf4j
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class MultiFeignClientFactoryBean
        implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(MultiFeignClientFactoryBean.class);

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

    private Set<Class<? extends RequestInterceptor>> interceptors;

    private ApplicationContext applicationContext;

    @Nullable
    @Override
    public Object getObject() throws Exception {
        Feign.Builder builder = Feign.builder();

        builder.encoder(new JacksonEncoder()).decoder(new JacksonDecoder());

        Object clientPoolBean = applicationContext.getBean(clientPoolName);
        if (ClientPoolTypeEnum.OK_HTTP == clientPoolType) {
            builder.client(new OkHttpClient((okhttp3.OkHttpClient) clientPoolBean));
        }
        if (ClientPoolTypeEnum.HTTP_CLIENT == clientPoolType) {
            builder.client(new ApacheHttpClient((HttpClient) clientPoolBean));
        }

        Retryer retryer = Retryer.NEVER_RETRY;
        if (retry > 0) {
            retryer = new Retryer.Default(retryPeriod, retryMaxPeriod, retry);
        }
        builder.retryer(retryer);

        if (LogLevelEnum.NONE != logLevel) {
            LoggerLevelMappingEnum loggerLevelMappingEnum = LoggerLevelMappingEnum.getMappingByLogLevel(logLevel);
            builder.logger(new Slf4jLogger()).logLevel(loggerLevelMappingEnum.getFeignLoggerLevel());
        }

        if (interceptors != null && !interceptors.isEmpty()) {
            interceptors.forEach(interceptor -> {
                try {
                    builder.requestInterceptor(interceptor.newInstance());
                } catch (InstantiationException e) {
                    logger.error("apply requestInterceptor:{} fail", interceptor, e);
                } catch (IllegalAccessException e) {
                    logger.error("apply requestInterceptor:{} fail", interceptor, e);
                }
            });
        }
        return builder.target(new Target.HardCodedTarget<>(this.type, this.name, url));
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
