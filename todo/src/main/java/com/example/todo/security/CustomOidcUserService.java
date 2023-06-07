package com.example.todo.security;

import com.example.todo.data.entity.user.User;
import com.example.todo.service.UsersAdminService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UsersAdminService usersService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        User user = updateGoogleUser(oidcUser.getAttributes());
        return new CustomUser(oidcUser, user);
    }

    private User updateGoogleUser(Map attributes) {
        var email = (String) attributes.get("email");
        var name = StringUtils.defaultString((String) attributes.get("name"));

        User user = usersService.getOrCreateUser(email, name);
        return user;
    }

    @AllArgsConstructor
    private class CustomUser implements OidcUser, UserPrincipal {

        private final OidcUser oidcUser;
        private final User user;


        @Override
        public User getUser() {
            return user;
        }

        @Override
        public Map<String, Object> getClaims() {
            return oidcUser.getClaims();
        }

        @Override
        public OidcUserInfo getUserInfo() {
            return oidcUser.getUserInfo();
        }

        @Override
        public OidcIdToken getIdToken() {
            return oidcUser.getIdToken();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return oidcUser.getAttributes();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return oidcUser.getAuthorities();
        }

        @Override
        public String getName() {
            return user.getName();
        }
    }
}