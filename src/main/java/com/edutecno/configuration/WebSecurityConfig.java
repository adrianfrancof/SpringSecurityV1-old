package com.edutecno.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	//configuracion de usuarios usados en memoria
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user1")//usuario de ingreso en memoria
		.password(passwordEncoder().encode("user1"))//password en memoria
		.roles("ADMIN")
		.and()
		.withUser("user2")
		.password(passwordEncoder().encode("user2"))
		.roles("USER");
	}	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable();
		http
		.authorizeRequests()//autorizando los request recibidos de los antMatchers y verificacion de rol
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/login")
		.permitAll()
		.anyRequest()//request que sea recibido a /login debe ser autenticado
		.authenticated()
		.and()
		.formLogin()//definiendo cual es la pagina inicial para el login
		.loginPage("/login")//URL para la pagina de login o inicio de sesión
		.failureUrl("/login?error=true")//URL para un login fallido
		.usernameParameter("user")//nombre del parametro del input en el formulario
		.passwordParameter("password")//nombre del parametro para el password en el input del formulario
		.defaultSuccessUrl("/default", true)
		.and()
		.exceptionHandling()//si ocurre un error de ingreso se ejecuta la pagina de recurso-prohibido
		.accessDeniedPage("/recurso-prohibido");
	}
	
	@Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
	
	//metodo encargado de codificar la password
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
