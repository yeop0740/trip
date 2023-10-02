package com.example.trip.global.config;

import com.example.trip.global.LoginInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 로그인 인터셉터가 체크할 도메인을 지정한다.
 *
 * 기본적으로 모든 도메일을 체크 범위에 지정했다.
 * 체크할 필요가 없는 경우 addInterceptors에서 설정하자
 *
 * @Validated 로 체크할 message source를 messages.~ 파일로 선택했다.
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 인터셉터 등록
     *
     * 체크하지 않을 도메인을 excludePathPatterns에 등록하자
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/member/create");
    }

    /**
     * 기본 mesage source 파일을 messages. 파일로 선택했다.
     * 인코딩 형식은 UTF-8로 설정했다.
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }


    /**
     * message source를 등록한 validator를 설정했다.
     * @return
     */
    @Bean
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
