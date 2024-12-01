package org.mediagate.auth.config;

import org.mediagate.auth.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

/**	see also ...WebConfig.java */
public class SecurityConfig 
{
	@Value("${security.swagger.kc_url}")
	private String kcServerUrl;
	@Value("${security.swagger.kc_admin.realm}")
	private String realm;
	@Value("${security.swagger.kc_admin.client_id}")
	private String clientId;
	@Value("${security.swagger.kc_admin.login}")
	private String login;
	@Value("${security.swagger.kc_admin.password}")
	private String password;

    private final UserSecurityService userDataService;

    //-------------------------------------------------------------------------
	/** 
	 * 	статические ресурсы размещены в /src/main/webapp/
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .anonymous(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests	( (request) -> request
					                        .requestMatchers(	".*", 
								            					"/web/.*" 	//скрытые файлы - запрещаеем
								            				).denyAll()
					                        .requestMatchers(	"/v3/api-docs/**", 
					                        					"/swagger-ui.html", 
					                        					"/swagger-ui/**",
					                        					"/about",	//описание версии и имени приложения, + доп. дисгностическая информация как это видит сервер.
					                        					"/*.*", 	//файлы корнеого уровня - любые.
					                        				//	"/*.html",
					                        				//	"/*.js",
					                        				//	"/*.css",
					                        					"/web/**", 	//файлы каталога web - статические ресурсы - тоже любые.
					                        					"/"
					                        				).permitAll()
					                        .anyRequest().authenticated()
                						)
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()))
                        .bearerTokenResolver(bearerTokenResolver())
                );
        return http.build();
    }

    public BearerTokenResolver bearerTokenResolver() {
        return ChatBearerTokenResolver.builder()
                .allowUriQueryParameter(true)
                .allowFormEncodedBodyParameter(true)
                .build();
    }

	@Bean
	public Keycloak keycloak() {
		return KeycloakBuilder.builder()
				.serverUrl(kcServerUrl)
				.realm(realm)
				.clientId(clientId)
				.username(login)
				.password(password)
				.grantType(OAuth2Constants.PASSWORD)
				.build();
	}
}
