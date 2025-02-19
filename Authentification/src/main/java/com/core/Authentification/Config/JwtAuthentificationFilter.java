package com.core.Authentification.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
//this class is the filter that accept the HTTP Request
public class JwtAuthentificationFilter extends OncePerRequestFilter{

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
		throws ServletException, IOException {
		
		//extract the headrer that contains the JWT token
			final String authHeader = request.getHeader("Authorization");

			final String jwt;

			final String userEmail;
			if(authHeader==null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			//extract th token from the autharization header it starts from the position number 7 after Bearer
			jwt=authHeader.substring(7);
//Now after extracting the JWT Token we need to verify if the user is in the database or not

		//1-Extract the userEmail
			userEmail= jwtService.extractUsername(jwt);
			
			if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()== null
					) {
				UserDetails userDetails =this.userDetailsService.loadUserByUsername(userEmail);
				if(jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
							
							);
					authToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request)
							);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
			
		
		
	}

}
