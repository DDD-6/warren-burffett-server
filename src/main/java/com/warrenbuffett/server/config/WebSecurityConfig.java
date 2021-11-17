package com.warrenbuffett.server.config;

import com.warrenbuffett.server.config.auth.CustomOAuthUserService;
import com.warrenbuffett.server.jwt.JwtAccessDeniedHandler;
import com.warrenbuffett.server.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuthUserService customOAuthUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String[] PUBLIC_URLS = {
            "/","/api/user/**","/oauth2/**","/signin/**","/login/**",
            "/console/**","/h2-console/**"
    };
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and().ignoring().mvcMatchers("/image/**")
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable().headers().frameOptions().disable()
                .and().authorizeRequests()
                .mvcMatchers(PUBLIC_URLS)
                .permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                // 인증 없이 페이지에 접근할 경우 리다이렉트
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and().logout().logoutUrl("/api/user/logout").logoutSuccessUrl("/")
                .and().oauth2Login().defaultSuccessUrl("/").userInfoEndpoint().userService(customOAuthUserService);
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 생성x, 사용x
                .and().apply(new JwtSecurityConfig(jwtTokenProvider));


    }
}
