package com.kk.userservice.authentication.controller;

import com.kk.userservice.authentication.dto.AuthenticationResponseDto;
import com.kk.userservice.authentication.dto.RegisteredUserDto;
import com.kk.userservice.authentication.model.RegisteredUser;
import com.kk.userservice.authentication.security.JwtTokenUtil;
import com.kk.userservice.authentication.service.AuthenticationService;
import com.kk.userservice.errorhandling.exception.InvalidCredentialsException;
import com.kk.userservice.errorhandling.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtTokenUtil jwtTokenUtil, ModelMapper modelMapper) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.modelMapper = modelMapper;
    }

    /**
     * REST Endpoint for authenticating Customers
     * URI: /api/v1/user/authenticate  METHOD: POST
     * Response status: success: 200(OK) , Customer Not Found : 404(Not Found)
     */
    @PostMapping("v1/users/authenticate")
    public AuthenticationResponseDto authenticateUser(@Valid @RequestBody RegisteredUserDto registeredUserDto) throws UserNotFoundException, InvalidCredentialsException {
        RegisteredUser registeredUser = authenticationService.authenticateUser(convertToEntity(registeredUserDto));
        String token = jwtTokenUtil.generateToken(registeredUser.getUserId());
        return new AuthenticationResponseDto(token, registeredUser.getEmail());
    }

    /**
     * REST Endpoint for updating Customer Password
     * URI: /api/v1/users  METHOD: PUT
     * Response status: success: 200(OK), Customer Not Found : 404(Not Found)
     */
    @PutMapping("v1/users")
    public void updatePassword(@Valid @RequestBody RegisteredUserDto registeredUserDto) throws UserNotFoundException {
        authenticationService.updateUserCredentials(convertToEntity(registeredUserDto));
    }

    private RegisteredUser convertToEntity(RegisteredUserDto registeredUserDto) {
        return modelMapper.map(registeredUserDto, RegisteredUser.class);
    }

}
