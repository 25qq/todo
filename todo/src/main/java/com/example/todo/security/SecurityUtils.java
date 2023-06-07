package com.example.todo.security;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.stream.Stream;

public class SecurityUtils {

    public static boolean isUserLoggedIn() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            return SecurityUtils.isUserLoggedIn(context.getAuthentication());
        }
        return false;
    }

    private static boolean isUserLoggedIn(Authentication authentication) {
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof UserPrincipal;
    }

    public static Optional<UserPrincipal> getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication auth = context.getAuthentication();
            if (auth != null) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserPrincipal) {
                    return Optional.of((UserPrincipal) context.getAuthentication().getPrincipal());
                }
            }
        }
        return Optional.empty();
    }

    public static boolean hasValidatedUser() {
        try {
            return getCurrentUser()
                    .map(p -> (p.getUser() != null) && p.getUser().getIsValidated())
                    .orElse(false);
        } catch (Exception ex) {
            return false;
        }
    }


    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(HandlerHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }



}
