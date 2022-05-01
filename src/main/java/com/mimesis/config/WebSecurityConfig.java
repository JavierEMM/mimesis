package com.mimesis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Override
    public void configure(HttpSecurity http) throws Exception{

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/processLogin")
                .defaultSuccessUrl("/redirectByRole");

        http.logout().logoutSuccessUrl("/");
        http.authorizeRequests()
                .antMatchers("/carrito","/carrito/**").hasAuthority("Cliente")
                .antMatchers("/operador","/operador/**").hasAuthority("Operador")
                .antMatchers("/admin","/admin/**").hasAuthority("Admin")
                .anyRequest().permitAll();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("SELECT correo,contrasena,valido FROM mimesis.usuario WHERE correo=?;")
                .authoritiesByUsernameQuery("SELECT correo,rol FROM mimesis.usuario WHERE correo= ? and valido = 1;");
    }
}
