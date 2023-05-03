package com.pet.petmily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
public class SwaggerConfig {

    // http://localhost:8080/swagger-ui/index.html
    @Bean
    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.pet.petmily.user.controller"))//유저컨트롤러 api명세
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());

        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .securityContexts(List.of(this.securityContext())) // SecurityContext 설정
                .securitySchemes(List.of(this.apiKey())) // ApiKey 설정
                .select()
                .apis(RequestHandlerSelectors.basePackage(("com.pet.petmily")))
                .apis(RequestHandlerSelectors.withMethodAnnotation(io.swagger.annotations.ApiOperation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))


                .paths(PathSelectors.any())
                .build();
    }
    // JWT SecurityContext 구성
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }
    // ApiKey 정의
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }




    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger Test")
                .description("SwaggerConfig")
                .version("3.0")
                .build();
    }

}