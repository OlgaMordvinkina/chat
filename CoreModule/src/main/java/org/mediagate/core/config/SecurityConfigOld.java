//package org.mediagate.core.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final DataSource dataSource;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic(Customizer.withDefaults())
//                .authorizeHttpRequests(authz -> authz
//                                .requestMatchers("/registration").permitAll()
//                                .requestMatchers("/ws/**").permitAll()
//                                .requestMatchers("/login/**").permitAll()
////                        .requestMatchers("/api/chats").
////                .requestMatchers("/api/**").hasAuthority(Role.REGISTERED.name())
//                                .anyRequest()
//                                .authenticated()
//                )
//                .exceptionHandling(it -> it.authenticationEntryPoint((request, response, authException) -> response.setStatus(HttpStatus.UNAUTHORIZED.value())))
//                .formLogin(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .usersByUsernameQuery(
//                        "select u.email as username, p.user_password as password, true as enable " +
//                                "from users as u " +
//                                "inner join passwords as p on u.password_id=p.id " +
//                                "where u.email=?"
//                )
//                .authoritiesByUsernameQuery(
//                        "select email as username, user_role as role " +
//                                "from users " +
//                                "where email=?"
//                );
//    }
//}
