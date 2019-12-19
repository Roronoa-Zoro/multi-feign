package com.illegalaccess.feign.support;

import com.illegalaccess.feign.enums.ClientPoolTypeEnum;
import com.illegalaccess.feign.enums.LogLevelEnum;
import com.illegalaccess.feign.enums.LoggerLevelMappingEnum;
import feign.Feign;
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
import org.apache.http.client.HttpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * Created by xiao on 2019/12/18.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class MultiFeignClientFactoryBean
        implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

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

    private MultiFeignBuildProperties multiFeignBuildProperties;

    private String path;

    private boolean decode404;

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
        // TODO 添加拦截器
//        builder.target(type, url);
        return builder.target(new Target.HardCodedTarget<>(this.type, this.name, url));
//        return builder.build();
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
