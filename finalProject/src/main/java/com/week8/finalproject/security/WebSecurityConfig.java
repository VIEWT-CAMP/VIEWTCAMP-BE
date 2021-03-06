package com.week8.finalproject.security;


import com.week8.finalproject.security.filter.FormLoginFilter;
import com.week8.finalproject.security.filter.FormLoginSuccessHandler;
import com.week8.finalproject.security.filter.JwtAuthFilter;
import com.week8.finalproject.security.jwt.HeaderTokenExtractor;
import com.week8.finalproject.security.provider.FormLoginAuthProvider;
import com.week8.finalproject.security.provider.JWTAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // ????????? Security ????????? ???????????? ???
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured ??????????????? ?????????
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // CustomAuthenticationProvider()??? ???????????? ????????? Overriding
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console ????????? ?????? ?????? (CSRF, FrameOptions ??????)
        web
                .ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "**/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers()
                .frameOptions().sameOrigin(); // SockJS??? ??????????????? HTML iframe ????????? ?????? ????????? ???????????? ????????? ??????????????? ?????? ????????? ????????????.



            http.sessionManagement()
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .expiredUrl("/")
                    .sessionRegistry(sessionRegistry());



                // cors?????? ??????
        http
                .cors()
                .configurationSource(corsConfigurationSource());

        // ???????????? ????????? JWT??? ???????????? ????????? Session??? ????????? ????????????.
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
         * 1.
         * UsernamePasswordAuthenticationFilter ????????? FormLoginFilter, JwtFilter ??? ???????????????.
         * FormLoginFilter : ????????? ????????? ???????????????.
         * JwtFilter       : ????????? ????????? JWT ?????? ??? ????????? ???????????????.
         */
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/v2/api-docs", "/**").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                // [???????????? ??????]
                .logout()
                // ???????????? ?????? ?????? URL
                .logoutUrl("/user/logout")
                .permitAll();
//                .and()
//                .exceptionHandling();
    }

    // logout ??? login??? ??? ??????????????? ??????
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // was??? ????????? ?????? ???(session clustering)
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }



    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/user/login");
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler()); // ?????? ????????? ????????? ????????? ??????
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler();
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }


    private JwtAuthFilter jwtFilter() throws Exception {
        List<String> skipPathList = new ArrayList<>();

        // h2-console ??????
        skipPathList.add("GET,/h2-console/**");
        skipPathList.add("POST,/h2-console/**");

        // ?????? ?????? API ??????
//        skipPathList.add("GET,/user/**");
        skipPathList.add("GET,/user/kakao/**");
        skipPathList.add("GET,/oauth/kakao/**");
//        skipPathList.add("POST,/user/**");
//        skipPathList.add("POST,/userinfo");


        // main ????????? ??????
        skipPathList.add("GET,/api/**");

//         chat , templates ??????
        skipPathList.add("GET,/chat/**");
        skipPathList.add("POST,/chat/**");
        skipPathList.add("GET,/ws-stomp/**");
        skipPathList.add("POST,/ws-stomp/**");


        skipPathList.add("GET,/room/**");
        skipPathList.add("GET,/room-page/**");
//        skipPathList.add("POST,/room/**");
        skipPathList.add("DELETE,/room/**");
        skipPathList.add("PUT,/room/**");



//         post ????????? ??????
        skipPathList.add("POST,/api/**");
        skipPathList.add("PUT,/api/**");
        skipPathList.add("DELETE,/api/**");

//         Swagger
        skipPathList.add("GET, /swagger-ui.html");
        skipPathList.add("GET, /swagger/**");
        skipPathList.add("GET, /swagger-resources/**");
        skipPathList.add("GET, /webjars/**");
        skipPathList.add("GET, /v2/api-docs");

        skipPathList.add("GET,/favicon.ico");

        FilterSkipMatcher matcher = new FilterSkipMatcher(
                skipPathList,
                "/**"
        );

        JwtAuthFilter filter = new JwtAuthFilter(
                matcher,
                headerTokenExtractor
        );
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true) ;
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedOrigin("http://localhost:3000"); // local ????????? ???
        configuration.addAllowedOrigin("https://localhost:3000"); // local ????????? ???
        // ?????? ??????
        configuration.addAllowedOrigin("http://example.amazonaws.com"); // ?????? ???
        configuration.addAllowedOrigin("https://example.amazonaws.com"); // ?????? ???
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
