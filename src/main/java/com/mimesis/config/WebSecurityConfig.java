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
                .defaultSuccessUrl("/redirectByRole",true);



        http.logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true);

        http.authorizeRequests()
                .antMatchers("/historial","/historial/**").hasAnyAuthority("Cliente","ROLE_USER")
                .antMatchers("/calificacion","/calificacion/**").hasAnyAuthority("Cliente","ROLE_USER")
                .antMatchers("/carrito","/carrito/**").hasAnyAuthority("Cliente","ROLE_USER")
                .antMatchers("/operador","/operador/**").hasAuthority("Operador")
                .antMatchers("/admin","/admin/**").hasAuthority("Admin")
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .defaultSuccessUrl("/redirectByRole",true);
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
