package com.portal.job_portal_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
@SuppressWarnings("null")
public class WebClientConfiguration {

  @Value("${client.job-board-api.base-url}")
  private String baseUrl;

  private static final String KEYCLOAK = "keycloak";

  @Bean
  public WebClient jobBoardAPIWebClient(OAuth2AuthorizedClientManager authorizedClientManager) {
      ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
          authorizedClientManager);

    oauth2Client.setDefaultClientRegistrationId(KEYCLOAK);

      return WebClient.builder()
          .apply(oauth2Client.oauth2Configuration())
          .baseUrl(baseUrl)
          .clientConnector(new ReactorClientHttpConnector(buildHttpClient()))
          .build();
  }

  private HttpClient buildHttpClient() {
    return HttpClient.create();
  }
}
