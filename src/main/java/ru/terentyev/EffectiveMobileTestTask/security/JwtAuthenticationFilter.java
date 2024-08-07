package ru.terentyev.EffectiveMobileTestTask.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.terentyev.EffectiveMobileTestTask.services.PersonDetailsService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private PersonDetailsService personDetailsService;
    private final JwtUtil jwtUtil;
    

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, PersonDetailsService personDetailsService
    		, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	       String header = request.getHeader("Authorization");
	       try {
	    	   if (header != null && header.startsWith("Bearer ")) {
	    		   String token = header.substring(7);
	    		   String username = jwtUtil.extractUsername(token);
	    		   if (jwtUtil.validateToken(token, username)) {
	                
	    			   UserDetails userDetails = personDetailsService.loadUserByUsername(username);
	    			   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    			   SecurityContextHolder.getContext().setAuthentication(authToken);
	    		   }	   
	            }
	        

	        filterChain.doFilter(request, response);
	       } catch (Exception e) {
	    	   throw e;
	       }
	}
}