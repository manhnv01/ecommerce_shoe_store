package com.nvm.shoestoreapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().invalidSessionUrl("/login?sessionTimeout");
        http.authorizeRequests().antMatchers("/login", "/logout").permitAll();
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        http
                .authorizeRequests()
                //.antMatchers("/t/**")
                //.authenticated()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                    .headers()
                    .frameOptions().disable() // Để H2 Console hoạt động trong iframe
                .and()
                    .csrf().disable(); // Tắt CSRF protection

         //Cấu hình cho Login Form.
        http
                .authorizeRequests()
                .and()
                    .formLogin()//
                    .loginProcessingUrl("/j_spring_security_check")
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/")
                    .usernameParameter("email")
                    .passwordParameter("password")
                .and()
                    .logout()
                    .deleteCookies("JSESSIONID").permitAll()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService).passwordEncoder(passwordEncoder());
    }
}
