package com.electronic.store.Electronic_Store.Security;

import com.electronic.store.Electronic_Store.Repositories.userRepository;
import com.electronic.store.Electronic_Store.Services.Implementations.CustomSecurityDetailServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter: Ensures this filter runs once per HTTP request — no duplicate executions.

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomSecurityDetailServiceImplementation customSecurityDetailService;

    //This method [doFilterInternal] is called for every request by Spring Security.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        //If there's no header or it doesn't start with "Bearer " → skip auth. Just forward the request without doing anything (might result in 401 later)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extract token by removing the prefix(Bearer) & Uses your custom JwtUtil to get the username from token.
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        //Check: Is User Not Already Authenticated?
        //SecurityContextHolder: stores security details for the current thread.
        //.getContext() gets the security context.
        //.getAuthentication() gets the authentication details (like username, roles, etc.) of the current
        //In short "SecurityContextHolder.getContext().getAuthentication()" : returns the authentication information of whoever is currently logged in.
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){ //Only proceed if: A valid username was extracted & No user is already authenticated (avoids duplicate authentication)

            // Load UserDetails from DB and Validate Token
            UserDetails userDetails = customSecurityDetailService.loadUserByUsername(username); //Loads full user object by username

            //Uses your jwtUtil to check if token is valid (e.g., signature + username match)
            if(username.equals(userDetails.getUsername()) && !jwtUtil.isTokenExpired(token)){
                //This condition means token is valid and need to create & set authentication
                //UsernamePasswordAuthenticationToken: is a class used to store authentication information (like username and roles)--> we create auth obj with help of this .
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //It attaches extra information about the current web request (such as the user's IP address and session) to the authentication object.
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Stores it in SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(auth);
                //Whether authentication was set or skipped, the request is now passed to the next filter/controller
                filterChain.doFilter(request, response);
            }
        }
    }
}
