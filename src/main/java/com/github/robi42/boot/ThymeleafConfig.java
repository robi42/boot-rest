package com.github.robi42.boot;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@AutoConfigureBefore(ThymeleafAutoConfiguration.class)
public class ThymeleafConfig implements EnvironmentAware {
    private RelaxedPropertyResolver environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = new RelaxedPropertyResolver(environment, "thymeleaf.");
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(defaultTemplateResolver());
        return templateEngine;
    }

    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        final TemplateResolver templateResolver = new TemplateResolver();
        templateResolver.setResourceResolver(thymeleafResourceResolver());
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding(UTF_8.name());
        templateResolver.setCacheable(environment.getProperty("cacheable", Boolean.class, true));
        return templateResolver;
    }

    @Bean
    public SpringResourceResourceResolver thymeleafResourceResolver() {
        return new SpringResourceResourceResolver();
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() throws IOException {
        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding(UTF_8.name());
        viewResolver.setViewNames(new String[]{"error"}); // TODO: don't hard-code...
        // Needs to come before any fallback resolver (e.g. an `InternalResourceViewResolver`)
        viewResolver.setOrder(LOWEST_PRECEDENCE - 20);
        return viewResolver;
    }
}
