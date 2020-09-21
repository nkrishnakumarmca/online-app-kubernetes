package com.kk.userservice.authentication.service;

import com.kk.userservice.authentication.model.RegisteredUser;
import com.kk.userservice.errorhandling.exception.InvalidCredentialsException;
import com.kk.userservice.errorhandling.exception.UserNotFoundException;

public interface AuthenticationService {

    RegisteredUser authenticateUser(RegisteredUser registeredUser) throws UserNotFoundException, InvalidCredentialsException;

    RegisteredUser saveUserCredentials(RegisteredUser registeredUser);

    RegisteredUser updateUserCredentials(RegisteredUser registeredUser) throws UserNotFoundException;

}
