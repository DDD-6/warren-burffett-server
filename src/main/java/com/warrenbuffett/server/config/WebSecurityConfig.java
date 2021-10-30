package com.warrenbuffett.server.config;

import com.warrenbuffett.server.config.auth.CustomOAuthUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuthUserService customOAuthUserService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and().ignoring().mvcMatchers("/image/**");
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable().headers().frameOptions().disable()
                .and().authorizeRequests()
                .antMatchers("/","/api/user/**","/oauth2/**","/signin/**","/login/**","/console/**","/h2-console/**")
                .permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                // 인증 없이 페이지에 접근할 경우 리다이렉트
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and().logout().logoutSuccessUrl("/")
                .and().oauth2Login().defaultSuccessUrl("/").userInfoEndpoint().userService(customOAuthUserService);

        // localhost/ - localhost/user[post]
        // localhost/signin
        // localhost/api/user

    }
}
