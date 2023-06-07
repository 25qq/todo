package com.example.todo.security;

import com.example.todo.data.entity.user.User;
import com.example.todo.service.UsersAdminService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private final UsersAdminService usersService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User auth2User = super.loadUser(userRequest);
        // TODO populate authorities?
        User user = updateFacebookUser(auth2User.getAttributes());
        return new CustomUser(auth2User, user);
    }

    private User updateFacebookUser(Map attributes) {
        var email = (String) attributes.get("email");
        var name = StringUtils.defaultString((String) attributes.get("name"));

        User user = usersService.getOrCreateUser(email, name);
        return user;
    }

    @Data
    @AllArgsConstructor
    private class CustomUser implements OAuth2User, UserPrincipal {

        private final OAuth2User auth2User;
        private final User user;


        @Override
        public Map<String, Object> getAttributes() {
            return auth2User.getAttributes();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return auth2User.getAuthorities();
        }

        @Override
        public String getName() {
            return user.getName();
        }
    }


}