/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.web.fahmikudo.keycloak.oauth2keycloak.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 * @author vulture
 */
@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "rest.security")
public class SecurityProperties {
    
    private boolean enabled;
    private String apiMatcher;
    private Cors cors;
    private String issuerUri;

    public CorsConfiguration getCorsConfiguration() {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
      corsConfiguration.setAllowedMethods(cors.getAllowedMethods());
      corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());
      corsConfiguration.setExposedHeaders(cors.getExposedHeaders());
      corsConfiguration.setAllowCredentials(cors.getAllowCredentials());
      corsConfiguration.setMaxAge(cors.getMaxAge());

      return corsConfiguration;
    }

    @Getter
    @Setter
    public static class Cors {

      private List<String> allowedOrigins;
      private List<String> allowedMethods;
      private List<String> allowedHeaders;
      private List<String> exposedHeaders;
      private Boolean allowCredentials;
      private Long maxAge;
    }
    
}
