package org.mediagate.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Resolve any Bearer Token value from the request. */
@Builder
public final class ChatBearerTokenResolver implements BearerTokenResolver {
    private static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);

    @Builder.Default
    private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

    @Builder.Default
    private boolean allowFormEncodedBodyParameter = false;

    @Builder.Default
    private boolean allowUriQueryParameter = false;

    private static String resolveFromRequestParameters(HttpServletRequest request) {
        String[] values = request.getParameterValues(ACCESS_TOKEN_PARAMETER_NAME);
        if (values == null || values.length == 0) {
            return null;
        }
        if (values.length == 1) {
            return values[0];
        }
        BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
        throw new OAuth2AuthenticationException(error);
    }

    private static boolean isGetRequest(HttpServletRequest request) {
        return HttpMethod.GET.name().equals(request.getMethod());
    }

    private static boolean isFormEncodedRequest(HttpServletRequest request) {
        return MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType());
    }

    private static boolean hasAccessTokenInQueryString(HttpServletRequest request) {
        return (request.getQueryString() != null) && request.getQueryString().contains(ACCESS_TOKEN_PARAMETER_NAME);
    }

    /**
     * Определите маркер доступа из запроса.
     * Если access_token и заголовок авторизации совпадают, верните заголовок авторизации.
     *
     * @param request the request
     * @return token value
     * @throws OAuth2AuthenticationException – if the found token is invalid
     */
    @Override
    public String resolve(final HttpServletRequest request) {
        final String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
        final String parameterToken = isParameterTokenSupportedForRequest(request)
                ? resolveFromRequestParameters(request) : null;
        if (authorizationHeaderToken != null) {
            if (parameterToken != null && !parameterToken.equals(authorizationHeaderToken)) {
                final BearerTokenError error = BearerTokenErrors
                        .invalidRequest("Found multiple bearer tokens in the request");
                throw new OAuth2AuthenticationException(error);
            }
            return authorizationHeaderToken;
        }
        if (parameterToken != null && isParameterTokenEnabledForRequest(request)) {
            return parameterToken;
        }
        return null;
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(this.bearerTokenHeaderName);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        }
        Matcher matcher = authorizationPattern.matcher(authorization);
        if (!matcher.matches()) {
            BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
            throw new OAuth2AuthenticationException(error);
        }
        return matcher.group("token");
    }

    private boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
        return isFormEncodedRequest(request) || isGetRequest(request);
    }

    private boolean isParameterTokenEnabledForRequest(HttpServletRequest request) {
        return ((this.allowFormEncodedBodyParameter && isFormEncodedRequest(request) && !isGetRequest(request)
                && !hasAccessTokenInQueryString(request)) || (this.allowUriQueryParameter && isGetRequest(request)));
    }
}
