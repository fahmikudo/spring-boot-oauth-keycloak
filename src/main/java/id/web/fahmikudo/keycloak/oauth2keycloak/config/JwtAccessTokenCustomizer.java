package id.web.fahmikudo.keycloak.oauth2keycloak.config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


/**
 *
 * @author vulture
 */
public class JwtAccessTokenCustomizer extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {
    
    private static final Logger LOG = LoggerFactory.getLogger(JwtAccessTokenCustomizer.class);
    
    private static final String CLIENT_NAME_ELEMENT_IN_JWT = "resource_access";

    private static final String ROLE_ELEMENT_IN_JWT = "roles";

    private ObjectMapper mapper;

    public JwtAccessTokenCustomizer(ObjectMapper mapper) {
        this.mapper = mapper;
        LOG.info("Initialized {}", JwtAccessTokenCustomizer.class.getSimpleName());
    }

    @Override
    public void configure(JwtAccessTokenConverter jatc) {
        jatc.setAccessTokenConverter(this);
        LOG.info("Configured {}", JwtAccessTokenConverter.class.getSimpleName());
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> tokenMap) {
        LOG.debug("Begin extractAuthentication: tokenMap = {}", tokenMap);   
        JsonNode token = mapper.convertValue(tokenMap, JsonNode.class);
        Set<String> audienceList = extractClients(token); // extracting client names
        List<GrantedAuthority> authorities = extractRoles(token); // extracting client roles
        
        OAuth2Authentication authentication = super.extractAuthentication(tokenMap);
        OAuth2Request oAuth2Request = authentication.getOAuth2Request();

        OAuth2Request request =
            new OAuth2Request(oAuth2Request.getRequestParameters(), 
                oAuth2Request.getClientId(), 
                authorities, true, 
                oAuth2Request.getScope(),
                audienceList, null, null, null);

        Authentication usernamePasswordAuthentication = 
                new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), 
                "N/A", authorities);

        LOG.debug("End extractAuthentication");
        return new OAuth2Authentication(request, usernamePasswordAuthentication);
    }
    
    private List<GrantedAuthority> extractRoles(JsonNode jwt) {
        LOG.debug("Begin extractRoles: jwt = {}", jwt);
        Set<String> rolesWithPrefix = new HashSet<>();

        jwt.path(CLIENT_NAME_ELEMENT_IN_JWT)
            .elements()
            .forEachRemaining(e -> e.path(ROLE_ELEMENT_IN_JWT)
                .elements()
                .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText())));

        final List<GrantedAuthority> authorityList = 
               AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));

        LOG.debug("End extractRoles: roles = {}", authorityList);
        return authorityList;
    }
    
    
    private Set<String> extractClients(JsonNode jwt) {
        LOG.debug("Begin extractClients: jwt = {}", jwt);
        if (jwt.has(CLIENT_NAME_ELEMENT_IN_JWT)) {
          JsonNode resourceAccessJsonNode = jwt.path(CLIENT_NAME_ELEMENT_IN_JWT);
          final Set<String> clientNames = new HashSet<>();
          resourceAccessJsonNode.fieldNames()
              .forEachRemaining(clientNames::add);

          LOG.debug("End extractClients: clients = {}", clientNames);
          return clientNames;

        } else {
          throw new IllegalArgumentException("Expected element " + 
                    CLIENT_NAME_ELEMENT_IN_JWT + " not found in token");
        }
    }
    
    
    
}
