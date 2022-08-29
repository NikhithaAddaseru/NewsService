package com.newsservice.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.newsservice.application.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity 
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter{

	 @Bean
	    public UserDetailsService userDetailsService() {
	        return new UserDetailsServiceImpl();
	    }
	
	 @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(getPasswordEncoder());
	         
	        return authProvider;
	    }
	 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		//auth.userDetailsService(userAuthService).passwordEncoder(getPasswordEncoder());
;		//.inMemoryAuthentication()
//		.withUser("1").password("admin").roles("ADMIN")
//		.and()
//		.withUser("2").password("admin").roles("ADMIN")
//		.and()
//		.withUser("3").password("admin").roles("ADMIN")
//		.and()
//		.withUser("4").password("admin").roles("ADMIN")
//		.and()
//		.withUser("5").password("user").roles("USER")
//		.and()
//		.withUser("6").password("user").roles("USER")
//		.and()
//		.withUser("7").password("user").roles("USER")
//		.and()
//		.withUser("8").password("user").roles("USER")
//		.and()
//		.withUser("9").password("publisher").roles("PUBLISHER")
//		.and()
//		.withUser("10").password("publisher").roles("PUBLISHER");
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.authorizeRequests()	
		.antMatchers("/news/deleteArticle/{newsId}").hasAnyAuthority("ADMIN")
		.antMatchers("/news/createUser").hasAnyAuthority("ADMIN")	
		.antMatchers("/news/assignRole").hasAnyAuthority("ADMIN")
		.antMatchers("/news/createRole").hasAnyAuthority("ADMIN")
		.antMatchers("/news/createArticle").hasAnyAuthority("PUBLISHER", "ADMIN")
		.antMatchers("/news/editArticle/{userId}").hasAnyAuthority("PUBLISHER", "ADMIN")
		.antMatchers("/news/publishArticle").hasAnyAuthority("PUBLISHER", "ADMIN")
		.antMatchers("/news/viewArticle/{userId}").hasAnyAuthority("USER", "ADMIN","PUBLISHER")
		.antMatchers("/news/viewPicture").hasAnyAuthority("USER", "ADMIN","PUBLISHER")
		.antMatchers("/news/viewTop10Article").hasAnyAuthority("USER", "ADMIN","PUBLISHER")
		//.antMatchers("/**").hasRole("ADMIN")
		.antMatchers("/news").permitAll()
		.and().formLogin();
	}
}
