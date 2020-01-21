package com.pedrogonic.swapi.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.paths.DefaultPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static springfox.documentation.spring.web.paths.Paths.removeAdjacentForwardSlashes;


@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig extends WebMvcConfigurationSupport {

    String version = "";
    String dsc = "";
    String name = "";

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public Docket productApi() {

        try  ( java.io.InputStream is = this.getClass().getResourceAsStream("/api.properties") ) {
            java.util.Properties p = new Properties();
            p.load(is);
            version = p.getProperty("api.versionNumber");
            dsc = p.getProperty("api.description");
            name = p.getProperty("api.name");
        } catch (Exception ex) {
            Logger.getLogger(SwaggerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.pedrogonic.swapi.controllers"))
                .paths(PathSelectors.any())
                .build()
                .pathProvider(new MyPathProvider())
                .apiInfo(apiInfo(version, dsc, name));

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiInfo apiInfo(String version, String dsc, String name) {

        return new ApiInfo(
                name,
                dsc,
                version,
                "Terms of service",
                new Contact("Pedro Coelho", "", "pedrogonic@gmail.com"),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());

    }

    private class MyPathProvider extends DefaultPathProvider {
        @Override
        public String getOperationPath(String operationPath) {
            if (operationPath.startsWith(contextPath)) {
                operationPath = operationPath.substring(contextPath.length());
            }
            return removeAdjacentForwardSlashes(UriComponentsBuilder.newInstance().replacePath(operationPath)
                    .build().toString());
        }
    }
}
