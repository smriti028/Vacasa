package com.airbnb.config;

import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
/*so that now We can apply the stereotype and handover this class to springBoot for its team lifecycle management -
so the object of this class can be created now using springIoc.
In order o make this special class , which can read and extract info. from header and the URL Automatically comes to this class in order to do that -> have extends this class -> extends OncePerRequestFilter
[THIS IS A SPECIAL CLASS WHEREIN WHATEVER http request NOW WE MADE AFTER LOGIN WILL COME HERE
*/

//OncePerRequestFilter -> its an abstract class,
// from this Ab class we're inheriting  incomplete method dofilter and overriding and completing it.
public class JWTRequestFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private PropertyUserRepository userRepository;
    public JWTRequestFilter(JWTService jwtService, PropertyUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override //THIS IA AN INCOMPLETE METHOD
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //it reads the token
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader != null && tokenHeader.startsWith("Bearer"))// token starts with ("Bearer ") removing the bearer using this.
        {
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            String username = jwtService.getUserName(token);
            request.setAttribute("username",username);
            Optional<PropertyUser> opUser = userRepository.findByUsername(username);
            if(opUser.isPresent()){
                PropertyUser user = opUser.get();

                //To keep track of current user logged in
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null,
                        Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request)); //[AdaniSetsNewWorldRecord] this line use for creating session
                SecurityContextHolder.getContext().setAuthentication(authentication); //SGS
            }
        }

        /*doFilterInternal- this method will call first, extract the token , after extracting token we'll validate the token
        once myjob of filter is done, it will further call the remaining filters to do the required things and acc.process the stuff
         */
      //REQUEST- IT IS AN OBJ.WHICH RECEIVES ALL THE INCOMING HTTP REQUEST
      //RESPONSE - USE TO SET THE RESPONSE BACK
      //FILTERCHAIN- AFTER EXTRACTING INFO. IT WILL INTERNALLY APPLY MORE FILTERS

        filterChain.doFilter(request,response);//this will automatically call further filters.
    }
}
