/**
 * 
 */
package com.gokoders.login.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * @author gokoders
 *
 */
@Configuration
public class FreemarkerConfig {
//	@Bean(name="freemarkerConfig")
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("/emailtemplates/");
        return bean;
    }
}
