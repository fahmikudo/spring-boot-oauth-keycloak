package id.web.fahmikudo.keycloak.oauth2keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
                         UserDetailsServiceAutoConfiguration.class})
public class Oauth2KeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2KeycloakApplication.class, args);
	}

}
