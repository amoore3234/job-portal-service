package com.portal.job_portal_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String authServerUrl;

  @Value("${keycloak.resource}")
  private String clientId;

  @Value("${keycloak.realm}")
  private String realm;

  private static final String SECURITY_SCHEME_NAME = "bearerAuth";

  @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(securityScheme());
    }

    private Info apiInfo() {
      return new Info()
        .title("Job Portal Services")
        .description("This is the API documentation for the Job Portal service. " +
                      "Primarily consumed via the Job Link web app. " +
                      "<p>" +
                      "Click on below Authorize button to access the endpoints from swagger." +
                      "</div>")
        .version("1.0");

    }

     private Components securityScheme() {

      OAuthFlow oAuthFlow = new OAuthFlow()
            .tokenUrl(authServerUrl + "/protocol/openid-connect/token")
            .authorizationUrl(authServerUrl + "/protocol/openid-connect/auth");

      OAuthFlows oAuthFlows = new OAuthFlows();
      oAuthFlows.setAuthorizationCode(oAuthFlow);

      return new Components()
          .addSecuritySchemes(SECURITY_SCHEME_NAME,
              new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(oAuthFlows)
                .name(SECURITY_SCHEME_NAME)
                .bearerFormat("JWT")
                .scheme("bearer")

          );

  }
}
