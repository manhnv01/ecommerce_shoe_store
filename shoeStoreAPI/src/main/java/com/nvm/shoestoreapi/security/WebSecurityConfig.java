package com.nvm.shoestoreapi.security;

import com.nvm.shoestoreapi.security.jwt.JwtAuthenticationFilter;
import com.nvm.shoestoreapi.security.jwt.JwtEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Cho phép sử dụng PreAuthorize
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final MyUserDetailService userService;
    final JwtEntryPoint jwtEntryPoint;
    final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable(); // Ngăn chặn request từ một domain khác
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Không sử dụng session lưu lại trạng thái của người dùng
        http.authorizeRequests()
                .antMatchers("/api/login", "/api/go-ship", "/api/register", "/images/*").permitAll() // Cho phép tất cả mọi người truy cập vào địa chỉ này
                .anyRequest().permitAll();
        http.exceptionHandling().authenticationEntryPoint(jwtEntryPoint); // Sử dụng ExceptionHandler để xử lý khi token không hợp lệ
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Thêm một lớp Filter kiểm tra jwt
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
