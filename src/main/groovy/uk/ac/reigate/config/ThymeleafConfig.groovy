package uk.ac.reigate.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.bind.RelaxedPropertyResolver
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.core.env.Environment
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader
import org.thymeleaf.spring4.SpringTemplateEngine
import org.thymeleaf.templateresolver.ServletContextTemplateResolver


@Configuration
public class ThymeleafConfig implements EnvironmentAware {
    
    @Autowired
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    
    private RelaxedPropertyResolver env;
    
    @Override
    public void setEnvironment(Environment environment) {
        this.env = new RelaxedPropertyResolver(environment, "spring.thymeleaf.");
    }
    
    //		@Bean
    //		@Description("Thymeleaf template engine with Spring integration")
    //		public SpringTemplateEngine templateEngine() {
    //			SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    //			templateEngine.addTemplateResolver(templateResolver());
    //			return templateEngine;
    //		}
    
    @Bean
    @Description("Thymeleaf template resolver serving HTML 5")
    public ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix(env.getProperty("prefix"));
        templateResolver.setSuffix(env.getProperty("suffix"));
        templateResolver.setTemplateMode(env.getProperty("mode"));
        templateResolver.setCacheable(env.getProperty("cache", Boolean.class, true))
        templateResolver.setOrder(1);
        return templateResolver;
    }
    
    //	@Bean
    //	@Description("Thymeleaf view resolver")
    //	public ThymeleafViewResolver viewResolver() {
    //		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    //		viewResolver.setTemplateEngine(templateEngine());
    //		return viewResolver;
    //	}
    
}
