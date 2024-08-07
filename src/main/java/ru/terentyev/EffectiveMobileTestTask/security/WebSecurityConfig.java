package ru.terentyev.EffectiveMobileTestTask.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.terentyev.EffectiveMobileTestTask.services.PersonDetailsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    
	private final PersonDetailsService personDetailsService;
	private final JwtUtil jwtUtil;
	private AuthenticationManager authManager;

	@Autowired
	public WebSecurityConfig(@Lazy PersonDetailsService personDetailsService, JwtUtil jwtUtil) {
		this.personDetailsService = personDetailsService;
		this.jwtUtil = jwtUtil;
		}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		
		.cors(AbstractHttpConfigurer::disable)
		.csrf(AbstractHttpConfigurer::disable).sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests((requests) -> requests
				.requestMatchers("/auth/**").anonymous()
				.requestMatchers("/v3/**", "/swagger-ui.html", "/swagger-ui/**", "/error").permitAll()
				.anyRequest().authenticated()
			);
			
		http.addFilterBefore(new JwtAuthenticationFilter(authManager, personDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
        
		return http.build();
	}
	
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                                     .passwordEncoder(bCryptPasswordEncoder());
        authManager = authenticationManagerBuilder.build();
        return authManager;
    }
		
	 
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	     auth.userDetailsService(personDetailsService).passwordEncoder(bCryptPasswordEncoder());
	 }
}
