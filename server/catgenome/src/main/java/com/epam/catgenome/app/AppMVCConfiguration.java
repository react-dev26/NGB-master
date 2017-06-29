package com.epam.catgenome.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.epam.catgenome.config.SwaggerConfig;
import com.epam.catgenome.controller.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Class provides MVC Configuration for Spring Boot application
 */
@Configuration
@Import(SwaggerConfig.class)
@ComponentScan(basePackages = {"com.epam.catgenome.config", "com.epam.catgenome.controller"})
public class AppMVCConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    //to skip tld scan completely
    @Bean
    @ConditionalOnClass({EmbeddedServletContainerFactory.class })
    public EmbeddedServletContainerCustomizer tomcatContainerCustomizer() {
        return container -> {
            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
            tomcat.setTldSkip("*.jar");

        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("/swagger-ui/", "classpath:/static/swagger-ui/",
                        "classpath:/META-INF/resources/webjars/swagger-ui/2.0.24/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        super.configurePathMatch(configurer);
        configurer.setUseSuffixPatternMatch(false);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new JsonMapper();
    }

    @Bean
    public SwaggerConfig swaggerConfig() {
        return new SwaggerConfig();
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet){
        ServletRegistrationBean bean =
                new ServletRegistrationBean(dispatcherServlet, "/restapi/*");
        bean.setAsyncSupported(true);
        bean.setName("catgenome");
        bean.setLoadOnStartup(1);

        return bean;
    }

    @Bean
    public ServletRegistrationBean oauth() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setApplicationContext(applicationContext);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet,
                "/oauth/token");
        servletRegistrationBean.setName("oauth");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean springSecurityFilterChain() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy securityFilter = new DelegatingFilterProxy();
        registrationBean.setFilter(securityFilter);
        registrationBean.setAsyncSupported(true);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    //we need to disable several default security filters, created by Spring boot
    @Bean
    public FilterRegistrationBean disableDefaultClientCredentials(
            ClientCredentialsTokenEndpointFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    //we need to disable several default security filters, created by Spring boot
    @Bean
    public FilterRegistrationBean disableDefaultUsernameAuth(UsernamePasswordAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    //we need to disable several default security filters, created by Spring boot
    @Bean
    public FilterRegistrationBean disableDefaultOAuth(OAuth2AuthenticationProcessingFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
