package com.illegalaccess.feign.annotation;

import com.illegalaccess.feign.support.MultiFeignClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by xiao on 2019/12/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MultiFeignClientsRegistrar.class)
public @interface EnableMultiFeignClient {

    /**
     * Base packages to scan for annotated components.
     * <p>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};

    /**
     * List of classes annotated with @MultiFeignClient. If not empty, disables classpath
     * scanning.
     * @return list of FeignClient classes
     */
    Class<?>[] clients() default {};
}
