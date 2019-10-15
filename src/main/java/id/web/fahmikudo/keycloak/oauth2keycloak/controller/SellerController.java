package id.web.fahmikudo.keycloak.oauth2keycloak.controller;

import id.web.fahmikudo.keycloak.oauth2keycloak.config.SecurityContextUtils;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vulture
 */
@RestController
@RequestMapping("/api/v1/sellers")
public class SellerController {
    
    @GetMapping(path = "/username")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<String> getAuthorizedUserName() {
      return ResponseEntity.ok(SecurityContextUtils.getUserName());
    }

    @GetMapping(path = "/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Set<String>> getAuthorizedUserRoles() {
      return ResponseEntity.ok(SecurityContextUtils.getUserRoles());
    }
    
}
