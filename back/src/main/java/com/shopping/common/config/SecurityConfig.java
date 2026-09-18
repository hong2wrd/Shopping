package com.shopping.common.config;

import com.shopping.common.dto.ResponseDto;
import com.shopping.common.jwt.JwtProperties;
import com.shopping.common.security.JwtAuthenticationFilter;
import com.shopping.common.security.JwtAuthorizationFilter;
import com.shopping.common.security.MemberDetailsService;
import com.shopping.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.shopping.common.util.ResponseUtil.response;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity httpSecurity,
                                              JwtAuthenticationFilter jwtAuthenticationFilter,
                                              JwtAuthorizationFilter jwtAuthorizationFilter) {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // TODO 추후 변경 예정
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/common/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/member").permitAll()
                        .requestMatchers(HttpMethod.GET, "/item/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/item").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/item").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/item/{itemId}").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .anyRequest().authenticated())

                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationFilter)

                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            response(response, new ResponseDto<>(-1, "로그인을 진행해 주세요."), HttpStatus.UNAUTHORIZED);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response(response, new ResponseDto<>(-1, "권한이 없습니다."), HttpStatus.FORBIDDEN);
                        }))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of(jwtProperties.getAccessTokenHeader()));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 URL 패턴에 cors 허용 설정
        return source;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       MemberDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return builder.build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> authenticationFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> authorizationFilterRegistration(JwtAuthorizationFilter filter) {
        FilterRegistrationBean<JwtAuthorizationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
