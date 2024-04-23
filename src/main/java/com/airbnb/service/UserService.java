package com.airbnb.service;

import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private PropertyUserRepository userRepository;

    private JWTService jwtService;


    public UserService(PropertyUserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    //method to add the user to the database with encryption and this will return back  Dto
    public PropertyUser addUser(PropertyUserDto propertyUserDto) {
        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(), BCrypt.gensalt(10))); //2nd way to encrypt PWD
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));1st way to encrypt pWD
        user.setUserRole(propertyUserDto.getUserRole());
        //to save the data
        PropertyUser savedUser = userRepository.save(user);
        return savedUser;

    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> opUser = userRepository.findByUsername(loginDto.getUsername());
        if (opUser.isPresent()) {
            PropertyUser propertyUser = opUser.get();
            if (BCrypt.checkpw(loginDto.getPassword(), propertyUser.getPassword())){
            return jwtService.generateToken(propertyUser);
            //generatetoken - generates a token and returns a string value,
            // encoded string value with 3 things -header,payload and signature.
        }
    }
        return null;
    }
}
