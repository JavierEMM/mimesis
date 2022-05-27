package com.mimesis.config;

import com.mimesis.google.CustomOAuth2UserService;
import com.mimesis.google.OAuth2LoginSuccessHandler;
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

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception{

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/processLogin")
                .defaultSuccessUrl("/redirectByRole")
                .and().oauth2Login().loginPage("/login")
                .userInfoEndpoint().userService(oAuth2UserService).and().defaultSuccessUrl("/redirectByRole").successHandler(oAuth2LoginSuccessHandler);



        http.logout().logoutSuccessUrl("/");
        http.authorizeRequests()
                .antMatchers("/historial","/historial/**").hasAuthority("Cliente")
                .antMatchers("/calificacion","/calificacion/**").hasAuthority("Cliente")
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
