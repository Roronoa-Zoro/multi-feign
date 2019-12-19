package com.illegalaccess.feign.annotation;

import com.illegalaccess.feign.support.MultiFeignClientsRegistrar;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by Jimmy on 2019/12/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MultiFeignClientsRegistrar.class)
public @interface EnableMultiFeignClient {

    /**
     * Base packages to scan for annotated components.
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};

    /**
     * List of classes annotated with @MultiFeignClient. If not empty, disables classpath
     * scanning.
     * @return list of FeignClient classes
     */
    Class<?>[] clients() default {};

    /**
     * these interceptors will be applied to all MultiFeignClient
     * @return
     */
    Class<? extends RequestInterceptor>[] globalInterceptors() default {};
}
