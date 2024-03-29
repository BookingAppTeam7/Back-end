package com.booking.BookingApp.security.jwt;

import com.booking.BookingApp.services.CustomDetailsServce;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private CustomDetailsServce jwtUserDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		if (request.getRequestURL().toString().contains("")) {
			System.out.println("####" + request.getMethod() + ":" + request.getRequestURL());
			System.out.println("#### Authorization: " + request.getHeader("Authorization"));
			String requestTokenHeader = request.getHeader("Authorization");
			String username = null;
			String jwtToken = null;
			if (requestTokenHeader != null && requestTokenHeader.contains("Bearer")) {
				jwtToken = requestTokenHeader.substring(requestTokenHeader.indexOf("Bearer ") + 7);
				System.out.println(">>>>>JWT TOKEN: " + jwtToken);
				try {
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);
					System.out.println("USERNAMMEEE " +username);
					UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
					if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						System.out.println("PRINT USERDETAILS USERNAME--> " + userDetails.getUsername());
						System.out.println("PRINT USERDETAILS AUTHOORITIESS --> " + userDetails.getAuthorities());
						System.out.println("PRINT USERDETAILS APASSWORD--> " + userDetails.getPassword());


						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						System.out.println("Username: " + userDetails.getUsername() + ", role: " + userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Unable to get JWT Token.");
				} catch (ExpiredJwtException e) {
					System.out.println("JWT Token has expired.");
				} catch (io.jsonwebtoken.MalformedJwtException e) {
					System.out.println("Bad JWT Token.");
				}
			} else {
				logger.warn("JWT Token does not exist.");
			}
		}
		chain.doFilter(request, response);
	}


}