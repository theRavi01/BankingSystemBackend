package com.banking.system.auth;


import com.banking.system.response.JwtAuthenticationResponse;
import com.banking.system.response.ResponseEntityObject;
import com.banking.system.response.UserResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.banking.system.request.LoginRequest;
import com.banking.system.request.RegisterRequest;
import com.banking.system.request.UserReq;
import com.banking.system.ImgCloud.CloudinaryImageServiceImp;
import com.banking.system.entity.User;
import com.banking.system.exception.UserException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CloudinaryImageServiceImp cloudinaryImageService;
	
	@Autowired
	private IUserRepository userRepository;
    private final IAuthenticationService authenticationService;
    private final IJwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ResponseEntity<User>> register(@RequestBody RegisterRequest registerRequest
    		,HttpServletRequest req) throws UserException {
    	String url = req.getRequestURL().toString();
    	url = url.replace(req.getServletPath(),"/api/v1/auth");
    	
//    	String url ="http://192.168.167.236/api/v1/auth";
    	
    	System.out.println(url);
        return ResponseEntity.ok(authenticationService.register(registerRequest,url));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/find-username")
    public ResponseEntity<UserResponse> getUserByToken(@Valid @RequestHeader("Authorization") String authorizationHeader) {
    	UserResponse user = new UserResponse(); 
    	if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
             // Extract the token value
             String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
             String username = jwtService.extractUserName(token);
             Optional<User> userDetail = userRepository.findByEmail(username);
             user.setDob(userDetail.get().getDob());
             user.setEmail(userDetail.get().getEmail());
             user.setFirstName(userDetail.get().getFirstName());
             user.setLastName(userDetail.get().getLastName());
             user.setGender(userDetail.get().getGender());
             user.setProfileImgUrl(userDetail.get().getProfileImgUrl());
             user.setPhone(userDetail.get().getPhone());
         }
    	
        return ResponseEntity.ok(user);
    }
    
	@GetMapping("{email}")
	public ResponseEntity<ResponseEntityObject> forgotPassword(@PathVariable("email")String mail)
	{
		return new ResponseEntity<ResponseEntityObject>(authenticationService.forgotPassword(mail),HttpStatus.OK);
	}
	
	@PostMapping("{otp}")
	public ResponseEntity<ResponseEntityObject> resetPassword(@RequestParam(value = "mail",required = true) String mail,
			@PathVariable("otp")String otp,
			@RequestBody UserReq req)
	{
		req.setPassword(passwordEncoder.encode(req.getPassword()));
		return new ResponseEntity<ResponseEntityObject>(authenticationService.resetPassword(mail ,otp, req),HttpStatus.CREATED);
		
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code")String code) {
		boolean f = authenticationService.verifyAccount(code);
		if(f) {
			return "Sucessfully your account is verfied..";
		}
		else {
			return "May be your verification code is incorrect or already verified";
		}
	}
	
	@PostMapping("profile-image")
	public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile file ,@Valid @RequestHeader("Authorization") String authorizationHeader){
	try {
			User user = new User(); 
	    	if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	             // Extract the token value
	             String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
	             String username = jwtService.extractUserName(token);
	             Optional<User> userDetail = userRepository.findByEmail(username);
		    		user = userDetail.get();
	    	}
	     		Map data = this.cloudinaryImageService.upload(file);
	    		String imageUrl=String.valueOf(data.get("url"));
	             user.setProfileImgUrl(imageUrl);
	             userRepository.save(user);
		       return new ResponseEntity<Map> (data , HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map> (HttpStatus.BAD_REQUEST);
		}
	}
}
	
  

























