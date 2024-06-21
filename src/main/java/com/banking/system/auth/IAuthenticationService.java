package com.banking.system.auth;

import org.springframework.http.ResponseEntity;

import com.banking.system.request.LoginRequest;
import com.banking.system.request.RegisterRequest;
import com.banking.system.request.UserReq;
import com.banking.system.response.JwtAuthenticationResponse;
import com.banking.system.response.ResponseEntityObject;
import com.banking.system.entity.User;
import com.banking.system.exception.UserException;

public interface IAuthenticationService {
    ResponseEntity<User> register(RegisterRequest registerRequest,String url) throws UserException;
    public JwtAuthenticationResponse login(LoginRequest loginRequest);
	ResponseEntityObject forgotPassword(String mail);
	ResponseEntityObject resetPassword(String mail, String otp, UserReq req);
	public void sendEmail(User user,String path);
	public boolean verifyAccount(String verificationCode);
}
