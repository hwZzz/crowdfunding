package com.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 启用web环境下权限控制功能
public class WebApplicationConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()                        // 对请求进行授权
                .antMatchers("/index.jsp")    // 针对 /index.jsp 路径进行授权
                .permitAll()                            // 可以无条件访问
                .antMatchers("/layui/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()                            // 任意请求
                .authenticated()                         // 需要登录以后才可以访问
                .and()
                .formLogin()                             // 使用表单形式登录
                .loginPage("/index.jsp")                 // 指定登录页面，不指定则访问SpringSecurity自带的登录页
                .loginProcessingUrl("/do/login.html")    // 指定提交登录表单的地址，设置后则覆盖loginPage设置的的默认值
                .permitAll()                             // 允许访问登录地址
                .defaultSuccessUrl("/main.html")         // 设置登录成功后默认前往的URL地址
                .and()
                // .csrf().disable()                        // 禁用csrf
                .logout()
                .logoutUrl("/do/logout.html")
                .logoutSuccessUrl("/index.jsp")
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        builder
                .inMemoryAuthentication()           // 在内存中完成账号、密码的检查
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("tom")           // 指定账号
                .password(new BCryptPasswordEncoder().encode("123456"))                 // 指定密码
                .roles("ADMIN")                     // 指定当前用户角色
                .and()
                .withUser("jerry")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities("UPDATE")                  // 也可以指定权限
        ;
    }
}
