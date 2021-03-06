package com.illegalaccess.feign.support;

import com.illegalaccess.feign.annotation.EnableMultiFeignClient;
import com.illegalaccess.feign.annotation.MultiFeignClient;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by xiao on 2019/12/17.
 * register the interface which is annotated with @MultiFeignClient into spring context
 *
 */
public class MultiFeignClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {
        doRegisterMultiFeignClients(metadata, registry);
    }

    private void doRegisterMultiFeignClients(AnnotationMetadata metadata,
                                             BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);


        Map<String, Object> attrs = metadata
                .getAnnotationAttributes(EnableMultiFeignClient.class.getName());
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                MultiFeignClient.class);
        final Class<?>[] clients = attrs == null ? null
                : (Class<?>[]) attrs.get("clients");

        Set<String> basePackages;
        // did not specify feign client explicitly
        if (clients == null || clients.length == 0) {
            scanner.addIncludeFilter(annotationTypeFilter);
            basePackages = getBasePackages(metadata);
        } else {
            final Set<String> clientClasses = new HashSet<>();
            basePackages = new HashSet<>();
            for (Class<?> clazz : clients) {
                basePackages.add(ClassUtils.getPackageName(clazz));
                clientClasses.add(clazz.getCanonicalName());
            }
            AbstractClassTestingTypeFilter filter = new AbstractClassTestingTypeFilter() {
                @Override
                protected boolean match(ClassMetadata metadata) {
                    String cleaned = metadata.getClassName().replaceAll("\\$", ".");
                    return clientClasses.contains(cleaned);
                }
            };

            scanner.addIncludeFilter(annotationTypeFilter);
            scanner.addIncludeFilter(filter);
        }

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(),
                            "@MultiFeignClient can only be specified on an interface");

                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    MultiFeignClient.class.getCanonicalName());

                    attributes.putAll(attrs);
                    String name = getClientName(attributes);

                    registerFeignClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerFeignClient(BeanDefinitionRegistry registry,
                                     AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(MultiFeignClientFactoryBean.class);
        definition.addPropertyValue("url", resolve((String) attributes.get("url")));
        String name = resolve((String) attributes.get("name"));
        definition.addPropertyValue("name", name);
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("clientPoolName", attributes.get("clientPoolName"));
        definition.addPropertyValue("clientPoolType", attributes.get("clientPoolType"));
        definition.addPropertyValue("logLevel", attributes.get("logLevel"));
        definition.addPropertyValue("retry", attributes.get("retry"));
        definition.addPropertyValue("retryPeriod", attributes.get("retryPeriod"));
        definition.addPropertyValue("retryMaxPeriod", attributes.get("retryMaxPeriod"));

        Set<Class<? extends RequestInterceptor>> interceptors = new HashSet<>();
        if (attributes.get("globalInterceptors") != null) {
            Class<? extends RequestInterceptor>[] globalInterceptors = (Class<? extends RequestInterceptor>[]) attributes.get("globalInterceptors");
            interceptors.addAll(Arrays.asList(globalInterceptors));
        }
        if (attributes.get("interceptors") != null) {
            Class<? extends RequestInterceptor>[] separateInterceptors = (Class<? extends RequestInterceptor>[]) attributes.get("interceptors");
            interceptors.addAll(Arrays.asList(separateInterceptors));
        }
        if (!interceptors.isEmpty()) {
            definition.addPropertyValue("interceptors", interceptors);
        }

        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    private String getClientName(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("name");
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("name");
        }

        if (StringUtils.hasText(value)) {
            return value;
        }

        throw new IllegalStateException("'name' must be provided in @"
                + MultiFeignClient.class.getSimpleName());
    }


    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableMultiFeignClient.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        // using package of starter class
        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }
}
